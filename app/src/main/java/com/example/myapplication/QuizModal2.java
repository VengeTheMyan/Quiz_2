package com.example.myapplication;

public class QuizModal2 {

    private String question, question1, question2, question3, question4, answer;

    public QuizModal2(String question, String question1, String question2, String question3, String question4, String answer) {
        this.question = question;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.question4 = question4;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestion1() {
        return question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public String getAnswer() {
        return answer;
    }
}
