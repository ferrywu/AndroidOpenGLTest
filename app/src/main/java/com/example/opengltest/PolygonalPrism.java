package com.example.opengltest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class PolygonalPrism extends Shape {
    private final int numOfEdges;
    private final float radius = 0.5f;

    private final FloatBuffer vertexBuffer;
    private final float[] vertexArray;

    private final ShortBuffer drawBuffer1;
    private final ShortBuffer drawBuffer2;
    private final ShortBuffer drawBuffer3;

    private final float[] colorBottomArray = { 1.0f, 0.0f, 0.0f, 1.0f };
    private final float[] colorTopArray = { 0.0f, 1.0f, 0.0f, 1.0f };

    private final String vertexShaderCode =
            "uniform mat4 vMatrix;" +
            "attribute vec4 vPosition;" +
            "uniform vec4 vColorBottom;" +
            "uniform vec4 vColorTop;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_Position = vMatrix * vPosition;" +
            "  if (vPosition.z <= 0.0f) {" +
            "    vColor = vColorBottom;" +
            "  } else {" +
            "    vColor = vColorTop;" +
            "  }" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final int mProgram;

    private float[] createPosition() {
        float[] data = new float[(numOfEdges+2)*2*3];
        float angle = 360f / numOfEdges;
        float angleSpan = 0.0f;
        int indexButton = (numOfEdges + 2) * 3;

        data[0] = 0.0f;
        data[1] = 0.0f;
        data[2] = -radius;
        data[indexButton] = 0.0f;
        data[indexButton+1] = 0.0f;
        data[indexButton+2] = radius;

        for (int i = 3; i < data.length/2; i += 3, angleSpan += angle) {
            data[i] = (float)(radius*Math.cos(angleSpan*Math.PI/180f));
            data[i+1] = (float)(radius*Math.sin(angleSpan*Math.PI/180f));
            data[i+2] = -radius;
            data[indexButton+i] = data[i];
            data[indexButton+i+1] = data[i+1];
            data[indexButton+i+2] = radius;
        }

        return data;
    }

    public PolygonalPrism(int numOfEdges) {
        this.numOfEdges = numOfEdges;

        vertexArray = createPosition();
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);

        ByteBuffer dbb1 = ByteBuffer.allocateDirect((numOfEdges + 2) * 2);
        dbb1.order(ByteOrder.nativeOrder());
        drawBuffer1 = dbb1.asShortBuffer();
        for (int i = 0; i < numOfEdges+2; i++)
            drawBuffer1.put((short)i);
        drawBuffer1.position(0);

        ByteBuffer dbb2 = ByteBuffer.allocateDirect((numOfEdges + 2) * 2);
        dbb2.order(ByteOrder.nativeOrder());
        drawBuffer2 = dbb2.asShortBuffer();
        for (int i = numOfEdges+2; i < (numOfEdges+2)*2; i++)
            drawBuffer2.put((short)i);
        drawBuffer2.position(0);

        ByteBuffer dbb3 = ByteBuffer.allocateDirect((numOfEdges + 1) * 2 * 2);
        dbb3.order(ByteOrder.nativeOrder());
        drawBuffer3 = dbb3.asShortBuffer();
        for (int i = 1; i < numOfEdges+2; i++) {
            drawBuffer3.put((short)i);
            drawBuffer3.put((short)(i+numOfEdges+2));
        }
        drawBuffer3.position(0);

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

        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColorBottom");
        GLES20.glUniform4fv(colorHandle, 1, colorBottomArray, 0);
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColorTop");
        GLES20.glUniform4fv(colorHandle, 1, colorTopArray, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, numOfEdges+2, GLES20.GL_UNSIGNED_SHORT, drawBuffer1);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, numOfEdges+2, GLES20.GL_UNSIGNED_SHORT, drawBuffer2);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, (numOfEdges+1)*2, GLES20.GL_UNSIGNED_SHORT, drawBuffer3);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    @Override
    public boolean is2DShape() {
        return false;
    }

    @Override
    public float[] getInitialAngle() {
        return new float[]{ 55.365818f, -35.413776f, 0.0f };
    }
}
