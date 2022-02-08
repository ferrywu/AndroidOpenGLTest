package com.example.opengltest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyGLSurfaceView myGLSurfaceView = findViewById(R.id.myGLSurfaceView);

        Button typeButton = findViewById(R.id.typeButton);
        Button opButton = findViewById(R.id.opButton);
        Button filterButton = findViewById(R.id.filterButton);

        typeButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, typeButton);
            popupMenu.getMenuInflater().inflate(R.menu.shape_type, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Configuration.shapeType = ShapeType.fromId(menuItem.getItemId());
                filterButton.setVisibility(Configuration.shapeType.isTexture() ? View.VISIBLE : View.GONE);
                myGLSurfaceView.onPause();
                myGLSurfaceView.onResume();
                return true;
            });
            popupMenu.show();
        });

        opButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, opButton);
            popupMenu.getMenuInflater().inflate(R.menu.shape_operation, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Configuration.shapeOperation = ShapeOperation.fromId(menuItem.getItemId());
                myGLSurfaceView.onPause();
                myGLSurfaceView.onResume();
                return true;
            });
            popupMenu.show();
        });

        filterButton.setVisibility(Configuration.shapeType.isTexture() ? View.VISIBLE : View.GONE);
        filterButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, filterButton);
            popupMenu.getMenuInflater().inflate(R.menu.texture_filter, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Configuration.textureFilter = TextureFilter.fromId(menuItem.getItemId());
                myGLSurfaceView.onPause();
                myGLSurfaceView.onResume();
                return true;
            });
            popupMenu.show();
        });
    }
}