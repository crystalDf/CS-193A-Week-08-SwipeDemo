package com.star.swipedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setOnTouchListener(new OnSwipeListener(this) {
            {
                setDragHorizontal(true);
                setExitScreenOnSwipe(true);
                setAnimationDelay(1000);
            }

            @Override
            public void onSwipeLeft(float distance) {
                super.onSwipeLeft(distance);
                Toast.makeText(MainActivity.this, "swiped left", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSwipeRight(float distance) {
                super.onSwipeRight(distance);
                Toast.makeText(MainActivity.this, "swiped right", Toast.LENGTH_LONG).show();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

}
