package com.example.opengltest;

import java.util.HashMap;
import java.util.Map;

enum TextureFilter {
    NONE(R.id.texture_filter_none, 0, new float[]{ 0.0f, 0.0f, 0.0f }),
    GRAY(R.id.texture_filter_gray, 1, new float[]{ 1.0f/3.0f, 1.0f/3.0f, 1.0f/3.0f }),
    COLD(R.id.texture_filter_cold, 2, new float[]{ 0.0f, 0.0f, 0.2f }),
    WARN(R.id.texture_filter_warn, 2, new float[]{ 0.2f, 0.2f, 0.0f }),
    BLUR(R.id.texture_filter_blur, 3, new float[]{ 0.012f, 0.006f, 0.003f }),
    MAGN(R.id.texture_filter_magnify, 4, new float[]{ 0.0f, 0.0f, 0.15f });

    private final int id;
    private final int vChangeType;
    private final float[] data;

    private static final Map<Integer, TextureFilter> lookup = new HashMap<>();
    static {
        for (TextureFilter textureFilter : TextureFilter.values()) {
            lookup.put(textureFilter.getId(), textureFilter);
        }
    }

    TextureFilter(int id, int vChangeType, float[] data) {
        this.id = id;
        this.vChangeType = vChangeType;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return vChangeType;
    }

    public float[] data() {
        return data;
    }

    public static TextureFilter fromId(int id) {
        return lookup.get(id);
    }
}
