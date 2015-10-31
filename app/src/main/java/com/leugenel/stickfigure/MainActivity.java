package com.leugenel.stickfigure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {


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

            fCatcher = new FigureCatcher(drawView, mDisplayWidth, mDisplayHeight);
        }
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
                Log.i("onTouchEvent ", "X: "+event.getX()+" Y:"+event.getY());
                fCatcher.isCatched(event.getX(), event.getY());
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

    public class DrawView extends View {
        Paint paint = new Paint();

        public DrawView(Context context) {
            super(context);
            paint.setColor(Color.BLACK);
        }

        @Override
        public void onDraw(Canvas canvas) {
            Log.i("onDraw", "mDisplayWidth:" + mDisplayWidth + " mDisplayHeight:" + mDisplayHeight);
            canvas.drawLine(0, 0, mDisplayWidth, mDisplayHeight, paint);
        }

    }

}
