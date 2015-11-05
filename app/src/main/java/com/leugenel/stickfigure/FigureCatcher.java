package com.leugenel.stickfigure;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by eugenel on 10/27/2015.
 */
public class FigureCatcher {
    private final int DIMENSION = 20;
    private int mainPixel;
    private int xPos;
    private int yPos;
    private Bitmap bitmap;


    public FigureCatcher(Bitmap bitmap){
        if(bitmap==null) {
            throw new NullPointerException("Bitmap not should be null");
        }
        this.bitmap = bitmap;
    }


    public Boolean isCatches(float x, float y){
        xPos=Math.round(x);
        Log.i("isCatches", "xPos float: "+x+" x Rounded:"+xPos);
        yPos=Math.round(y);
        Log.i("isCatches", "yPos float: "+y+" y Rounded:"+yPos);

        if( xPos>=bitmap.getWidth() || yPos>=bitmap.getHeight() || xPos<=0 || yPos<=0) {
            Log.i("isCatches", "Out of boundaries");
            return false;
        }

        mainPixel = bitmap.getPixel(xPos, yPos);
        return isInDimension();
    }

    public Rect getAroundRect(){
        Rect around = new Rect();
        around.left = xPos-DIMENSION;
        around.top = yPos-DIMENSION;
        around.right = xPos+DIMENSION;
        around.bottom = yPos+DIMENSION;
        return around;
    }

    private Boolean isInDimension(){
        int pixel;
        Log.i("isInDimension"," MAIN PIXEL Red:"+ Color.red(mainPixel)+" Blue:"+Color.blue(mainPixel)+" Green:"+Color.green(mainPixel));
        for (int x = xPos-DIMENSION; x<xPos+DIMENSION; x++ ){
            for (int y = yPos-DIMENSION; y<yPos+DIMENSION; y++ ){
                if( x>=bitmap.getWidth() || y>=bitmap.getHeight() || x<=0 || y<=0)
                    continue;
                pixel = bitmap.getPixel(x, y);
                if(!isTheSameColor(Color.red(pixel), Color.blue(pixel), Color.green(pixel))) {
                    Log.i("isInDimension YES"," Red:"+ Color.red(pixel)+" Blue:"+Color.blue(pixel)+" Green:"+Color.green(pixel));
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
