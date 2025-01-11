package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity2 extends AppCompatActivity {
    private TextView questionTV, questionNumberTV;
    private Button option1Btn, option2Btn, option3Btn, option4Btn;
    private ArrayList<QuizModal2> quizModalArrayList;

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

        // Set OnClickListener for option1Btn
        option1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase()
                        .equals(option1Btn.getText().toString().trim().toLowerCase(Locale.ROOT))) {
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
        });

        // Set OnClickListener for option2Btn
        option2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase()
                        .equals(option2Btn.getText().toString().trim().toLowerCase(Locale.ROOT))) {
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
        });

        // Set OnClickListener for option3Btn
        option3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase()
                        .equals(option3Btn.getText().toString().trim().toLowerCase(Locale.ROOT))) {
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
        });

        // Set OnClickListener for option4Btn
        option4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase()
                        .equals(option4Btn.getText().toString().trim().toLowerCase(Locale.ROOT))) {
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
        });
        String imageUrl = "https://static.wikitide.net/greatcharacterswiki/9/9b/GoodShrekImage.png";
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get()
                .load(imageUrl)  // The image URL
                .into(imageView);  // The ImageView where the image will be loaded
        endButton = findViewById(R.id.idBtnEnd);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(); // Show the bottom sheet
            }
        });

    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(21000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTV.setText("Time Left: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
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
    private void saveHighscore() {
        // Get the current highscore stored in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("QuizHighscores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Use the actual current score from the quiz
        int currentScore = this.currentScore; // Use the score calculated during the quiz

        // Get the previous highscore for Quiz 2
        int previousHighscore = sharedPreferences.getInt("quiz2_highscore", 0);

        // Only save the new highscore if it is higher than the previous one
        if (currentScore > previousHighscore) {
            // Save the new highscore for Quiz 2
            editor.putInt("quiz2_highscore", currentScore);
            editor.apply();
        }
    }
    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity2.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.score_bottom_sheet,
                (LinearLayout) findViewById(R.id.idLLScore)
        );
        saveHighscore();
        TextView scoreTV = bottomSheetView.findViewById(R.id.idTvScore);
        scoreTV.setText("Your Score is\n" + currentScore + "/10");

        Button restartQuizBtn = bottomSheetView.findViewById(R.id.idBtnRestart);
        restartQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = random.nextInt(quizModalArrayList.size());
                questionAttempted = 0;
                currentScore = 0;
                setDataToViews(currentPos);
                bottomSheetDialog.dismiss();
            }
        });

        Button backToHomeBtn = bottomSheetView.findViewById(R.id.idBtnBackToHome);
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, StartQuizActivity.class);
                startActivity(intent);
                finish();
            }
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

    private void getQuizQuestion(ArrayList<QuizModal2> quizModalArrayList) {
        quizModalArrayList.add(new QuizModal2("What is the name of Shrek's wife?", "Fiona", "Ginger", "Mary", "Bella", "Fiona"));
        quizModalArrayList.add(new QuizModal2("What animal is Donkey?", "Horse", "Donkey", "Dragon", "Elephant", "Donkey"));
        quizModalArrayList.add(new QuizModal2("Who is Shrek's best friend?", "Fiona", "Donkey", "Puss in Boots", "Lord Maximus", "Donkey"));
        quizModalArrayList.add(new QuizModal2("What is the name of the dragon who falls in love with Donkey?", "Daphne", "Dragonella", "Elizabeth", "Dragon", "Dragon"));
        quizModalArrayList.add(new QuizModal2("Which villain tries to take over Farquaad's kingdom in the first Shrek movie?", "Lord Maximus", "Prince Charming", "Lord Farquaad", "The Big Bad Wolf", "Lord Farquaad"));
        quizModalArrayList.add(new QuizModal2("What is the name of Shrek’s swamp?", "Shrek's Swamp", "Fiona's Swamp", "Dragon's Lair", "The Murky Marsh", "Shrek's Swamp"));
        quizModalArrayList.add(new QuizModal2("What is the name of Fiona's father?", "King Harold", "King Ferdinand", "King Arthur", "King Charles", "King Harold"));
        quizModalArrayList.add(new QuizModal2("Who is the voice of Shrek?", "Mike Myers", "Eddie Murphy", "Antonio Banderas", "Chris Farley", "Mike Myers"));
        quizModalArrayList.add(new QuizModal2("Who is the voice of Donkey?", "Chris Rock", "Eddie Murphy", "Jack Black", "Will Smith", "Eddie Murphy"));
        quizModalArrayList.add(new QuizModal2("Which character says the line: 'I'm an ogre! You know, grab your torch and pitchforks!'?", "Shrek", "Donkey", "Lord Farquaad", "Fiona", "Shrek"));
        quizModalArrayList.add(new QuizModal2("Which character becomes a 'noble steed' to Shrek?", "Donkey", "Puss in Boots", "Fiona", "Lord Farquaad", "Donkey"));
        quizModalArrayList.add(new QuizModal2("Who is the villain in Shrek 2?", "Prince Charming", "Lord Farquaad", "Dragon", "The Big Bad Wolf", "Prince Charming"));
        quizModalArrayList.add(new QuizModal2("What is the name of the potion that makes Fiona thin in Shrek 2?", "Happily Ever After Potion", "Love Potion", "Fiona's Beauty Potion", "The Potion of Thinness", "Happily Ever After Potion"));
        quizModalArrayList.add(new QuizModal2("Which character does Shrek end up fighting in the first movie?", "Fiona", "Donkey", "Lord Farquaad", "The Dragon", "Lord Farquaad"));
        quizModalArrayList.add(new QuizModal2("In Shrek the Third, who is revealed to be the heir to the throne?", "Puss in Boots", "Fiona", "Arthur Pendragon", "Donkey", "Arthur Pendragon"));
        quizModalArrayList.add(new QuizModal2("What is the name of Shrek's father-in-law?", "King Harold", "King Arthur", "King Charming", "King Maximus", "King Harold"));
        quizModalArrayList.add(new QuizModal2("Which character first introduces Shrek as 'The Green Giant'?", "Donkey", "Lord Farquaad", "Puss in Boots", "Dragon", "Donkey"));
        quizModalArrayList.add(new QuizModal2("In Shrek 4, what happens when Shrek makes a deal with Rumpelstiltskin?", "Shrek becomes a human", "Shrek becomes a king", "Shrek forgets about Fiona", "Shrek becomes an ogre again", "Shrek becomes a human"));
        quizModalArrayList.add(new QuizModal2("Which movie shows Shrek trying to fix his life after becoming a father?", "Shrek Forever After", "Shrek the Third", "Shrek 2", "Shrek", "Shrek the Third"));
        quizModalArrayList.add(new QuizModal2("In which movie do we meet Puss in Boots?", "Shrek the Third", "Shrek 2", "Shrek", "Shrek Forever After", "Shrek 2"));

    }
}
