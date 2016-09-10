package com.xjh1994.ad;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.xjh1994.ad.module.map.service.LocationService;


/**
 * Created by Administrator on 2016/8/24.
 */

public class CustomApplication extends Application {

    public static CustomApplication mInstance;

    public static CustomApplication getInstance() {
        return mInstance;
    }

    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        init();
    }

    private void init() {
        initBaiduMap();
    }

    private void initBaiduMap() {
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void logout() {

    }
}
