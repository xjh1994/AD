package com.xjh1994.ad.module.home.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.xjh1994.ad.R;
import com.xjh1994.ad.base.BaseTabLayoutActivity;
import com.xjh1994.ad.module.home.fragment.HomeFragment;

import java.util.ArrayList;


public class MainActivity extends BaseTabLayoutActivity {

    private Toolbar toolbar;
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initViews() {
        super.initViews();
        setSwipeBackEnable(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabBar(savedInstanceState);
    }

    private void initTabBar(Bundle savedInstanceState) {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

        });
        final IProfile profile3 = new ProfileDrawerItem()
                .withName(getCurrentUser().getNick())
                .withIdentifier(0);
        if (!TextUtils.isEmpty(getCurrentUser().getAvatar())) {
            profile3.withIcon(getCurrentUser().getAvatar());
        }

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile3)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(new PrimaryDrawerItem()
                                .withName(R.string.home)
                                .withIcon(FontAwesome.Icon.faw_home)
                                .withIdentifier(1)
                                .withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.action_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(2))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {

                            }

                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String[] getTitles() {
        return new String[]{"首页", "搞笑", "泰国", "公益"};
    }

    @Override
    protected void addFragments(ArrayList<Fragment> fragments) {
        fragments.add(new HomeFragment());
        fragments.add(new Fragment());
        fragments.add(new HomeFragment());
        fragments.add(new Fragment());
    }
}
