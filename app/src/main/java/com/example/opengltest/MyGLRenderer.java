package com.example.opengltest;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final int shapeType;
    private final int shapeOperation;

    private Triangle triangle;
    private Square square;
    private Polygon polygon;
    private Circle circle;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] rotateMatrix = new float[16];

    public float mAngle;
    public float mScaleX = 1.0f;
    public float mScaleY = 1.0f;

    public MyGLRenderer(int shapeType, int shapeOperation) {
        this.shapeType = shapeType;
        this.shapeOperation = shapeOperation;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void setScale(float dScaleX, float dScaleY) {
        mScaleX += dScaleX;
        mScaleY += dScaleY;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
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
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float[] vMatrix = new float[16];

        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(vMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        switch (shapeOperation) {
            case ShapeOperation.AUTO_ROTATE:
                long time = System.currentTimeMillis() % 4000L;
                float angle = 0.09f * ((int) time);
                Matrix.setRotateM(rotateMatrix, 0, angle, 0, 0, -1);
                Matrix.multiplyMM(vMatrix, 0, vMatrix, 0, rotateMatrix, 0);
                break;

            case ShapeOperation.MANUAL_ROTATE:
                Matrix.setRotateM(rotateMatrix, 0, mAngle, 0, 0, -1);
                Matrix.multiplyMM(vMatrix, 0, vMatrix, 0, rotateMatrix, 0);
                break;

            case ShapeOperation.MANUAL_SCALE:
                Matrix.scaleM(vMatrix, 0, mScaleX, mScaleY, 0);
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
        }
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
