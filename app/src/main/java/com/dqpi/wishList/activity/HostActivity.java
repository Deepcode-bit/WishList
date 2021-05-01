package com.dqpi.wishList.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dqpi.wishList.Dao.WishDao;
import com.dqpi.wishList.R;
import com.dqpi.wishList.databinding.ActivityHostBinding;
import com.dqpi.wishList.fragment.CalendarFragment;
import com.dqpi.wishList.fragment.DataFragment;
import com.dqpi.wishList.fragment.HomeFragment;
import com.dqpi.wishList.model.Wish;
import com.dqpi.wishList.util.App;
import com.dqpi.wishList.util.MyDataBase;
import com.dqpi.wishList.viewmodel.HostViewModel;

import java.io.Serializable;

public class HostActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    CalendarFragment calendarFragment;
    DataFragment dataFragment;
    ActivityHostBinding binding;
    HostViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_host);
        viewModel = new ViewModelProvider(this).get(HostViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setActivity(this);
        InitView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void InitView(){
        homeFragment = new HomeFragment();
        calendarFragment=new CalendarFragment();
        dataFragment=new DataFragment();
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Fragment fragment;
            switch (checkedId){
                case R.id.radio2:fragment=calendarFragment;break;
                case R.id.radio3:fragment=dataFragment;break;
                default:fragment=homeFragment;break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    public void onNewWishClick(View v) {
        Intent intent = new Intent().setClass(this, NewWishActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "round_but");
        startActivityForResult(intent,1,activityOptionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1) {
            if (data == null || data.getSerializableExtra("wish") == null) return;
            Wish wish = (Wish) data.getSerializableExtra("wish");
            homeFragment.mAdapter.notifyDataSetChanged();
            //存储到数据库中
            viewModel.addWish(wish);
        }
    }
}
