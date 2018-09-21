package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.carlt.autogo.R;

import butterknife.BindView;
import butterknife.OnClick;

public class FreezeCommitDialog extends BaseDialog {
    @BindView(R.id.tv_freeze_cancle)
    TextView tvFreezeCancle;
    @BindView(R.id.tv_freeze_commit)
    TextView tvFreezeCommit;

    public  FreezeCommitDialogClickLisniter lisniter;

    public FreezeCommitDialog(@NonNull Context context, int style) {
        super(context, style);
    }

    @Override
    void setWindowParams() {

    }

    @Override
    int setRes() {
        return R.layout.dialog_freeze_commit;
    }

    @Override
    void init() {

    }

    @OnClick({R.id.tv_freeze_cancle, R.id.tv_freeze_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_freeze_cancle:
                dismiss();
                break;
            case R.id.tv_freeze_commit:

                if(lisniter != null){
                    lisniter.commit();
                }
                dismiss();
                break;
        }
    }

    public void setLisniter(FreezeCommitDialogClickLisniter lisniter) {
        this.lisniter = lisniter;
    }

    public  interface  FreezeCommitDialogClickLisniter{
        void commit();
    }
}
