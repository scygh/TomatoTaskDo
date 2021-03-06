package com.frank.loadbutton;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LoadButton extends View implements Animator.AnimatorListener {
    private static final String TAG = "LoadButton";
    private final int mStrokeColor;
    private final int mTextColor;
    private final float mProgressWidth;
    private  OnClickListener mListenner;
    private Paint mPaint;
    //默认字体和半径
    private int mDefaultWidth;
    private int mDefaultRadiu;
    private int rectWidth;
    private TextPaint mTextPaint;
    private int mDefaultTextSize;
    private int mTopBottomPadding;
    private int mLeftRightPadding;
    private String mText;
    private int mTextWidth;
    private int mTextSize;
    private int mRadiu;
    private Path mPath;
    private RectF leftRect;
    private RectF rightRect;
    private RectF contentRect;
    private RectF progressRect;
    private int left;
    private int right;
    private int top;
    private int bottom;
    private boolean isUnfold;
    private int mBackgroundColor;
    private State mCurrentState;
    private float circleSweep;
    private ObjectAnimator loadAnimator;
    private ObjectAnimator shrinkAnim;
    private Drawable mSuccessedDrawable;
    private Drawable mErrorDrawable;
    private Drawable mPauseDrawable;
    private boolean progressReverse = true;
    private int mProgressSecondColor;
    private int mProgressColor;
    private int mProgressStartAngel;

    //回调接口
    LoadListenner mLoadListenner;
    public void setListenner(LoadListenner listenner) {
        this.mLoadListenner = listenner;
    }
    public interface LoadListenner {
        void onClick(boolean isSuccessed);
        void starttime(boolean start);
    }

    public void setCircleSweep(float circleSweep) {
        this.circleSweep = circleSweep;
        invaidateSelft();
    }
    //状态枚举
    enum State {
        INITIAL,
        FODDING,
        LOADDING,
        COMPLETED_ERROR,
        COMPLETED_SUCCESSED,
        LOADDING_PAUSE
    }

    //三个构造器初始化
    public LoadButton(Context context) {
        this(context,null);
    }

    public LoadButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取xml的属性
        mDefaultRadiu = 40;
        mDefaultTextSize = 24;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.LoadButton);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.LoadButton_android_textSize, mDefaultTextSize);
        mStrokeColor = typedArray.getColor(R.styleable.LoadButton_stroke_color, Color.RED);
        mTextColor = typedArray.getColor(R.styleable.LoadButton_content_color, Color.WHITE);
        mText = typedArray.getString(R.styleable.LoadButton_android_text);
        mRadiu = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_radiu,mDefaultRadiu);
        mTopBottomPadding = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_contentPaddingTB,10);
        mLeftRightPadding = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_contentPaddingLR,10);
        mBackgroundColor = typedArray.getColor(R.styleable.LoadButton_backColor,Color.WHITE);
        mProgressColor = typedArray.getColor(R.styleable.LoadButton_progressColor,Color.WHITE);
        mProgressSecondColor = typedArray.getColor(R.styleable.LoadButton_progressSecondColor,Color.parseColor("#c3c3c3"));
        mProgressWidth = typedArray.getDimensionPixelOffset(R.styleable.LoadButton_progressedWidth,2);
        mSuccessedDrawable = typedArray.getDrawable(R.styleable.LoadButton_loadSuccessDrawable);
        mErrorDrawable = typedArray.getDrawable(R.styleable.LoadButton_loadErrorDrawable);
        mPauseDrawable = typedArray.getDrawable(R.styleable.LoadButton_loadPauseDrawable);
        //程序在运行时维护了一个 TypedArray的池，程序调用时，会向该池中请求一个实例，用完之后，调用 recycle() 方法来释放该实例，从而使其可被其他模块复用。
        typedArray.recycle();
        //progress画笔对象
        mPaint = new Paint();
        //chu锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(mStrokeColor);
        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mDefaultWidth = 200;
        //字体画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        //中间矩形宽度
        rectWidth = mDefaultWidth - mDefaultRadiu * 2;

        leftRect = new RectF();
        rightRect = new RectF();
        contentRect = new RectF();
        isUnfold = true;

        mListenner = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mCurrentState == State.FODDING) {
                    return;
                }

                if ( mCurrentState == State.INITIAL ) {
                    if ( isUnfold ) {
                        shringk();
                        mLoadListenner.starttime(true);
                    }
                } else if ( mCurrentState == State.COMPLETED_SUCCESSED ) {
                    if (mLoadListenner != null ) {
                        mLoadListenner.onClick(true);
                    }
                } else if ( mCurrentState == State.LOADDING) {
                    mLoadListenner.starttime(false);
                    cancelAnimation();
                    invaidateSelft();
                }

            }
        };

        setOnClickListener(mListenner);
        mCurrentState = State.INITIAL;
        if (mSuccessedDrawable == null) {
            mSuccessedDrawable = context.getResources().getDrawable(R.drawable.yes);
        }
        if (mErrorDrawable == null) {
            //mErrorDrawable = context.getResources().getDrawable(R.drawable.no);
        }
        if (mPauseDrawable == null) {
            mPauseDrawable = context.getResources().getDrawable(R.drawable.pause);
        }

        mProgressSecondColor = Color.parseColor("#F26A69");
        mProgressColor = Color.WHITE;


    }

    /**
     * 再次进入界面，如果在计时，按钮自动伸缩
    *@Params :
    *@Author :scy
    *@Date :2019/4/18
    */
    public void isTiming() {
        shringk();
    }

    /**
     * 重置
    *@Params :
    *@Author :scy
    *@Date :2019/4/18
    */
    public void reset(){
        mCurrentState = State.INITIAL;
        rectWidth = getWidth() - mRadiu * 2;
        isUnfold = true;
        cancelAnimation();
        invaidateSelft();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int resultW = widthSize;
        int resultH = heightSize;

        int contentW = 0;
        int contentH = 0;

        if ( widthMode == MeasureSpec.AT_MOST ) {
            mTextWidth = (int) mTextPaint.measureText(mText);
            contentW += mTextWidth + mLeftRightPadding * 2 + mRadiu * 2;

            resultW = contentW < widthSize ? contentW : widthSize;
        }

        if ( heightMode == MeasureSpec.AT_MOST ) {
            contentH += mTopBottomPadding * 2 + mTextSize;
            resultH = contentH < heightSize ? contentH : heightSize;
        }

        resultW = resultW < 2 * mRadiu ? 2 * mRadiu : resultW;
        resultH = resultH < 2 * mRadiu ? 2 * mRadiu : resultH;

        mRadiu = resultH / 2;
        rectWidth = resultW - 2 * mRadiu;
        setMeasuredDimension(resultW,resultH);

        Log.d(TAG,"onMeasure: w:"+resultW+" h:"+resultH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        drawPath(canvas,cx,cy);

        int textDescent = (int) mTextPaint.getFontMetrics().descent;
        int textAscent = (int) mTextPaint.getFontMetrics().ascent;
        int delta = Math.abs(textAscent) - textDescent;

        int circleR = mRadiu - 6;

        if ( mCurrentState == State.INITIAL) {

            canvas.drawText(mText,cx,cy + delta / 2,mTextPaint);

        } else if ( mCurrentState == State.LOADDING ) {

            if ( progressRect == null ) {
                progressRect = new RectF();
            }
            progressRect.set(cx - circleR,cy - circleR,cx + circleR,cy + circleR);



            mPaint.setColor(mProgressSecondColor);
            canvas.drawCircle(cx,cy,circleR,mPaint);
            mPaint.setColor(mProgressColor);
            Log.d(TAG,"onDraw() pro:"+progressReverse+" swpeep:"+circleSweep);
            if ( circleSweep != 360 ) {
                mProgressStartAngel = progressReverse ? 270 : (int) (270 + circleSweep);
                canvas.drawArc(progressRect
                ,mProgressStartAngel,progressReverse ? circleSweep : (int) (360 - circleSweep),
                        false,mPaint);
            }

            mPaint.setColor(mBackgroundColor);
            mPauseDrawable.setBounds(cx - circleR,cy - circleR,cx + circleR,cy + circleR);
            mPauseDrawable.draw(canvas);
        } else if ( mCurrentState == State.COMPLETED_ERROR ) {
            mErrorDrawable.setBounds(cx - circleR,cy - circleR,cx + circleR,cy + circleR);
            mErrorDrawable.draw(canvas);
        } else if (mCurrentState == State.COMPLETED_SUCCESSED) {
            mSuccessedDrawable.setBounds(cx - circleR,cy - circleR,cx + circleR,cy + circleR);
            mSuccessedDrawable.draw(canvas);
        }


    }

    private void drawPath(Canvas canvas,int cx,int cy) {
        if (mPath == null) {
            mPath = new Path();
        }

        mPath.reset();

        left = cx - rectWidth / 2 - mRadiu;
        top = 0;
        right = cx + rectWidth / 2 + mRadiu;
        bottom = getHeight();

        leftRect.set(left,top,left + mRadiu * 2,bottom);
        rightRect.set(right - mRadiu * 2,top,right,bottom);
        contentRect.set(cx-rectWidth/2,top,cx + rectWidth/2,bottom);
        mPath.moveTo(cx - rectWidth /2,bottom);
        mPath.arcTo(leftRect,
                90.0f,180f);
        mPath.lineTo(cx + rectWidth/2,top);
        mPath.arcTo(rightRect,
                270.0f,180f);

        mPath.close();


        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawPath(mPath,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mStrokeColor);
    }

    public void setRectWidth (int width) {
        rectWidth = width;
        invaidateSelft();
    }

    private void invaidateSelft() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void shringk() {
        if (shrinkAnim == null) {
            shrinkAnim = ObjectAnimator.ofInt(this,"rectWidth", rectWidth,0);
        }
        shrinkAnim.addListener(this);

        shrinkAnim.setDuration(500);
        shrinkAnim.start();
        mCurrentState = State.FODDING;
    }

    public void load() {
        if (loadAnimator == null) {
            loadAnimator = ObjectAnimator.ofFloat(this,"circleSweep",0,360);
        }

        loadAnimator.setDuration(10000);
        loadAnimator.setRepeatCount(150);
        loadAnimator.setRepeatMode(ValueAnimator.RESTART);
        loadAnimator.removeAllListeners();

        loadAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //progressReverse = !progressReverse;
            }
        });
        loadAnimator.start();
        mCurrentState = State.LOADDING;
    }

    public void loadSuccessed() {
        mCurrentState = State.COMPLETED_SUCCESSED;
        cancelAnimation();
        invaidateSelft();
    }

    public void loadFailed() {
        mCurrentState = State.COMPLETED_ERROR;
        cancelAnimation();
        invaidateSelft();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isUnfold = false;
        load();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        cancelAnimation();
        
    }

    private void cancelAnimation() {
        if ( shrinkAnim != null && shrinkAnim.isRunning() ) {
            shrinkAnim.removeAllListeners();
            shrinkAnim.cancel();
            shrinkAnim = null;
        }
        if ( loadAnimator != null && loadAnimator.isRunning() ) {
            loadAnimator.removeAllListeners();
            loadAnimator.cancel();
            loadAnimator = null;
        }
    }




}
