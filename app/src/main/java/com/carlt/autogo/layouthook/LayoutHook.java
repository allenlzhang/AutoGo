package com.carlt.autogo.layouthook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class LayoutHook {


    /**  参考 源码setcotentView 方法实现, 使用反射自定义拦截加载
     * @param
     * @param res  资源文件路径
     */
    public static View layoutInflaterHook (Context context , int res , ViewGroup  contentParent){

        MyPhoneLayoutInflater layoutInflater = new MyPhoneLayoutInflater(context);
        View view = layoutInflater.inflate(res, contentParent);
//        Class  cl = appCompatDelegate.getClass();

//        try {
//            Method ensureSubDecor = cl.getDeclaredMethod("ensureSubDecor");
//            ensureSubDecor.setAccessible(true);
//            ensureSubDecor.invoke(appCompatDelegate);
//            Field mSubDecor = cl.getDeclaredField("mSubDecor");
//            mSubDecor.setAccessible(true);
//            ViewGroup viewGroup = (ViewGroup) mSubDecor.get(appCompatDelegate);
//            int a = 16908290;
//            ViewGroup contentParent = viewGroup.findViewById(a);
//            contentParent.removeAllViews();

            //设置父控件背景
 //           contentParent.setBackgroundColor(Color.WHITE);
           // window.getCallback().onContentChanged();

 //       }

//        catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        return  view;
    }
}
