package com.xjh1994.ad.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.xjh1994.ad.CustomApplication;
import com.xjh1994.ad.R;
import com.xjh1994.ad.common.model.User;
import com.xjh1994.ad.module.login.activity.LoginActivity;
import com.xjh1994.ad.util.AppManager;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by xjh1994 on 2015/12/28.
 */
public abstract class BaseActivity extends MySwipeBackActivity {

    public static final String TAG = "iql";

    public CustomApplication mApplication;

    protected User currentUser = null;

    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;

    public int activityState;

    protected SwipeBackLayout mSwipeBackLayout;
    protected ProgressDialog mDialog;

    // 是否允许全屏
    private boolean mAllowFullScreen = false;

    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        }

        super.onCreate(savedInstanceState);

        mApplication = CustomApplication.getInstance();

//        checkLogin();

        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AppManager.getAppManager().addActivity(this);

        //Actionbar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
//        actionBar.setDisplayShowHomeEnabled(true);
        // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，
        // 否则，显示应用程序图标，对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME

        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initDialog();
        initViews();
        mSwipeBackLayout = getSwipeBackLayout();
        initData();
    }

    private void initDialog() {
        mDialog = new ProgressDialog(this);
    }

    /**
     * 隐藏软键盘
     * hideSoftInputView
     *
     * @param
     * @return void
     * @throws
     * @Title: hideSoftInputView
     * @Description: TODO
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setRefreshing(boolean isRefreshing) {
        if (isRefreshing) {
            mDialog.show();
        } else {
            mDialog.dismiss();
        }
    }

    /**
     * 显示下线的对话框
     * showOfflineDialog
     *
     * @return void
     * @throws
     */
    public void showOfflineDialog(final Context context) {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_offline)
                .positiveText(R.string.relogin)
                .negativeText(R.string.exit)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        CustomApplication.getInstance().logout();
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AppManager.getAppManager().finishAllActivity();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 设置布局文件
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化布局文件中的控件
     */
    public abstract void initViews();

    /**
     * 进行数据初始化
     * initData
     */
    public abstract void initData();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startAnimActivity(Class<?> cla) {
        this.startActivity(new Intent(this, cla));
    }

    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
    }

    protected User getCurrentUser() {
        currentUser = BmobUser.getCurrentUser(User.class);
        if (null == currentUser) {
            toast(getString(R.string.toast_not_login));
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            return null;
        }

        return currentUser;
    }

    public void log(Object msg) {
        Logger.d(msg);
    }

    Toast mToast;

    public void toast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    public void toastLong(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_LONG);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ACTIVITY_RESUME;
        MobclickAgent.onPageStart(this.getClass().toString());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = ACTIVITY_STOP;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = ACTIVITY_PAUSE;
        MobclickAgent.onPageEnd(this.getClass().toString());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        ButterKnife.unbind(this);
        activityState = ACTIVITY_DESTROY;
        AppManager.getAppManager().finishActivity(this);
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
