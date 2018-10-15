package com.carlt.autogo.utils.gildutils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Bert on 2016/8/18.
 */
public class LoadLocalImageUtil {
    private LoadLocalImageUtil() {
    }

    private static LoadLocalImageUtil instance = null;

    public static synchronized LoadLocalImageUtil getInstance() {
        if (instance == null) {
            instance = new LoadLocalImageUtil();
        }
        return instance;
    }

    /**
     * 从内存卡中异步加载本地图片无缓存
     * @param uri
     * @param imageView
     */
    public void displayFromSDCard(Activity contxt, Object uri, ImageView imageView) {
        Glide.with(contxt)
                .load(uri)
                .into(imageView);

    }

    /**
     * 从内存卡中异步加载本地图片
     * @param uri
     * @param imageView
     */
    public void displayFromSDCardCache(Activity contxt, String uri, ImageView imageView) {
        Glide.with(contxt)
                .load(uri)
                .into(imageView);

    }


    /**
     * 从drawable中异步加载本地图片
     * @param imageId
     * @param imageView
     */
    public void displayFromDrawable(Activity contxt, int imageId, ImageView imageView) {
        Glide.with(contxt)
                .load(imageId)
                .into(imageView);
    }


    /**
     * 加载网络图片从网络
     * @param uri
     * @param imageView
     * @param defaulePic
     */
    public void displayFromWeb(Activity contxt, String uri, ImageView imageView, int defaulePic) {
        Glide.with(contxt)
                .load(uri)
                .into(imageView);
    }


    /**
     * 从网络显示圆头像
     * @param uri
     * @param imageView
     * @param defaulePic
     */
    public void displayCircleFromWeb(Activity contxt, String uri, ImageView imageView, int defaulePic) {
        Glide.with(contxt)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .transform(new GlideCircleTransform(contxt))
                .error(defaulePic)
                .into(imageView);
    }

    /**
     * 显示圆角矩形从网络
     * @param uri
     * @param imageView
     * @param defaulePic
     * @param radius
     */
    public void displayRoundFromWeb(Activity contxt, String uri, ImageView imageView, int defaulePic, int radius) {
        Glide.with(contxt)
                .load(uri)
                .transform(new GlideRoundTransform(contxt, radius))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .placeholder(defaulePic)
                .error(defaulePic)
                .into(imageView);
    }
}
