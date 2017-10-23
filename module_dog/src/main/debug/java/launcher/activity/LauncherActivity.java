package launcher.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.weiqi.modulebase.activity.BaseLauncherActivity;
import com.weiqi.modulebase.model.dog.DogObject;
import com.weiqi.moduledog.R;
import com.weiqi.moduledog.fragment.DogFragment;

/**
 * Created by alexwangweiqi on 17/9/5.
 */

public class LauncherActivity extends BaseLauncherActivity {

    private static final String TAG = LauncherActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    public void initView() {
    }

    @Override
    public void onContinue() {
        DogObject dogObject = new DogObject();
        dogObject.setName("corgi");
        dogObject.setAge(1);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DogFragment fragment = DogFragment.newInstance(dogObject);
        fragmentTransaction.add(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }
}
