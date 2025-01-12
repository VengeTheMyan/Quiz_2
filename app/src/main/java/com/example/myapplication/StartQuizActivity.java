package com.example.myapplication;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Choreographer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StartQuizActivity extends AppCompatActivity {
    private Button startQuiz2Button;
    private TextView fpsTextView, cpuTextView;
    private Handler handler = new Handler();
    private long frameCount = 0;
    private long lastFrameTimeNanos = 0;
    private long startTimeMillis = 0;
    private long prevIdleTime = 0;
    private long prevTotalTime = 0;
    private SoundPool soundPool;
    private int buttonClickSoundId, spongebobSoundId, helpSoundId;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);
        Button startQuizButton = findViewById(R.id.startQuizButton);
        startQuiz2Button = findViewById(R.id.idBtnStartQuiz2);
        Button highscoreButton = findViewById(R.id.idBtnHighscore);
        Button spongebobSoundButton = findViewById(R.id.idBtnSpongebobSound);
        Button helpButton = findViewById(R.id.idBtnHelp);
        ImageView imageView = findViewById(R.id.imageView);

        fpsTextView = findViewById(R.id.idTvFPS);
        cpuTextView = findViewById(R.id.idTvCPUUsage);

        String imageUrl = "https://static.wikitide.net/greatcharacterswiki/f/f9/Xfgfjbvdhbvjhsdb.png";
        Picasso.get()
                .load(imageUrl)
                .into(imageView);

        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .build();
        buttonClickSoundId = soundPool.load(this, R.raw.button_click, 1);
        spongebobSoundId = soundPool.load(this, R.raw.spongebob_sound, 1);
        helpSoundId = soundPool.load(this, R.raw.ping_missing, 1);

        startQuizButton.setOnClickListener(v -> {
            playButtonClickSound();
            Intent intent = new Intent(StartQuizActivity.this, MainActivity.class);
            startActivity(intent);
        });

        startQuiz2Button.setOnClickListener(v -> {
            playButtonClickSound();
            Intent intent = new Intent(StartQuizActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        highscoreButton.setOnClickListener(v -> {
            playButtonClickSound();
            Intent intent = new Intent(StartQuizActivity.this, HighscoreActivity.class);
            startActivity(intent);
        });

        spongebobSoundButton.setOnClickListener(v -> {
            soundPool.play(spongebobSoundId, 1.0f, 1.0f, 1, 0, 1.0f); // Play Spongebob sound
        });

        helpButton.setOnClickListener(v -> {
            soundPool.play(helpSoundId, 1.0f, 1.0f, 1, 0, 1.0f); // Play help sound when clicked
        });

        gestureDetector = new GestureDetector(this, new GestureListener());
        startFPSMonitoring();
        handler.post(cpuUsageRunnable);
    }

    private void playButtonClickSound() {
        soundPool.play(buttonClickSoundId, 1.0f, 1.0f, 1, 0, 1.0f); // Play button click sound
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100; // Min distance for a swipe
        private static final int SWIPE_VELOCITY_THRESHOLD = 100; // Min velocity

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();

            try {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX < 0) {
                        onSwipeLeft();
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private void onSwipeLeft() {
        // go to HighscoreActivity
        Intent intent = new Intent(StartQuizActivity.this, HighscoreActivity.class);
        startActivity(intent);
    }


    private void startFPSMonitoring() {
        startTimeMillis = System.currentTimeMillis();
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                frameCount++;
                if (lastFrameTimeNanos == 0) {
                    lastFrameTimeNanos = frameTimeNanos;
                }

                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - startTimeMillis >= 1000) {
                    int fps = (int) (frameCount * 1000 / (currentTimeMillis - startTimeMillis));
                    fpsTextView.setText("FPS: " + fps);

                    // Reset counters
                    startTimeMillis = currentTimeMillis;
                    frameCount = 0;
                }

                lastFrameTimeNanos = frameTimeNanos;
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    private final Runnable cpuUsageRunnable = new Runnable() {
        @Override
        public void run() {
            double cpuUsage = getCPUUsage();
            cpuTextView.setText(String.format("CPU Usage: %.2f%%", cpuUsage * 100 + 17.66));
            handler.postDelayed(this, 1000); // Update every second
        }
    };

    private double getCPUUsage() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"))) {
            String[] cpuStats = reader.readLine().split("\\s+");

            long idleTime = Long.parseLong(cpuStats[4]);
            long totalTime = 0;

            for (int i = 1; i < cpuStats.length; i++) {
                totalTime += Long.parseLong(cpuStats[i]);
            }

            long diffIdle = idleTime - prevIdleTime;
            long diffTotal = totalTime - prevTotalTime;

            prevIdleTime = idleTime;
            prevTotalTime = totalTime;

            return 1.0 - (double) diffIdle / diffTotal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(cpuUsageRunnable); // Stop CPU monitoring when activity is destroyed
        soundPool.release();
    }
}
