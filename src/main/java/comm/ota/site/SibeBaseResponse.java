package comm.ota.site;

/**
 * Created by yangdehua on 18/3/11.
 */
public class SibeBaseResponse {

    //响应信息
    private String msg;

    //UUID
    private String uuid;

    //请求GDS平台
    private String gds;

    //调用GDS时使用的对应账号（OfficeID或PCC）
    private String officeId;

    // 缓存生成的时间
    private Long timeLapse;



    /**
     * Gets time lapse.
     *
     * @return the time lapse
     */
    public Long getTimeLapse() {
        return timeLapse;
    }

    /**
     * Sets time lapse.
     *
     * @param timeLapse the time lapse
     */
    public void setTimeLapse(Long timeLapse) {
        this.timeLapse = timeLapse;
    }

    /**
     * Gets gds.
     *
     * @return the gds
     */
    public String getGds() {
        return gds;
    }

    /**
     * Sets gds.
     *
     * @param gds the gds
     */
    public void setGds(String gds) {
        this.gds = gds;
    }

    /**
     * Gets office id.
     *
     * @return the office id
     */
    public String getOfficeId() {
        return officeId;
    }

    /**
     * Sets office id.
     *
     * @param officeId the office id
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }


    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }


    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets uuid.
     *
     * @param uuid the uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


}
