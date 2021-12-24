package com.example.opengltest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer glRenderer;
    private final int shapeType = ShapeType.PYRAMID;
    private final int shapeOperation = ShapeOperation.MANUAL_ROTATE;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        glRenderer = new MyGLRenderer(shapeType, shapeOperation);
        setRenderer(glRenderer);
        if (shapeOperation != ShapeOperation.AUTO_ROTATE)
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 0.6f;
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
                        float[] angle = glRenderer.getAngle();
                        if (ShapeType.is2DShape(shapeType)) {
                            if (y < getHeight() / 2.0f) {
                                dx = dx * -1;
                            }
                            if (x > getWidth() / 2.0f) {
                                dy = dy * -1;
                            }
                            angle[2] += (dx + dy) * TOUCH_SCALE_FACTOR;
                        } else {
                            angle[0] += dy * TOUCH_SCALE_FACTOR;
                            angle[0] %= 360;
                            angle[1] += dx * TOUCH_SCALE_FACTOR;
                            angle[1] %= 360;
                        }
                        glRenderer.setAngle(angle);
                        break;

                    case ShapeOperation.MANUAL_SCALE:
                        float[] scale = glRenderer.getScale();
                        int width = getWidth();
                        int height = getHeight();
                        scale[0] += dx / width * (width > height ? width/height : 1);
                        scale[1] += dy / getHeight() * (height > width ? height/width : 1);
                        if (ShapeType.is2DShape(shapeType))
                            scale[2] = 0.0f;
                        else
                            scale[2] += (dx + dy) / (getWidth() + getHeight());
                        glRenderer.setScale(scale);
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
