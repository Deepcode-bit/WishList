package com.dqpi.wishList.model;

import android.icu.math.BigDecimal;
import android.support.v4.media.RatingCompat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dqpi.wishList.util.BigDecimalConverter;

import java.io.Serializable;

@TypeConverters(value = BigDecimalConverter.class)
@Entity
public class Wish implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private BigDecimal saved;
    private BigDecimal target;
    private String sticker;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Wish SetTitle(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title){
        this.title=title;
    }
    public BigDecimal getSaved() {
        return saved;
    }

    public Wish SetSaved(BigDecimal saved) {
        this.saved = saved;
        return this;
    }
    public void setSaved(BigDecimal saved) {
        this.saved = saved;
    }
    public BigDecimal getTarget() {
        return target;
    }

    public Wish SetTarget(BigDecimal target) {
        this.target = target;
        return this;
    }
    public void setTarget(BigDecimal target) {
        this.target = target;
    }
    public String getSticker() {
        return sticker;
    }

    public Wish SetSticker(String sticker) {
        this.sticker = sticker;
        return this;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }
}
