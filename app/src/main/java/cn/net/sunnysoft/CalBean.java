package cn.net.sunnysoft;

public class CalBean {
    private int mYear;
    private int mMonth;

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }
    
    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    @Override
    public String toString() {
        return "(" + "year:" + mYear + ",month:" + mMonth + ")";
    }
}
