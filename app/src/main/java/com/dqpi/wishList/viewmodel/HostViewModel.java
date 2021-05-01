package com.dqpi.wishList.viewmodel;

import android.app.Application;
import android.icu.math.BigDecimal;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dqpi.wishList.Dao.MoneyDao;
import com.dqpi.wishList.Dao.WishDao;
import com.dqpi.wishList.model.SavedMoney;
import com.dqpi.wishList.model.Wish;
import com.dqpi.wishList.util.App;
import com.dqpi.wishList.util.MyDataBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HostViewModel extends AndroidViewModel {
    public MutableLiveData<List<Wish>> wishes;
    public MutableLiveData<String> year,month;
    public MutableLiveData<List<SavedMoney>> savedMonies;
    public MutableLiveData<String> averMoney;
    public List<Integer> dayMonies;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HostViewModel(@NonNull Application application) {
        super(application);
        Calendar calendar = Calendar.getInstance();
        year = new MutableLiveData<>(calendar.get(Calendar.YEAR) + "年");
        month = new MutableLiveData<>(calendar.get(Calendar.MONTH) + "月");
        wishes=new MutableLiveData<>(new ArrayList<>());
        savedMonies =new MutableLiveData<>(new ArrayList<>());
        dayMonies=new ArrayList<>();
        averMoney=new MutableLiveData<>("0");
        getData();
    }

    private void getData(){
        //从本地数据库中读取数据
        App.mThreadPool.execute(this::getWishes);
        App.mThreadPool.execute(this::getSavedMoneys);
        App.mThreadPool.execute(this::getDay);
    }

    private void getWishes(){
        WishDao wishDao = MyDataBase.getInstance(getApplication()).getWishDao();
        if(wishes.getValue()!=null) {
            wishes.getValue().clear();
            //wishes.postValue(wishDao.getAllWishes());//这个不好使
            List<Wish> wishList = wishDao.getAllWishes();
            wishList.add(0,new Wish());
            for(Wish wish : wishList){
                wishes.getValue().add(wish);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean addMoney(int position, BigDecimal money) {
        if (wishes.getValue() == null) return false;
        Wish wish = wishes.getValue().get(position);
        BigDecimal afterMoney = wish.getSaved().add(money);
        if(afterMoney.compareTo(wish.getTarget()) > 0){
            return false;
        }
        wish.setSaved(afterMoney);
        //保存在数据库
        App.mThreadPool.execute(() -> {
            WishDao wishDao = MyDataBase.getInstance(getApplication()).getWishDao();
            wishDao.updateWish(wish);
            MoneyDao moneyDao = MyDataBase.getInstance(getApplication()).getMoneyDao();
            //获取当前日期
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String timeStamp=formatter.format(new Date());
            SavedMoney savedMoney=moneyDao.getMoneyByDate(timeStamp);
            if(savedMoney==null)
                moneyDao.insertMoney(new SavedMoney(timeStamp,money));
            else {
                BigDecimal sum = savedMoney.getMoney().add(money);
                savedMoney.setMoney(sum);
                moneyDao.updateMoney(savedMoney);
            }
            //刷新数据
            getData();
        });
        return true;
    }

    public void deleteWish(int position) {
        if (wishes.getValue() == null) return;
        Wish wish = wishes.getValue().get(position);
        wishes.getValue().remove(wish);
        App.mThreadPool.execute(() -> {
            WishDao wishDao = MyDataBase.getInstance(getApplication()).getWishDao();
            wishDao.deleteWish(wish);
        });
    }

    private void getSavedMoneys(){
        MoneyDao moneyDao = MyDataBase.getInstance(getApplication()).getMoneyDao();
        List<SavedMoney> allMoney = moneyDao.getAllMoney();
        if(savedMonies.getValue()==null)return;
        savedMonies.getValue().clear();
        for(SavedMoney money : allMoney){
            savedMonies.getValue().add(money);
        }
        App.mThreadPool.execute(this::getAverageMoney);
    }

    /**
     * 获取本周的存贮数据
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDay(){
        MoneyDao moneyDao = MyDataBase.getInstance(getApplication()).getMoneyDao();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //国外从周天开始
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Date date=new Date();
        dayMonies.clear();
        for(int i=day;i!=0;i--) {
            date.setDate(calendar.get(Calendar.DAY_OF_MONTH));
            String timeStamp = formatter.format(date);
            SavedMoney money = moneyDao.getMoneyByDate(timeStamp);
            int moneyNum = money == null ? 0 : money.getMoney().intValue();
            dayMonies.add(0,moneyNum);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        //未到的用0补齐
        while (dayMonies.size()<7){
            dayMonies.add(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getAverageMoney(){
        if(savedMonies.getValue()==null)return;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            BigDecimal sum=new BigDecimal(0);
            int count=0;
            for (int i = 0; i < savedMonies.getValue().size(); i++) {
                SavedMoney money = savedMonies.getValue().get(i);
                String dateStamp = money.getDateStamp();
                Date date = formatter.parse(dateStamp);
                Calendar calendar =Calendar.getInstance();
                calendar.setTime(date);
                if(calendar.get(Calendar.MONTH)==Calendar.getInstance().get(Calendar.MONTH)){
                    sum=sum.add(money.getMoney());
                    count++;
                }
            }
            BigDecimal aver = sum.divide(new BigDecimal(count));
            averMoney.postValue(aver.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.i("PBF",e.getMessage());
        }
    }

    public void addWish(Wish wish){
        App.mThreadPool.execute(()->{
            WishDao wishDao = MyDataBase.getInstance(getApplication()).getWishDao();
            wishDao.insertWish(wish);
            getData();
        });
    }
}
