package com.carlt.autogo.view.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.TipsAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 输入提示功能实现
 */
public class InputtipsActivity extends Activity implements TextWatcher, InputtipsListener ,View.OnClickListener{

	private String city = "北京";
	private AutoCompleteTextView mKeywordText;
	private RecyclerView minputlist;
	private ImageView mIvReturn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputtip);
		String city = getIntent().getStringExtra("city");
		if (!TextUtils.isEmpty(city)){
			this.city = city;
		}
		mIvReturn = findViewById(R.id.ivInputBack);
		minputlist = (RecyclerView) findViewById(R.id.inputlist);
		minputlist.setLayoutManager(new LinearLayoutManager(this));
		minputlist.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
		mKeywordText = (AutoCompleteTextView)findViewById(R.id.input_edittext);
        mKeywordText.addTextChangedListener(this);
        mIvReturn.setOnClickListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(InputtipsActivity.this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
        
		
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/**
	 * 输入提示结果的回调
	 * @param tipList
	 * @param rCode
     */
	@Override
	public void onGetInputtips(final List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if(tipList != null) {
				TipsAdapter aAdapter = new TipsAdapter(tipList);
				minputlist.setAdapter(aAdapter);
				aAdapter.notifyDataSetChanged();
				minputlist.addOnItemTouchListener(new OnItemClickListener() {
					@Override
					public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
						Tip tip = (Tip) adapter.getItem(position);
						if (tip != null&&tip.getPoint()!=null){
							String LatLonPoint = tip.getPoint().getLatitude()+","+tip.getPoint().getLongitude();
							Intent intent = new Intent();
							intent.putExtra("LatLonPoint",LatLonPoint);
							InputtipsActivity.this.setResult(RESULT_OK,intent);
							InputtipsActivity.this.finish();
						}else {
							ToastUtils.showShort("选择地址有误");
						}
					}
				});
			}

        } else {
			ToastUtils.showShort( rCode);
		}
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ivInputBack:
				finish();
				break;
		}
	}
}
