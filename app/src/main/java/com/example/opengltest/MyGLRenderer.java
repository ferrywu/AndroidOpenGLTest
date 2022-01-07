package com.example.opengltest;

import static com.example.opengltest.ShapeType.*;
import static com.example.opengltest.ShapeOperation.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final ShapeType shapeType = SQUARE_TEXTURE;
    private final ShapeOperation shapeOperation = MANUAL_ROTATE;
    private Shape shape;
    private Bitmap textureBitmap;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    public float[] mAngle = { 0.0f, 0.0f, 0.0f };
    public float[] mScale = { 1.0f, 1.0f, 1.0f };

    private final float TOUCH_SCALE_FACTOR = 0.6f;
    private float previousX;
    private float previousY;

    public MyGLRenderer(Context context) {
        if (shapeType == SQUARE_TEXTURE) {
            textureBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.texture1);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        shape = shapeType.createShape();
        if (shape.is3DShape()) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        shape.setAspectRatio((float) width / height);

        if (shapeType != SQUARE_TEXTURE) {
            float ratio = (float) width / height;
            if (width > height) {
                Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
            } else {
                Matrix.frustumM(projectionMatrix, 0, -1, 1, -1/ratio, 1/ratio, 3, 7);
            }
        } else {
            int wTexture = textureBitmap.getWidth();
            int hTexture = textureBitmap.getHeight();
            float ratioTexture = (float) wTexture / hTexture;
            float ratio = (float) width / height;

            if (width > height) {
                if (ratioTexture > ratio) {
                    Matrix.orthoM(projectionMatrix, 0, -ratio*ratioTexture, ratio*ratioTexture, -1, 1, 3, 7);
                } else {
                    Matrix.orthoM(projectionMatrix, 0, -ratio/ratioTexture, ratio/ratioTexture, -1, 1, 3, 7);
                }
            } else {
                if (ratioTexture > ratio) {
                    Matrix.orthoM(projectionMatrix, 0, -1, 1, -1/(ratio*ratioTexture), 1/(ratio*ratioTexture), 3, 7);
                } else {
                    Matrix.orthoM(projectionMatrix, 0, -1, 1, -ratioTexture/ratio, ratioTexture/ratio, 3, 7);
                }
            }
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (shape.is3DShape()) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float[] vMatrix = new float[16];

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(vMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        if (shape.is3DShape()) {
            float[] initialAngle = shape.getInitialAngle();
            Matrix.translateM(vMatrix, 0, 0, 0,-1);
            Matrix.rotateM(vMatrix, 0, initialAngle[0], 1, 0, 0);
            Matrix.rotateM(vMatrix, 0, initialAngle[1], 0, 1, 0);
            Matrix.rotateM(vMatrix, 0, initialAngle[2], 0, 0, 1);
        }

        switch (shapeOperation) {
            case AUTO_ROTATE:
                long time = System.currentTimeMillis() % 4000L;
                float angle = 0.09f * ((int) time);
                if (shape.is2DShape()) {
                    Matrix.rotateM(vMatrix, 0, angle, 0, 0, -1);
                } else {
                    Matrix.rotateM(vMatrix, 0, angle, 1, 0, 0);
                    Matrix.rotateM(vMatrix, 0, angle, 0, 1, 0);
                }
                break;

            case MANUAL_ROTATE:
                Matrix.rotateM(vMatrix, 0, mAngle[0], 1, 0, 0);
                Matrix.rotateM(vMatrix, 0, mAngle[1], 0, 1, 0);
                Matrix.rotateM(vMatrix, 0, mAngle[2], 0, 0, 1);
                Log.e(this.getClass().getName(), "mAngle = " + mAngle[0] + "f, " + mAngle[1] + "f, " + mAngle[2] + "f");
                break;

            case MANUAL_SCALE:
                Matrix.scaleM(vMatrix, 0, mScale[0], mScale[1], mScale[2]);
                break;
        }

        shape.createTexture(textureBitmap);
        shape.draw(vMatrix);
        shape.releaseTexture();
    }

    public boolean onTouchEvent(MotionEvent event, int width, int height) {
        boolean shouldRender;
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;

                switch (shapeOperation) {
                    case MANUAL_ROTATE:
                        if (shape.is2DShape()) {
                            if (y < height / 2.0f) {
                                dx = dx * -1;
                            }
                            if (x > width / 2.0f) {
                                dy = dy * -1;
                            }
                            mAngle[2] += (dx + dy) * TOUCH_SCALE_FACTOR;
                        } else {
                            mAngle[0] += dy * TOUCH_SCALE_FACTOR;
                            mAngle[0] %= 360;
                            mAngle[1] += dx * TOUCH_SCALE_FACTOR;
                            mAngle[1] %= 360;
                        }
                        break;

                    case MANUAL_SCALE:
                        mScale[0] += dx / width * (width > height ? (float)width/height : 1);
                        mScale[1] += dy / height * (height > width ? (float)height/width : 1);
                        if (shape.is2DShape())
                            mScale[2] = 0.0f;
                        else
                            mScale[2] += (dx + dy) / (width + height);
                        break;
                }

                shouldRender = true;
                break;

            default:
                shouldRender = false;
                break;
        }

        previousX = x;
        previousY = y;
        return shouldRender;
    }

    public boolean renderWhenDirty() {
        return (shapeOperation != AUTO_ROTATE);
    }
}
