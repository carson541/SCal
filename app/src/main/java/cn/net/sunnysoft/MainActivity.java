package cn.net.sunnysoft;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

public class MainActivity extends WearableActivity {
    private final static String TAG = "SCal";

    private CalController mCalController;
    private CalView mCalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        Log.d(TAG, "onCreate");

        mCalController = CalController.getInstance();
        mCalView = (CalView)this.findViewById(R.id.calView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        mCalController.update();
        mCalView.update();
    }
}
