package com.example.opengltest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Polygon extends Shape {
    private final int numOfEdges;
    private final float radius = 0.5f;

    private final FloatBuffer vertexBuffer;
    private final float[] vertexArray;

    private final float[] colorArray = { 0.0f, 1.0f, 0.0f, 1.0f };

    private final String vertexShaderCode =
            "uniform mat4 vMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final int mProgram;

    private float[] createPosition() {
        float[] data = new float[(numOfEdges+2)*3];
        float angle = 360f / numOfEdges;
        float angleSpan = 0.0f;

        data[0] = 0.0f;
        data[1] = 0.0f;
        data[2] = 0.0f;

        for (int i = 3; i < data.length; i += 3, angleSpan += angle) {
            data[i] = (float)(radius*Math.cos(angleSpan*Math.PI/180f));
            data[i+1] = (float)(radius*Math.sin(angleSpan*Math.PI/180f));
            data[i+2] = 0.0f;
        }

        return data;
    }

    public Polygon(int numOfEdges) {
        this.numOfEdges = numOfEdges;

        vertexArray = createPosition();
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);

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

        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, colorArray, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexArray.length/3);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
