package com.example.opengltest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square extends Shape {
    private final FloatBuffer vertexBuffer;
    private final float[] vertexArray = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };

    private final float[] colorLeftArray = { 1.0f, 0.0f, 0.0f, 1.0f };
    private final float[] colorRightArray = { 0.0f, 1.0f, 0.0f, 1.0f };

    private final ShortBuffer drawBuffer;
    private final short[] drawArray = { 0, 1, 2, 0, 2, 3 };

    private final String vertexShaderCode =
            "uniform mat4 vMatrix;" +
            "attribute vec4 vPosition;" +
            "uniform vec4 vColorLeft;" +
            "uniform vec4 vColorRight;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  if (vPosition.x < 0.0f) {" +
            "    vColor = vColorLeft;" +
            "  } else {" +
            "    vColor = vColorRight;" +
            "  }" +
            "  gl_Position = vMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final int mProgram;

    public Square() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);

        ByteBuffer dbb = ByteBuffer.allocateDirect(drawArray.length * 2);
        dbb.order(ByteOrder.nativeOrder());
        drawBuffer = dbb.asShortBuffer();
        drawBuffer.put(drawArray);
        drawBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void draw(float[] vMatrix) {
        GLES20.glUseProgram(mProgram);

        int matrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, vMatrix, 0);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

        int colorHandle1 = GLES20.glGetUniformLocation(mProgram, "vColorLeft");
        GLES20.glUniform4fv(colorHandle1, 1, colorLeftArray, 0);
        int colorHandle2 = GLES20.glGetUniformLocation(mProgram, "vColorRight");
        GLES20.glUniform4fv(colorHandle2, 1, colorRightArray, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawArray.length, GLES20.GL_UNSIGNED_SHORT, drawBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
