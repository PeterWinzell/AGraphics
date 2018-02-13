package com.android.volvocars.drawsomestuff;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.ArrayMap;
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

    private final Color mGreenColor  = Color.valueOf(Color.GREEN);
    private final Color mRedColor    = Color.valueOf(Color.RED);
    private final Color mYellowColor = Color.valueOf(Color.YELLOW);

    private Paint mGradPaint;

    private Paint mbackgroundPaint;
    private Paint mbackgroundInnerPaint;
    private Paint mBlackPaint;
    private Paint mWhitePaint;

    private boolean initialized = false;

    // coordinate systems default max values
    private float mLevel = (float) 50.0;
    private float mMaxIndata = 9000;


    private float mVal;

    private float[] positions = {0.0f, 0.33f, 0.66f, 1.0f};

    int[] colors = {
            0xFF34383D, // grayblue
            0xFF414A56, // grayblueblue
            0xFF4A688E, // graygrayblue
            0xFF3B6AA3  // grayblueblueblue
    };

    private SweepGradient mSweepgradient;
    private RadialGradient mRadialgradient;
    private LinearGradient mLineargradient;

    /*
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setTextSize(18f);
    int[] colors = {
            0xFFFFFF88, // yellow
            0xFF0088FF, // blue
            0xFF000000, // black
            0xFFFFFF88  // yellow
    };
    float[] positions = {0.0f, 0.33f, 0.66f, 1.0f};
    { // draw the left heart
        SweepGradient sweepGradient;
        { // initialize the sweep gradient
            sweepGradient = new SweepGradient(50, 50, colors, positions);
            paint.setShader(sweepGradient);
        }
        c.drawPath(heart, paint);
        c.drawText("SweepGradient", 50, 190, paint);
    }
    { // draw the middle heart
        LinearGradient linearGradient;
        { // initialize a linear gradient
            linearGradient = new LinearGradient(250, 0, 350, 150, colors, positions, Shader.TileMode.CLAMP);
            paint.setShader(linearGradient);
        }
        heart.offset(210, 0); // move the heart shape to the middle
        c.drawPath(heart, paint);
        c.drawText("LinearGradient", 260, 190, paint);
    }
    { // draw the right heart
        RadialGradient radialGradient;
        { // initialize a linear gradient
            radialGradient = new RadialGradient(550, 50, 100, colors, positions, Shader.TileMode.CLAMP);
            paint.setShader(radialGradient);
        }
        heart.offset(210, 0); // move the heart shape to the right
        c.drawPath(heart, paint);
        c.drawText("RadialGradient", 470, 190, paint);
    }
    */


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

    private void initGradients(){

        mGradPaint = new Paint();
        mGradPaint.setStrokeWidth(50);

        mBlackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlackPaint.setStyle(Paint.Style.FILL);
        mBlackPaint.setColor(Color.BLACK);

        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setColor(Color.WHITE);

        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test);

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

        canvas.drawArc(aRect,180,360,true,mWhitePaint);
        aRect.inset(20,20);
        canvas.drawArc(aRect,180,360,true,mBlackPaint);
        aRect.inset(10,10);
        canvas.drawArc(aRect,180,360,true,mbackgroundPaint);

       // canvas.drawBitmap(mBitmap,null,aRect,null);
    }

    /**
     *  Returns the color where you map your value (v[0,maxval] / maxval)
     *  * @param percentage_value
     * @return
     */
    public Color GetBlendedColor(float percentage_value){
        if (percentage_value < .5){
            return Interpolate(mRedColor,mYellowColor, (double)(percentage_value / .5));
        }
        return Interpolate(mYellowColor,mGreenColor,(percentage_value - .5)/.5);
    }

    private Color Interpolate(Color c1,Color c2,double frac){

        double r = Interpolate(c1.red(),c2.red(),frac);
        double g = Interpolate(c1.green(),c2.green(),frac);
        double b = Interpolate(c1.blue(),c2.blue(),frac);

        return Color.valueOf(Color.rgb((float)r,(float)g,(float)b));
    }

    private double Interpolate(double d1, double d2,double frac){
        return d1 + (d2 - d1)*frac;
    }


    private void init(){

        initGradients();

        mPaint  = new Paint();
        mPaint.setStrokeWidth(50);
        mPaint.setColor(Color.BLUE);


        mbackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbackgroundPaint.setStyle(Paint.Style.FILL);
        mbackgroundPaint.setColor(Color.rgb(69, 134, 213));

        mbackgroundInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbackgroundInnerPaint.setStyle(Paint.Style.STROKE);
        mbackgroundInnerPaint.setColor(Color.rgb(150, 150, 150));

        mSweepgradient = new SweepGradient(this.getWidth()/2,this.getHeight()/2,colors,positions);
        mRadialgradient = new RadialGradient(this.getWidth() /2,this.getHeight() / 2,
                this.getWidth()/2,colors,positions, Shader.TileMode.CLAMP);
        mLineargradient = new LinearGradient(0,0,this.getWidth(),this.getHeight(),colors,positions,Shader.TileMode.CLAMP);

        //mbackgroundPaint.setShader(mSweepgradient);
        //mbackgroundPaint.setShader(mRadialgradient);
        mbackgroundPaint.setShader(mLineargradient);

        // R.drawable.spot_mask
        initialized = true;
    }

    private Bitmap drawSome(){



        Bitmap mbG = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mbG);

        Rect r = mCanvas.getClipBounds();
        drawSinx(mCanvas);

        return mbG;
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
        invalidate();
    }

    float getIntialLevel(float indata){
        return ((float)mLevel / (float)mMaxIndata) * indata;
    }


    private void drawSinx(Canvas canvas){

        RectF rect = getOval(canvas,(float).5);


        double currentlevel = getIntialLevel(mVal);
        // double y_pixels  = (canvas.getHeight() / mLevel) * currentlevel;
        // double x_pixels  = canvas.getWidth();
        double y_pixels  = (rect.height() / mLevel) * currentlevel;
        double x_pixels  = canvas.getWidth();// rect.width();


        double x_c = 0;
        while (x_c <= Math.PI ){
            double yval = Math.sin(x_c);
            drawCurve(x_pixels,y_pixels,x_c,yval,canvas,rect);
            x_c = x_c + (Math.PI / 24);

        }


    }

    private int getPixelLevelStep(Canvas canvas){
        return Math.round((float)(canvas.getHeight() / mLevel));
    }

    private void drawCurve(double x_max,double y_max,double x, double y,Canvas canvas,RectF rect){
        int actual_x = (int) Math.round( (x_max / Math.PI) * x);
        //int actual_y = (int ) Math.round ( (canvas.getHeight() -  (y_max / 1.0) * y));
        int actual_y = (int ) Math.round ( (rect.height() -  (y_max / 1.0) * y));

        float maxy = rect.height();
        int yd = actual_y;
        int ystep = getPixelLevelStep(canvas);

        while (yd <= maxy) {

            float key = Math.round( (mLevel / (float)maxy) * (float)yd);
            mGradPaint.setColor( GetBlendedColor( key / (float)mLevel).toArgb());
            float offsetx = actual_x + rect.width()/2;
            float offsety = rect.height()/2 - 130;
            //canvas.drawLine(actual_x + rect.width()/2, yd, actual_x, Math.min(yd+ystep, (int)maxy),mGradPaint);
            float ystart = yd + offsety;
            float yend = Math.min(yd+ystep,(int)maxy) + offsety;

            canvas.drawLine(actual_x,ystart, actual_x, yend,mGradPaint);

            yd = yd+ystep;
        }
    }

}
