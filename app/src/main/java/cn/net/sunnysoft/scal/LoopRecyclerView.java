package cn.net.sunnysoft.scal;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LoopRecyclerView extends RecyclerView {
    private final static String TAG = "LoopRecyclerView";

    private int mStartPosition;

    public LoopRecyclerView(Context context) {
        super(context);

        init(context);
    }

    public LoopRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public LoopRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }
    
    private void init(Context context) {
        mStartPosition = 0;
    }

    @Override
    public LoopAdapter getAdapter() {
        return (LoopAdapter) super.getAdapter();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        int position = getAdapter().getItemRawCount() / 2;
        Log.d(TAG, "setAdapter, start position = " + position);

        mStartPosition = position;

        scrollToPosition(position);
    }

    public static abstract class LoopAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
        ArrayList<CalBean> datas = new ArrayList<>();

        public void setDatas(ArrayList<CalBean> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        public ArrayList<CalBean> getDatas() {
            return datas;
        }

        public int getItemRawCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        final public int getItemViewType(int position) {
            return getLoopItemViewType(position % getItemRawCount());
        }

        protected int getLoopItemViewType(int position) {
            return 0;
        }

        @Override
        final public void onBindViewHolder(T holder, int position) {
            onBindLoopViewHolder(holder, position);
        }

        public abstract void onBindLoopViewHolder(T holder, int position);

        @Override
        final public int getItemCount() {
            int rawCount = getItemRawCount();

            return rawCount;
        }
    }

    public void gotoStartPosition() {
        Log.d(TAG, "gotoStartPosition");

        scrollToPosition(mStartPosition);
    }
}
