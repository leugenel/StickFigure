package com.leugenel.stickfigure;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  /*Activity*/ {

    //Need to adjust the cursor view to the x and y
    private final int MAGIC_SHIFT = 64;
    DrawView drawView;
    RelativeLayout rl;
    FigureCatcher fCatcher;
    TextView level;
    int actionBarHeight=0;
    float scale =0;
    int levelCounter=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "STARTED");

        setContentView(R.layout.activity_main);
        drawView = new DrawView(this);

        level = (TextView) findViewById(R.id.level);
        level.append(" " + Integer.toString(levelCounter));


        drawView.setBackgroundResource(getDynamicDrawableId());
//        scale = getResources().getDisplayMetrics().density;

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT /*width*/,
                         RelativeLayout.LayoutParams.MATCH_PARENT /*height*/);


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

    /***
     *
     *
     * @param view
     * @return
     */
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

    /***
     *
     * @param event
     * @return
     */
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

                int textHeight = (int)getResources().getDimension(R.dimen.text_level_height)+15;
                if ((event.getY()- MAGIC_SHIFT - actionBarHeight - textHeight)<0) {
                    Log.i("onTouchEvent", "On the text view");
                    return true;
                }
                Rect newRect = fCatcher.getAroundRect(event.getX(), event.getY() - MAGIC_SHIFT - actionBarHeight);
                if(!drawView.isInsideRectList(newRect)) {
                    drawView.addRect(newRect);
                }
                else{
                    Log.i("onTouchEvent", "Rectangle inside existing rectangle list");
                    drawView.aroundRect.clear();
                }
                drawView.invalidate();
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.i("onTouchEvent", "Action was UP");
                if(drawView.aroundRect.size()!=0) {
                    if (fCatcher.isDone(Color.BLUE, Color.GREEN, getBitmapFromView(drawView))) {
                        winAlert();
                        Log.i("onTouchEvent", "YOU WIN!!!");
                    }
                }
                drawView.aroundRect.clear();
                drawView.invalidate();
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

    /***
     *
     */
    private  void winAlert(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.windialog_message)
                .setTitle(R.string.windialog_title);
        // Add the buttons
        builder.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Next button
                Log.i("winAlert", "User clicked Next button");
                nextLevel();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                System.out.println("User cancelled the dialog");
            }
        });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     *
     * @return
     */
    private int getDynamicDrawableId(){
        String drawPrefix="f";
        String drawName = drawPrefix+Integer.toString(levelCounter);
        return getResources().getIdentifier(drawName, "drawable", getPackageName());
    }

    /***
     *
     */
    private void nextLevel(){
        levelCounter++;
        level.setText(getResources().getString(R.string.Level) + " " + Integer.toString(levelCounter));

        drawView.setBackgroundResource(getDynamicDrawableId());

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

        public Boolean isInsideRectList(Rect newRect){
            int DECREASE_DIMENSION = 5;
            newRect.bottom=newRect.bottom-DECREASE_DIMENSION;
            newRect.right=newRect.right-DECREASE_DIMENSION;
            newRect.top=newRect.top+DECREASE_DIMENSION;
            newRect.left=newRect.left+DECREASE_DIMENSION;

            for (Rect rect : aroundRect) {
                if(rect.contains(newRect)){
                    return true;
                }
            }
            return false;
        }



    }

}
