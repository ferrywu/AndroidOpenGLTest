package com.example.opengltest;

class ShapeType {
    public static final int TRIANGLE = 0;
    public static final int SQUARE = 1;
    public static final int POLYGON = 2;
    public static final int CIRCLE = 3;
    public static final int POLYGON_CIRCLE = 4;
    public static final int CUBE = 5;

    public static boolean is2DShape(int shapeType) {
        switch (shapeType) {
            case ShapeType.TRIANGLE:
            case ShapeType.SQUARE:
            case ShapeType.POLYGON:
            case ShapeType.CIRCLE:
            case ShapeType.POLYGON_CIRCLE:
                return true;
            case ShapeType.CUBE:
                return false;
            default:
                return true;
        }
    }

    public static boolean is3DShape(int shapeType) {
        return !is2DShape(shapeType);
    }
}

class ShapeOperation {
    public static final int AUTO_ROTATE = 0;
    public static final int MANUAL_ROTATE = 1;
    public static final int MANUAL_SCALE = 2;
}
