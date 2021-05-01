package com.dqpi.wishList.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.dqpi.wishList.Dao.UserDao;
import com.dqpi.wishList.R;
import com.dqpi.wishList.databinding.ActivityLoginBinding;
import com.dqpi.wishList.model.User;
import com.dqpi.wishList.util.App;
import com.dqpi.wishList.util.MyDataBase;

import java.util.List;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    public MutableLiveData<String> userName,pwd;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        binding.setData(this);
        InitView();
        InitData();
    }

    private void InitView(){
        int drawable=R.drawable.head_bg;
        Random random=new Random();
        int num = random.nextInt(5);
        switch (num){
            case 0:drawable=R.drawable.head_bg;break;
            case 1:drawable=R.drawable.head_bg2;break;
            case 2:drawable=R.drawable.head_bg3;break;
            case 3:drawable=R.drawable.head_bg4;break;
            case 4:drawable=R.drawable.head_bg5;break;
            default:break;
        }
        binding.headBg.setImageDrawable(getDrawable(drawable));
    }

    void InitData(){
        userName=new MutableLiveData<>("");
        pwd=new MutableLiveData<>("");
        AutoFill();
    }

    public void Login() {
        if (userName.getValue() == null || pwd.getValue() == null || userName.getValue().isEmpty() || pwd.getValue().isEmpty()) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        App.mThreadPool.execute(()->{
            UserDao userDao = MyDataBase.getInstance(this).getUserDao();
            List<User> users = userDao.getAllUser();
            for(User user : users){
                if(user.getUserName().equals(userName.getValue())){
                    if(!user.getPwd().equals(pwd.getValue())){
                        runOnUiThread(()-> Toast.makeText(this,"密码不正确",Toast.LENGTH_SHORT).show());
                        return;
                    }
                    runOnUiThread(()-> {
                        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                        loginSuccess();
                    });
                    return;
                }
            }
            userDao.insertUser(new User(userName.getValue(),pwd.getValue()));
            runOnUiThread(()->{
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                loginSuccess();
            });
        });
    }

    void loginSuccess() {
        SharedPreferences sharedPreferences = getSharedPreferences("sp", MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("userName", userName.getValue())
                .putString("pwd", pwd.getValue())
                .apply();
        Intent intent = new Intent().setClass(this, HostActivity.class);
        App.localUser = new User(userName.getValue(), pwd.getValue());
        startActivity(intent);
        this.finish();
    }


    /**
     * 自动填写登录信息
     */
    void AutoFill(){
        SharedPreferences sharedPreferences=getSharedPreferences("sp",MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", this.userName.getValue());
        String pwd=sharedPreferences.getString("pwd",this.pwd.getValue());
        if(userName==null || pwd==null)return;
        this.userName.setValue(userName);
        this.pwd.setValue(pwd);
    }
}
