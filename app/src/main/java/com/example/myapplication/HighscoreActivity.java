package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
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

    private GestureDetector gestureDetector; // For swipe detection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        createNotificationChannel();

        quiz1HighscoreTV = findViewById(R.id.idTvQuiz1Highscore);
        quiz2HighscoreTV = findViewById(R.id.idTvQuiz2Highscore);
        resetHighscoresTV = findViewById(R.id.idTvResetHighscores);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back navigation
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
        int quiz1Highscore = sharedPreferences.getInt("quiz1_highscore", 0);
        int quiz2Highscore = sharedPreferences.getInt("quiz2_highscore", 0);

        quiz1HighscoreTV.setText("Spongebob Quiz Highscore: " + quiz1Highscore);
        quiz2HighscoreTV.setText("Shrek Quiz Highscore: " + quiz2Highscore);

        resetHighscoresTV.setOnClickListener(v -> {
            SharedPreferences sharedPreferences1 = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();

            editor.putInt("quiz1_highscore", 0);
            editor.putInt("quiz2_highscore", 0);
            editor.apply();

            quiz1HighscoreTV.setText("Spongebob Quiz Highscore: 0");
            quiz2HighscoreTV.setText("Shrek Quiz Highscore: 0");

            Toast.makeText(HighscoreActivity.this, "Highscores Reset", Toast.LENGTH_SHORT).show();
            sendHighscoreResetNotification();
        });
        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100; // Min distance for swipe
        private static final int SWIPE_VELOCITY_THRESHOLD = 100; // Min velocity

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();

            try {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Right Swipe
                        onSwipeRight();
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
    private void onSwipeRight() {
        finish(); // go back to StartQuizActivity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();  // back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
    private void sendHighscoreResetNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Highscores Reset")
                    .setContentText("Your quiz highscores have been reset to 0.")
                    .setSmallIcon(R.drawable.quiz_icon)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .build();
        }

        notificationManager.notify(0, notification);
    }
}
