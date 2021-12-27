package com.example.opengltest;

class ShapeType {
    public static final int TRIANGLE = 0;
    public static final int SQUARE = 1;
    public static final int POLYGON = 2;
    public static final int CIRCLE = 3;
    public static final int POLYGON_CIRCLE = 4;
    public static final int CUBE = 5;
    public static final int PYRAMID = 6;
    public static final int POLYGONAL_PYRAMID = 7;
    public static final int CONE = 8;

    public static boolean is2DShape(int shapeType) {
        switch (shapeType) {
            case ShapeType.TRIANGLE:
            case ShapeType.SQUARE:
            case ShapeType.POLYGON:
            case ShapeType.CIRCLE:
            case ShapeType.POLYGON_CIRCLE:
                return true;
            case ShapeType.CUBE:
            case ShapeType.PYRAMID:
            case ShapeType.POLYGONAL_PYRAMID:
            case ShapeType.CONE:
                return false;
            default:
                return true;
        }
    }

    public static boolean is3DShape(int shapeType) {
        return !is2DShape(shapeType);
    }

    public static float[] getInitialAngle(int shapeType) {
        switch (shapeType) {
            case ShapeType.CUBE:
                return new float[]{ 37.9703f, -205.63934f, 0.0f };
            case ShapeType.PYRAMID:
                return new float[]{ 37.55401f, -68.83713f, 0.0f };
            case ShapeType.POLYGONAL_PYRAMID:
            case ShapeType.CONE:
                return new float[]{ 54.71598f, -143.12686f, 0.0f };
            default:
                return new float[]{ 0.0f, 0.0f, 0.0f };
        }
    }
}

class ShapeOperation {
    public static final int AUTO_ROTATE = 0;
    public static final int MANUAL_ROTATE = 1;
    public static final int MANUAL_SCALE = 2;
}
