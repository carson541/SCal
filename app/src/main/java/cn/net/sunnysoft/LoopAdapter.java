package cn.net.sunnysoft;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoopAdapter extends LoopRecyclerView.LoopAdapter<LoopViewHolder> {
    private final static String TAG = "LoopAdapter";

    @Override
    public LoopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item, parent, false);
        return new LoopViewHolder(inflate);
    }

    @Override
    public void onBindLoopViewHolder(LoopViewHolder holder, int position) {
        CalView calview = (CalView) holder.calview;

        Log.d(TAG, "onBindLoopViewHolder, position = " + position);

        int year = getDatas().get(position).getYear();
        int month = getDatas().get(position).getMonth();

        Log.d(TAG, "onBindLoopViewHolder, year = " + year + ", month = " + month);

        calview.setYear(year);
        calview.setMonth(month);
    }
}
