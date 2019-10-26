package cn.net.sunnysoft.scal;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends WearableActivity {
    private final static String TAG = "SCal";

    private CalController mCalController;
    private LoopRecyclerView mRecyclerView;
    private LoopAdapter mAdapter;
    private ArrayList<CalBean> mDatas = null;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        Log.d(TAG, "onCreate");

        mCalController = CalController.getInstance();

        mRecyclerView = (LoopRecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        LoopPagerSnapHelper snapHelper = new LoopPagerSnapHelper();
        snapHelper.setOnPageListener(new LoopPagerSnapHelper.OnPageListener() {
                @Override
                public void onPageSelector(int position) {
                    Log.d(TAG, "onPageSelector: " + position);

                    if (mDatas != null) {
                        int year = mDatas.get(position).getYear();
                        int month = mDatas.get(position).getMonth();
                        mCalController.setYear(year);
                        mCalController.setMonth(month);
                    }
                }
            });
        snapHelper.attachToRecyclerView(mRecyclerView);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "onSingleTapConfirmed(" + e.getX() + "," + e.getY() + ")");

                if (mOnItemClickListener != null) {
                    View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = mRecyclerView.getChildLayoutPosition(childView);
                        mOnItemClickListener.onItemClick(position, childView, e.getX(), e.getY());
                        return true;
                    }
                }

                return super.onSingleTapConfirmed(e);
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (mGestureDetector.onTouchEvent(e)) {
                    return true;
                }
                return false;
            }
        });

        mAdapter = new LoopAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        mCalController.update();

        initDatas();

        mAdapter.setDatas(mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas() {
        Log.d(TAG, "initDatas");

        mDatas = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        /* this month */
        CalBean calBean = new CalBean();
        calBean.setYear(year);
        calBean.setMonth(month);
        mDatas.add(calBean);

        /* last several years */
        for (int i = 0; i < CalConst.MONTHS_SPAN; i++) {
            c.add(Calendar.MONTH, -1);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            
            calBean = new CalBean();
            calBean.setYear(year);
            calBean.setMonth(month);
            mDatas.add(0, calBean);
        }

        /* next several years */
        c = Calendar.getInstance();
        for (int i = 0; i < CalConst.MONTHS_SPAN; i++) {
            c.add(Calendar.MONTH, 1);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            
            calBean = new CalBean();
            calBean.setYear(year);
            calBean.setMonth(month);
            mDatas.add(calBean);
        }

        // for (int i = 0; i < mDatas.size(); i++) {
        //     Log.d(TAG, "data(" + i + ") = " + mDatas.get(i).toString());
        // }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View childView, float x, float y);

    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position, View childView, float x, float y) {
            Log.d(TAG, "onItemClick(" + x + "," + y + ")" + ", position = " + position);

            CalView view = (CalView) childView;
            view.onClick(x, y, mRecyclerView);
        }
    };
}
