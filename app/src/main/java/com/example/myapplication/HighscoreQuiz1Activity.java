package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HighscoreQuiz1Activity extends AppCompatActivity {
    private TextView highscoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_quiz1);

        highscoreTextView = findViewById(R.id.tvQuiz1Highscore);
        SharedPreferences sharedPreferences = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
        int highscore = sharedPreferences.getInt("quiz1_highscore", 0);

        highscoreTextView.setText("Highscore for Quiz 1: " + highscore);
    }
}
