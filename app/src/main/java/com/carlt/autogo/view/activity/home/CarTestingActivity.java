package com.carlt.autogo.view.activity.home;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.adapter.WaringLampAdapter;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.home.WaringItemInfo;
import com.carlt.autogo.entry.home.WaringLampInfo;
import com.carlt.autogo.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Marlon on 2019/1/21.
 */
public class CarTestingActivity extends BaseMvpActivity {
    @BindView(R.id.rvCarTesting)
    RecyclerView rvCarTesting;

    private WaringLampInfo waringLampInfo;
    private List<WaringItemInfo> dataList = new ArrayList<>();
    private int[] icon = {R.mipmap.ic_abs, R.mipmap.ic_tpms, R.mipmap.ic_esp, R.mipmap.ic_engine_temperature,
            R.mipmap.ic_epb, R.mipmap.ic_svs, R.mipmap.ic_srs, R.mipmap.ic_engine, R.mipmap.ic_eps, R.mipmap.ic_cvt,
            R.mipmap.ic_ag_temperature, R.mipmap.ic_ag_malfunction};

    private int[] iconLight = {R.mipmap.ic_abs_light, R.mipmap.ic_tpms_light, R.mipmap.ic_esp_light, R.mipmap.ic_engine_temperature_light,
            R.mipmap.ic_epb_light, R.mipmap.ic_svs_light, R.mipmap.ic_srs_light, R.mipmap.ic_engine_light, R.mipmap.ic_eps_light,
            R.mipmap.ic_cvt_light, R.mipmap.ic_ag_temperature_light, R.mipmap.ic_ag_malfunction_light};

    private String[] txt = {"ABS故障指示灯", "TPMS故障指示灯", "ESP故障指示灯", "发动机冷却液温度\n故障指示灯", "EPB故障指示灯",
            "发动机系统\n故障指示灯", "SRS故障指示灯", "发动机排放\n故障指示灯", "EPS故障指示灯", "CVT故障指示灯","自动变速箱高油温\n故障指示灯",
            "自动变速箱\n故障指示灯"};

    @Override
    protected int getContentView() {
        return R.layout.activity_car_testing;
    }

    @Override
    public void init() {
        setTitleText("车况检测");
        getData();
        loadData();
    }
    public void loadData() {
        dataList = new ArrayList<>();
        if (waringLampInfo != null) {
            int[] light = {waringLampInfo.ABS, waringLampInfo.TPMS, waringLampInfo.ESP, waringLampInfo.WATERTMP,
                    waringLampInfo.EPB, waringLampInfo.SVS, waringLampInfo.SRS, waringLampInfo.EOBD,
                    waringLampInfo.EPS,waringLampInfo.CVT,waringLampInfo.AGTEMP,waringLampInfo.AG};
            for (int i = 0; i < light.length; i++) {
                addData(light[i], i);
            }
            View view = LayoutInflater.from(CarTestingActivity.this).inflate(R.layout.layout_testing_header,null,false);
            TextView mTxtDate = view.findViewById(R.id.txtTestingHeader);
            mTxtDate.setText(MyTimeUtils.getDateSecond());
            WaringLampAdapter adapter = new WaringLampAdapter(dataList);
            adapter.setHeaderView(view);
            rvCarTesting.setHasFixedSize(true);
            rvCarTesting.setLayoutManager(new GridLayoutManager(CarTestingActivity.this,2));
            rvCarTesting.setAdapter(adapter);

        }
    }
    private void getData(){
        waringLampInfo = new WaringLampInfo();
        waringLampInfo.ABS =1;
        waringLampInfo.TPMS = 0;
        waringLampInfo.WATERTMP = 0;
        waringLampInfo.CVT = 1;
        waringLampInfo.EOBD = 1;
        waringLampInfo.EPB = 0;
        waringLampInfo.EPS = 0;
        waringLampInfo.ESP = 1;
        waringLampInfo.SRS = 0;
        waringLampInfo.SVS = 0;
        waringLampInfo.AGTEMP = 0;
        waringLampInfo.AG = 1;
    }

    private List<WaringItemInfo> addData(int light, int i) {
        WaringItemInfo info = new WaringItemInfo();
        switch (light) {
            case WaringLampInfo.LIGHT:
                info.img = iconLight[i];
                info.txt = txt[i];
                info.color = getResources().getColor(R.color.orange);
                break;
            case WaringLampInfo.NORMAL:
                info.img = icon[i];
                info.txt = txt[i];
                info.color = getResources().getColor(R.color.textColorGray2);
                break;
            default:
                break;
        }
        dataList.add(info);
        return dataList;
    }
}
