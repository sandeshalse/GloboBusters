package com.sandesh.GloboBusters.utility;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by sande on 3/30/2017.
 */

public class Score {
    private static final String PGLOBAL = "prefs_global";
    private static final String PHIGH_SCORE = "pref_top_score";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(
                PGLOBAL, Context.MODE_PRIVATE);
    }


    public static boolean isHighScore(Context context, int newScore) {
        int topScore = getPreferences(context).getInt(PHIGH_SCORE, 0);
        return newScore > topScore;
    }

    public static int getHighScore(Context context) {
        return getPreferences(context).getInt(PHIGH_SCORE, 0);
    }

    public static void setHighScore(Context context, int score) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(PHIGH_SCORE, score);
        editor.apply();
    }
}
