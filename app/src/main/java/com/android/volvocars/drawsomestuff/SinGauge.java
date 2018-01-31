package com.android.volvocars.drawsomestuff;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;

/**
 * Created by pwinzell on 2018-01-25.
 */

public class SinGauge extends View {

    Bitmap mBitmap;
    Canvas mCanvas;

    Paint mPaint;

    private Paint mbackgroundPaint;
    private Paint mbackgroundInnerPaint;
    private boolean initialized = false;

    // coordinate systems default max values
    private float mLevel = (float) 10.0;
    private float mMaxIndata = 9000;

    private float mVal;

    public SinGauge(Context context) {

        super(context);


    }

    public SinGauge(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public void setMaxIndata(float max){
        mMaxIndata = max;
    }

    public void setLevels(float levels){
        mLevel = levels;
    }


    /*private void drawBackground(Canvas canvas) {
        RectF oval = getOval(canvas, 1);
        canvas.drawArc(oval, 180, 180, true, backgroundPaint);

        RectF innerOval = getOval(canvas, 0.9f);
        canvas.drawArc(innerOval, 180, 180, true, backgroundInnerPaint);

        Bitmap mask = Bitmap.createScaledBitmap(mMask, (int) (oval.width() * 1.1), (int) (oval.height() * 1.1) / 2, true);
        canvas.drawBitmap(mask, oval.centerX() - oval.width()*1.1f/2, oval.centerY()-oval.width()*1.1f/2, maskPaint);

        canvas.drawText(unitsText, oval.centerX(), oval.centerY()/1.5f, unitsPaint);
    }*/

    private RectF getOval(Canvas canvas, float factor) {
        RectF oval;
        final int canvasWidth = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        final int canvasHeight = canvas.getHeight() - getPaddingTop() - getPaddingBottom();

        if (canvasHeight*2 >= canvasWidth) {
            oval = new RectF(0, 0, canvasWidth*factor, canvasWidth*factor);
        } else {
            oval = new RectF(0, 0, canvasHeight*2*factor, canvasHeight*2*factor);
        }

        oval.offset((canvasWidth-oval.width())/2 + getPaddingLeft(), (canvasHeight*2-oval.height())/2 + getPaddingTop());

        return oval;
    }


    private void init(){

        mPaint  = new Paint();
        mPaint.setStrokeWidth(50);
        mPaint.setColor(Color.BLUE);


        mbackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbackgroundPaint.setStyle(Paint.Style.FILL);
        mbackgroundPaint.setColor(Color.rgb(127, 127, 127));

        mbackgroundInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbackgroundInnerPaint.setStyle(Paint.Style.FILL);
        mbackgroundInnerPaint.setColor(Color.rgb(150, 150, 150));

        // R.drawable.spot_mask
        initialized = true;
    }

    private Bitmap drawSome(){



        Bitmap mbG = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mbG);

        Rect r = mCanvas.getClipBounds();
        setValue(1000);


        drawSinx(mCanvas);

        return mbG;
    }

    private void drawBackground(Canvas canvas){
        RectF aRect = getOval(canvas,1);
        canvas.drawArc(aRect,180,180,true,mbackgroundPaint);

    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (!initialized)  init();

        drawBackground(canvas);
        Bitmap bm = drawSome();
        canvas.drawBitmap(bm,0,0,mPaint);
    }


    public void setValue(float indata){
            mVal =  indata;
    }

    float getIntialLevel(float indata){
        return (mLevel / mMaxIndata) * indata;
    }


    private void drawSinx(Canvas canvas){


        double currentlevel = getIntialLevel(mVal);

        double y_pixels  = (canvas.getHeight() / mLevel) * currentlevel;

        double x_pixels  = canvas.getWidth();

        double x_c = 0;
        while (x_c <= Math.PI ){
            double yval = Math.sin(x_c);
            drawCurve(x_pixels,y_pixels,x_c,yval,canvas);
            x_c = x_c + (Math.PI / 24);

        }


    }

    private void drawCurve(double x_max,double y_max,double x, double y,Canvas canvas){
        int actual_x = (int) Math.round( (x_max / Math.PI) * x);
        int actual_y = (int ) Math.round ( (canvas.getHeight() -  (y_max / 1.0) * y));
        canvas.drawLine(actual_x,actual_y,actual_x,canvas.getHeight(),mPaint);
    }

}
