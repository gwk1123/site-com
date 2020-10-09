package com.sibecommon.ota.site;


import com.sibecommon.repository.entity.GdsPcc;

/**
 * The type Sibe route.
 */
public class SibeRoute  {

    //查询PCC信息
    private GdsPcc searchPcc;

    //验价同查询，不需要
    private GdsPcc verifyPcc;

    //生单PCC信息
    private GdsPcc orderPcc;


    /**
     * Gets searc pcc.
     *
     * @return the searc pcc
     */
    public GdsPcc getSearchPcc() {
        if(searchPcc==null){
            searchPcc=new GdsPcc();
        }
        return searchPcc;
    }

    /**
     * Sets searc pcc.
     *
     * @param searchPcc the searc pcc
     */
    public void setSearchPcc(GdsPcc searchPcc) {
        this.searchPcc = searchPcc;
    }

    /**
     * Gets searc pcc.
     *
     * @return the searc pcc
     */
    public GdsPcc getVerifyPcc() {
        if(verifyPcc==null){
            verifyPcc=new GdsPcc();
        }
        return verifyPcc;
    }

    /**
     * Sets searc pcc.
     *
     * @param verifyPcc the searc pcc
     */
    public void setVerifyPcc(GdsPcc verifyPcc) {
        this.verifyPcc = verifyPcc;
    }

    /**
     * Gets order pcc.
     *
     * @return the order pcc
     */
    public GdsPcc getOrderPcc() {

        if(orderPcc==null){
            orderPcc=new GdsPcc();
        }
        return orderPcc;
    }

    /**
     * Sets order pcc.
     *
     * @param orderPcc the order pcc
     */
    public void setOrderPcc(GdsPcc orderPcc) {
        this.orderPcc = orderPcc;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        SibeRoute sibeRoute = (SibeRoute) o;

        if (searchPcc != null ? !searchPcc.equals(sibeRoute.searchPcc) : sibeRoute.searchPcc != null) {
            return false;
        }
        return orderPcc != null ? orderPcc.equals(sibeRoute.orderPcc) : sibeRoute.orderPcc == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (searchPcc != null ? searchPcc.hashCode() : 0);
        result = 31 * result + (orderPcc != null ? orderPcc.hashCode() : 0);
        return result;
    }
}
