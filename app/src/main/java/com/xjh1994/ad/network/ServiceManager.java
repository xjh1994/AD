package com.xjh1994.ad.network;


import com.xjh1994.ad.common.config.Constant;
import com.xjh1994.ad.network.api.QQService;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/25.
 */

public class ServiceManager {

    public static final String TAG = ServiceManager.class.getSimpleName();

    private static HashMap<String, Object> mServiceMap = new HashMap<String, Object>();

    /**
     * 创建Retrofit Service
     *
     * @param t   Service类型
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> t) {
        T service = (T) mServiceMap.get(t.getName());

        if (service == null) {
            OkHttpClient okHttpClient = new OkHttpClient();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getEndPoint(t))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            service = retrofit.create(t);
            mServiceMap.put(t.getName(), service);
        }
        return service;
    }

    /**
     * 获取EndPoint URL
     *
     * @param t   Service类型
     * @param <T>
     * @return
     */
    public static <T> String getEndPoint(Class<T> t) {
        String endPoint = "";
        if (t.getName().equals(QQService.class.getName())) {
            endPoint = Constant.URL_QQ;
        }
        if ("".equals(endPoint)) {
            throw new IllegalArgumentException("Error: Can't get end point mUrl. Please configure at the method " + ServiceManager.class.getSimpleName() + ".getEndPoint(T t)");
        }
        return endPoint;
    }
}
