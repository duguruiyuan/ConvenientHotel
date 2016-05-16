package hotel.convenient.com.domain;

import java.util.List;

/**
 * Created by cwy on 2016/1/8 15:23
 */
public class Data<T> {
    private int count;
    private int currentPage;
    private int countPage;
    private List<T> list;

    public void setCount(int count) {
        this.count = count;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCountPage() {
        return countPage;
    }

    public List<T> getList() {
        return list;
    }
}
