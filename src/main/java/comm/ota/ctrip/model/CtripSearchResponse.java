package comm.ota.ctrip.model;


import comm.ota.site.SibeBaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdehua on 18/3/11.
 */
public class CtripSearchResponse extends SibeBaseResponse {

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
//
//    //提示信息，长度小于 64。
//    private String msg ;

    //报价信息
    private List<CtripRouting> routings;


    /**
     * Gets routings.
     *
     * @return the routings
     */
    public List<CtripRouting> getRoutings() {
        if (routings == null) {
            routings = new ArrayList<>();
        }
        return this.routings;
    }

    /**
     * Sets routings.
     *
     * @param routings the routings
     */
    public void setRoutings(List<CtripRouting> routings) {
        this.routings = routings;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}
