package com.example.opengltest;

import java.util.HashMap;
import java.util.Map;

enum ShapeOperation {
    AUTO_ROTATE(R.id.shape_operation_auto_rotate),
    MANUAL_ROTATE(R.id.shape_operation_manual_rotate),
    MANUAL_SCALE(R.id.shape_operation_manual_scale);

    private final int id;

    private static final Map<Integer, ShapeOperation> lookup = new HashMap<>();
    static {
        for (ShapeOperation shapeOperation : ShapeOperation.values()) {
            lookup.put(shapeOperation.getId(), shapeOperation);
        }
    }

    ShapeOperation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ShapeOperation fromId(int id) {
        return lookup.get(id);
    }
}
