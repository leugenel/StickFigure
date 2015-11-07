package com.leugenel.stickfigure;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by eugenel on 10/27/2015.
 */
public class FigureCatcher {
    private final int DIMENSION = 20;
    private Bitmap bitmap;


    public FigureCatcher(Bitmap bitmap){
        if(bitmap==null) {
            throw new NullPointerException("Bitmap not should be null");
        }
        this.bitmap = bitmap;
    }


    public Boolean isCatches(float x, float y){
        int xPos=Math.round(x);
        Log.i("isCatches", "xPos float: "+x+" x Rounded:"+xPos);
        int yPos=Math.round(y);
        Log.i("isCatches", "yPos float: "+y+" y Rounded:"+yPos);

        if( xPos>=bitmap.getWidth() || yPos>=bitmap.getHeight() || xPos<=0 || yPos<=0) {
            Log.i("isCatches", "Out of boundaries");
            return false;
        }

        return isInDimension(xPos, yPos);
    }

    public Rect getAroundRect(float x, float y){
        int xPos=Math.round(x);
        int yPos=Math.round(y);

        Log.i("getAroundrect", "Float x:"+x+" y:"+y+" INT x:"+xPos+" y:"+yPos);
        Rect around = new Rect();
        around.left = xPos-DIMENSION;
        around.top = yPos-DIMENSION;
        around.right = xPos+DIMENSION;
        around.bottom = yPos+DIMENSION;

        return around;
    }

    private Boolean isInDimension(int xPos, int yPos){
        int pixel;
        for (int x = xPos-DIMENSION; x<xPos+DIMENSION; x++ ){
            for (int y = yPos-DIMENSION; y<yPos+DIMENSION; y++ ){
                if( x>=bitmap.getWidth() || y>=bitmap.getHeight() || x<=0 || y<=0)
                    continue;
                if(!isTheSameColor(x,y, xPos, yPos)) {
                    Log.i("isInDimension", "isInDimension YES");
                    return true;
                }
            }
        }
        Log.i("isInDimension", "NO");
        return false;
    }

    private Boolean isInDimension(int xPos, int yPos, int userColor){
        int pixel;
        for (int x = xPos-DIMENSION; x<xPos+DIMENSION; x++ ){
            for (int y = yPos-DIMENSION; y<yPos+DIMENSION; y++ ){
                if( x>=bitmap.getWidth() || y>=bitmap.getHeight() || x<=0 || y<=0)
                    continue;
                if(isTheSameColor(x,y, userColor)) {
                    Log.i("isInDimension", "isInDimension YES");
                    return true;
                }
            }
        }
        Log.i("isInDimension", "NO");
        return false;
    }


    private Boolean isTheSameColor(int x, int y, int comparex, int comparey) {
        return bitmap.getPixel(x, y) == bitmap.getPixel(comparex, comparey);
    }

    private Boolean isTheSameColor(int x, int y, int userColor) {
        return bitmap.getPixel(x, y) == userColor;
    }

    public Boolean isDone(int figColor, int userColor, Bitmap bitmap){
        int pixel;
        this.bitmap=bitmap;
        if(bitmap==null || bitmap.getWidth()==0 || bitmap.getHeight()==0){
            Log.i("isDone", "Bitmap size is 0");
            return false;
        }
        for (int x = 1; x<bitmap.getWidth(); x++ ) {
            for (int y = 1; y < bitmap.getHeight(); y++) {
                pixel = bitmap.getPixel(x, y);
                if(pixel==figColor){ //User not filled the pixel
                    if(!isInDimension(x, y, userColor)){
                        Log.i("isDone", "NOT done x="+x+" y="+y);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
