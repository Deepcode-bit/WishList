package com.dqpi.wishList.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dqpi.wishList.R;
import com.dqpi.wishList.model.Sticker;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    public interface onItemSelectListener{
        void onItemSelect(View v,int id);
    }
    private onItemSelectListener listener;

    private List<Sticker> stickers;

    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
    }

    public void setListener(onItemSelectListener listener){
        this.listener=listener;
    }

    public int selectIndex=-1;

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.emoji_cell, null, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Sticker sticker = stickers.get(position);
        holder.unicodeText.setText(sticker.getUnicode());
        int bgId = position == selectIndex ? R.drawable.cicler_bg : R.drawable.cicler_bg2;
        holder.emojiLayout.setBackgroundResource(bgId);
        holder.emojiLayout.setOnClickListener((v) -> {
            selectIndex = position == selectIndex ? -1 : position;
            if (listener != null)
                listener.onItemSelect(v, selectIndex);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    static class StickerViewHolder extends RecyclerView.ViewHolder {
        TextView unicodeText;
        LinearLayout emojiLayout;

        StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            unicodeText=itemView.findViewById(R.id.unicode);
            emojiLayout=itemView.findViewById(R.id.emoji_bg);
        }
    }
}
