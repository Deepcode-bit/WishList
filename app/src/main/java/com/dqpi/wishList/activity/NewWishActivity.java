package com.dqpi.wishList.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.SeekBar;

import com.dqpi.wishList.R;
import com.dqpi.wishList.databinding.ActivityNewWishBinding;
import com.dqpi.wishList.model.Sticker;
import com.dqpi.wishList.viewmodel.NewWishViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewWishActivity extends AppCompatActivity{

    public ActivityNewWishBinding binding;
    private NewWishViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_new_wish);
        binding.setData(this);
        binding.setLifecycleOwner(this);
        viewModel= new ViewModelProvider(this).get(NewWishViewModel.class);
    }
}
