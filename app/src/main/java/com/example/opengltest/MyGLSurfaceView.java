package com.example.opengltest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer glRenderer;
    private final int shapeType = ShapeType.POLYGON_CIRCLE;
    private final int shapeOperation = ShapeOperation.MANUAL_ROTATE;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        glRenderer = new MyGLRenderer(shapeType, shapeOperation);
        setRenderer(glRenderer);
        if (shapeOperation != ShapeOperation.AUTO_ROTATE)
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;

                switch (shapeOperation) {
                    case ShapeOperation.MANUAL_ROTATE:
                        if (y > getHeight() / 2.0f) {
                            dx = dx * -1;
                        }
                        if (x < getWidth() / 2.0f) {
                            dy = dy * -1;
                        }
                        glRenderer.setAngle(glRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
                        break;

                    case ShapeOperation.MANUAL_SCALE:
                        glRenderer.setScale(dx/getWidth()*2, dy/getHeight());
                        break;
                }

                requestRender();
                break;
        }

        previousX = x;
        previousY = y;
        return true;
    }
}
