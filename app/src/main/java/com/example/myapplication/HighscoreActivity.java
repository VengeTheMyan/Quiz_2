package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HighscoreActivity extends AppCompatActivity {

    private TextView quiz1HighscoreTV, quiz2HighscoreTV, resetHighscoresTV;

    private static final String CHANNEL_ID = "highscore_reset_channel"; // Unique channel ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);  // Ensure this points to the correct layout

        // Create notification channel for highscore reset notifications
        createNotificationChannel();

        // Initialize the TextViews for displaying high scores
        quiz1HighscoreTV = findViewById(R.id.idTvQuiz1Highscore);
        quiz2HighscoreTV = findViewById(R.id.idTvQuiz2Highscore);
        resetHighscoresTV = findViewById(R.id.idTvResetHighscores);  // Initialize the reset button

        // Set up the Toolbar and enable the "Up" button (back button)
        Toolbar toolbar = findViewById(R.id.toolbar);  // Ensure the Toolbar ID is correct
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Enable back navigation
        getSupportActionBar().setDisplayShowHomeEnabled(true);  // Show home button

        // Retrieve the stored high scores from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
        int quiz1Highscore = sharedPreferences.getInt("quiz1_highscore", 0);  // Default to 0 if no score saved
        int quiz2Highscore = sharedPreferences.getInt("quiz2_highscore", 0);  // Default to 0 if no score saved

        // Display the high scores in the TextViews
        quiz1HighscoreTV.setText("Spongebob Quiz Highscore: " + quiz1Highscore);
        quiz2HighscoreTV.setText("Shrek Quiz Highscore: " + quiz2Highscore);

        // Set up click listener for "Reset Highscores"
        resetHighscoresTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the highscores in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Reset quiz scores to 0
                editor.putInt("quiz1_highscore", 0);
                editor.putInt("quiz2_highscore", 0);
                editor.apply();

                // Update the UI to reflect the reset scores
                quiz1HighscoreTV.setText("Spongebob Quiz Highscore: 0");
                quiz2HighscoreTV.setText("Shrek Quiz Highscore: 0");

                // Display a toast message to inform the user
                Toast.makeText(HighscoreActivity.this, "Highscores Reset", Toast.LENGTH_SHORT).show();

                // Send a notification about the highscore reset
                sendHighscoreResetNotification();
            }
        });
    }

    // Handle the back navigation (Up button)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();  // Navigate back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Create notification channel for Android 8.0+ (Oreo and later)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Highscore Reset Channel";
            String description = "Channel for highscore reset notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Send a notification about the highscore reset
    private void sendHighscoreResetNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Highscores Reset")
                    .setContentText("Your quiz highscores have been reset to 0.")
                    .setSmallIcon(R.drawable.quiz_icon)  // Use your own icon here
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .build();
        }

        // Send the notification (0 is the notification ID)
        notificationManager.notify(0, notification);
    }
}
