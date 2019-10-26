package cn.net.sunnysoft.scal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CalView extends View {
    private final static String TAG = "CalView";

    private Context mContext;
    private CalMonth mCalMonth;
    private CalSpec mCalSpec;
    private int mFontSize;

    public CalView(Context context) {
        super(context);

        init(context);
    }

    public CalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    public CalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mCalMonth = new CalMonth();
        mCalSpec = new CalSpec();
        mFontSize = CalConst.FONT_SIZE_DEFAULT;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

        int d = Math.min(measuredWidth, measuredHeight);
        Log.d(TAG, "onMeasure, d = " + d);

        doCalculation(mCalSpec, d, d);
        // Log.d(TAG, "(x1,y1) = " + "(" + mCalSpec.get_x1() + "," + mCalSpec.get_y1() + ")");
        // Log.d(TAG, "w = " + mCalSpec.get_w());

        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = CalConst.DEFAULT_MEASURESPEC_SIZE;
        } else {
            result = specSize;
        }

        return result;
    }

    private void doCalculation(CalSpec spec, int X, int Y) {
        int R = Math.min(X, Y) / 2;

        int x0 = (X - R * 2) / 2;
        int y0 = (Y - R * 2) / 2;

        int x1, y1, x2, y2;
        int root = (int)Math.sqrt(R * R / 2);
        int W = Math.round(root * 2);
        int H = W;

        final int L_count = CalConst.DAYS_OF_WEEK;
        final int R_count = CalConst.COLS_OF_MONTH;

        int w = W / L_count;
        int h = H / R_count;

        W = w * L_count;
        H = h * R_count;

        x1 = (X - W) / 2 + x0;
        y1 = (Y - H) / 2 + y0;
        x2 = x1 + W;
        y2 = y1 + H;

        if (spec != null) {
            spec.set_X(X);
            spec.set_Y(Y);
            spec.set_L_count(L_count);
            spec.set_R_count(R_count);
            spec.set_W(W);
            spec.set_H(H);
            spec.set_w(w);
            spec.set_h(h);
            spec.set_x1(x1);
            spec.set_y1(y1);
            spec.set_x2(x2);
            spec.set_y2(y2);
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");

        Paint paint = new Paint();

        int fontSize = CalConst.FONT_SIZE_DEFAULT;
        String boundtext = "00";
        Rect bounds = new Rect();
        for (int i = CalConst.FONT_SIZE_START; i < CalConst.FONT_SIZE_END; i++) {
            paint.setTextSize(i);

            Paint.FontMetrics fm = paint.getFontMetrics();
            int height = (int)(fm.bottom - fm.top);

            paint.getTextBounds(boundtext, 0, boundtext.length(), bounds);
            int width = bounds.right - bounds.left;

            // Log.d(TAG, "i = " + i + ", w = " + width + ", h = " + height);

            if (height + CalConst.FONT_SIZE_GAP >= mCalSpec.get_h()) {
                fontSize = i;
                Log.d(TAG, "fontSize = " + fontSize);
                break;
            }
        }
        mFontSize = fontSize;

        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(fontSize);
        paint.setStrokeWidth(CalConst.TEXT_STROKE_WIDTH);

        Paint.FontMetrics fontMetris = paint.getFontMetrics();
        float distance = (fontMetris.bottom - fontMetris.top) / 2 - fontMetris.bottom;

        /* days */
        for (int i = 1; i < mCalSpec.get_R_count(); i++) {
            for (int j = 0; j < mCalSpec.get_L_count(); j++) {
                int w = mCalSpec.get_w();
                int h = mCalSpec.get_h();
                int x = mCalSpec.get_x1() + j * w;
                int y = mCalSpec.get_y1() + i * h;

                DayInfo[][] dayinfos = mCalMonth.getDayInfos();
                DayInfo dayInfo = dayinfos[i][j];
                String text = String.valueOf(dayInfo.getDay());

                int monthinfo = dayInfo.getMonthInfo();
                if (DayInfo.isCurrentMonth(monthinfo)) {
                    paint.setColor(Color.WHITE);
                } else {
                    paint.setColor(Color.GRAY);
                }

                float baseline = y + h / 2 + distance;

                paint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, x + w / 2, baseline, paint);
            }
        }
        paint.setColor(Color.WHITE);

        /* week text */
        for (int i = 0; i < mCalSpec.get_R_count(); i++) {
            int w = mCalSpec.get_w();
            int h = mCalSpec.get_h();
            int x = mCalSpec.get_x1() + i * w;
            int y = mCalSpec.get_y1();

            String[] text = mCalMonth.getWeekText(getContext());

            float baseline = y + h / 2 + distance;

            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(text[i], x + w / 2, baseline, paint);
        }

        /* month year text */
        {
            int x = mCalSpec.get_X() / 2;
            int y = (int)(mCalSpec.get_y1() - (fontMetris.bottom - fontMetris.top) / 2 + distance);
            String text = mCalMonth.getMonthYearText(getContext());
            canvas.drawText(text, x, y, paint);
        }

        /* back today text */
        if (!mCalMonth.isThisMonth()) {
            int x = mCalSpec.get_X() / 2;
            int y = (int)(mCalSpec.get_y2() + (fontMetris.bottom - fontMetris.top) / 2 + distance);
            String text = getContext().getResources().getString(R.string.back_today);
            canvas.drawText(text, x, y, paint);
        }

        /* today */
        int todayPos = mCalMonth.getTodayPos();
        if (todayPos != -1) {
            int i = todayPos / CalConst.DAYS_OF_WEEK;
            int j = todayPos % CalConst.DAYS_OF_WEEK;
            int w = mCalSpec.get_w();
            int h = mCalSpec.get_h();
            int x = mCalSpec.get_x1() + j * w;
            int y = mCalSpec.get_y1() + i * h;

            paint.setStrokeWidth(CalConst.FRAME_STROKE_WIDTH);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x+CalConst.FRAME_GAP, y+CalConst.FRAME_GAP,
                    x+w-CalConst.FRAME_GAP, y+w-CalConst.FRAME_GAP, paint);
        }

        super.onDraw(canvas);
    }

    public void setYear(int year) {
        Log.d(TAG, "setYear to " + year);

        mCalMonth.setYear(year);
    }

    public void setMonth(int month) {
        Log.d(TAG, "setMonth to " + month);

        mCalMonth.setMonth(month);
    }

    public void onClick(float x, float y, LoopRecyclerView recyclerView) {
        Log.d(TAG, "onClick(" + x + "," + y + ")");

        if (isPointInBackTodayBox(x, y) &&
            !mCalMonth.isThisMonth()) {
            Log.d(TAG, "call back this month");

            recyclerView.gotoStartPosition();
        }
    }

    private boolean isPointInBackTodayBox(float x, float y) {
        String boundtext = getContext().getResources().getString(R.string.back_today);
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(mFontSize);
        paint.getTextBounds(boundtext, 0, boundtext.length(), bounds);
        int width = bounds.right - bounds.left;
        int GAP = 10;

        int x1 = mCalSpec.get_X() / 2 - width / 2 - GAP;
        int x2 = mCalSpec.get_X() / 2 + width / 2 + GAP;
        int y1 = mCalSpec.get_y2();
        int y2 = mCalSpec.get_y2() + mCalSpec.get_h() + GAP;

        Log.d(TAG, "back today[(" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ")]");

        if (x < x1) return false;
        if (x > x2) return false;
        if (y < y1) return false;
        if (y > y2) return false;

        return true;
    }
}
