package mud.arca.io.mud.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.View;


public class SmileView extends View {
    private int mExampleColor = Color.BLACK; // TODO: use a default from R.color...
    private ShapeDrawable mMouthDrawable;
    private ShapeDrawable mEyeDrawable;
    private float mStrokeWidth = .05f; // TODO: Export, use default

    Paint circlePaint = new Paint();

    private float progress = 0.5f;

    public SmileView(Context context) {
        super(context);
        init(null, 0);
    }

    public SmileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SmileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (getForegroundTintList() != null) {
            mExampleColor = getForegroundTintList().getDefaultColor();
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mMouthDrawable = makeMouthDrawable(progress);

        mEyeDrawable = makeEyeDrawable(progress);

        invalidate();
    }


    private ShapeDrawable makeMouthDrawable(float progress) {
        Path mouthPath = new Path();
        float mouthscale = .25f;
        float cornerHeight = 0.5f - (mouthscale / 2) + mouthscale * (1 - progress);
        float controlHeight = 0.5f - (mouthscale / 2) + mouthscale * progress;
        mouthPath.moveTo(0, cornerHeight);
        mouthPath.cubicTo(
                .33f, controlHeight,
                .66f, controlHeight,
                1, cornerHeight
        );
        ShapeDrawable drawable = new ShapeDrawable(new PathShape(mouthPath, 1, 1));
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.getPaint().setStrokeCap(Paint.Cap.ROUND);
        drawable.getPaint().setStrokeWidth(mStrokeWidth);
        return drawable;
    }

    private ShapeDrawable makeEyeDrawable(float progress) {
        Path eyePath = new Path();
        float eyescale = 1f;
        float cornerHeight = 0.5f - (eyescale / 2) + eyescale * (1 - progress);
        float controlHeight = 0.5f - (eyescale / 2) + eyescale * progress;
        eyePath.moveTo(0, cornerHeight);
        eyePath.cubicTo(
                .33f, controlHeight,
                .66f, controlHeight,
                1, cornerHeight
        );

        PathShape pathShape = new PathShape(eyePath, 1, 1);

        ShapeDrawable drawable = new ShapeDrawable(pathShape);
        drawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        drawable.getPaint().setStrokeWidth(mStrokeWidth);
        return drawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        int contentSize = Math.min(contentHeight, contentWidth);

        float cx = paddingLeft + (contentWidth - contentSize) / 2.f + contentSize / 2.f;
        float cy = paddingTop + (contentHeight - contentSize) / 2.f + contentSize / 2.f;

        // Draw circle outline
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(mStrokeWidth * contentSize);
        circlePaint.setColor(mExampleColor);
        canvas.drawCircle(cx, cy, contentSize / 2f - (mStrokeWidth * contentSize) / 2f, circlePaint);


        // Position and draw mouth
        float mouthWidth = .5f;
        float mouthYOffset = .25f;
        mMouthDrawable.setBounds(
                (int) (cx - contentSize * mouthWidth / 2f),
                (int) (cy - contentSize * mouthWidth / 2f + contentSize * mouthYOffset),
                (int) (cx + contentSize * mouthWidth / 2f),
                (int) (cy + contentSize * mouthWidth / 2f + contentSize * mouthYOffset)
        );
        mMouthDrawable.getPaint().setStrokeWidth(mStrokeWidth / mouthWidth);
        mMouthDrawable.getPaint().setColor(mExampleColor);
        mMouthDrawable.draw(canvas);


        // Position and draw eyes
        for (int i = 0; i < 2; i++) {
            float eyeWidth = .15f;
            float eyeYOffset = -.15f;
            float eyeXOffset = (i == 0 ? 1 : -1) * .20f;
            mEyeDrawable.setBounds(
                    (int) (cx - contentSize * eyeWidth / 2f + contentSize * eyeXOffset),
                    (int) (cy - contentSize * eyeWidth / 2f + contentSize * eyeYOffset),
                    (int) (cx + contentSize * eyeWidth / 2f + contentSize * eyeXOffset),
                    (int) (cy + contentSize * eyeWidth / 2f + contentSize * eyeYOffset)
            );
            mEyeDrawable.getPaint().setStrokeWidth(mStrokeWidth / eyeWidth);
            mEyeDrawable.getPaint().setColor(mExampleColor);
            mEyeDrawable.draw(canvas);

        }


    }

    public void setProgress(float progress) {
        if (progress < 0 || progress > 1) {
            throw new IllegalArgumentException("Progress must be between 0 and 1.");
        }
        this.progress = progress;
        invalidateTextPaintAndMeasurements();
    }
}
