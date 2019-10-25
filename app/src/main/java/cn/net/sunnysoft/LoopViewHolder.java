package cn.net.sunnysoft;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class LoopViewHolder extends RecyclerView.ViewHolder {
    CalView calview;

    public LoopViewHolder(View view) {
        super(view);
        calview = (CalView) view.findViewById(R.id.calview);
    }
}
