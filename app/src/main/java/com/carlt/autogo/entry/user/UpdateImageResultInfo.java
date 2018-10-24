package com.carlt.autogo.entry.user;

import java.util.List;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/25 17:58
 */
public class UpdateImageResultInfo {

    /**
     * data : []
     * message : {"filePath":"face/2018/09/25/20180925172152544148.jpg","id":6361,"imageUrl":"http://ossimage.linewin.cc/face/2018/09/25/20180925172152544148.jpg"}
     * status : 1
     */

    public MessageBean message;
    public int     status;
    public List<?> data;
    public BaseError err;

    @Override
    public String toString() {
        return "UpdateImageResultInfo{" +
                "message=" + message +
                ", status=" + status +
                ", data=" + data +
                '}';
    }

    public static class MessageBean {
        /**
         * filePath : face/2018/09/25/20180925172152544148.jpg
         * id : 6361
         * imageUrl : http://ossimage.linewin.cc/face/2018/09/25/20180925172152544148.jpg
         */

        public String filePath;
        public int    id;
        public String imageUrl;

        @Override
        public String toString() {
            return "MessageBean{" +
                    "filePath='" + filePath + '\'' +
                    ", id=" + id +
                    ", imageUrl='" + imageUrl + '\'' +
                    '}';
        }
    }
}
