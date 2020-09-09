package comm.ota.gds;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;

/**
 * Created by yangdehua on 10/26/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GDSSearchResponseDTO {

    private int status;
    private String msg;
    private String uid;
    private ArrayList<Routing> routings;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Routing> getRoutings() {
        if (routings == null) {
            routings = new ArrayList<Routing>();
        }
        return this.routings;
    }

    public void setRoutings(ArrayList<Routing> routings) {
        this.routings = routings;
    }
}


