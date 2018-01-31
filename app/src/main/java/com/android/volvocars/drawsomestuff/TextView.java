package com.android.volvocars.drawsomestuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
/**
 * Created by pwinzell on 2018-01-30.
 */

public class TextView extends View {


    private float mValue = 100;
    private String mUnitstr = "km/h";
    private boolean mRoundtoint = true;

    private boolean mInitialized = false;
    private Paint mbackgroundPaint;
    private TextPaint mTextcolor_2;
    private TextPaint mTextcolor_1;

    public TextView(Context context) {

        super(context);


    }

    public TextView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public void shouldRoundToInt(boolean roundtoint){
        mRoundtoint =  roundtoint;
    }

    public void setValue(float outvalue){
        mValue = outvalue;
    }

    public void setmUnitstr(String unitstr){
        mUnitstr = unitstr;
    }

    private void init(){
        mInitialized = true;

        mbackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbackgroundPaint.setStyle(Paint.Style.FILL);
        mbackgroundPaint.setColor(Color.rgb(127, 127, 127));

        mTextcolor_2 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextcolor_2.setColor(Color.BLUE);
        mTextcolor_2.setTypeface(Typeface.SANS_SERIF);
        mTextcolor_2.setTextSize(250);

        mTextcolor_1 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextcolor_1.setColor(Color.WHITE);
        mTextcolor_1.setTypeface(Typeface.DEFAULT);
        mTextcolor_1.setTextSize(250);

    }

    private RectF getOval(Canvas canvas, float factor) {
        RectF oval;
        final int canvasWidth = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        final int canvasHeight = canvas.getHeight() - getPaddingTop() - getPaddingBottom();

        if (canvasHeight*2 >= canvasWidth) {
            oval = new RectF(0, 0, canvasWidth*factor, canvasWidth*factor);
        } else {
            oval = new RectF(0, 0, canvasHeight*2*factor, canvasHeight*2*factor);
        }

        oval.offset((canvasWidth-oval.width())/2 + getPaddingLeft(), (canvasHeight-oval.height())/2 + getPaddingTop());

        return oval;
    }

    private void drawBackground(Canvas canvas){
        RectF aRect = getOval(canvas,1);
        canvas.drawArc(aRect,180,360,true,mbackgroundPaint);
    }


    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (!mInitialized)  init();

        drawBackground(canvas);

        String out = new String();

        if (mRoundtoint){
            out = Integer.toString(Math.round(mValue)) + " " + mUnitstr;
        }
        else {
            out = Float.toString(mValue) + " " + mUnitstr;
        }
            Rect bounds = new Rect();

        mTextcolor_1.getTextBounds(out,0,out.length(),bounds);

        int xt_start = (canvas.getWidth() / 2) - (bounds.width()/2);
        int yt_start = (canvas.getHeight() /2) + (bounds.height()/2);

        canvas.drawText(out,xt_start,yt_start, mTextcolor_1);
        canvas.drawText(out,xt_start + 5,yt_start + 5, mTextcolor_2);
    }
}
