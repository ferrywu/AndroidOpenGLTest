package com.example.opengltest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Pyramid {
    private final boolean drawLinesOnly = false;

    private final FloatBuffer vertexBuffer;
    private final float[] vertexArray = {
            -0.5f, 0.0f, -0.5f,
            -0.5f, 0.0f, 0.5f,
            0.5f, 0.0f, 0.5f,
            0.5f, 0.0f, -0.5f,
            0.0f, 1.0f, 0.0f,
    };

    private final FloatBuffer colorBuffer;
    private final float[] colorArray = {
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
    };

    private final ShortBuffer drawBuffer;
    private final short[] drawArray;
    private final short[] drawLinesArray = {
            4, 0, 4, 1, 4, 2, 4, 3,
            0, 1, 1, 2, 2, 3, 3, 0, 0, 2,
    };
    private final short[] drawTrianglesArray = {
            4, 0, 3,
            4, 1, 2,
            4, 1, 0,
            4, 2, 3,
            0, 1, 2,
            2, 3, 0,
    };

    private final String vertexShaderCode =
            "uniform mat4 vMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec4 vColorVertex;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  vColor = vColorVertex;" +
            "  gl_Position = vMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final int mProgram;

    public Pyramid() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colorArray);
        colorBuffer.position(0);

        if (drawLinesOnly) {
            drawArray = drawLinesArray;
        } else {
            drawArray = drawTrianglesArray;
        }
        ByteBuffer dbb = ByteBuffer.allocateDirect(drawArray.length * 2);
        dbb.order(ByteOrder.nativeOrder());
        drawBuffer = dbb.asShortBuffer();
        drawBuffer.put(drawArray);
        drawBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] vMatrix) {
        GLES20.glUseProgram(mProgram);

        int matrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, vMatrix, 0);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

        int colorHandle = GLES20.glGetAttribLocation(mProgram, "vColorVertex");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 16, colorBuffer);

        if (drawLinesOnly) {
            GLES20.glDrawElements(GLES20.GL_LINES, drawArray.length, GLES20.GL_UNSIGNED_SHORT, drawBuffer);
        } else {
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawArray.length, GLES20.GL_UNSIGNED_SHORT, drawBuffer);
        }

        GLES20.glDisableVertexAttribArray(colorHandle);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
