package com.example.opengltest;

import android.graphics.Bitmap;
import android.opengl.GLES20;

abstract class Shape {
    protected float mAspectRatio;

    public abstract void draw(float[] vMatrix);

    public void createTexture(Bitmap bitmap) {}

    public void releaseTexture() {}

    public int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public boolean is2DShape() {
        return true;
    }

    public boolean is3DShape() {
        return !is2DShape();
    }

    public float[] getInitialAngle() {
        return new float[]{ 0.0f, 0.0f, 0.0f };
    }

    public void setAspectRatio(float ratio) {
        mAspectRatio = ratio;
    }
}
