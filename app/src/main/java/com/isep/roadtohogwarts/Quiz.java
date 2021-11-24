package com.isep.roadtohogwarts;

import java.util.List;

public class Quiz {
    private List<Question> questionList;
    private int score;
    private int questionNumber;

    public Quiz(List<Question> questionList) {
        this.questionList = questionList;
        this.score =0;
        this.questionNumber=1;

    }


    public List<Question> getQuestionList() {
        return questionList;
    }

    public int getScore() {
        return score;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void addPoints(){
        this.score = score+1;
    }

    public void nextQuestion(){
        this.questionNumber= questionNumber+1;
    }
    public Question getCurrentQuestion(){
        return questionList.get(questionNumber-1);
    }


}
