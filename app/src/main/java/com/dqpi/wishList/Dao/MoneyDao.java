package com.dqpi.wishList.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dqpi.wishList.model.SavedMoney;

import java.util.Date;
import java.util.List;

@Dao
public interface MoneyDao {
    @Insert
    void insertMoney(SavedMoney money);
    @Delete
    void deleteMoney(SavedMoney money);
    @Update
    void updateMoney(SavedMoney money);
    @Query("select * from savedmoney order by id desc")
    List<SavedMoney> getAllMoney();
    @Query("select * from savedmoney where dateStamp=:dataStamp")
    SavedMoney getMoneyByDate(String dataStamp);
}
