package com.example.opengltest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sphere {
    private final float radius = 0.5f;
    private final float latitudeStep = 2.0f;
    private final float longitudeStep = latitudeStep * 2;

    private final FloatBuffer vertexBuffer;
    private final float[] vertexArray;

    private final String vertexShaderCode =
            "precision mediump float;" +
            "uniform mat4 vMatrix;" +
            "attribute vec4 vPosition;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_Position = vMatrix * vPosition;" +
            "  /* set color value between 0.2 and 0.8 */" +
            "  vColor = abs(vPosition) * 0.6f + 0.2f;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final int mProgram;

    private float[] createPosition() {
        int numOfVertex = ((int)(180.0f / latitudeStep) + 1) * ((int)(360.0f / longitudeStep) + 1) * 2;
        float[] data = new float[numOfVertex * 3];
        int index = 0;

        for (float i = -90f; i < 90f+latitudeStep; i += latitudeStep) {
            float r1 = (float) (radius * Math.cos(i * Math.PI / 180f));
            float r2 = (float) (radius * Math.cos((i + latitudeStep) * Math.PI / 180f));
            float h1 = (float) (radius * Math.sin(i * Math.PI / 180f));
            float h2 = (float) (radius * Math.sin((i + latitudeStep) * Math.PI / 180f));
            for (float j = 0f; j < 360f+longitudeStep; j += longitudeStep) {
                float cos = (float) Math.cos(j * Math.PI / 180f);
                float sin = (float) -Math.sin(j * Math.PI / 180f);
                data[index++] = r2 * cos;
                data[index++] = h2;
                data[index++] = r2 * sin;
                data[index++] = r1 * cos;
                data[index++] = h1;
                data[index++] = r1 * sin;
            }
        }

        return data;
    }

    public Sphere() {
        vertexArray = createPosition();
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);

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

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexArray.length/3);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
