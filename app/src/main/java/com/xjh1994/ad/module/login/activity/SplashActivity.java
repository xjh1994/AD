package com.xjh1994.ad.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.xjh1994.ad.common.config.Constant;
import com.xjh1994.ad.module.home.activity.MainActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/24.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Constant.BMOB_APPID);
        MobclickAgent.openActivityDurationTrack(false);

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
