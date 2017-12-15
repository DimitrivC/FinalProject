package com.example.dimitrivc.finalproject;

/**
 * Created by DimitrivC on 9-12-2017. Object to be stored in Firebase. Each user of the quiz app
 * gets one, with their emailadress, and with their score.
 */

public class Score {

    public Integer score;
    public String email;

    // Default constructor for firebase
    public Score() {}

    public Score(Integer aScore,  String aEmail){

        this.score = aScore;
        this.email = aEmail;
    }
}
