package com.leugenel.stickfigure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    //Need to adjust the cursor view to the x and y
    private final int MAGIC_SHIFT = 70;
    // Display dimensions
    private int mDisplayWidth, mDisplayHeight;
    DrawView drawView;
    FigureCatcher fCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        setContentView(drawView);
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
        if (hasFocus) {
            // Get the size of the display so this View knows where borders are
            mDisplayWidth = drawView.getWidth();
            mDisplayHeight = drawView.getHeight();
            Log.i("onWindowFocusChanged", "mDisplayWidth:" + mDisplayWidth + " mDisplayHeight:" + mDisplayHeight);

            drawView.invalidate();

            fCatcher = new FigureCatcher(drawView.getDrawingCache(true));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.i("onTouchEvent", "Action was DOWN");
//                drawView.addCircle(new Circle(event.getX(), event.getY()-70, 10));
//                drawView.invalidate();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.i("onTouchEvent", "Action was MOVE");
                Log.i("onTouchEvent ", "X: " + event.getX() + " Y:" + event.getY());
                fCatcher.isCatches(event.getX(), event.getY()-70);
                drawView.addRect(fCatcher.getAroundRect());
                drawView.invalidate();
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.i("onTouchEvent", "Action was UP");
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

    public class DrawView extends View {
        Paint paint = new Paint();
        List<Rect> aroundRect = new ArrayList<Rect>();
        //List<Circle> aroundCircle = new ArrayList<Circle>();


        public DrawView(Context context) {
            super(context);
            this.setDrawingCacheEnabled(true);
//            paint.setStrokeWidth(20); //setStyle(Paint.Style.FILL_AND_STROKE);
//            paint.setColor(Color.RED);
        }

        @Override
        public void onDraw(Canvas canvas) {
            Log.i("onDraw", "mDisplayWidth:" + mDisplayWidth + " mDisplayHeight:" + mDisplayHeight);
            paint.setStrokeWidth(20); //setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.RED);
            canvas.drawLine(0, 0, mDisplayWidth, mDisplayHeight, paint);
            paint.setStrokeWidth(5); //setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.GREEN);
            if(aroundRect.size()>0) {
                for (Rect rect : aroundRect) {
                    canvas.drawRect(rect, paint);
                }
            }
//            paint.setColor(Color.BLUE);
//            if(aroundCircle.size()>0) {
//                for (Circle circle : aroundCircle) {
//                    canvas.drawCircle(circle.x, circle.y, circle.radius, paint);
//                }
//            }
        }

        public void addRect(Rect rect){
            aroundRect.add(rect);
        }
//        public void addCircle(Circle circle){
//            aroundCircle.add(circle);
//        }
    }

    /***************************** Class Circle **************************/
//    public class Circle {
//        float x,y;
//        int radius;
//
//        public Circle(float x, float y, int radius){
//            this.x = x;
//            this.y =y;
//            this.radius = radius;
//        }
//
//        @Override
//        public String toString() {
//            return x + ", " + y + "r:"+ radius;
//        }
//    }
}
