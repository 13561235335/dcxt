package cn.wzgzs.springboot.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LogicUtil
{

    private static int ZERO = 0;
    private static String EMPTY_STRING = "";

    public static boolean isEmptyString(String subject)
    {
        if (null == subject || EMPTY_STRING.equals(subject.trim()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isNullOrEmpty(String subject)
    {
        if (null == subject || EMPTY_STRING.equals(subject))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(String subject)
    {
        return !isNullOrEmpty(subject);
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNullOrEmpty(Map map)
    {
        if (null == map || ZERO == map.size())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNotNullAndEmpty(Map map)
    {
        return !isNullOrEmpty(map);
    }

    public static boolean isNullOrEmpty(Collection<?> collection)
    {
        if (null == collection || ZERO == collection.size())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNotNullAndEmpty(Collection collection)
    {
        return !isNullOrEmpty(collection);
    }

    public static boolean isNull(Object object)
    {
        if (object == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNull(Object Object)
    {
        return !isNull(Object);
    }

    public static boolean isNullOrEmpty(Object[] objects)
    {
        if (null == objects || ZERO == objects.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(Object[] objects)
    {
        return !isNullOrEmpty(objects);
    }

    public static boolean isNullOrEmpty(byte[] array)
    {
        if (null == array || ZERO == array.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(byte[] array)
    {
        return !isNullOrEmpty(array);
    }

    public static boolean isNullOrEmpty(short[] array)
    {
        if (null == array || ZERO == array.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(short[] array)
    {
        return !isNullOrEmpty(array);
    }

    public static boolean isNullOrEmpty(int[] array)
    {
        if (null == array || ZERO == array.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(int[] array)
    {
        return !isNullOrEmpty(array);
    }

    public static boolean isNotNullAndEmpty(long[] array)
    {
        return !isNullOrEmpty(array);
    }

    public static boolean isNullOrEmpty(long[] array)
    {
        if (null == array || ZERO == array.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNullOrEmpty(char[] array)
    {
        if (null == array || ZERO == array.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(char[] array)
    {
        return !isNullOrEmpty(array);
    }

    public static boolean isNullOrEmpty(float[] array)
    {
        if (null == array || ZERO == array.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(float[] array)
    {
        return !isNullOrEmpty(array);
    }

    public static boolean isNullOrEmpty(double[] array)
    {
        if (null == array || ZERO == array.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotNullAndEmpty(double[] array)
    {
        return !isNullOrEmpty(array);
    }

    // 轮播一个,互联网产品爱好随机曝光
    public static <V> List<V> shuffleAndSubList(List<V> list, int subSize)
    {
        if (list == null || list.size() == 0)
        {
            return null;
        }
        Collections.shuffle(list);
        list = list.size() > subSize ? list.subList(0, subSize) : list;
        return list;
    }
}
