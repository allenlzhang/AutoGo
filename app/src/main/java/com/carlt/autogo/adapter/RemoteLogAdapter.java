package com.carlt.autogo.adapter;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.entry.remote.RemoteLogInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Marlon on 2019/1/17.
 */
public class RemoteLogAdapter extends BaseQuickAdapter<RemoteLogInfo.DataBean.ListBean,BaseViewHolder> {
    int lastPosition;
    public RemoteLogAdapter(List<RemoteLogInfo.DataBean.ListBean> data) {
        super(R.layout.item_remote_log, data);
        if (data!=null&&data.size()>0){
            lastPosition = data.size()-1;
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteLogInfo.DataBean.ListBean item) {
        helper.setText(R.id.txtRemoteLogType,adapterTypeText(item.getLogtype()));
        helper.setText(R.id.txtLogDeviceName,item.getLog_device_name());
        helper.setImageResource(R.id.ivLogResult,showImage(item.getLog_result()));
        helper.setText(R.id.txtLogTime, item.getLogtime());
        LogUtils.e("remoteLogAdapter---"+helper.getAdapterPosition());
        if (helper.getAdapterPosition() == lastPosition){
            helper.setVisible(R.id.remoteLogLine,false);
        }else {
            helper.setVisible(R.id.remoteLogLine,true);
        }
    }
    private int showImage(String logResult){
        return TextUtils.equals(logResult,"1")?R.mipmap.icon_log_result:R.mipmap.icon_log_error;
    }
    private final static String TYPE_FLASHING               = "11";//闪灯鸣笛
    private final static String TYPE_UNLOCK                 = "21";//解锁
    private final static String TYPE_LOCK                   = "22";//落锁
    private final static String TYPE_START                  = "31";//启动
    private final static String TYPE_STOP                   = "41";//熄火
    private final static String TYPE_AIROPEN                = "51";//远程开启空调
    private final static String TYPE_AIRCLOSE               = "52";//远程关闭空调
    private final static String TYPE_AIRDEFROST             = "53";//空调/一键除霜
    private final static String TYPE_AIRCOLD                = "54";//空调/最大制冷
    private final static String TYPE_AIRHOT                 = "55";//空调/最大制热
    private final static String TYPE_AIRANION               = "56";//空调/负离子
    private final static String TYPE_AIRCLEAN               = "57";//空调/座舱清洁
    private final static String TYPE_AIRTEMP                = "58";//空调/温度调节
    private final static String TYPE_WINOPEN                = "61";//远程开窗
    private final static String TYPE_WINCLOSE               = "62";//远程关窗
    private final static String TYPE_SKYLIGHT_OPEN          = "63";//远程开启天窗
    private final static String TYPE_SKYLIGHT_CLOSE         = "64";//远程关闭天窗
    private final static String TYPE_SKYLIGHT_UP            = "65";//远程开侨天窗
    private final static String TYPE_TRUNKOPEN              = "71";//远程开启后备箱
    private final static String TYPE_TRUNKCLOSE             = "72";//远程开启后备箱
    private final static String TYPE_SEATOPEN               = "81";//远程开启座椅加热
    private final static String TYPE_SEATCLOSE              = "82";//远程关闭座椅加热
    private final static String TYPE_PURIFYOPEN             = "91";//远程开启空气净化
    private final static String TYPE_PURIFYCLOSE            = "92";//远程关闭空气净化
    private final static String TYPE_START_INPOWER          = "93";// 远程立即充电
    private final static String TYPE_TIMEING_INPOWER        = "94";// 远程定时冲电
    private final static String TYPE_STOP_INPOWER           = "95"; //远程停止充电
    private final static String TYPE_CANCEL_TIMEING_INPOWER = "96"; //取消定时充电

    private String adapterTypeText(String type) {
        String typeString = "--";
        switch (type) {
            case TYPE_FLASHING:
                typeString = "一键寻车";
                break;
            case TYPE_UNLOCK:
                typeString = "远程解锁";
                break;
            case TYPE_LOCK:
                typeString = "远程落锁";
                break;
            case TYPE_START:
                typeString = "远程启动";
                break;
            case TYPE_STOP:
                typeString = "远程熄火";
                break;
            case TYPE_AIROPEN:
                typeString = "远程开启空调";
                break;
            case TYPE_AIRCLOSE:
                typeString = "远程关闭空调";
                break;
            case TYPE_AIRDEFROST:
                typeString = "远程开启空调/一键除霜";
                break;
            case TYPE_AIRCOLD:
                typeString = "远程开启空调/最大制冷";
                break;
            case TYPE_AIRHOT:
                typeString = "远程开启空调/最大制热";
                break;
            case TYPE_AIRANION:
                typeString = "远程开启空调/负离子";
                break;
            case TYPE_AIRCLEAN:
                typeString = "远程开启空调/座舱清洁";
                break;
            case TYPE_AIRTEMP:
                typeString = "远程温度调节";
                break;
            case TYPE_WINOPEN:
                typeString = "远程开窗";
                break;
            case TYPE_WINCLOSE:
                typeString = "远程关窗";
                break;
            case TYPE_SKYLIGHT_OPEN:
                typeString = "远程开启天窗";
                break;
            case TYPE_SKYLIGHT_CLOSE:
                typeString = "远程关闭天窗";
                break;
            case TYPE_SKYLIGHT_UP:
                typeString = "远程开翘天窗";
                break;
            case TYPE_TRUNKOPEN:
                typeString = "远程开启后备箱";
                break;
            case TYPE_TRUNKCLOSE:
                typeString = "远程关闭后备箱";
                break;
            case TYPE_SEATOPEN:
                typeString = "远程开启座椅加热";
                break;
            case TYPE_SEATCLOSE:
                typeString = "远程关闭座椅加热";
                break;
            case TYPE_PURIFYOPEN:
                typeString = "远程开启空气净化";
                break;
            case TYPE_PURIFYCLOSE:
                typeString = "远程关闭空气净化";
                break;
            case TYPE_START_INPOWER:
                typeString = "远程立即充电";
                break;
            case TYPE_TIMEING_INPOWER:
                typeString = "远程定时冲电";
                break;
            case TYPE_STOP_INPOWER:
                typeString = "远程停止充电";
                break;
            case TYPE_CANCEL_TIMEING_INPOWER:
                typeString = "取消定时充电";
                break;
            default:
                typeString = "--";
                break;
        }
        return typeString;
    }
}
