package com.dqpi.wishList.util;

import android.icu.math.BigDecimal;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

public class BigDecimalConverter {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @TypeConverter
    public BigDecimal fromLong(Long value) {
        return value == null ? null : new BigDecimal(value).divide(new BigDecimal(100));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @TypeConverter
    public Long toLong(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } else {
            return bigDecimal.multiply(new BigDecimal(100)).longValue();
        }
    }
}
