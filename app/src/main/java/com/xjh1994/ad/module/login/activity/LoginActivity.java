package com.xjh1994.ad.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xjh1994.ad.R;
import com.xjh1994.ad.base.BaseActivity;
import com.xjh1994.ad.common.config.Constant;
import com.xjh1994.ad.common.model.User;
import com.xjh1994.ad.module.home.activity.MainActivity;
import com.xjh1994.ad.network.ServiceManager;
import com.xjh1994.ad.network.api.QQService;
import com.xjh1994.ad.network.model.QQ;
import com.xjh1994.ad.network.model.QQUserInfo;
import com.xjh1994.ad.util.AppManager;
import com.xjh1994.ad.util.RxUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/8/24.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_username)
    MaterialEditText etUsername;
    @Bind(R.id.et_password)
    MaterialEditText etPassword;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {

    }

    @OnClick({R.id.btn_login, R.id.btn_qq, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_qq:
                toast(getString(R.string.openning_qq));
                loginByQQ();
                break;
            case R.id.btn_register:
                startAnimActivity(RegisterActivity.class);
                break;
            default:
                break;
        }
    }

    private void login() {
        if (checkNotNull()) {
            KeyboardUtil.hideKeyboard(this);

            BmobUser bu2 = new BmobUser();
            bu2.setUsername(username);
            bu2.setPassword(password);
            bu2.login(new SaveListener<BmobUser>() {

                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if(e==null){
                        toast("登录成功");
                        finish();
                        startAnimActivity(MainActivity.class);
                    }else{
                        log(e);
                    }
                }
            });
        }
    }

    private boolean checkNotNull() {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            return true;
        }

        toast(getString(R.string.username_or_password_cannot_be_empty));
        return false;
    }

    public static Tencent mTencent;
    private IUiListener loginListener;

    private void loginByQQ() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constant.QQ_APP_ID, getApplicationContext());
        }
        mTencent.logout(this);
        loginListener = new IUiListener() {

            @Override
            public void onComplete(Object arg0) {
                if (arg0 != null) {
                    JSONObject jsonObject = (JSONObject) arg0;
                    try {
                        String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                        String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                        String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, token, expires, openId);
                        loginWithAuth(authInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(UiError arg0) {
                toast("QQ授权出错：" + arg0.errorCode + "--" + arg0.errorDetail);
            }

            @Override
            public void onCancel() {
                toast("取消qq授权");
            }
        };
        mTencent.login(this, "all", loginListener);
    }

    private void loginWithAuth(BmobUser.BmobThirdUserAuth authInfo) {
        setRefreshing(true);
        BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
            @Override
            public void done(JSONObject jsonObject, BmobException e) {
                if (e == null) {
                    QQ qq = new Gson().fromJson(jsonObject.toString(), QQ.class);
                    getUserInfo(qq);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUserInfo(QQ qq) {
        QQService service = ServiceManager.createService(QQService.class);
        service.getUserInfo(qq.getQq().getAccess_token(), qq.getQq().getOpenid(), Constant.QQ_APP_ID, "json")
                .compose(RxUtils.<QQUserInfo>applySchedulers())
                .subscribe(new Subscriber<QQUserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        setRefreshing(false);
                    }

                    @Override
                    public void onNext(QQUserInfo qqUserInfo) {
                        String nickName = qqUserInfo.getNickname();
                        String avatar = qqUserInfo.getFigureurl_2();
                        bindBmob(nickName, avatar);
                    }
                });
    }

    private void bindBmob(String nickName, final String avatar) {
        final User user = getCurrentUser();
        user.setNick(nickName);
        user.setAvatar(avatar);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast(getString(R.string.login_success));
                    startAnimActivity(MainActivity.class);
                    finish();
                } else {
                    toast(getString(R.string.login_failed));
                }

                setRefreshing(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
