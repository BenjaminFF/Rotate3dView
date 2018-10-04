package com.benjamin.mylib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

/**
 * Created by benja on 2018/9/21.
 */

public class Rotate3dView extends ViewGroup {

    private int mWidth,mHeight;
    private Adapter mAdapter;
    private boolean initState,isRotating;
    private Camera mCamera;
    private float mAngle;
    private float mXDown,mXMove,mXUp;
    private int mTouchSlop;
    private boolean frontReversed,antiClockWised;
    float scale = 1;    // <------- 像素密度

    private int duration;

    public abstract static class Adapter<FrontVH extends ViewHolder,BackVH extends ViewHolder>{
        public abstract FrontVH onCreateFrontViewHolder(ViewGroup parent);
        public abstract void onBindFrontViewHolder(FrontVH holder);

        public abstract BackVH onCreateBackViewHolder(ViewGroup parent);
        public abstract void onBindBackViewHolder(BackVH holder);

        public final FrontVH CreateFrontViewHolder(ViewGroup parent){
            final FrontVH holder=onCreateFrontViewHolder(parent);
            return holder;
        }

        public final BackVH CreateBackViewHolder(ViewGroup parent){
            final BackVH holder=onCreateBackViewHolder(parent);
            return holder;
        }
    }

    public abstract static class ViewHolder{
        public View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    public Rotate3dView(Context context) {
        super(context);
    }

    public Rotate3dView(Context context, AttributeSet attrs){
        super(context, attrs);

        mCamera=new Camera();
        duration=2000;      //duration默认2s
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        // 获取手机像素密度 （即dp与px的比例）
        scale = context.getResources().getDisplayMetrics().density;
    }

    public void setDuration(int duration){
        this.duration=duration;
    }

    public void setAdapter(Adapter adapter){
        initState=true;
        frontReversed=false;
        isRotating=false;

        int childCount=getChildCount();
        if(childCount!=0){
            removeViews(0,childCount);
        }
        mAdapter=adapter;
        addViewFromAdapter();
        requestLayout();
    }

    private void addViewFromAdapter(){
        ViewHolder frontHolder=mAdapter.CreateFrontViewHolder(this);
        mAdapter.onBindFrontViewHolder(frontHolder);

        ViewHolder backHolder=mAdapter.CreateBackViewHolder(this);
        mAdapter.onBindBackViewHolder(backHolder);

        addView(frontHolder.itemView);
        addView(backHolder.itemView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
        for(int i=0;i<2;i++){
            View childView=getChildAt(i);
            measureChildWithMargins(childView,widthMeasureSpec,0,heightMeasureSpec,0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i=0;i<2;i++){
            View childView=getChildAt(i);
            ViewGroup.MarginLayoutParams params=(MarginLayoutParams) childView.getLayoutParams();

            childView.layout(params.leftMargin,params.topMargin,
                    params.leftMargin+childView.getMeasuredWidth(),
                    params.topMargin+childView.getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop||isRotating) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop||isRotating) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mXDown=event.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                //Log.i("ACTION_UP","ACTION_UP");
                mXUp=event.getRawX();
                if(Math.abs(mXUp-mXDown)>mTouchSlop&&!isRotating) {
                    if (mXUp - mXDown > 0) {      //从左向右滑动,逆时针旋转
                        antiClockWised = true;
                        startRotateAnimation();
                        Log.i("ACTION_UP", "ACTION_UP");
                    } else {
                        antiClockWised = false;
                        Log.i("ACTION_UP", "ACTION_UP");
                        startRotateAnimation();
                    }
                }
        }
        return true;
    }

    public void rotateCard(boolean antiClockWised){
        this.antiClockWised=antiClockWised;
        startRotateAnimation();
    }

    private void startRotateAnimation(){     //reverse==true代表逆时针旋转
        ValueAnimator valueAnimator;
        if(antiClockWised){
            valueAnimator=ValueAnimator.ofFloat(0,180);
        }else {
            valueAnimator=ValueAnimator.ofFloat(0,-180);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngle=(float)animation.getAnimatedValue();
                invalidate();
                if(mAngle==180||mAngle==-180){
                    isRotating=false;
                    if(frontReversed){
                        frontReversed=false;
                        getChildAt(1).setVisibility(INVISIBLE);
                        getChildAt(0).setVisibility(VISIBLE);
                    }else {
                        frontReversed=true;
                        getChildAt(0).setVisibility(INVISIBLE);
                        getChildAt(1).setVisibility(VISIBLE);
                    }
                    mAngle=0;
                }
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
        isRotating=true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(initState){
            initChildViews(canvas);
            getChildAt(1).setVisibility(INVISIBLE);   //可以禁用背面所有控件的事件
            initState=false;
        }else {
            if(!frontReversed){      //如果正面在上,就先绘制正面
                RotateChildView(canvas,getChildAt(0),mAngle);
                if(antiClockWised){  //顺时针旋转
                    RotateChildView(canvas,getChildAt(1),mAngle-180);
                }else {
                    RotateChildView(canvas,getChildAt(1),mAngle+180);
                }
            }else {
                RotateChildView(canvas,getChildAt(1),mAngle);
                if(antiClockWised){  //顺时针旋转
                    RotateChildView(canvas,getChildAt(0),mAngle-180);
                }else {
                    RotateChildView(canvas,getChildAt(0),mAngle+180);
                }
            }
        }
       // super.dispatchDraw(canvas);
    }

    private void initChildViews(Canvas canvas){
        //先画childView2
        canvas.save();
        View childView2=getChildAt(1);
        Matrix matrix=childView2.getMatrix();
        Camera camera=new Camera();
        camera.save();
        camera.rotateY(180);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-mWidth/2, -mHeight/2);
        matrix.postTranslate(mWidth/2, mHeight/2);

        canvas.concat(matrix);
        drawChild(canvas,childView2,getDrawingTime());
        canvas.restore();

        //再画childView1
        canvas.save();
        View childView1=getChildAt(0);
        drawChild(canvas,childView1,getDrawingTime());
        canvas.restore();
    }

    private void RotateChildView(Canvas canvas,View childView,float mAngle){
        Matrix mMatrix=childView.getMatrix();

        if((mAngle>=90&&mAngle<=180)||(mAngle>=-180&&mAngle<=-90)){
            return;
        }

        canvas.save();

        mCamera.save();
        mCamera.rotateY(mAngle);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();

        // 修正失真，主要修改 MPERSP_0 和 MPERSP_1
        float[] mValues = new float[9];
        mMatrix.getValues(mValues);			    //获取数值
        mValues[6] = mValues[6]/scale;			//数值修正
        mValues[7] = mValues[7]/scale;			//数值修正
        mMatrix.setValues(mValues);			    //重新赋值

        mMatrix.preTranslate(-mWidth/2,-mHeight/2);
        mMatrix.postTranslate(mWidth/2,mHeight/2);

        canvas.concat(mMatrix);
        drawChild(canvas,childView,getDrawingTime());
        canvas.restore();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
