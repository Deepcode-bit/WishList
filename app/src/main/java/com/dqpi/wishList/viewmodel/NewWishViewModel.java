package com.dqpi.wishList.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dqpi.wishList.model.Sticker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewWishViewModel extends AndroidViewModel {
    public MutableLiveData<List<Sticker>> stickers;
    public MutableLiveData<String> currentNum;
    public MutableLiveData<String> searchId;
    public MutableLiveData<String> wishName;
    public MutableLiveData<Integer> money;
    public MutableLiveData<Integer> maxMoney;

    public NewWishViewModel(@NonNull Application application) {
        super(application);
        stickers=new MutableLiveData<>(new ArrayList<>());
        currentNum=new MutableLiveData<>("550¥");
        searchId = new MutableLiveData<>("");
        wishName=new MutableLiveData<>("");
        money=new MutableLiveData<>(550);
        maxMoney=new MutableLiveData<>(1000);
        getMaxMoney();
        getEmojis();
    }

    private void getEmojis(){
        SharedPreferences preferences=getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        //设置默认值
        Set<String> codes=new HashSet<String>(){
            {
                add("128513");
                add("128514");
                add("128515");
                add("128516");
            }
        };
        Set<String> set= preferences.getStringSet("emoji",codes);
        while (set.size()<4){
            set.add("128513");
        }
        stickers.getValue().clear();
        for(String code : set){
            stickers.getValue().add(new Sticker("",Integer.parseInt(code)));
        }
    }

    private void setEojis(){
        if(stickers.getValue()==null)return;
        Set<String> set=new HashSet<>();
        for(Sticker sticker : stickers.getValue()){
            String code = sticker.getOriginCode();
            set.add(code);
        }
        SharedPreferences sharedPreferences=getApplication().getSharedPreferences("data",Context.MODE_PRIVATE);
        sharedPreferences.edit().putStringSet("emoji",set).apply();
    }

    public void ReplaceSticker(Sticker sticker) {
        if (stickers.getValue() == null) return;
        stickers.getValue().add(0, sticker);
        stickers.getValue().remove(stickers.getValue().size() - 1);
        setEojis();
    }

    private void getMaxMoney(){
        SharedPreferences sharedPreferences=getApplication().getSharedPreferences("data",Context.MODE_PRIVATE);
        int maxMoney = sharedPreferences.getInt("maxMoney", 1000);
        this.maxMoney.setValue(maxMoney);
        this.money.setValue(maxMoney/2);
    }

    public void setMaxMoney(int maxMoney) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("maxMoney", maxMoney).apply();
        this.maxMoney.setValue(maxMoney);
    }
}
