package comm.ota.site;


import comm.repository.entity.GdsPcc;

/**
 * The type Sibe route.
 */
public class SibeRoute  {

    //查询PCC信息
    private GdsPcc searcPcc;

    //验价同查询，不需要

    //生单PCC信息
    private GdsPcc orderPcc;

    /**
     * Gets searc pcc.
     *
     * @return the searc pcc
     */
    public GdsPcc getSearcPcc() {
        if(searcPcc==null){
            searcPcc=new GdsPcc();
        }
        return searcPcc;
    }

    /**
     * Sets searc pcc.
     *
     * @param searcPcc the searc pcc
     */
    public void setSearcPcc(GdsPcc searcPcc) {
        this.searcPcc = searcPcc;
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

        if (searcPcc != null ? !searcPcc.equals(sibeRoute.searcPcc) : sibeRoute.searcPcc != null) {
            return false;
        }
        return orderPcc != null ? orderPcc.equals(sibeRoute.orderPcc) : sibeRoute.orderPcc == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (searcPcc != null ? searcPcc.hashCode() : 0);
        result = 31 * result + (orderPcc != null ? orderPcc.hashCode() : 0);
        return result;
    }
}
