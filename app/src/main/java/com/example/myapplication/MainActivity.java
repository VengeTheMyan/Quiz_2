package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView questionTV, questionNumberTV;
    private Button option1Btn, option2Btn, option3Btn, option4Btn;
    private ArrayList<QuizModal> quizModalArrayList;

    private CountDownTimer countDownTimer;
    private TextView timerTV;
    private boolean quizFinished = false;
    private Button endButton;

    Random random;
    int currentScore = 0, questionAttempted = 0, currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTV = findViewById(R.id.idTVQuestion);
        questionNumberTV = findViewById(R.id.idTVQuestionAttempted);
        timerTV = findViewById(R.id.idTVTimer);
        option1Btn = findViewById(R.id.idBtnOption1);
        option2Btn = findViewById(R.id.idBtnOption2);
        option3Btn = findViewById(R.id.idBtnOption3);
        option4Btn = findViewById(R.id.idBtnOption4);
        random = new Random();
        quizModalArrayList = new ArrayList<>();
        getQuizQuestion(quizModalArrayList);
        currentPos = random.nextInt(quizModalArrayList.size());
        setDataToViews(currentPos);

        option1Btn.setOnClickListener(v -> handleOptionClick(option1Btn));
        option2Btn.setOnClickListener(v -> handleOptionClick(option2Btn));
        option3Btn.setOnClickListener(v -> handleOptionClick(option3Btn));
        option4Btn.setOnClickListener(v -> handleOptionClick(option4Btn));

        String imageUrl = "https://static.wikitide.net/greatcharacterswiki/0/0b/Nickelodeon_SpongeBob_SquarePants_Characters_Cast.png";
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get()
                .load(imageUrl)
                .into(imageView);

        endButton = findViewById(R.id.idBtnEnd);
        endButton.setOnClickListener(v -> showBottomSheet());
    }

    private void handleOptionClick(Button optionBtn) {
        countDownTimer.cancel();
        if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(optionBtn.getText().toString().trim().toLowerCase(Locale.ROOT))) {
            currentScore++;
        }
        questionAttempted++;
        if (!quizModalArrayList.isEmpty()) {
            currentPos = random.nextInt(quizModalArrayList.size());
            setDataToViews(currentPos);
        } else {
            showBottomSheet();
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(21000, 1000) { // 21 seconds
            @Override
            public void onTick(long millisUntilFinished) {
                timerTV.setText("Time Left: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                // Play sound effect on timer expiration
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sadbob);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> mp.release()); // Release the MediaPlayer resource after playback

                questionAttempted++;
                if (quizModalArrayList.isEmpty() || questionAttempted >= 10 || quizFinished) {
                    showBottomSheet();
                    quizFinished = true;
                    return;
                }

                quizModalArrayList.remove(currentPos);

                if (!quizModalArrayList.isEmpty()) {
                    currentPos = random.nextInt(quizModalArrayList.size());
                    setDataToViews(currentPos);
                    startTimer();
                }
            }
        }.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel timer when activity is paused
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel timer when activity is destroyed
        }
    }


    private void saveHighscore() {
        SharedPreferences sharedPreferences = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int currentScore = this.currentScore;
        int previousHighscore = sharedPreferences.getInt("quiz1_highscore", 0);

        if (currentScore > previousHighscore) {
            editor.putInt("quiz1_highscore", currentScore);
            editor.apply();
        }
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.score_bottom_sheet,
                (LinearLayout) findViewById(R.id.idLLScore)
        );

        TextView scoreTV = bottomSheetView.findViewById(R.id.idTvScore);
        scoreTV.setText("Your Score is\n" + currentScore + "/10");

        saveHighscore();

        Button restartQuizBtn = bottomSheetView.findViewById(R.id.idBtnRestart);
        restartQuizBtn.setOnClickListener(v -> {
            currentPos = random.nextInt(quizModalArrayList.size());
            questionAttempted = 0;
            currentScore = 0;
            setDataToViews(currentPos);
            bottomSheetDialog.dismiss();
        });

        Button backToHomeBtn = bottomSheetView.findViewById(R.id.idBtnBackToHome);
        backToHomeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StartQuizActivity.class);
            startActivity(intent);
            finish();
        });

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void setDataToViews(int currentPos) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        questionNumberTV.setText("Questions Attempted: " + questionAttempted + "/10");
        if (questionAttempted >= 10) {
            countDownTimer.cancel();
            showBottomSheet();
        } else {
            questionTV.setText(quizModalArrayList.get(currentPos).getQuestion());
            option1Btn.setText(quizModalArrayList.get(currentPos).getQuestion1());
            option2Btn.setText(quizModalArrayList.get(currentPos).getQuestion2());
            option3Btn.setText(quizModalArrayList.get(currentPos).getQuestion3());
            option4Btn.setText(quizModalArrayList.get(currentPos).getQuestion4());
            startTimer();
        }
    }

    private void getQuizQuestion(ArrayList<QuizModal> quizModalArrayList) {
        quizModalArrayList.add(new QuizModal("What is SpongeBob's job?", "A lifeguard", "A chef at the Krusty Krab", "A teacher", "A mailman", "A chef at the Krusty Krab"));
        quizModalArrayList.add(new QuizModal("Who is SpongeBob's best friend?", "Patrick Star", "Squidward Tentacles", "Mr. Krabs", "Plankton", "Patrick Star"));
        quizModalArrayList.add(new QuizModal("What is the name of SpongeBob's pet snail?", "Gary", "Tom", "Benny", "Spike", "Gary"));
        quizModalArrayList.add(new QuizModal("Where does SpongeBob live?", "In a pineapple under the sea", "In a jellyfish house", "In a giant clam shell", "On a boat", "In a pineapple under the sea"));
        quizModalArrayList.add(new QuizModal("What is the name of the Krusty Krab's rival restaurant?", "Chum Bucket", "Burger Palace", "Seafood Shack", "The Crab Shack", "Chum Bucket"));
        quizModalArrayList.add(new QuizModal("Who is the owner of the Krusty Krab?", "Squidward", "Sandy Cheeks", "Mr. Krabs", "Plankton", "Mr. Krabs"));
        quizModalArrayList.add(new QuizModal("What is the name of the Krusty Krab's secret formula?", "The Krabby Patty Secret Formula", "The Golden Recipe", "The Big Burger", "The Secret Ingredient", "The Krabby Patty Secret Formula"));
        quizModalArrayList.add(new QuizModal("Who is SpongeBob's neighbor?", "Patrick Star", "Squidward Tentacles", "Mr. Krabs", "Sandy Cheeks", "Squidward Tentacles"));
        quizModalArrayList.add(new QuizModal("What does Squidward play?", "Clarinet", "Guitar", "Trumpet", "Drums", "Clarinet"));
        quizModalArrayList.add(new QuizModal("What type of animal is Sandy Cheeks?", "A dog", "A squirrel", "A cat", "A rabbit", "A squirrel"));
        quizModalArrayList.add(new QuizModal("What is Mr. Krabs' favorite thing?", "Gold", "Money", "Crabs", "Fish", "Money"));
        quizModalArrayList.add(new QuizModal("What is Plankton's main goal?", "To steal Mr. Krabs' money", "To make friends", "To take over Bikini Bottom", "To open a restaurant", "To steal Mr. Krabs' money"));
        quizModalArrayList.add(new QuizModal("Who is the voice of SpongeBob SquarePants?", "Tom Kenny", "Billy West", "John DiMaggio", "Dan Castellaneta", "Tom Kenny"));
        quizModalArrayList.add(new QuizModal("What is the name of SpongeBob's boating school teacher?", "Ms. Frizzle", "Mrs. Puff", "Mr. Krabs", "Squidward", "Mrs. Puff"));
        quizModalArrayList.add(new QuizModal("What is the name of SpongeBob's favorite song?", "The Campfire Song Song", "Ripped Pants", "F.U.N. Song", "The Krabby Patty Song", "The Campfire Song Song"));
        quizModalArrayList.add(new QuizModal("Which character has a pet jellyfish?", "Patrick", "Sandy", "Squidward", "Mr. Krabs", "Patrick"));
        quizModalArrayList.add(new QuizModal("What is the name of the city where SpongeBob lives?", "Bikini Bottom", "Rock Bottom", "Atlantis", "Jellyfish Fields", "Bikini Bottom"));
        quizModalArrayList.add(new QuizModal("Who is the superhero alter ego of SpongeBob?", "Captain Man", "Super Sponge", "The Quickster", "Merman Man", "The Quickster"));
        quizModalArrayList.add(new QuizModal("What is the name of SpongeBob's boss?", "Squidward", "Sandy Cheeks", "Mr. Krabs", "Plankton", "Mr. Krabs"));
        quizModalArrayList.add(new QuizModal("Which character is always trying to steal the Krabby Patty secret formula?", "Squidward", "Patrick", "Plankton", "Sandy", "Plankton"));
        quizModalArrayList.add(new QuizModal("What is SpongeBob's favorite food?", "Kelp Shake", "Krabby Patty", "Pickles", "Spaghetti", "Krabby Patty"));
        quizModalArrayList.add(new QuizModal("What is the name of SpongeBob's boss?", "Squidward", "Sandy Cheeks", "Mr. Krabs", "Plankton", "Mr. Krabs"));
        quizModalArrayList.add(new QuizModal("Who is the owner of the Chum Bucket?", "SpongeBob", "Plankton", "Mr. Krabs", "Patrick", "Plankton"));
        quizModalArrayList.add(new QuizModal("Which character is always wearing a tie?", "Patrick", "Sandy Cheeks", "Squidward", "Mr. Krabs", "Squidward"));
        quizModalArrayList.add(new QuizModal("What is the name of the jellyfish that lives with Patrick?", "Squishy", "Jelly", "Moby", "Frank", "Squishy"));
        quizModalArrayList.add(new QuizModal("Who runs the Krusty Krab 2?", "Mr. Krabs", "SpongeBob", "Patrick", "Squidward", "Mr. Krabs"));
        quizModalArrayList.add(new QuizModal("Who does SpongeBob idolize?", "Plankton", "Mrs. Puff", "Sandy Cheeks", "Krusty Krab", "Sandy Cheeks"));
        quizModalArrayList.add(new QuizModal("What does Squidward do for fun?", "He plays clarinet", "He likes to go to the beach", "He goes jellyfishing", "He enjoys working", "He plays clarinet"));
        quizModalArrayList.add(new QuizModal("What is Sandy's favorite hobby?", "Science", "Jellyfishing", "Tennis", "Rock climbing", "Science"));
        quizModalArrayList.add(new QuizModal("What is the name of SpongeBob's favorite movie?", "The Adventures of Mermaid Man and Barnacle Boy", "SpongeBob SquarePants: The Movie", "Jaws", "The Fast and the Furious", "SpongeBob SquarePants: The Movie"));

    }
}
