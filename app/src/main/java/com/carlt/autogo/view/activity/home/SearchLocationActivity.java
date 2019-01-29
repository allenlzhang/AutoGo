package com.carlt.autogo.view.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.SearchAddrItemAdapter;
import com.carlt.autogo.base.BaseMvpActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
 /**
  * Description : 地址搜索页面
  * Author     : zhanglei
  * Date       : 2019/1/23
  */
public class SearchLocationActivity extends BaseMvpActivity {

    @BindView(R.id.iv_back)
    ImageView    ivBack;
    @BindView(R.id.tvSearch)
    TextView     tvSearch;
    @BindView(R.id.ivDelete)
    ImageView    ivDelete;
    @BindView(R.id.searchaddr_edt_addr)
    EditText     mEdtAddr;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    private AMapLocation          current;
    private SearchAddrItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search_location;
    }

    @Override
    public void init() {
        goneTitle();
        rvList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        String latlng = getIntent().getStringExtra("latlng");
        if (TextUtils.isEmpty(latlng)) {
            showToast("未获取到当前位置");
            return;
        }
        //        cityCode = getIntent().getStringExtra("cityCode");
        current = new AMapLocation("");
        current.setLatitude(Double.parseDouble(latlng.split(",")[0]));
        current.setLongitude(Double.parseDouble(latlng.split(",")[1]));
        mEdtAddr.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchLoc(mEdtAddr.getText().toString() + "", "");
                }
                return false;
            }
        });


    }

    @OnClick({R.id.iv_back, R.id.tvSearch, R.id.ivDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tvSearch:
                searchLoc(mEdtAddr.getText().toString() + "", "");
                break;
            case R.id.ivDelete:
                mEdtAddr.setText("");
                if (mAdapter != null) {
                    mAdapter.clear();
                }
                break;
        }
    }

    /**
     * 传入 用户输入地址，城市，返回可能的地址列表
     * @param addr
     * @param city
     */
    public void searchLoc(final String addr, String city) {
        if (TextUtils.isEmpty(addr)) {
            showToast("请输入地点");
            return;
        }
        //        showLoading(this);
        InputtipsQuery inputquery = new InputtipsQuery(addr, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(this, inputquery);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> arg0, int arg1) {
                if (arg1 == 1000) {
                    if (arg0.size() > 0) {
                        //                        dissMissLoading();
                        Collections.sort(arg0, new Comparator<Tip>() {
                            @Override
                            public int compare(Tip o1, Tip o2) {
                                LatLonPoint llp1 = o1.getPoint();
                                LatLonPoint llp2 = o2.getPoint();
                                float[] ff1 = new float[4];
                                float[] ff2 = new float[4];
                                AMapLocation.distanceBetween(current.getLatitude(), current.getLongitude(), llp1.getLatitude(), llp1.getLongitude(), ff1);
                                AMapLocation.distanceBetween(current.getLatitude(), current.getLongitude(), llp2.getLatitude(), llp2.getLongitude(), ff2);
                                float dis1 = ff1[0] / 1000;
                                float dis2 = ff2[0] / 1000;
                                if (dis1 > dis2) {
                                    return 1;
                                }
                                if (dis1 == dis2) {
                                    return 0;
                                }
                                return -1;
                            }
                        });
                        mAdapter = new SearchAddrItemAdapter(arg0, current);
                        rvList.setAdapter(mAdapter);
                        mAdapter.setText(addr);
                        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent();
                                Tip tip = (Tip) adapter.getItem(position);
                                intent.putExtra("latlng", tip.getPoint().getLatitude() + "," + tip.getPoint().getLongitude());
                                intent.putExtra("name", tip.getName());
                                intent.putExtra("address", tip.getAddress());
                                setResult(1, intent);
                                finish();
                            }
                        });
                        //                        if (mAdapter == null) {
                        //                            mAdapter = new AddressListAdapter(SearchAddrActivity.this, arg0, current);
                        //                            mAdapter.setText(addr);
                        //                            mList.setAdapter(mAdapter);
                        //                            mAdapter.notifyDataSetChanged();
                        //                        } else {
                        //                            mAdapter.setmList(arg0);
                        //                            mAdapter.setText(addr);
                        //                            mAdapter.notifyDataSetChanged();
                        //                        }
                    } else {
                        //                        loadError("没有结果");
                    }
                } else {
                    if (arg1 == 1804 || arg1 == 1806) {
                        //                        loadError("网络不稳定，请稍后再试");
                    } else {
                        //                        loadError("没有结果");
                    }
                }
            }
        });
        inputTips.requestInputtipsAsyn();
    }
}
