package com.example.opengltest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private final int shapeType;
    private final int shapeOperation;

    private Triangle triangle;
    private Square square;
    private Polygon polygon;
    private Circle circle;
    private Cube cube;
    private Pyramid pyramid;
    private PolygonalPyramid polygonalPyramid;
    private Cone cone;
    private PolygonalPrism polygonalPrism;
    private Cylinder cylinder;
    private Sphere sphere;
    private SquareTexture squareTexture;
    private Bitmap textureBitmap;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    public float[] mAngle = { 0.0f, 0.0f, 0.0f };
    public float[] mScale = { 1.0f, 1.0f, 1.0f };

    public MyGLRenderer(Context context, int shapeType, int shapeOperation) {
        this.context = context;
        this.shapeType = shapeType;
        this.shapeOperation = shapeOperation;
    }

    public float[] getAngle() {
        return mAngle;
    }
    public void setAngle(float[] angle) {
        mAngle = angle;
    }

    public float[] getScale() {
        return mScale;
    }
    public void setScale(float[] scale) {
        mScale = scale;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        if (ShapeType.is3DShape(shapeType)) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        switch (shapeType) {
            case ShapeType.TRIANGLE:
                triangle = new Triangle();
                break;
            case ShapeType.SQUARE:
                square = new Square();
                break;
            case ShapeType.POLYGON:
                polygon = new Polygon(6);
                break;
            case ShapeType.CIRCLE:
                circle = new Circle();
                break;
            case ShapeType.POLYGON_CIRCLE:
                polygon = new Polygon(6);
                circle = new Circle();
                break;
            case ShapeType.CUBE:
                cube = new Cube();
                break;
            case ShapeType.PYRAMID:
                pyramid = new Pyramid();
                break;
            case ShapeType.POLYGONAL_PYRAMID:
                polygonalPyramid = new PolygonalPyramid(6);
                break;
            case ShapeType.CONE:
                cone = new Cone();
                break;
            case ShapeType.POLYGONAL_PRISM:
                polygonalPrism = new PolygonalPrism(6);
                break;
            case ShapeType.CYLINDER:
                cylinder = new Cylinder();
                break;
            case ShapeType.SPHERE:
                sphere = new Sphere();
                break;
            case ShapeType.SQUARE_TEXTURE:
                textureBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.texture1);
                squareTexture = new SquareTexture(textureBitmap);
                break;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        if (shapeType != ShapeType.SQUARE_TEXTURE) {
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
        if (ShapeType.is3DShape(shapeType)) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float[] vMatrix = new float[16];

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(vMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        if (ShapeType.is3DShape(shapeType)) {
            float[] initialAngle = ShapeType.getInitialAngle(shapeType);
            Matrix.translateM(vMatrix, 0, 0, 0,-1);
            Matrix.rotateM(vMatrix, 0, initialAngle[0], 1, 0, 0);
            Matrix.rotateM(vMatrix, 0, initialAngle[1], 0, 1, 0);
            Matrix.rotateM(vMatrix, 0, initialAngle[2], 0, 0, 1);
        }

        switch (shapeOperation) {
            case ShapeOperation.AUTO_ROTATE:
                long time = System.currentTimeMillis() % 4000L;
                float angle = 0.09f * ((int) time);
                if (ShapeType.is2DShape(shapeType)) {
                    Matrix.rotateM(vMatrix, 0, angle, 0, 0, -1);
                } else {
                    Matrix.rotateM(vMatrix, 0, angle, 1, 0, 0);
                    Matrix.rotateM(vMatrix, 0, angle, 0, 1, 0);
                }
                break;

            case ShapeOperation.MANUAL_ROTATE:
                Matrix.rotateM(vMatrix, 0, mAngle[0], 1, 0, 0);
                Matrix.rotateM(vMatrix, 0, mAngle[1], 0, 1, 0);
                Matrix.rotateM(vMatrix, 0, mAngle[2], 0, 0, 1);
                Log.e(this.getClass().getName(), "mAngle = " + mAngle[0] + "f, " + mAngle[1] + "f, " + mAngle[2] + "f");
                break;

            case ShapeOperation.MANUAL_SCALE:
                Matrix.scaleM(vMatrix, 0, mScale[0], mScale[1], mScale[2]);
                break;
        }

        switch (shapeType) {
            case ShapeType.TRIANGLE:
                triangle.draw(vMatrix);
                break;
            case ShapeType.SQUARE:
                square.draw(vMatrix);
                break;
            case ShapeType.POLYGON:
                polygon.draw(vMatrix);
                break;
            case ShapeType.CIRCLE:
                circle.draw(vMatrix);
                break;
            case ShapeType.POLYGON_CIRCLE:
                float[] polygonMatrix = new float[16];
                Matrix.translateM(polygonMatrix, 0, vMatrix, 0, -0.7f, 0, 0);
                float[] circleMatrix = new float[16];
                Matrix.translateM(circleMatrix, 0, vMatrix, 0, 0.7f, 0, 0);
                polygon.draw(polygonMatrix);
                circle.draw(circleMatrix);
                break;
            case ShapeType.CUBE:
                cube.draw(vMatrix);
                break;
            case ShapeType.PYRAMID:
                pyramid.draw(vMatrix);
                break;
            case ShapeType.POLYGONAL_PYRAMID:
                polygonalPyramid.draw(vMatrix);
                break;
            case ShapeType.CONE:
                cone.draw(vMatrix);
                break;
            case ShapeType.POLYGONAL_PRISM:
                polygonalPrism.draw(vMatrix);
                break;
            case ShapeType.CYLINDER:
                cylinder.draw(vMatrix);
                break;
            case ShapeType.SPHERE:
                sphere.draw(vMatrix);
                break;
            case ShapeType.SQUARE_TEXTURE:
                squareTexture.draw(vMatrix);
                break;
        }
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
