package com.sandesh.GloboBusters;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.sandesh.GloboBusters.utility.Pix;

/**
 * Created by sande on 3/29/2017.
 */

public class Bal extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator anm;
    private BalloonListener Lstnr;
    private boolean bur;


    public Bal(Context context) {
        super(context);

    }
    public Bal(Context context, int col, int height) {
        super(context);
        Lstnr= (BalloonListener) context;
        this.setImageResource(R.drawable.bal);
        this.setColorFilter(col);
        int width=height/2;
        int displayheight= Pix.pixelsToDp(height,context);
        int displaywidth= Pix.pixelsToDp(width,context);

        ViewGroup.LayoutParams p=new ViewGroup.LayoutParams(displaywidth,displayheight);
        setLayoutParams(p);
    }
    public void bal_rel(int scr_ht,int dur)
    {
        anm=new ValueAnimator();
        anm.setDuration(dur);
        anm.setFloatValues(scr_ht,0f); //ballon starts at the bottom of the screen
        anm.setInterpolator(new LinearInterpolator());
        anm.setTarget(this);
        anm.addListener(this);
        anm.addUpdateListener(this);
        anm.start();

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!bur) {
            Lstnr.burstBalloon(this, false);//This ballon bursted beacuse it got to the top of the screen
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setY((float) animation.getAnimatedValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!bur && event.getAction() == MotionEvent.ACTION_DOWN) { //react only if ballon is not bursted and if the user touches the screen
            Lstnr.burstBalloon(this, true);//the ballon bursted because the user touched the ballon
            bur = true;//cannot be bursted again
            anm.cancel(); //freezes the animation
        }
        return super.onTouchEvent(event);
    }
    public void setbursted(boolean b){
        bur=b;
        if(b){
            anm.cancel();
        }
    }

    public interface BalloonListener {
        void burstBalloon(Bal bln, boolean usrtch);
    }
}
