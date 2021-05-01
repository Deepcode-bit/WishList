package com.dqpi.wishList.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.dqpi.wishList.R;

import java.util.ArrayList;
import java.util.List;

public class CurveGraph extends View {
    private int staNum;//统计数量
    private List<Integer> staValues;//统计数据
    private List<Path> paths;//曲线路径集合
    private List<Rect> rectList;//矩形集合
    private List<String> titles;//数据名称
    private Rect maxRect;//最高的矩形
    private RectF numRect;//显示金额的矩形
    private int widthSize,heightSize;
    private int max;
    private Paint mPaint;

    public CurveGraph(Context context) {
        this(context,null);
    }

    public CurveGraph(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CurveGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.CurveGraph);
        staNum= array.getInt(R.styleable.CurveGraph_staNum, 0);
        initPaint();
    }

    public void setStaValues(List<Integer> values){
        this.staValues=values;
        invalidate();
    }

    public void setTitles(List<String> titles){
        this.titles=titles;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize=MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        heightSize=MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
    }

    private void initPaint() {
        mPaint=new Paint();
        mPaint.setColor(getResources().getColor(R.color.lineColor));       //设置画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);  //设置画笔模式为填充
        mPaint.setStrokeWidth(5f);
    }

    private void initRecList(){
        rectList=new ArrayList<>();
        int n=staValues.size();
        //求最大值
        for (int num:staValues){
            if(max<num) max=num;
        }
        if(max==0)return;
        for(int i=0;i<n;i++) {
            Rect rect = new Rect();
            int num = staValues.get(i);
            int realH = heightSize * 4 / 5;
            int left = widthSize * i / n;
            int top = realH * (max-num) / max;
            int right = left + widthSize / (n + 1);
            if (max == num) {
                top = heightSize / 5;
                maxRect=rect;
                numRect=new RectF(left,0,right,top-20);
            }
            rect.set(left, top, right, realH);
            rectList.add(rect);
        }
    }

    private void initPaths(){
        Point start,end,control1,control2;//四个与曲线相关的点
        start=new Point(); end=new Point(); control1=new Point(); control2=new Point();
        paths=new ArrayList<>();
        for(int i=0;i<rectList.size();i++){
            Rect rect=rectList.get(i);
            start.x=rect.left;
            end.y=start.y=rect.top + (rect.bottom-rect.top)/2;
            control1.x=rect.left + (rect.right-rect.left)/2;
            control1.y=rect.top;
            Path path=new Path();
            path.setLastPoint(start.x,start.y);
            //path.moveTo(start.x,start.y);

            if(i<rectList.size()-1){
                Rect nextRect=rectList.get(i+1);
                end.x=nextRect.left;
                end.y=nextRect.top + (nextRect.bottom-nextRect.top)/2;
                control2.x= rect.left +(nextRect.right-rect.left)/2;
                control2.y=nextRect.bottom;
                path.cubicTo(control1.x,control1.y,control2.x,control2.y,end.x,end.y);
            }else{
                end.x=rect.right;
                path.quadTo(control1.x,control1.y,end.x,end.y);
            }

            paths.add(path);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画底部的直线
        int y=heightSize * 4/5;
        canvas.drawLine(0,y,widthSize,y,mPaint);
        if(staValues!=null){
            initRecList();
            initPaths();
            mPaint.setAlpha(150);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(getResources().getColor(R.color.recColor1));
            for(Rect rect :rectList){
                canvas.drawRect(rect,mPaint);
            }
            mPaint.setAlpha(200);
            mPaint.setColor(getResources().getColor(R.color.recColor2));
            if(maxRect==null)return;
            canvas.drawRect(maxRect,mPaint);
            canvas.drawRoundRect(numRect,40,40,mPaint);
            mPaint.setColor(getResources().getColor(R.color.light_pink));
            mPaint.setPathEffect(new PathEffect());
            canvas.drawLine(maxRect.left,maxRect.bottom,maxRect.right,maxRect.bottom,mPaint);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(10f);
            for(Path path :paths){
                canvas.drawPath(path,mPaint);
            }
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(3f);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(40);
            float centerX=numRect.left + (numRect.right-numRect.left)/2 -40;
            float centerY=numRect.top + (numRect.bottom-numRect.top)/2 +15;
            canvas.drawText("¥"+max,centerX,centerY,mPaint);
            if(titles!=null){
                mPaint.setColor(getResources().getColor(R.color.fontColor2));
                for(int i=0;i<titles.size();i++){
                    Rect rect=rectList.get(i);
                    float fontX=rect.left+ (rect.right- rect.left)/2 -35;
                    float fontY=heightSize-40;
                    canvas.drawText(titles.get(i),fontX,fontY,mPaint);
                }
            }
        }
    }
}
