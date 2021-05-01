package com.dqpi.wishList.util;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dqpi.wishList.Dao.MoneyDao;
import com.dqpi.wishList.Dao.UserDao;
import com.dqpi.wishList.Dao.WishDao;
import com.dqpi.wishList.model.SavedMoney;
import com.dqpi.wishList.model.User;
import com.dqpi.wishList.model.Wish;

@Database(entities = {User.class, Wish.class, SavedMoney.class},version = 1,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    private static MyDataBase instance;

    public static MyDataBase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),MyDataBase.class,"my_data").build();
        }
        return instance;
    }

    public abstract UserDao getUserDao();

    public abstract WishDao getWishDao();

    public abstract MoneyDao getMoneyDao();
}
