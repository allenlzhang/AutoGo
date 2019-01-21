package com.carlt.autogo.view.activity.home;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;

/**
 * Created by Marlon on 2019/1/19.
 */
public class CarLocationActivity extends BaseMvpActivity implements AMapLocationListener{

    @BindView(R.id.locationMap)
    MapView mMapView;
    private AMap               mMap;
    private AMapLocationClient mLocationClient;
    private AMapLocation       mFirstLoc;
    private Marker mLocMarker;
    private boolean isMyLocenable = false;
    private boolean isNeedRefresh = false;//是否需要刷新
    private AMapLocation      mCurrentLoc;
    private final static int ZOOM = 17;// 缩放级别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_location;
    }

    @Override
    public void init() {
        setTitleText("定位寻车");
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    private void init(Bundle savedInstanceState) {
        //        loadingDataUI();
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();

        // 定位图标样式
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.icon_loc);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        myLocationStyle.myLocationIcon(des);
        myLocationStyle.strokeColor(STROKE_COLOR);
        myLocationStyle.radiusFillColor(FILL_COLOR);
        myLocationStyle.strokeWidth(1.0f);
        myLocationStyle.anchor(0.5f, 0.5f);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.showMyLocation(true);
        mMap.setMyLocationStyle(myLocationStyle);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        setMylocEnable(false);

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //            mLocationOption.setInterval(2000);
            //            设置只定位一次
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //        loadSuccessUI();
        LogUtils.e(aMapLocation.getErrorCode() + aMapLocation.getAddress());
        if (aMapLocation.getErrorCode() == 0) {
            LogUtils.e("mFirstLoc----" + mFirstLoc);

            if (mFirstLoc == null) {
                mFirstLoc = aMapLocation;

                LatLng location = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());
                addCircle(location, aMapLocation.getAccuracy());// 添加定位精度圆
                addMarker(location);// 添加定位图标
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
                        ZOOM));
            }
            mFirstLoc = aMapLocation;

            if (isMyLocenable) {
                if (mCurrentLoc != null) {
                    if (mCurrentLoc.getLatitude() == aMapLocation.getLatitude()
                            && mCurrentLoc.getLongitude() == aMapLocation
                            .getLongitude()) {
                        isNeedRefresh = false;
                    } else {
                        isNeedRefresh = true;
                    }
                } else {
                    isNeedRefresh = true;
                }
                if (isNeedRefresh) {
                    // 显示更新 定位蓝点
                    if (mLocMarker != null) {
                        mLocMarker.remove();
                        mLocMarker = null;
                    }

                    addCircle(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()), mFirstLoc.getAccuracy());
                    addMarker(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
                }

                mCurrentLoc = aMapLocation;
            }

        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR   = Color.argb(10, 0, 0, 180);
    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_my_loc);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = mMap.addMarker(options);
    }

    public void setMylocEnable(boolean enable) {
        this.isMyLocenable = enable;
        if (!enable) {
            if (mLocMarker != null) {
                mLocMarker.remove();
            }
        }
    }
}
