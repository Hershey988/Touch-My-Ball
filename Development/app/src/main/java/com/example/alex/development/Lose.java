package com.example.alex.development;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Alex on 9/9/2016.
 */
public class Lose extends Activity {

    public void menuPress(View view) {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);
    }

    public void playPress(View view) {
        Intent intent = new Intent(getApplicationContext(), LoadingScreen.class);
        startActivity(intent);
    }

    /*
    Checks to see if the current score was within the top 5 scores
    if it is then it is added to it's respective place
    otherwise we discard the score.
    score5 holds lowest high score
    score1 holds highest score
    currScore holds the score the user got this round
    returns true if there is a high score
    returns false if there is no high score
     */


    public boolean checkHighScore(int currScore) {
        String scoreKey = getResources().getString(R.string.score_key);;

        SharedPreferences userHighScores = getSharedPreferences(scoreKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = getSharedPreferences(scoreKey, Context.MODE_PRIVATE).edit();
        int defaultValue = 0;
        String[] topScores = getResources().getStringArray(R.array.top_scores);

//        int newHighScore = -1;
        for (int i = 0; i < topScores.length; i++) {
            int prevScore = userHighScores.getInt(topScores[i], defaultValue);
            //Uncomment the next 2 lines to reset the highScores to 0 remember to recomment after
//            edit.putInt(topScores[i], 0);
//            edit.commit();
            if (prevScore < currScore) {
                for (int j = topScores.length - 1; j > i; j--){
                    prevScore = userHighScores.getInt(topScores[j-1], defaultValue);
                    edit.putInt(topScores[j], prevScore);
                }

                edit.putInt(topScores[i], currScore);
                edit.commit();//You gotta commit to your scores human
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_screen);
        TextView loseScreen = (TextView) findViewById(R.id.scoreText);
        Bundle getResults = getIntent().getExtras(); //Gets results from the last activity Play
        int score = -1;
        if (getResults != null) {
            score = getResults.getInt("Score");
        }
        if (score < 2) {
            score = 0;
        }
        int level = getResults.getInt("Level");
        loseScreen.setText("Score:" + score);

        boolean newHighScore = checkHighScore(score);
        if(newHighScore) {
            loseScreen = (TextView) findViewById(R.id.highScoreText);
            loseScreen.setText("A NEW HIGH SCORE!");
        }
        loseScreen = (TextView) findViewById(R.id.levelText);
        loseScreen.setText("Final Level: " + level);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
