package com.carlt.autogo.entry.user;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseError implements Parcelable {


    /**
     * err : {"code":1006,"msg":"用户登录Token不能为空"}
     */

    public int code;
    public String msg;


    public static final Creator<BaseError> CREATOR = new Creator<BaseError>() {
        @Override
        public BaseError createFromParcel(Parcel in) {
            return new BaseError(in);
        }

        @Override
        public BaseError[] newArray(int size) {
            return new BaseError[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.msg);
    }

    public BaseError() {
    }

    protected BaseError(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
    }

}
