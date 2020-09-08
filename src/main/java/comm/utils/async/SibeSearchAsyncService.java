package comm.utils.async;

/**
 * The type Search util.
 */


import comm.ota.site.SibeSearchRequest;

/**
 * The interface Search async service.
 */
public interface SibeSearchAsyncService {


    /**
     * B2C GDS数据查询
     *
     * @param sibeSearchRequest the sibe search request
     */
    public void requestGdsAsyncB2C(SibeSearchRequest sibeSearchRequest);

    /**
     * 验价，组装Search请求参数,并进行异步请求GDS.
     *
     * @param sibeVerifyRequest the ota verify request
     * @throws TechnicalException the technical exception
     */
//    public void requestGdsAsyncB2CsAsync(SibeVerifyRequest sibeVerifyRequest);


    /**
     * 生单，组装Search请求参数,并进行异步请求GDS
     *
     * @param sibeOrderRequest the ota order request
     * @throws TechnicalException the technical exception
     */
//    public void requestGdsAsync(SibeOrderRequest sibeOrderRequest) ;


    /**
     * 如果存在Key且缓存刷新时间已经过期，则返回1 和缓存结果
     *
     * @param sibeSearchRequest the ota search request
     */
    public void requestGdsAsync(SibeSearchRequest sibeSearchRequest, int methodType) ;


    public void asynRequestGDSBySiteCacheExpired(SibeSearchRequest sibeSearchRequest);

}
