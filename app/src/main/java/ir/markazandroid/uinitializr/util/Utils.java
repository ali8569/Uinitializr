package ir.markazandroid.uinitializr.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Coded by Ali on 29/11/2017.
 */

public class Utils {
    public static void fadeVisible(View toVisibale, int duration) {
        toVisibale.setAlpha(0f);
        toVisibale.setVisibility(View.VISIBLE);
        toVisibale.animate().alpha(1f)
                .setDuration(duration)
                .setListener(null);
        /*toFade.animate().alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toFade.setVisibility(View.GONE);
                    }
                });*/

    }

    public static void fade(View toVisibale, final View toFade, int duration) {
        toVisibale.setAlpha(0f);
        toVisibale.setVisibility(View.VISIBLE);
        toVisibale.animate().alpha(1f)
                .setDuration(duration)
                .setListener(null);
        toFade.animate().alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toFade.setVisibility(View.GONE);
                    }
                });

    }

    public static void fadeFade(final View toFade, int duration) {
        toFade.animate().alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toFade.setVisibility(View.GONE);
                    }
                });

    }

    public static String timeStampToPastString(long unixTimeStamp) {
        int minutes = (int) ((System.currentTimeMillis() - unixTimeStamp) / (1000 * 60));
        int hours = minutes / 60;
        int days = hours / 24;
        int months = days / 30;
        if (months > 0) return Roozh.gregorianToPersian(unixTimeStamp);
        if (days > 0) return days + " روز پیش";
        if (hours > 0) return hours + " ساعت پیش";
        if (minutes > 0) return minutes + " دقیقه پیش";
        return "لحظاتی پیش";
    }

    private static int[] pixels;

    // public static int bitmapWidth,bitmapHeight;
    public static Bitmap getMutableBitmap(Resources resources, int resId, int bitmapWidth, int bitmapHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return getBitmap(resId, resources, bitmapWidth, bitmapHeight);
    }

    /*public static int[] getPixels(Context context,int bitmapWidth,int bitmapHeight) {
        if (pixels==null){
            Bitmap bitmap=getMutableBitmap(context.getResources(), R.drawable.box_mat,bitmapWidth,bitmapHeight);
            pixels=new int[bitmap.getHeight()*bitmap.getWidth()];
            bitmap.getPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
            bitmap.recycle();
        }
        return Arrays.copyOf(pixels,pixels.length);
    }*/

    private static Bitmap getBitmap(int drawableRes, Resources resources, int bitmapWidth, int bitmapHeight) {
        Drawable drawable = resources.getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, bitmapWidth, bitmapHeight);
        drawable.draw(canvas);

        return bitmap;
    }

/*
    public static Animator percent(final Context context, final float ratio, final TextView having, final int have, final ImageView percent, final int bitmapWidth, final int bitmapHeight){

        final Bitmap bitmap = Utils.getMutableBitmap(context.getResources(), R.drawable.box_mat, bitmapWidth, bitmapHeight);
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);// animate from 0 to 1
        anim.setDuration(have*1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int[] pixels = Utils.getPixels(context,bitmapWidth,bitmapHeight);
                float cPercent =1- (ratio*animation.getAnimatedFraction());
                //having.setText((Math.round(have*animation.getAnimatedFraction()))+"");
                for (int j = 0; j <bitmap.getWidth()*cPercent ; j++) {
                    int start = j;
                    while (start<pixels.length){
                        int color =(pixels[start]&0x00000000)+0xFFffc002;
                        pixels[start]=color;
                        start+=bitmap.getWidth();
                    }
                }

                bitmap.setPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
                percent.setImageBitmap(bitmap);
            }
        });
        anim.start();
        return anim;

    }
*/

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String replacePersianNumbers(CharSequence original) {
        if (original != null) {
            return original.toString()
                    .replaceAll("0", "٠")
                    .replaceAll("1", "١")
                    .replaceAll("2", "٢")
                    .replaceAll("3", "٣")
                    .replaceAll("4", "۴")
                    .replaceAll("5", "۵")
                    .replaceAll("6", "۶")
                    .replaceAll("7", "٧")
                    .replaceAll("8", "٨")
                    .replaceAll("9", "٩");
        }

        return null;
    }


}
