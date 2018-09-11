package cn.wzgzs.springboot.utils;

/**
 * Created by Seven on 16/2/29.
 */
public class PagingUtils {
    public static boolean hasMore(long total, long pageSize, long pageNum, long curSize){
        if(total > (pageNum-1) * pageSize + curSize){
            return true;
        } else {
            return false;
        }
    }
}
