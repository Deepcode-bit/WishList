package com.dqpi.wishList.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dqpi.wishList.model.Wish;

import java.util.List;

@Dao
public interface WishDao {
    @Insert
    void insertWish(Wish wish);
    @Delete
    void deleteWish(Wish wish);
    @Update
    void updateWish(Wish wish);
    @Query("select * from wish order by id desc")
    List<Wish> getAllWishes();
}
