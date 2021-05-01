package com.dqpi.wishList.model;

import android.icu.math.BigDecimal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dqpi.wishList.util.BigDecimalConverter;
import com.dqpi.wishList.util.DateConverter;

import java.util.Date;

@TypeConverters(value = BigDecimalConverter.class)
@Entity
public class SavedMoney {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String dateStamp;
    private BigDecimal money;

    public SavedMoney(String dateStamp, BigDecimal money) {
        this.dateStamp = dateStamp;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
