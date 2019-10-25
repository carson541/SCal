package cn.net.sunnysoft;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class LoopPagerSnapHelper extends PagerSnapHelper {
    private final static String TAG = "LoopPagerSnapHelper";

    OnPageListener mOnPageListener;
    int mCurrentPosition = 0;

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public LoopPagerSnapHelper setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
        return this;
    }

    public OnPageListener getOnPageListener() {
        return mOnPageListener;
    }

    public LoopPagerSnapHelper setOnPageListener(OnPageListener onPageListener) {
        mOnPageListener = onPageListener;
        return this;
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) targetView.getLayoutParams();
        int position = params.getViewAdapterPosition();

        if (mOnPageListener != null && mCurrentPosition != position) {
            mOnPageListener.onPageSelector(mCurrentPosition = position);
        }

        return super.calculateDistanceToFinalSnap(layoutManager, targetView);
    }

    public interface OnPageListener {
        void onPageSelector(int position);
    }
}
