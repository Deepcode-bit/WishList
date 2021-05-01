package com.dqpi.wishList.adapter;

import android.icu.math.BigDecimal;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.dqpi.wishList.R;
import com.dqpi.wishList.model.Wish;
import com.dqpi.wishList.util.App;
import com.dqpi.wishList.util.Rotate3dAnimation;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishViewHolder> {

    private List<Wish> wishes;
    //定义ViewType常量
    private static final int TYPE_NORMAL=1;
    private static final int TYPE_HEAD=0;
    private static final int TYPE_SELECT=2;
    private int selectIndex=-1;
    private onItemLongClickListener listener;
    private String averNumText;

    public interface onItemLongClickListener{
        void onItemLongClick(View v,int position);
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        this.listener = listener;
    }

    public void setWishes(List<Wish> list){
        this.wishes=list;
    }

    public void setAverNumText(String text) {
        this.averNumText = "¥" + text;
    }

    @NonNull
    @Override
    public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.wish_cell, parent, false);
        if (viewType == TYPE_HEAD) {
            itemView = inflater.inflate(R.layout.wish_head, parent, false);
        }
        if (viewType == TYPE_SELECT) {
            itemView = inflater.inflate(R.layout.wish_detail_cell, parent, false);
        }
        return new WishViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull WishViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEAD) {
            if (App.localUser != null && App.localUser.getUserName() != null)
                holder.nameText.setText(App.localUser.getUserName());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int hour = calendar.get(Calendar.HOUR);
            //选择时段
            String hello = calendar.get(Calendar.AM_PM) == 0 ? hour > 9 ? "上午好" : "早上好" : hour > 6 ? "晚上好" : "下午好";
            holder.helloText.setText(hello);
            if(averNumText!=null)holder.averNum.setText(averNumText);
            return;
        }
        Wish wish = wishes.get(position);
        String number = "¥" + wish.getSaved().toString();
        String target = "¥" + wish.getTarget().toString();
        if (getItemViewType(position) == TYPE_SELECT) {
            holder.savedNum.setText(number);
            holder.targetNum.setText(target);
            holder.wishDetail.setOnClickListener((v) -> onItemClick(v, position));
            holder.seekBar.setMax(wish.getTarget().intValue());
            holder.seekBar.setProgress(wish.getSaved().intValue());
            //计算达成目标的百分比
            if (wish.getTarget().intValue() != 0) {
                BigDecimal result = wish.getSaved().divide(wish.getTarget(), 2, BigDecimal.ROUND_HALF_UP);
                result = result.multiply(new BigDecimal(100));
                String progress = result.intValue() + "%";
                holder.progressTxt.setText(progress);
            } else
                holder.progressTxt.setText("0%");
            return;
        }
        holder.title.setText(wish.getTitle());
        holder.savedNumber.setText(number);
        holder.sticker.setText(wish.getSticker());
        holder.wishLayout.setOnClickListener((v) -> onItemClick(v, position));
        holder.wishLayout.setOnLongClickListener((v) -> {
            if (listener != null)
                listener.onItemLongClick(v, position);
            return true;
        });
        holder.qmuiProgressBar.setMaxValue(wish.getTarget().intValue());
        holder.qmuiProgressBar.setProgress(wish.getSaved().intValue());
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
        if (position == selectIndex)
            return TYPE_SELECT;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return wishes.size();
    }

    private void onItemClick(View v,int position){
        final float centerX=100;
        final float centerY=100;
        Rotate3dAnimation rotationAnimator = new Rotate3dAnimation(v.getContext(),0, 90, centerX, centerY, 0f, false);
        rotationAnimator.setDuration(200);                         //设置动画时长
        rotationAnimator.setFillAfter(true);                        //保持旋转后效果
        rotationAnimator.setInterpolator(new LinearInterpolator());
        v.startAnimation(rotationAnimator);
        rotationAnimator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                selectIndex = selectIndex == position ? -1 : position;
                notifyDataSetChanged();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    static class WishViewHolder extends RecyclerView.ViewHolder{
        private TextView title,progressTxt,averNum;
        private TextView savedNumber,sticker,savedNum,targetNum,helloText,nameText;
        private LinearLayout wishLayout,wishDetail;
        private SeekBar seekBar;
        private QMUIProgressBar qmuiProgressBar;

        WishViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.wish_title);
            savedNumber = itemView.findViewById(R.id.saved_number);
            sticker = itemView.findViewById(R.id.sticker);
            wishLayout = itemView.findViewById(R.id.wish_layout);
            wishDetail=itemView.findViewById(R.id.wish_detail_layout);
            savedNum = itemView.findViewById(R.id.saved_num);
            targetNum = itemView.findViewById(R.id.target_num);
            helloText=itemView.findViewById(R.id.hello_text);
            nameText=itemView.findViewById(R.id.name_text);
            seekBar=itemView.findViewById(R.id.seek_bar);
            progressTxt=itemView.findViewById(R.id.progress_text);
            qmuiProgressBar=itemView.findViewById(R.id.progress_wish);
            averNum=itemView.findViewById(R.id.aver_num);
        }
    }
}
