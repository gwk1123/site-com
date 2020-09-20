package comm.sibe;

import java.util.List;

/**
 * Created by yangdehua on 18/3/14.
 */
public interface SibeEntityMapper<D, E> {

    public E toGds(D gds);

    public D toSibe(E sibe);

    public List<E> toGds(List<D> gdsList);

    public List <D> toSibe(List<E> sibeList);

}
