package com.example.clicea.circleprogresslayout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by clicea on 29/09/2015.
 */
@SuppressWarnings("unused")
public class CircleProgressLayout extends ViewGroup {

    private CircleProgressView circle;


    /**
     * The amount of space used by children in the left gutter.
     */
    private int mLeftWidth;

    /**
     * The amount of space used by children in the right gutter.
     */
    private int mRightWidth;

    /**
     * These are used for computing child frames based on their gravity.
     */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

    public CircleProgressLayout(Context context) {
        super(context);
    }

    public CircleProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CircleProgressLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        circle = new CircleProgressView(context, attrs);
        addView(circle);
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        // These keep track of the space we are using on the left and right for
        // views positioned there; we need member variables so we can also use
        // these for layout later.
        mLeftWidth = 0;
        mRightWidth = 0;

        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // Measure the child.
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                // Update our size information based on the layout params.  Children
                // that asked to be positioned on the left or right go in those gutters.
                final CircleLayoutParams lp = (CircleLayoutParams) child.getLayoutParams();
                if (i != 0) {
                    int margin = getStrokeProgress() + 4;
                    lp.setMargins(margin, margin, margin, margin);
                }


                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);

                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        // Total width is the maximum width of all inner children plus the gutters.
        maxWidth += mLeftWidth + mRightWidth;

        // Check against our minimum height and width
        maxHeight = maxWidth;
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();

        // This is the middle region inside of the gutter.
        final int middleLeft = leftPos + mLeftWidth;
        final int middleRight = rightPos - mRightWidth;

        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final CircleLayoutParams lp = (CircleLayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();

                if (i == 0) {
                    height = width;
                }

                // Compute the frame in which we are placing this child.

                mTmpContainerRect.left = middleLeft + lp.leftMargin;
                mTmpContainerRect.right = middleRight - lp.rightMargin;
                mTmpContainerRect.top = parentTop + lp.topMargin;
                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;

                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);

                // Place the child.
                child.layout(mTmpChildRect.left, mTmpChildRect.top,
                        mTmpChildRect.right, mTmpChildRect.bottom);
            }
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null;
    }


    public void setShapeColor(int shapeColor) {
        circle.setShapeColor(shapeColor);
    }


    public void setInitColor(int initColor) {
        circle.setInitColor(initColor);
    }

    public void setEndColor(int endColor) {
        circle.setEndColor(endColor);
    }

    public void setTrackColor(int color) {
        circle.setTrackColor(color);
    }

    public float getProgress() {
        return circle.getProgress();
    }

    public void setProgress(int progress) {
        circle.setProgress(progress);
    }

    public int getStartAngle() {
        return circle.getStartAngle();
    }

    public void setStartAngle(int startAngle) {
        circle.setStartAngle(startAngle);
    }

    public int getStrokeProgress() {
        return circle.getStrokeProgress();
    }

    public void setStrokeProgress(int strokeProgress) {
        circle.setStrokeProgress(strokeProgress);
    }


    public int getDegrees() {
        return circle.getDegrees();
    }

    public void setDegrees(int degrees) {
        circle.setDegrees(degrees);
    }


    public int getMax() {
        return circle.getMax();
    }

    public void setMax(int max) {
        circle.setMax(max);
    }

    public void setGradient(int gradient) {
        circle.setGradient(gradient);
    }

    public void setProgressWithAnimation(int progress) {
        circle.setProgressWithAnimation(progress);
    }

    public void setIndeterminate(boolean isIndeterminate) {
        circle.setIndeterminate(isIndeterminate);
    }
}


