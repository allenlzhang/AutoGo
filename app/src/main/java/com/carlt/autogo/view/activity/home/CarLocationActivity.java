package com.carlt.autogo.view.activity.home;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.utils.overlay.WalkRouteOverlay;

import butterknife.BindView;

/**
 * Created by Marlon on 2019/1/19.
 */
public class CarLocationActivity extends BaseMvpActivity implements AMapLocationListener ,AMap.InfoWindowAdapter{

    @BindView(R.id.locationMap)
    MapView mMapView;
    private AMap mMap;
    private AMapLocationClient mLocationClient;
    private AMapLocation mFirstLoc;
    private Marker mLocMarker;
    private boolean isMyLocenable = false;
    private boolean isNeedRefresh = false;//是否需要刷新
    private AMapLocation mCurrentLoc;
    private final static int ZOOM = 17;// 缩放级别
    private View infoWindow = null;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
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
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                        if (walkRouteResult.getPaths().size() > 0) {
                            if (mLocMarker != null){
                                mLocMarker.remove();
                            }
                            mWalkRouteResult = walkRouteResult;
                            final WalkPath walkPath = mWalkRouteResult.getPaths()
                                    .get(0);
                            if(walkPath == null) {
                                return;
                            }
                            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                    CarLocationActivity.this, mMap, walkPath,
                                    mWalkRouteResult.getStartPos(),
                                    mWalkRouteResult.getTargetPos());
                            walkRouteOverlay.removeFromMap();
                            walkRouteOverlay.addToMap();
                            walkRouteOverlay.zoomToSpan();
                        } else if (walkRouteResult.getPaths() == null) {
                            ToastUtils.showShort("没有结果");
                        }
                    } else {
                        ToastUtils.showShort("没有结果");
                    }
                } else {
                    ToastUtils.showShort(i);
                }

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
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
        mMap.setInfoWindowAdapter(this);
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
        LogUtils.e(aMapLocation.getErrorCode() + aMapLocation.getAddress() + "(" + aMapLocation.getLongitude() + "," + aMapLocation.getLatitude() + ")");
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
//                addCarMarker(new LatLng(34.2168,108.887));
                walkRoute(new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude()),
                        new LatLonPoint(34.2168,108.887));
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

    /**
     * 步行路线规划
     * @param from 起点坐标
     * @param to   终点坐标
     */
    private void walkRoute(LatLonPoint from,LatLonPoint to){
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from,to);

        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
        mRouteSearch.calculateWalkRouteAsyn(query);
    }

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

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

    private void addCarMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_car_location);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        markerOptions.icon(des);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(latLng);
        markerOptions.setInfoWindowOffset(0,310);
        Marker marker = mMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }
    /**
     * 自定义infowinfow窗口
     */
    GeocodeSearch geocoderSearch;
    String city;
    public void render(Marker marker, View view) {
        TextView mTxtNav = view.findViewById(R.id.txtLocationNav);
        TextView mTxtUpdateLoc = view.findViewById(R.id.txtUpdateLoc);
        final TextView mTxtLocation = view.findViewById(R.id.txtCarLocation);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS){
                    city = regeocodeResult.getRegeocodeAddress().getCity();
                    mTxtLocation.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude), 200,GeocodeSearch.AMAP);

        geocoderSearch.getFromLocationAsyn(query);

        mTxtNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("导航");
            }
        });
        mTxtUpdateLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showShort("修改位置");
                Intent intent = new Intent(CarLocationActivity.this,InputtipsActivity.class);
                intent.putExtra("city",city);
                startActivityForResult(intent,111);
            }
        });
    }
    public void setMylocEnable(boolean enable) {
        this.isMyLocenable = enable;
        if (!enable) {
            if (mLocMarker != null) {
                mLocMarker.remove();
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null){
            infoWindow = LayoutInflater.from(CarLocationActivity.this).inflate(R.layout.layout_car_location_txt,null);
        }
        render(marker,infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK&&requestCode == 111){
            if (data != null) {
                String LatLonPoint = data.getStringExtra("LatLonPoint");
                if (LatLonPoint != null) {
                    String latitude = LatLonPoint.split(",")[0];
                    String longitude = LatLonPoint.split(",")[1];
                    LatLng latLng = new LatLng(Double.valueOf(latitude),Double.valueOf(longitude));
                    mMap.clear();
                    walkRoute(new LatLonPoint(mFirstLoc.getLatitude(),mFirstLoc.getLongitude()),new LatLonPoint(latLng.latitude,latLng.longitude));
                }
            }
        }
    }
}
