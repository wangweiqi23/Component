package com.weiqi.modulehome.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.DataConstants;
import com.weiqi.modulebase.activity.BaseActivity;
import com.weiqi.modulebase.fragment.BaseFragment;
import com.weiqi.modulebase.manager.AccountManager;
import com.weiqi.modulebase.model.account.Account;
import com.weiqi.modulebase.model.dog.DogObject;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;
import com.weiqi.modulebase.moduleinterface.provider.IDogProvider;
import com.weiqi.modulebase.moduleinterface.provider.IHomeProvider;
import com.weiqi.modulebase.util.DensityUtil;
import com.weiqi.modulebase.widget.Indicator;
import com.weiqi.modulebase.widget.IndicatorViewPager;
import com.weiqi.modulebase.widget.OnTransitionTextListener;
import com.weiqi.modulebase.widget.ToastUtil;
import com.weiqi.modulebase.widget.slidebar.TextWidthColorBar;
import com.weiqi.modulehome.R;

/**
 * Created by alexwangweiqi on 17/9/20.
 */
@Route(path = IHomeProvider.HOME_ACTIVITY)
public class HomeActivity extends BaseActivity implements IndicatorViewPager
        .OnIndicatorPageChangeListener {

    private final String[] TITLES = {
            "猫",
            "狗"
    };

    private final int DEFAULT_SELECTED_TAB = 0;

    private EditText mEditFood;

    private ViewPager mViewPager;
    private IndicatorViewPager mIndicatorViewPager;
    private Indicator mIndicator;

    private BaseFragment mCatFragment;
    private BaseFragment mDogFragment;

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mPreferences = getSharedPreferences("feeding", Context.MODE_PRIVATE);
        initIntentData();
        setContentView(R.layout.activity_home);
    }

    @Override
    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("HomeActivity");

        mEditFood = (EditText) findViewById(R.id.edit_food);
        mEditFood.setText(String.valueOf(mPreferences.getString("food", null)));

        ((Button) findViewById(R.id.btn_feeding)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("food", mEditFood.getText().toString());
                editor.commit();
                ToastUtil.show(String.format("放入了%s个食物", mEditFood.getText().toString()));
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.socail_viewpager);
        mIndicator = (Indicator) findViewById(R.id.sliding_tabs);
        initFragment();
        initViewPager();
    }

    @Override
    protected void finishInitView() {
        Account account = AccountManager.getAccount();
        if (account != null && account.isLogin()) {
            ((TextView) findViewById(R.id.tv_person_name)).setText(String.format("姓名：%s", account
                    .getName()));
        }
    }

    private void initFragment() {
        mCatFragment = (BaseFragment) ARouter.getInstance()
                .build(ICatProvider.CAT_FRAGMENT)
                .withInt(DataConstants.EXTRA_AGE, 2)
                .navigation();

        DogObject dogObject = new DogObject();
        dogObject.setName("corgi");
        dogObject.setAge(1);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataConstants.EXTRA_OBJECT_DOG, dogObject);
        mDogFragment = (BaseFragment) ARouter.getInstance()
                .build(IDogProvider.DOG_FRAGMENT)
                .with(bundle)
                .navigation();
    }

    private void initViewPager() {
        if (mCatFragment == null || mDogFragment == null) {
            return;
        }
        final int selectColor = ContextCompat.getColor(this, R.color.gold);
        int unSelectColor = ContextCompat.getColor(this, R.color.gray);
        mIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor,
                unSelectColor).setSizeId(this, R.dimen.dp_16, R.dimen.dp_16));
        mViewPager.setOffscreenPageLimit(TITLES.length);
        mIndicatorViewPager = new IndicatorViewPager(mIndicator, mViewPager);
        mIndicatorViewPager.setOnIndicatorPageChangeListener(this);
        mIndicatorViewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        mIndicatorViewPager.setCurrentItem(DEFAULT_SELECTED_TAB, false);

        mIndicator.setScrollBar(new TextWidthColorBar(this,
                mIndicatorViewPager.getIndicatorView(), R.drawable.bg_scrollbar,
                DensityUtil.dip2px(4)));
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    public void onIndicatorPageChange(int preItem, int currentItem) {

    }

    private class TabAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public TabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container, int
                currentSelectedIndex) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tv_title);
            textView.setText(TITLES[position]);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            if (currentSelectedIndex == position) {
                textView.setTextColor(getResources().getColor(R.color.gold));
            } else {
                textView.setTextColor(getResources().getColor(R.color.gray));
            }

            ImageView iamgeView = (ImageView) convertView.findViewById(R.id.iv_red_point);

            if (position == 0) {
                iamgeView.setVisibility(View.VISIBLE);
            } else {
                iamgeView.setVisibility(View.GONE);
            }

            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            switch (position) {
                case 0:
                    return mCatFragment;
                case 1:
                    return mDogFragment;
            }
            return mCatFragment;
        }

    }

}
