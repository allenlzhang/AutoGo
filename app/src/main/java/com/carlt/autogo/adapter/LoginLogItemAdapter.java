package com.carlt.autogo.adapter;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.user.LoginLogInfo;
import com.carlt.autogo.utils.MyTimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Description:
 * Copyright  :
 * Company    : HangAnTong
 * Author     : zhangLei
 * Date       : 2016/12/7 15:18
 */

public class LoginLogItemAdapter extends BaseQuickAdapter<LoginLogInfo.LogsBean, BaseViewHolder> {

    private List<LoginLogInfo.LogsBean> infos;

    public LoginLogItemAdapter(List<LoginLogInfo.LogsBean> data) {
        super(R.layout.item_login_device_management, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, LoginLogInfo.LogsBean info) {
        helper.setText(R.id.tv_management_item_login_device, info.deviceName);
        String date = MyTimeUtils.formatDateSecend(info.time);
        helper.setText(R.id.tv_management_item_login_device_date, date);

    }


}
