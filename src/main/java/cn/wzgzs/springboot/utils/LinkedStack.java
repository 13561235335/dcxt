package cn.wzgzs.springboot.utils;

import java.util.EmptyStackException;
import java.util.LinkedList;

/**
 * @author: smartlv
 * @date 2014年2月26日 下午6:22:38
 */
public class LinkedStack<E>
{
    private final LinkedList<E> list = new LinkedList<E>();

    public LinkedStack()
    {
    }

    public int size()
    {
        return list.size();
    }

    public void clear()
    {
        list.clear();
    }

    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    // 只返回,不删除
    public E peek()
    {
        if (isEmpty())
        {
            throw new EmptyStackException();
        }
        return list.getLast();
    }

    // 出栈
    public E pop()
    {
        if (isEmpty())
        {
            throw new EmptyStackException();
        }
        return list.removeLast();
    }

    // 入栈
    public void push(E element)
    {
        list.addLast(element);
    }

    /**
     * remove.
     * 
     * @param index
     * @return element
     */
    public E remove(int index)
    {
        E ret = list.remove(index);
        return ret;
    }

    @Override
    public String toString()
    {
        return list.toString();
    }
}
