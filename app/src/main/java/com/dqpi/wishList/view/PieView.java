package com.dqpi.wishList.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dqpi.wishList.R;
import com.dqpi.wishList.model.PieData;

import java.util.ArrayList;

public class PieView extends View {
    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private ArrayList<PieData> mData;
    // 宽高
    private int mWidth, mHeight;
    private final int fontSize=30;
    // 画笔
    private Paint mPaint = new Paint();
    private Paint fontPaint=new Paint();
    //颜色为固定值
    private int[] mColors =
            { R.color.dark_blue, R.color.light_blue, R.color.dark_purple, R.color.light_pink,
                    R.color.font_color_3,R.color.font_color_1,R.color.fontColor2};

    public PieView(Context context) {
        this(context,null);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        fontPaint.setColor(Color.BLACK);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setTextSize(fontSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    // 初始化数据
    private void initData(ArrayList<PieData> mData) {
        if (null == mData || mData.size() == 0)   // 数据有问题 直接返回
            return;

        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            sumValue += pie.getValue();       //计算数值和
            pie.setColor(mColors[i]);
        }

        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            float percentage = pie.getValue() / sumValue;   // 百分比
            float angle = percentage * 360;                 // 对应的角度

            pie.setPercentage(percentage);                  // 记录百分比
            pie.setAngle(angle);                            // 记录角度大小
        }
    }

    // 设置数据
    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initData(mData);
        invalidate();   // 刷新
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mData)
            return;
        float currentStartAngle = mStartAngle;                    // 当前起始角度
        canvas.translate(mWidth * 1/4, mHeight / 2);                // 将画布坐标原点移动到中心位置
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);  // 饼状图半径
        RectF rect = new RectF(-r, -r, r, r);                     // 饼状图绘制区域

        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            if(pie.getValue()!=0) {
                mPaint.setColor(getResources().getColor(pie.getColor()));
                canvas.drawArc(rect, currentStartAngle, pie.getAngle(), true, mPaint);
                currentStartAngle += pie.getAngle();
            }
        }
        //绘制说明文字
        for(int i=0,j=0,x=0;i<mData.size();i++,j++) {
            PieData pie = mData.get(i);
            if (j == 3) {
                j = 0;
                x++;
            }
            float xDis = x*170 + 30;
            canvas.drawText(pie.getName(), rect.right + xDis, rect.top + j * 100, fontPaint);
            mPaint.setColor(getResources().getColor(pie.getColor()));
            float top = rect.top + j * 100 - fontSize;
            float left = rect.right + xDis + 70;
            float right = left + 50;
            float bottom = top + 50;
            RectF miniRect = new RectF(left, top, right, bottom);
            canvas.drawRect(miniRect, mPaint);
        }
    }
}
