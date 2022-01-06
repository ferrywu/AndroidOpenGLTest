package com.example.opengltest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.opengl.GLSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new MyGLSurfaceView(this);
        setContentView(glSurfaceView);
    }
}