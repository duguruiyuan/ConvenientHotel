package hotel.convenient.com.domain;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Gyb on 2016/4/8 15:58
 */
public class ChooseDealerInfo  implements Serializable{
    private Calendar startCalendar;
    private Calendar endCalendar;

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

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }

    public void setStartCalendar(Calendar startCalendar) {
        this.startCalendar = startCalendar;
    }

    public void setEndCalendar(Calendar endCalendar) {
        this.endCalendar = endCalendar;
    }
}
