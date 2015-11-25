package com.leugenel.stickfigure;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  /*Activity*/ {

    //Need to adjust the cursor view to the x and y
    private final int MAGIC_SHIFT = 64;
    DrawView drawView;
    RelativeLayout rl;
    FigureCatcher fCatcher;
    int actionBarHeight=0;
    float scale =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "STARTED");

        setContentView(R.layout.activity_main);
        drawView = new DrawView(this);

        drawView.setBackgroundResource(R.drawable.line);
        scale = getResources().getDisplayMetrics().density;

        Log.i("onCreate", "scale:" + scale + " horizontal_margin:" +
                (int) (getResources().getDimension(R.dimen.activity_horizontal_margin)) +
                "vertical_margin: " + (int) (getResources().getDimension(R.dimen.activity_vertical_margin)));

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                                RelativeLayout.LayoutParams.MATCH_PARENT);

        rl = (RelativeLayout)findViewById(R.id.frame);
        rl.addView(drawView, params);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.i("onWindowFocusChanged", "RL Width:" + rl.getWidth() + " RL Height:" + rl.getHeight());

        if (hasFocus) {
            drawView.invalidate();
            fCatcher = new FigureCatcher(getBitmapFromView(drawView));
        }

        //Get Action bar height to calculate Y
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            Log.i("onWindowFocusChanged ", "actionBarHeight: " +actionBarHeight);
        }
    }

    public static Bitmap getBitmapFromView(ImageView view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);

        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.i("onTouchEvent", "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.i("onTouchEvent", "Action was MOVE");
                Log.i("onTouchEvent ", "Regular X: " + event.getX() + " Y:" + event.getY());
                //fCatcher.isCatches(event.getX(), event.getY()-MAGIC_SHIFT);
                drawView.addRect(fCatcher.getAroundRect(event.getX(), event.getY()  - MAGIC_SHIFT - actionBarHeight));
                drawView.invalidate();
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.i("onTouchEvent", "Action was UP");

                if(fCatcher.isDone(Color.BLUE, Color.GREEN, getBitmapFromView(drawView))){
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle("You win")
//                            .setMessage("Yes you win")
//                            .setCancelable(true)
//                            .show();
                    Log.i("onTouchEvent", "YOU WIN!!!");
                }
                else {
                    drawView.aroundRect.clear();
                }
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.i("onTouchEvent", "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.i("onTouchEvent", "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    /***************************** Class DrawView **************************/

    public class DrawView extends ImageView {
        Paint paint = new Paint();
        List<Rect> aroundRect = new ArrayList<Rect>();

        public DrawView(Context context) {
           super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {

            paint.setStrokeWidth(5); //setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.GREEN);
            if(aroundRect.size()>0) {
                for (Rect rect : aroundRect) {
                    canvas.drawRect(rect, paint);
                }
            }
       }

        public void addRect(Rect rect){
            aroundRect.add(rect);
        }

    }

}
