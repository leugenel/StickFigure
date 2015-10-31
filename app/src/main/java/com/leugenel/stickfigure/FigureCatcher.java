package com.leugenel.stickfigure;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by eugenel on 10/27/2015.
 */
public class FigureCatcher {
    private final int DIMENSION = 5;
    private int mainPixel;
    private int xPos;
    private int yPos;
    private Bitmap bitmap;
    private Color pointColor;

    public FigureCatcher(View v, int displayWidth, int displayHeight){
//        ImageView imageView = ((ImageView)v);
//        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888);
        bitmap = BitmapFactory.decodeResource(v, R.drawable.pic1);
        if(bitmap==null)
            Log.i("Constructor", "bitmap==null");
    }


    public Boolean isCatched(float x, float y){
        xPos=Math.round(x);
        Log.i("isCatched", "xPos float: "+x+" x Rounded:"+xPos);
        yPos=Math.round(y);
        Log.i("isCatched", "yPos float: "+y+" y Rounded:"+yPos);
        mainPixel = bitmap.getPixel(Math.round(xPos), yPos);

        return isInDimension();
    }

    private Boolean isInDimension(){
        int pixel;
        for (int x = xPos-DIMENSION; x<xPos+DIMENSION; x++ ){
            for (int y = yPos-DIMENSION; y<yPos+DIMENSION; y++ ){
                pixel = bitmap.getPixel(x, y);
                Log.i("isInDimension"," Red:"+ Color.red(pixel)+" Blue:"+Color.blue(pixel)+" Green:"+Color.green(pixel));
                if(!isTheSameColor(Color.red(pixel), Color.blue(pixel), Color.green(pixel))) {
                    Log.i("isInDimension", "YES");
                    return true;
                }
            }
        }
        Log.i("isInDimension", "NO");
        return false;
    }

    private Boolean isTheSameColor(int red, int blue, int green) {
        return (Color.red(mainPixel)==red)&&(Color.blue(mainPixel)==blue)&&(Color.green(mainPixel) == green);
    }


}
