package com.carlt.autogo.view.fragment;

import com.carlt.autogo.base.BaseMvpFragment;

import java.util.HashMap;

/**
 * Created by tfpc on 2016/11/3.
 */
public class FragmentFactory {
    public static HashMap<Integer, BaseMvpFragment> saveFrament = new HashMap<>();

    public static BaseMvpFragment getFragment(int position) {
        BaseMvpFragment fragment = saveFrament.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new RemoteFragment();
                    break;
                case 2:
                    fragment = new MoreFragment();
                    break;
                default:

            }
            saveFrament.put(position, fragment);
        }
        return fragment;
    }


}
