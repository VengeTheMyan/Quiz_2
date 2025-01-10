package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;  // Import Picasso

public class StartQuizActivity extends AppCompatActivity {
    private Button startQuiz2Button; // Declare the Start Quiz 2 button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        // Initialize the Start Quiz button and image view
        Button startQuizButton = findViewById(R.id.startQuizButton);
        startQuiz2Button = findViewById(R.id.idBtnStartQuiz2); // Initialize the Start Quiz 2 button
        ImageView imageView = findViewById(R.id.imageView);

        String imageUrl = "https://static.wikitide.net/greatcharacterswiki/f/f9/Xfgfjbvdhbvjhsdb.png";
        Picasso.get()
                .load(imageUrl)  // The image URL
                .into(imageView);  // The ImageView where the image will be loaded

        startQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartQuizActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Handle the Start Quiz 2 button click event
        startQuiz2Button.setOnClickListener(v -> {
            Intent intent = new Intent(StartQuizActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }
}
