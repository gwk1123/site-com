package com.sibecommon.ota.site;


/**
 * The type Sibe route.
 */
public class SibeRoute  {

    //查询PCC信息
    private SibeGdsPcc searchPcc;

    //验价同查询，不需要
    private SibeGdsPcc verifyPcc;

    //生单PCC信息
    private SibeGdsPcc orderPcc;


    /**
     * Gets searc pcc.
     *
     * @return the searc pcc
     */
    public SibeGdsPcc getSearchPcc() {
        if(searchPcc==null){
            searchPcc=new SibeGdsPcc();
        }
        return searchPcc;
    }

    /**
     * Sets searc pcc.
     *
     * @param searchPcc the searc pcc
     */
    public void setSearchPcc(SibeGdsPcc searchPcc) {
        this.searchPcc = searchPcc;
    }

    /**
     * Gets searc pcc.
     *
     * @return the searc pcc
     */
    public SibeGdsPcc getVerifyPcc() {
        if(verifyPcc==null){
            verifyPcc=new SibeGdsPcc();
        }
        return verifyPcc;
    }

    /**
     * Sets searc pcc.
     *
     * @param verifyPcc the searc pcc
     */
    public void setVerifyPcc(SibeGdsPcc verifyPcc) {
        this.verifyPcc = verifyPcc;
    }

    /**
     * Gets order pcc.
     *
     * @return the order pcc
     */
    public SibeGdsPcc getOrderPcc() {

        if(orderPcc==null){
            orderPcc=new SibeGdsPcc();
        }
        return orderPcc;
    }

    /**
     * Sets order pcc.
     *
     * @param orderPcc the order pcc
     */
    public void setOrderPcc(SibeGdsPcc orderPcc) {
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
