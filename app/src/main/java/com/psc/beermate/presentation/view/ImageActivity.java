package com.psc.beermate.presentation.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.psc.beermate.R;
import com.squareup.picasso.Picasso;

public class ImageActivity extends Activity {

    public static final String URL_KEY = "URL";
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setUp();
    }

    private void setUp() {
        imageView = findViewById(R.id.image_full_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadImage();
    }

    private void loadImage() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String imageUrl = intent.getStringExtra(URL_KEY);
        Picasso.with(getBaseContext())
                .load(imageUrl)
                .into(imageView);
    }
}
