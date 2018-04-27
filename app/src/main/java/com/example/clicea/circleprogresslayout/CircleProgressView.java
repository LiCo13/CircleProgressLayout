package com.example.clicea.circleprogresslayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


/**
 * Created by cesar Licea 25/09/2015.
 */
public class CircleProgressView extends View {
    private Paint paintShapeEx;
    private Paint paintShapeIn;
    private Paint mPaintProgress;
    private Paint mPaintTrack;

    private int shapeColor;
    private int initColor;
    private int endColor;
    private int trackColor;


    private float mProgress;
    private int startAngle;
    private int strokeProgress;
    private int degrees;
    private int max;

    private boolean roundCorners;

    private int gradient;


    private RectF mOval;


    private final int stroke = 3;


    private int widthCircle;
    private int widthContent;


    private Handler handler = new Handler();

    private boolean mIsIndeterminate;


    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
    }


    private void setupAttributes(AttributeSet attrs) {
        // Obtain a typed array of attributes
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressAttrs, 0, 0);
        // Extract custom attributes into member variables
        try {
            setShapeColor(a.getColor(R.styleable.CircleProgressAttrs_cpl_circle_color, Color.GRAY));
            setInitColor(a.getColor(R.styleable.CircleProgressAttrs_cpl_init_progress_color, Color.RED));
            setEndColor(a.getColor(R.styleable.CircleProgressAttrs_cpl_end_progress_color, Color.YELLOW));
            setTrackColor(a.getColor(R.styleable.CircleProgressAttrs_cpl_track_color, Color.GRAY));

            setProgress(a.getInt(R.styleable.CircleProgressAttrs_cpl_progress, 0));
            setStartAngle(a.getInt(R.styleable.CircleProgressAttrs_cpl_star_angle, 270));
            setStrokeProgress(a.getInt(R.styleable.CircleProgressAttrs_cpl_stroke_progress, 8));
            setDegrees(a.getInt(R.styleable.CircleProgressAttrs_cpl_degrees, 360));
            setMax(a.getInt(R.styleable.CircleProgressAttrs_cpl_max, 100));

            roundCorners=a.getBoolean(R.styleable.CircleProgressAttrs_cpl_round_corners, false);
            setIndeterminate(a.getBoolean(R.styleable.CircleProgressAttrs_cpl_indeterminate, false));

            setGradient(a.getInt(R.styleable.CircleProgressAttrs_cpl_gradient, 0));

        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int shapeWidth = 100;

        int minW = shapeWidth + getPaddingLeft() + getPaddingRight();
        int w = resolveSizeAndState(minW, widthMeasureSpec, 0);

        int shapeHeight = 10;
        int minH = shapeHeight + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minH, heightMeasureSpec, 0);

        widthCircle = w - (stroke * 2);

        widthContent = w;
        setMeasuredDimension(w, h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int RadioSizeEx = widthCircle / 2;
        int space = strokeProgress + 4;
        int RadioSizeIn = RadioSizeEx - space - (stroke * 2);

        int centerX = widthContent / 2;

        float end = degrees * (getProgress() / 100);

        setupCircle();
        canvas.drawCircle(centerX, centerX, RadioSizeIn, paintShapeIn);
        canvas.drawCircle(centerX, centerX, RadioSizeEx, paintShapeEx);

        setupPaint();
        setupTrack();
        canvas.drawArc(mOval, getStartAngle(), degrees, false, this.mPaintTrack);
        setupProgress();
        canvas.drawArc(mOval, getStartAngle(), end, false, this.mPaintProgress);
    }


    private void setupPaint() {
        mOval = new RectF();
        int startProgressX = (stroke * 3) + (strokeProgress / 2);
        int startProgressY = (stroke * 3) + (strokeProgress / 2);
        int endProgressX = widthContent - (stroke * 3) - (strokeProgress / 2);
        int endProgressY = widthContent - (stroke * 3) - (strokeProgress / 2);
        mOval.set(startProgressX, startProgressY, endProgressX, endProgressY);
    }


    private void setupCircle() {
        paintShapeEx = new Paint();
        paintShapeEx.setAntiAlias(true);
        paintShapeEx.setStyle(Paint.Style.STROKE);
        paintShapeEx.setColor(shapeColor);
        paintShapeEx.setStrokeWidth(stroke);

        paintShapeIn = new Paint();
        paintShapeIn.setAntiAlias(true);
        paintShapeIn.setStyle(Paint.Style.FILL);
        paintShapeIn.setColor(shapeColor);
    }

    private void setupProgress() {
        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setStyle(Paint.Style.STROKE);

        if (roundCorners) {
            mPaintProgress.setStrokeCap(Paint.Cap.ROUND);
        } else {
            mPaintProgress.setStrokeCap(Paint.Cap.SQUARE);
        }

        mPaintProgress.setStrokeWidth(getStrokeProgress());
        mPaintProgress.setColor(initColor);

        float centerGradient = mOval.centerX();
        mPaintProgress.setShader(getShader());

        //this code helps the gradient to start at the same angle as the "startAngle"
        Matrix matrix = new Matrix();
        mPaintProgress.getShader().getLocalMatrix(matrix);
        matrix.postTranslate(-centerGradient, -centerGradient);
        matrix.postRotate(startAngle);
        matrix.postTranslate(centerGradient, centerGradient);
        mPaintProgress.getShader().setLocalMatrix(matrix);
    }

    private void setupTrack() {
        mPaintTrack = new Paint();
        mPaintTrack.setAntiAlias(true);
        mPaintTrack.setStyle(Paint.Style.STROKE);
        mPaintTrack.setStrokeCap(Paint.Cap.ROUND);
        mPaintTrack.setStrokeWidth(getStrokeProgress());
        mPaintTrack.setColor(trackColor);
    }


    public void setShapeColor(int shapeColor) {
        this.shapeColor = shapeColor;
        invalidate();
    }


    public void setInitColor(int initColor) {
        this.initColor = initColor;
        invalidate();
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
        invalidate();
    }

    public void setTrackColor(int color) {
        trackColor = color;
        invalidate();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        if (getMax() > 0) {
            progress = progress * 100 / getMax();
        }
        this.mProgress = progress;
        invalidate();
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    public int getStrokeProgress() {
        return strokeProgress;
    }

    public void setStrokeProgress(int strokeProgress) {
        this.strokeProgress = convertDpToPixel(strokeProgress);
        invalidate();
    }


    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
        invalidate();
    }


    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setGradient(int gradient) {
        this.gradient = gradient;
    }

    public void setProgressWithAnimation(float progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(1500);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    public void setIndeterminate(boolean isIndeterminate) {
        mIsIndeterminate = isIndeterminate;
        Thread threadProgress;
        threadProgress = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsIndeterminate) {
                    mProgress++;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mProgress == 100) {
                        mProgress = 0;
                    }

                    handler.post(new Runnable() {
                        public void run() {
                            setProgress(mProgress);
                        }
                    });

                }


            }
        });
        threadProgress.setName("CircleProgress Indeterminate");
        threadProgress.start();


    }


    private int convertDpToPixel(int valueInt) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInt,
                getContext().getResources().getDisplayMetrics());
    }

    private Shader getShader() {
        float centerX = mOval.centerX();
        float centerY = mOval.centerY();

        int radius = widthContent - (stroke * 2);

        int barColors[] = {
                initColor,
                endColor
        };

        float stopColors[] = {
                0.3f,
                0.6f
        };

        switch (gradient) {
            case 1:
                return new SweepGradient(centerX, centerY, barColors, null);
            case 2:
                return new RadialGradient(centerX, centerY, radius, barColors, stopColors, Shader.TileMode.REPEAT);
            default:
                return new LinearGradient(0, 0, 0, getHeight(), initColor, endColor, Shader.TileMode.MIRROR);
        }
    }
}




