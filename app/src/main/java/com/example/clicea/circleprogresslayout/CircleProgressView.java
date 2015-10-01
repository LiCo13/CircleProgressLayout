package com.example.clicea.circleprogresslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cesar licea 25/09/2015.
 */
public class CircleProgressView extends View {

    private int startAngle;
    private int progressColor;
    private int strokeProgress;
    private int mProgress;

    private RectF mOval;
    private Paint mPaintProgress;
    private int stroke = 3;
    private int space = strokeProgress + 4;
    private int shapeColor;

    int whithCircle;
    int whithContent;


    private Paint paintShapeEx;
    private Paint paintShapeIn;


    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);

    }


    private void setupAttributes(AttributeSet attrs) {
        // Obtain a typed array of attributes
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressAttrs, 0, 0);
        // Extract custom attributes into member variables
        try {
            shapeColor = a.getColor(R.styleable.CircleProgressAttrs_circle_color, Color.RED);
            setProgressColor(a.getColor(R.styleable.CircleProgressAttrs_progress_color, Color.RED));
            setProgress(a.getInt(R.styleable.CircleProgressAttrs_progress, 50));
            setStrokeProgress(a.getInt(R.styleable.CircleProgressAttrs_stroke_progress, 8));
            setStartAngle(a.getInt(R.styleable.CircleProgressAttrs_star_angle, -90));

        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }


    private void setupPaint() {
        space = strokeProgress + 4;
String PrimaryToTransparentPrimary=Integer.toHexString(getProgressColor());

int R=Integer.parseInt(PrimaryToTransparentPrimary.substring(2,3),16);
int G=Integer.parseInt(PrimaryToTransparentPrimary.substring(4,5),16);
int B=Integer.parseInt(PrimaryToTransparentPrimary.substring(6,7),16);

        int barColors[] = {
                Color.argb(28,R,G,B), getProgressColor()
        };
        //variable para las propiedades de la linea Progress
        mOval = new RectF();
        int centerProgress = (stroke * 3) + (strokeProgress / 2);

        int whithProgress = whithContent - (stroke * 3) - (strokeProgress / 2);

        mOval.set(centerProgress, centerProgress, whithProgress, whithProgress);

        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeWidth(getStrokeProgress());
        mPaintProgress.setColor(getProgressColor());


        float centerGradient = mOval.centerX();
        mPaintProgress.setShader(new SweepGradient(centerGradient, centerGradient, barColors, null));

        Matrix matrix = new Matrix();
        mPaintProgress.getShader().getLocalMatrix(matrix);
        matrix.postTranslate(-centerGradient, -centerGradient);
        matrix.postRotate(startAngle);
        matrix.postTranslate(centerGradient, centerGradient);
        mPaintProgress.getShader().setLocalMatrix(matrix);


        //variable para las propiedades del circulo externo (el relieve)
        paintShapeEx = new Paint();
        paintShapeEx.setStyle(Paint.Style.STROKE);
        paintShapeEx.setColor(shapeColor);
        paintShapeEx.setStrokeWidth(stroke);

        //variable para las propiedades del circulo interno
        paintShapeIn = new Paint();
        paintShapeIn.setStyle(Paint.Style.FILL);
        paintShapeIn.setColor(shapeColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int shapeWidth = 100;

        int minw = shapeWidth + getPaddingLeft() + getPaddingRight();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

        int shapeHeight = 100;
        int minh = shapeHeight + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
        whithCircle = w - (stroke * 2);
        whithContent = w;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        setupPaint();
        super.onDraw(canvas);

        int RadioSizeEx = whithCircle / 2;
        int RadioSizeIn = RadioSizeEx - space - (stroke * 2);
        int center = whithContent / 2;

        canvas.drawCircle(center, center, RadioSizeIn, paintShapeIn);
        canvas.drawCircle(center, center, RadioSizeEx, paintShapeEx);
        canvas.drawArc(mOval, getStartAngle(), 360f * ((float) getProgress() / 100), false, this.mPaintProgress);


    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();

    }

    public int getStrokeProgress() {
        return strokeProgress;
    }

    public void setStrokeProgress(int strokeProgress) {
        this.strokeProgress = strokeProgress;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }
}




