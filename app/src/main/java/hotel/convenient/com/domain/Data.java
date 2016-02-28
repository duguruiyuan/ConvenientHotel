package hotel.convenient.com.domain;

import java.util.List;

/**
 * Created by Gyb on 2016/1/8 15:23
 */
public class Data<T> {
    private int count;
    private int page;
    private int pagecount;
    private List<T> list;

    public void setCount(int count) {
        this.count = count;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public int getPage() {
        return page;
    }

    public int getPagecount() {
        return pagecount;
    }

    public List<T> getList() {
        return list;
    }
}
