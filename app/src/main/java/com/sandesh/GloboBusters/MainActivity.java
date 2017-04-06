package com.sandesh.GloboBusters;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandesh.GloboBusters.utility.Score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Bal.BalloonListener {
    public static final int MinAnmDelay =500;
    public static final int MaxAnmDelay = 1500;
    public static final int pin_no=5;
    public static final int balloonsPerLevel=12;
 private ViewGroup ContVw;
    private int[] balcol=new int[3];
    private int nxtcol,gscrht,gscrwidth;

    private int Lvl;
    private int score;
    private int pinused;
    TextView scoredisp,leveldisp;
    private List<ImageView> pinimages=new ArrayList<>();
    private List<Bal> balloons=new ArrayList<>();
    private Button gobtn;
    private boolean playing;
    private boolean gamestopped=true;
    private int balloonsbursted;
    public static final int MinAnmDur = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balcol[0]= Color.argb(255,255,0,0);
        balcol[1]= Color.argb(255,0,255,0);
        balcol[2]= Color.argb(255,0,0,255);

        getWindow().setBackgroundDrawableResource(R.drawable.appbgimage);

        ContVw = (ViewGroup) findViewById(R.id.activity_main);

        makefullscreen();
        ViewTreeObserver viewTreeObserver=ContVw.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ContVw.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    gscrwidth = ContVw.getWidth();
                    gscrht = ContVw.getHeight();
                }
            });
        }

        ContVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makefullscreen();
            }
        });

        scoredisp= (TextView) findViewById(R.id.score_display);
        leveldisp= (TextView) findViewById(R.id.level_display);
        pinimages.add((ImageView) findViewById(R.id.pin1));
        pinimages.add((ImageView) findViewById(R.id.pin2));
        pinimages.add((ImageView) findViewById(R.id.pin3));
        pinimages.add((ImageView) findViewById(R.id.pin4));
        pinimages.add((ImageView) findViewById(R.id.pin5));
        gobtn= (Button) findViewById(R.id.start_button);

        updatescoreonscreen();

      /*  ContVw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    Bal b=new Bal(MainActivity.this,balcol[nxtcol],100);
                    b.setX(event.getX());
                    b.setY(gscrht); //start from bottom of screen
                    ContVw.addView(b);
                    b.bal_rel(gscrht,3000);

                    if(nxtcol+1==balcol.length)
                    {
                        nxtcol=0;
                    }
                    else
                    {
                        nxtcol++;
                    }

                }

                return false;
            }
        });*/

    }

    private void makefullscreen(){
        ViewGroup rlt= (ViewGroup) findViewById(R.id.activity_main);
        rlt.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    protected void onResume(){
        super.onResume();
        makefullscreen();
    }
    private void startGame() {
        makefullscreen();
        score = 0;
        Lvl = 0;
        pinused = 0;
        for (ImageView pin :
                pinimages) {
            pin.setImageResource(R.drawable.needle);
        }
        gamestopped = false;
        startLevel();
    }

    private void startLevel() {
        Lvl++;
        updatescoreonscreen();
        ShootBal sh = new ShootBal();
        sh.execute(Lvl);
        playing=true;
        balloonsbursted=0;
        gobtn.setText("Stop Game");
    }
    private void levelend()
    {
        Toast.makeText(this, String.format("You have finished level: %d", Lvl),
                Toast.LENGTH_SHORT).show();
        playing= false;
        gobtn.setText(String.format("Start Level: %d", Lvl + 1));
    }




    public void goButtonClickHandler(View view) {

        if (playing) {
            endgame(false);
        } else if (gamestopped) {
            startGame();
        } else {
            startLevel();
        }
    }

    @Override
    public void burstBalloon(Bal bln, boolean usrtch) {
        balloonsbursted++;
        ContVw.removeView(bln);
        balloons.remove(bln);//remove balloon from the list

        if(usrtch){ //if user has touched the ballon, increament the score
            score++;
        }
        else
        {
            pinused++;
            if(pinused<=pinimages.size()){
                pinimages.get(pinused-1).setImageResource(R.drawable.needle_off);
            }
            if(pinused==pin_no){
                endgame(true);
                return;
            }
            else
            {
                Toast.makeText(this,"You Missed that balloon!",Toast.LENGTH_SHORT).show();

            }
        }
        updatescoreonscreen();

        if(balloonsbursted==balloonsPerLevel){
            levelend();

        }
    }

    private void endgame(boolean entire_pins_used) {
        Toast.makeText(this,"Game OVER!!",Toast.LENGTH_SHORT).show();
        for(Bal bl:balloons){
            ContVw.removeView(bl);
            bl.setbursted(true);
        }
        balloons.clear();
        playing=false;
        gamestopped=true;
        gobtn.setText("Start Game");
        if(entire_pins_used){
            if(Score.isHighScore(this,score)){
                Score.setHighScore(this,score);
                Toast.makeText(this, String.format("HIGH SCORE!! Congrats.Your new High Score is %d",score),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatescoreonscreen() {
        scoredisp.setText(String.valueOf(score));
        leveldisp.setText(String.valueOf(Lvl));
    }

    private class ShootBal extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Atleast one parameter for the current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MinAnmDelay,
                    (MaxAnmDelay - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int balloonsLaunched = 0;
            while (playing && balloonsLaunched < balloonsPerLevel) {


                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(gscrwidth - 200);
                publishProgress(xPosition);
                balloonsLaunched++;


                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchBalloon(xPosition);
        }

    }

    private void launchBalloon(int x) {

        Bal bl = new Bal(this, balcol[nxtcol], 150);
        balloons.add(bl);

        if (nxtcol + 1 == balcol.length) {
            nxtcol = 0;
        } else {
            nxtcol++;
        }


        bl.setX(x);
        bl.setY(gscrht + bl.getHeight());
        ContVw.addView(bl);


        int duration = Math.max(MinAnmDur, MaxAnmDelay - (Lvl * 1000));
        bl.bal_rel(gscrht, duration);

    }

}
