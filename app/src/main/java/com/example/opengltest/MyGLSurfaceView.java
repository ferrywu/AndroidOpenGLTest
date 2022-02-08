package com.example.opengltest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer glRenderer;

    public MyGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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

    @Override
    public void onResume() {
        if (glRenderer.renderWhenDirty())
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        else
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        super.onResume();
    }
}
