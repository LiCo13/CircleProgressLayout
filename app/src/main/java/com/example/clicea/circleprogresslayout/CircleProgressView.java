package com.example.clicea.circleprogresslayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by cesar licea 25/09/2015.
 */
public class CircleProgressView extends View {
    private final String TAG = this.getClass().getSimpleName();

    //propiedades que puede manipular el usuario desde java o xml
    private int startAngle;
    private int progressColor;


    private int percentageColor;
    private int strokeProgress;
    private float mProgress;
    private int mProgressEfect;
    private int shapeColor;
    private int max;


    //private boolean isIndeterminate;
    private boolean seePercentage;
    int positionTextAngle;

    private boolean efect = true;
    private RectF mOval;


    private int stroke = 3;
    private int space = strokeProgress + 4;


    private int widthCircle;
    private int widthContent;


    private Paint paintShapeEx;
    private Paint paintShapeIn;
    private Paint paintText;
    private Paint mPaintProgress;
    private Paint mPaintProgressEfect;

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
            setProgressColor(a.getColor(R.styleable.CircleProgressAttrs_cpl_progress_color, Color.BLUE));
            setPercentageColor(a.getColor(R.styleable.CircleProgressAttrs_cpl_percentage_color, Color.BLACK));
            setProgress(a.getInt(R.styleable.CircleProgressAttrs_cpl_progress, 50));
            setStrokeProgress(a.getInt(R.styleable.CircleProgressAttrs_cpl_stroke_progress, 8));
            setMax(a.getInt(R.styleable.CircleProgressAttrs_cpl_max, 100));
            setIndeterminate(a.getBoolean(R.styleable.CircleProgressAttrs_cpl_indeterminate, false));
            setSeePercentage(a.getBoolean(R.styleable.CircleProgressAttrs_cpl_see_percentage, false));
            setStartAngle(a.getInt(R.styleable.CircleProgressAttrs_cpl_star_angle, 270));



        } finally {
            // TypedArray objects are shared and must be recycled.
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int shapeWidth = 100;

        int minw = shapeWidth + getPaddingLeft() + getPaddingRight();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

        int shapeHeight = 10;
        int minh = shapeHeight + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);


        widthCircle = w - (stroke * 2);

        widthContent = w;
        setMeasuredDimension(w, h);
    }


    private void setupPaint() {
        space = strokeProgress + 4;

        //convertir el color primario a transparente
        String PrimaryToTransparentPrimary = Integer.toHexString(getProgressColor());
        int R = Integer.parseInt(PrimaryToTransparentPrimary.substring(2, 3), 16);
        int G = Integer.parseInt(PrimaryToTransparentPrimary.substring(4, 5), 16);
        int B = Integer.parseInt(PrimaryToTransparentPrimary.substring(6, 7), 16);

        int barColors[] = {
                // Color.WHITE, getProgressColor()
                getProgressColor(), getProgressColor()
        };
        //variable para las propiedades de la linea Progress
        mOval = new RectF();
        int startProgressX = (stroke * 3) + (strokeProgress / 2);
        //int startProgressY = (heightContent/2)-(widthCircle/2-(stroke * 2) - (strokeProgress / 2));

        int endProgressX = widthContent - (stroke * 3) - (strokeProgress / 2);
        // int endProgressY = heightContent - startProgressY;

        mOval.set(startProgressX, startProgressX, endProgressX, endProgressX);


        mPaintProgressEfect = new Paint();
        mPaintProgressEfect.setAntiAlias(true);
        mPaintProgressEfect.setStyle(Paint.Style.STROKE);

        // mPaintProgressEfect.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mPaintProgressEfect.setStrokeCap(Paint.Cap.ROUND);
        mPaintProgressEfect.setStrokeWidth(getStrokeProgress());
        mPaintProgressEfect.setColor(Color.RED);


        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setStyle(Paint.Style.STROKE);

        // mPaintProgress.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);
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
        paintShapeEx.setAntiAlias(true);
        paintShapeEx.setStyle(Paint.Style.STROKE);
        paintShapeEx.setColor(getShapeColor());
        paintShapeEx.setStrokeWidth(stroke);

        //variable para las propiedades del circulo interno
        paintShapeIn = new Paint();
        paintShapeIn.setAntiAlias(true);
        paintShapeIn.setStyle(Paint.Style.FILL);
        paintShapeIn.setColor(getShapeColor());


        paintText = new Paint();
        paintText.setColor(getPercentageColor());
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(11);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        setupPaint();

        super.onDraw(canvas);

        int RadioSizeEx = widthCircle / 2;
        int RadioSizeIn = RadioSizeEx - space - (stroke * 2);

        int centerX = widthContent / 2;


        float end = 360f * ((float) getProgress() / 100);
        float endEfect = 360f * ((float) getmProgressEfect() / 100);

        if (isSeePercentage()) {


            int r = RadioSizeIn;
            end = 330f * ((float) getProgress() / 100);
            endEfect = 330f * ((float) getmProgressEfect() / 100);
            float angleText = getStartAngle() + end + 15;

            positionTextAngle = (int) ((angleText) > 360 ? (angleText) - 360 : (angleText));

            int x = (int) (r * Math.cos(Math.toRadians(positionTextAngle)) + centerX) - 4;
            int y = (int) (r * Math.sin(Math.toRadians(positionTextAngle)) + centerX);


            if (positionTextAngle > 85 && positionTextAngle <= 275) {
                x -= 9;
                if (positionTextAngle > 85 && positionTextAngle <= 190) {
                    y += 6;
                }
                if (positionTextAngle > 175 && positionTextAngle <= 185) {
                    x -= 5;
                }

            }
            if (efect) {
                x = (int) (r * Math.cos(Math.toRadians(getStartAngle() - 15)) + centerX) - 4;
                y = (int) (r * Math.sin(Math.toRadians(getStartAngle() - 15)) + centerX);

            }

            canvas.drawText(getProgress() + " %", x, y, paintText);
        }


        canvas.drawCircle(centerX, centerX, RadioSizeIn, paintShapeIn);
        canvas.drawCircle(centerX, centerX, RadioSizeEx, paintShapeEx);

        canvas.drawArc(mOval, getStartAngle(), 360f, false, this.mPaintProgressEfect);

        canvas.drawArc(mOval, getStartAngle(), end, false, this.mPaintProgress);


        if (efect) {
            canvas.drawArc(mOval, getStartAngle(), endEfect, false, this.mPaintProgressEfect);
        }

    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        if (progress <= 1 && efect) {
            setEfect();
            this.mProgress = 100;

        } else if (!efect) {
            if (getMax() > 0) {
                progress = progress * 100 / getMax();
            }
            this.mProgress = progress;
            invalidate();
        }

    }


    public void setProgressWithAnimation(float progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(1500);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    public int getmProgressEfect() {
        return mProgressEfect;
    }

    public void setmProgressEfect(int mProgressEfect) {
        if (getMax() > 0) {
            mProgressEfect = mProgressEfect * 100 / getMax();
        }

        this.mProgressEfect = mProgressEfect;
        invalidate();
    }


    public int getStrokeProgress() {
        return strokeProgress;
    }

    public void setStrokeProgress(int strokeProgress) {
        this.strokeProgress = convertDpToPixel(strokeProgress);
        invalidate();
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }


    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        startAngle = isSeePercentage() ? startAngle + 15 : startAngle;
        this.startAngle = startAngle;
        invalidate();
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int shapeColor) {
        this.shapeColor = shapeColor;
        invalidate();
    }

    public boolean isSeePercentage() {
        return seePercentage;
    }

    public void setSeePercentage(boolean seePercentage) {
        this.seePercentage = seePercentage;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getPercentageColor() {
        return percentageColor;
    }

    public void setPercentageColor(int percentageColor) {
        this.percentageColor = percentageColor;
    }


    //metodos para hacer el progress indeterminado
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setProgress(msg.what);
        }
    };

    public void setIndeterminate(boolean isIndeterminate) {
        if (isIndeterminate) {
            Thread threadProgress = new Thread(new Runnable() {
                @Override
                public void run() {

                    int progress = 1;
                    while (true) {
                        progress++;
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (progress == 100) {
                            progress = 0;
                        }
                        handler.sendEmptyMessage(progress);


                    }


                }
            });
            threadProgress.setName("CircleProgressView");
            threadProgress.start();
        }
    }


    //metodos para que el efecto de inicio se ejecute
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setmProgressEfect(msg.what);
        }
    };

    public void setEfect() {
        efect = true;
        Thread threadProgress = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress < 100) {
                    progress++;
                    if (progress < 5) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (progress < 8) {
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (progress > 95) {
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (progress > 90) {
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Thread.sleep(8);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    handler1.sendEmptyMessage(progress);
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                efect = false;
            }
        });
        threadProgress.setName("CircleProgressView Efect");
        threadProgress.start();

    }

    private int convertDpToPixel(int valueInt) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInt,
                getContext().getResources().getDisplayMetrics());
    }

}




