package com.sibecommon.ota.site;

public class SibeCacheResponse<T extends SibeBaseResponse> {


    /**
     * Instantiates a new Sibe search base.
     *
     * @param cacheExist     the cache exist
     * @param searchResponse the search response
     */
    public SibeCacheResponse(String cacheExist, T searchResponse) {
        this.cacheExist = cacheExist;
        this.searchResponse = searchResponse;
    }

    /**
     * 判断缓存是否存在，如果存在则返回缓存值
     * 如果存在Key且缓存刷新时间没有过期，则返回0 和缓存结果
     * 如果存在Key且缓存刷新时间已经过期，则返回1 和缓存结果
     * 如果不存在Key，则返回2 和缓存结果
     * 如果存在Key,Value=wait，则返回3 和缓存结果
     */
    public  String  cacheExist;

    /**
     * The Search response.
     */
    public  T searchResponse;

    /**
     * Gets cache exist.
     *
     * @return the cache exist
     */
    public String getCacheExist() {
        return cacheExist;
    }

    /**
     * Sets cache exist.
     *
     * @param cacheExist the cache exist
     */
    public void setCacheExist(String cacheExist) {
        this.cacheExist = cacheExist;
    }

    /**
     * Gets search response.
     *
     * @return the search response
     */
    public T getSearchResponse() {
        return searchResponse;
    }

    /**
     * Sets search response.
     *
     * @param searchResponse the search response
     */
    public void setSearchResponse(T searchResponse) {
        this.searchResponse = searchResponse;
    }
}

