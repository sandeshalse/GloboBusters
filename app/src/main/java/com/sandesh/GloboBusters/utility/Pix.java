package com.sandesh.GloboBusters.utility;

/**
 * Created by sande on 3/29/2017.
 */

import android.content.Context;
import android.util.TypedValue;

public class Pix {

    public static int pixelsToDp(int px, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics());
    }

}