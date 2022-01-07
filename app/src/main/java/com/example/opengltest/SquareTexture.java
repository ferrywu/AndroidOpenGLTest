package com.example.opengltest;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SquareTexture extends Shape {
    private final Filter filter = Filter.NONE;

    private final FloatBuffer vertexBuffer;
    private final float[] vertexArray = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f
    };

    private final FloatBuffer textureBuffer;
    private final float[] textureArray = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
    };

    private final String vertexShaderCode =
            "uniform mat4 vMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec2 vTexCoordIn;" +
            "varying vec4 vPositionOut;" +
            "varying vec2 vTexCoordOut;" +
            "void main() {" +
            "  gl_Position = vMatrix * vPosition;" +
            "  vPositionOut = gl_Position;" +
            "  vTexCoordOut = vTexCoordIn;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform sampler2D vTexture;" +
            "uniform int vChangeType;" +
            "uniform vec3 vChangeColor;" +
            "uniform float vAspectRatio;" +
            "varying vec4 vPositionOut;" +
            "varying vec2 vTexCoordOut;" +
            "void modifyColor(vec4 color) {" +
            "  color.r = max(min(color.r, 1.0), 0.0);" +
            "  color.g = max(min(color.g, 1.0), 0.0);" +
            "  color.b = max(min(color.b, 1.0), 0.0);" +
            "  color.a = max(min(color.a, 1.0), 0.0);" +
            "}" +
            "void main() {" +
            "  vec4 color = texture2D(vTexture, vTexCoordOut);" +
            "  if (vChangeType == 1) {" +
            "    /* gray */" +
            "    float c = color.r * vChangeColor.r + color.g * vChangeColor.g + color.b * vChangeColor.b;" +
            "    gl_FragColor = vec4(c, c, c, color.a);" +
            "  } else if (vChangeType == 2) {" +
            "    /* warn, cold */" +
            "    vec4 deltaColor = color + vec4(vChangeColor, 0.0);" +
            "    modifyColor(deltaColor);" +
            "    gl_FragColor = deltaColor;" +
            "  } else if (vChangeType == 3) {" +
            "    /* blur */" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x - vChangeColor.r, vTexCoordOut.y - vChangeColor.r));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x - vChangeColor.r, vTexCoordOut.y + vChangeColor.r));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x + vChangeColor.r, vTexCoordOut.y - vChangeColor.r));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x + vChangeColor.r, vTexCoordOut.y + vChangeColor.r));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x - vChangeColor.g, vTexCoordOut.y - vChangeColor.g));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x - vChangeColor.g, vTexCoordOut.y + vChangeColor.g));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x + vChangeColor.g, vTexCoordOut.y - vChangeColor.g));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x + vChangeColor.g, vTexCoordOut.y + vChangeColor.g));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x - vChangeColor.b, vTexCoordOut.y - vChangeColor.b));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x - vChangeColor.b, vTexCoordOut.y + vChangeColor.b));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x + vChangeColor.b, vTexCoordOut.y - vChangeColor.b));" +
            "    color += texture2D(vTexture, vec2(vTexCoordOut.x + vChangeColor.b, vTexCoordOut.y + vChangeColor.b));" +
            "    color /= 13.0;" +
            "    gl_FragColor = color;" +
            "  } else if (vChangeType == 4) {" +
            "    /* magnify */" +
            "    float dis = distance(vec2(vPositionOut.x, vPositionOut.y / vAspectRatio), vec2(vChangeColor.r, vChangeColor.g));" +
            "    if (dis < vChangeColor.b) {" +
            "      color = texture2D(vTexture, vec2(vTexCoordOut.x / 2.0 + 0.25, vTexCoordOut.y / 2.0 + 0.25));" +
            "    }" +
            "    gl_FragColor = color;" +
            "  } else {" +
            "    gl_FragColor = color;" +
            "  }" +
            "}";

    private final int mProgram;
    private int[] mTextures;

    public SquareTexture() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(textureArray.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();
        textureBuffer.put(textureArray);
        textureBuffer.position(0);

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

        int texCoordHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoordIn");
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

        int textureHandle = GLES20.glGetUniformLocation(mProgram, "vTexture");
        GLES20.glUniform1i(textureHandle, 0);

        int changeTypeHandle = GLES20.glGetUniformLocation(mProgram, "vChangeType");
        GLES20.glUniform1i(changeTypeHandle, filter.getType());

        int changeColorHandle = GLES20.glGetUniformLocation(mProgram, "vChangeColor");
        GLES20.glUniform3fv(changeColorHandle, 1, filter.data(), 0);

        int aspectRatioHandle = GLES20.glGetUniformLocation(mProgram, "vAspectRatio");
        GLES20.glUniform1f(aspectRatioHandle, mAspectRatio);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glDisableVertexAttribArray(texCoordHandle);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    @Override
    public void createTexture(Bitmap bitmap) {
        if ((bitmap == null) || (bitmap.isRecycled())) {
            return;
        }

        mTextures = new int[1];
        GLES20.glGenTextures(1, mTextures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    @Override
    public void releaseTexture() {
        if (mTextures != null) {
            GLES20.glDeleteTextures(1, mTextures, 0);
        }
    }
}
