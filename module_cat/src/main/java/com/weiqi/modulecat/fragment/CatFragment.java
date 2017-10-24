package com.weiqi.modulecat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.weiqi.modulebase.DataConstants;
import com.weiqi.modulebase.fragment.BaseFragment;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;

/**
 * Created by alexwangweiqi on 17/9/20.
 */
@Route(path = ICatProvider.CAT_FRAGMENT)
public class CatFragment extends BaseFragment {

    private static final String TAG = CatFragment.class.getSimpleName();

    private int mAge;

    public static CatFragment newInstance(int age) {
        CatFragment fragment = new CatFragment();
        Bundle args = new Bundle();
        args.putInt(DataConstants.EXTRA_AGE, age);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "CatFragment");
        initIntentData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("This is a Cat \nage:" + mAge);
        return textView;
    }

    @Override
    protected void initIntentData() {
        mAge = getArguments().getInt(DataConstants.EXTRA_AGE);
    }
}