package com.example.opengltest;

import android.graphics.Bitmap;
import android.opengl.GLES20;

abstract class Shape {
    protected float mAspectRatio;

    public abstract void draw(float[] vMatrix);

    public void createTexture(Bitmap bitmap) {}

    public void releaseTexture() {}

    public int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public boolean is2DShape() {
        return true;
    }

    public boolean is3DShape() {
        return !is2DShape();
    }

    public float[] getInitialAngle() {
        return new float[]{ 0.0f, 0.0f, 0.0f };
    }

    public void setAspectRatio(float ratio) {
        mAspectRatio = ratio;
    }
}

enum ShapeType {
    TRIANGLE {
        @Override
        public Shape createShape() {
            return new Triangle();
        }
    },
    SQUARE {
        @Override
        public Shape createShape() {
            return new Square();
        }
    },
    POLYGON {
        @Override
        public Shape createShape() {
            return new Polygon(6);
        }
    },
    CIRCLE {
        @Override
        public Shape createShape() {
            return new Circle();
        }
    },
    CUBE {
        @Override
        public Shape createShape() {
            return new Cube();
        }
    },
    PYRAMID {
        @Override
        public Shape createShape() {
            return new Pyramid();
        }
    },
    POLYGONAL_PYRAMID {
        @Override
        public Shape createShape() {
            return new PolygonalPyramid(6);
        }
    },
    CONE {
        @Override
        public Shape createShape() {
            return new Cone();
        }
    },
    POLYGONAL_PRISM {
        @Override
        public Shape createShape() {
            return new PolygonalPrism(6);
        }
    },
    CYLINDER {
        @Override
        public Shape createShape() {
            return new Cylinder();
        }
    },
    SPHERE {
        @Override
        public Shape createShape() {
            return new Sphere();
        }
    },
    SQUARE_TEXTURE {
        @Override
        public Shape createShape() {
            return new SquareTexture();
        }
    };

    public abstract Shape createShape();
}

enum ShapeOperation {
    AUTO_ROTATE, MANUAL_ROTATE, MANUAL_SCALE
}

enum Filter {
    NONE(0, new float[]{ 0.0f, 0.0f, 0.0f }),
    GRAY(1, new float[]{ 1.0f/3.0f, 1.0f/3.0f, 1.0f/3.0f }),
    COOL(2, new float[]{ 0.0f, 0.0f, 0.2f }),
    WARN(2, new float[]{ 0.2f, 0.2f, 0.0f }),
    BLUR(3, new float[]{ 0.012f, 0.006f, 0.003f }),
    MAGN(4, new float[]{ 0.0f, 0.0f, 0.15f });

    private final int vChangeType;
    private final float[] data;

    Filter(int vChangeType, float[] data) {
        this.vChangeType = vChangeType;
        this.data = data;
    }

    public int getType() {
        return vChangeType;
    }

    public float[] data() {
        return data;
    }
}
