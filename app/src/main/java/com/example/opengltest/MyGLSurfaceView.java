package com.example.opengltest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer glRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        glRenderer = new MyGLRenderer(context);
        setRenderer(glRenderer);
        if (glRenderer.renderWhenDirty())
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (glRenderer.onTouchEvent(event, getWidth(), getHeight())) {
            requestRender();
        }
        return true;
    }
}
