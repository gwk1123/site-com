package comm.utils.comparator;


import comm.ota.site.SibeSegment;

import java.util.Comparator;

/**
 * The type Segment comparator.
 */
public class SegmentComparator implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        SibeSegment s1 = (SibeSegment)o1;
        SibeSegment s2 = (SibeSegment)o2;

        if(s1.getFlightIndicator() < s2.getFlightIndicator()){
            return -1;
        }else if(s1.getFlightIndicator() >  s2.getFlightIndicator()){
            return 1;
        }else {
            if(s1.getItemNumber() < s2.getItemNumber()){
                return -1;
            }else if(s1.getItemNumber() > s2.getItemNumber()){
                return 1;
            }else {
                return 0;
            }
        }
    }
}
