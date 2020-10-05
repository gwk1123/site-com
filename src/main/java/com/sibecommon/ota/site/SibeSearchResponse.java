package com.sibecommon.ota.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwk on 18/3/11.
 */
public class SibeSearchResponse extends SibeBaseResponse implements Serializable {

    private static final long serialVersionUID = 1587422156784638757L;

    //报价信息重复的(航司，航班号，出发时间，舱位）
    private List<SibeRouting> routings;

    //Redis缓存有效时间
    private Integer cacheValidTime;

    //Redis缓存刷新时间
    private Integer cacheRefreshTime;


    //响应状态代码
    //todo 进一步完善状态,后续再优化
    // 0. 成功;
    // 1. 请求参数错误;
    // 2. 程序异常;
    // 3. 舱位数不足;
    // 4. 查无价格;
    // 5. 无法获取税费;
    // 6. 退改签异常,含获取不到或者异常;
    // 7. 网络异常
    // 8. 价格变化;
    // 101, 客户重复在多个平台系统使用同样客户信息预订相同的航班舱位信息，导致订位失败
    // 999: 其他失败原因 msg 注明;
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Gets routings.
     *
     * @return the routings
     */
    public List<SibeRouting> getRoutings() {
        if(routings==null){
            routings = new ArrayList<>();
        }
        return this.routings;
    }

    /**
     * Sets routings.
     *
     * @param routings the routings
     */
    public void setRoutings(List<SibeRouting> routings) {
        this.routings = routings;
    }

    /**
     * Gets cache valid time.
     *
     * @return the cache valid time
     */
    public Integer getCacheValidTime() {
        return cacheValidTime;
    }

    /**
     * Sets cache valid time.
     *
     * @param cacheValidTime the cache valid time
     */
    public void setCacheValidTime(Integer cacheValidTime) {
        this.cacheValidTime = cacheValidTime;
    }

    /**
     * Gets cache refresh time.
     *
     * @return the cache refresh time
     */
    public Integer getCacheRefreshTime() {
        return cacheRefreshTime;
    }

    /**
     * Sets cache refresh time.
     *
     * @param cacheRefreshTime the cache refresh time
     */
    public void setCacheRefreshTime(Integer cacheRefreshTime) {
        this.cacheRefreshTime = cacheRefreshTime;
    }
}
