package com.xjh1994.ad.module.login.activity;

import android.text.TextUtils;
import android.view.View;

import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xjh1994.ad.R;
import com.xjh1994.ad.base.BaseActivity;
import com.xjh1994.ad.common.model.User;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/9/11.
 */

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.et_username)
    MaterialEditText etUsername;
    @Bind(R.id.et_password)
    MaterialEditText etPassword;

    private String username;
    private String password;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViews() {

    }

    @OnClick({R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        if (checkNotNull()) {
            KeyboardUtil.hideKeyboard(this);

            BmobUser bu = new BmobUser();
            bu.setUsername(username);
            bu.setPassword(password);
            bu.signUp(new SaveListener<User>() {
                @Override
                public void done(User s, BmobException e) {
                    if (e == null) {
                        toast(getString(R.string.toast_register_success));
                        finish();
                    } else {
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

    @Override
    public void initData() {

    }
}
