package com.xjh1994.ad.network.api;

import com.xjh1994.ad.network.model.QQUserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/25.
 */

public interface QQService {

    @GET("user/get_user_info")
    Observable<QQUserInfo> getUserInfo(@Query("access_token") String access_token,
                                       @Query("openid") String openid,
                                       @Query("oauth_consumer_key") String oauth_consumer_key,
                                       @Query("format") String format);
}
