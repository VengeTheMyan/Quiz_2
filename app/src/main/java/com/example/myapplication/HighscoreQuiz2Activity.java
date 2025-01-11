package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HighscoreQuiz2Activity extends AppCompatActivity {
    private TextView highscoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_quiz2);

        highscoreTextView = findViewById(R.id.tvQuiz2Highscore);

        // Retrieve the highscore from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
        int highscore = sharedPreferences.getInt("quiz2_highscore", 0);

        // Display the highscore
        highscoreTextView.setText("Highscore for Quiz 2: " + highscore);
    }
}
