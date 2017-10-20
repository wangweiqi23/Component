package com.weiqi.moduledog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.DataConstants;
import com.weiqi.modulebase.fragment.BaseFragment;
import com.weiqi.modulebase.model.dog.DogObject;
import com.weiqi.modulebase.moduleinterface.module.CatModuleService;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;
import com.weiqi.modulebase.moduleinterface.provider.IDogProvider;
import com.weiqi.moduledog.R;

/**
 * Created by alexwangweiqi on 17/9/20.
 */
@Route(path = IDogProvider.DOG_FRAGMENT)
public class DogFragment extends BaseFragment {

    private static final String TAG = DogFragment.class.getSimpleName();

    private DogObject mDogObject;

    public static DogFragment newInstance(DogObject object) {
        DogFragment fragment = new DogFragment();
        Bundle args = new Bundle();
        args.putSerializable(DataConstants.EXTRA_OBJECT_DOG, object);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DogFragment");
        initIntentData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textViewDog = new TextView(this.getContext());
        textViewDog.setText(String.format("this is a Dog \nname:%s age:%s", mDogObject.getName(),
                mDogObject.getAge()));

        Button button = new Button(this.getContext());
        button.setText("去猫咪那里");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ICatProvider.CAT_ACTIVITY)
                        .withInt(DataConstants.EXTRA_AGE, 2)
                        .navigation();
            }
        });

        TextView textViewCat = new TextView(this.getContext());
        textViewCat.setTextColor(getResources().getColor(R.color.red));
        textViewCat.setText(String.format("获取猫咪Kitty的年龄:%s", CatModuleService.getCatAge("Kitty")));

        linearLayout.addView(textViewDog);
        linearLayout.addView(button);
        linearLayout.addView(textViewCat);

        return linearLayout;
    }

    @Override
    protected void initIntentData() {
        mDogObject = (DogObject) getArguments().getSerializable(DataConstants.EXTRA_OBJECT_DOG);
    }

    private void test() {
        int ftestId_XXXXXXxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = R
                .layout
                .fragment_dog;
        int fId = R.layout.fragment_dog;
        int baseDrawableId = R.drawable.icon_me_vip;
        String fIdStr = "R.Layout";

//        View testView = inflater.inflate(R.layout.fragment_dog, null);
    }
}