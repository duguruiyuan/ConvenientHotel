package hotel.convenient.com.domain;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by cwy on 2016/4/8 15:58
 */
public class ChooseDealerInfo  implements Serializable{
    private GregorianCalendar startCalendar;
    private GregorianCalendar endCalendar;

    private Publish publish;

    public ChooseDealerInfo(Publish publish) {
        this.publish = publish;
    }

    public void setPublish(Publish publish) {
        this.publish = publish;
    }

    public Publish getPublish() {
    
        return publish;
    }

    public GregorianCalendar getStartCalendar() {
        return startCalendar;
    }

    public GregorianCalendar getEndCalendar() {
        return endCalendar;
    }

    public void setStartCalendar(GregorianCalendar startCalendar) {
        this.startCalendar = startCalendar;
    }

    public void setEndCalendar(GregorianCalendar endCalendar) {
        this.endCalendar = endCalendar;
    }
}
