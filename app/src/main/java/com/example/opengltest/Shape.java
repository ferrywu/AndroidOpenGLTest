package com.example.opengltest;

import android.graphics.Bitmap;
import android.opengl.GLES20;

abstract class Shape {
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
