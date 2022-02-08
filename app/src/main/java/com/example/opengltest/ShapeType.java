package com.example.opengltest;

import java.util.HashMap;
import java.util.Map;

enum ShapeType {
    TRIANGLE(R.id.shape_type_triangle) {
        @Override
        public Shape createShape() {
            return new Triangle();
        }
    },
    SQUARE(R.id.shape_type_square) {
        @Override
        public Shape createShape() {
            return new Square();
        }
    },
    POLYGON(R.id.shape_type_polygon) {
        @Override
        public Shape createShape() {
            return new Polygon(6);
        }
    },
    CIRCLE(R.id.shape_type_circle) {
        @Override
        public Shape createShape() {
            return new Circle();
        }
    },
    CUBE(R.id.shape_type_cube) {
        @Override
        public Shape createShape() {
            return new Cube();
        }
    },
    PYRAMID(R.id.shape_type_pyramid) {
        @Override
        public Shape createShape() {
            return new Pyramid();
        }
    },
    POLYGONAL_PYRAMID(R.id.shape_type_polygonal_pyramid) {
        @Override
        public Shape createShape() {
            return new PolygonalPyramid(6);
        }
    },
    CONE(R.id.shape_type_cone) {
        @Override
        public Shape createShape() {
            return new Cone();
        }
    },
    POLYGONAL_PRISM(R.id.shape_type_polygonal_prism) {
        @Override
        public Shape createShape() {
            return new PolygonalPrism(6);
        }
    },
    CYLINDER(R.id.shape_type_cylinder) {
        @Override
        public Shape createShape() {
            return new Cylinder();
        }
    },
    SPHERE(R.id.shape_type_sphere) {
        @Override
        public Shape createShape() {
            return new Sphere();
        }
    },
    SQUARE_TEXTURE(R.id.shape_type_square_texture) {
        @Override
        public Shape createShape() {
            return new SquareTexture();
        }
        @Override
        public boolean isTexture() {
            return true;
        }
    };

    private final int id;

    private static final Map<Integer, ShapeType> lookup = new HashMap<>();
    static {
        for (ShapeType shapeType : ShapeType.values()) {
            lookup.put(shapeType.getId(), shapeType);
        }
    }

    ShapeType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract Shape createShape();

    public boolean isTexture() {
        return false;
    }

    public static ShapeType fromId(int id) {
        return lookup.get(id);
    }
}
