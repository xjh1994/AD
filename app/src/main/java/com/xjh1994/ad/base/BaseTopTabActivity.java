package com.xjh1994.ad.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xjh1994.ad.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/6.
 */

public abstract class BaseTopTabActivity extends BaseActivity {

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private String[] mTitles;
    protected ViewPager mViewPager;
    protected SlidingTabLayout mTabLayout_1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_tab;
    }

    @Override
    public void initViews() {
        mTitles = getTitles();  // return new String[] {"待安排", "已安排"};
        int currentItem = getCurrentItem();

        addFragments(mFragments);
        mViewPager = (ViewPager) findViewById(R.id.vp_2);
        mViewPager.setOffscreenPageLimit(mFragments.size() - 1);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout_1 = (SlidingTabLayout) findViewById(R.id.tl_1);
        mTabLayout_1.setViewPager(mViewPager);

        mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_1.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(currentItem);
    }

    @Override
    public void initData() {

    }

    private int getCurrentItem() {
        return 0;
    }

    protected abstract String[] getTitles();

    protected abstract void addFragments(ArrayList<Fragment> fragments);

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
