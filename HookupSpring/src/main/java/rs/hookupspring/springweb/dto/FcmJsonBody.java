package rs.hookupspring.springweb.dto;

import java.util.Map;

/**
 * Created by Bandjur on 8/21/2016.
 */
public class FcmJsonBody {

    private String to;
    private Map<String, String> data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}
