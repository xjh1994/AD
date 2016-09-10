package com.xjh1994.ad.module.map.activity;

import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xjh1994.ad.CustomApplication;
import com.xjh1994.ad.R;
import com.xjh1994.ad.base.BaseActivity;
import com.xjh1994.ad.module.map.service.LocationService;

import butterknife.Bind;

public class LocationActivity extends BaseActivity {

	@Bind(R.id.tv_location)
	TextView tvLocation;

	private LocationService locationService;

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_location;
	}

	@Override
	public void initViews() {

	}

	@Override
	public void initData() {

	}

	@Override
	protected void onStop() {
		locationService.unregisterListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		locationService = ((CustomApplication) getApplication()).locationService;
		locationService.registerListener(mListener);
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			locationService.setLocationOption(locationService.getDefaultLocationClientOption());
		} else if (type == 1) {
			locationService.setLocationOption(locationService.getOption());
		}
		locationService.start();
	}

	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				tvLocation.setText(location.getLocationDescribe());
			}
		}

	};
}
