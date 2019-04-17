package com.itqingning.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例辅助类
 *
 * @author lvws
 * @since 2012-07-18
 */
public final class InstanceUtil {

    private InstanceUtil() {
    }

    /**
     * @param oldBean
     * @param newBean
     * @return
     */
    public static <T> T getDiff(T oldBean, T newBean) {
        if (oldBean == null && newBean != null) {
            return newBean;
        } else if (newBean == null) {
            return null;
        }

        Class<?> clazz = oldBean.getClass();
        try {
            @SuppressWarnings("unchecked")
            T object = (T) clazz.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            boolean flag = false;

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (key.equals("class")) {
                    continue;
                }

                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                // 得到property对应的setter方法
                Method setter = property.getWriteMethod();
                Object oldValue = getter.invoke(oldBean);
                Object newValue = getter.invoke(newBean);
                if (newValue != null) {
                    if (oldValue == null) {
                        setter.invoke(object, newValue);
                        flag = true;
                    } else if (!newValue.equals(oldValue)) {
                        setter.invoke(object, newValue);
                        flag = true;
                    }
                }
            }
            return flag ? object : newBean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs an empty ArrayList.
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Constructs an empty HashMap.
     */
    public static <k, v> HashMap<k, v> newHashMap() {
        return new HashMap<k, v>();
    }

    /**
     * Constructs an empty HashSet.
     */
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    /**
     * Constructs an empty Hashtable.
     */
    public static <k, v> Hashtable<k, v> newHashtable() {
        return new Hashtable<k, v>();
    }

    /**
     * Constructs an empty LinkedHashMap.
     */
    public static <k, v> LinkedHashMap<k, v> newLinkedHashMap() {
        return new LinkedHashMap<k, v>();
    }

    /**
     * Constructs an empty LinkedHashSet.
     */
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    /**
     * Constructs an empty LinkedList.
     */
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

    /**
     * Constructs an empty TreeMap.
     */
    public static <k, v> TreeMap<k, v> newTreeMap() {
        return new TreeMap<k, v>();
    }

    /**
     * Constructs an empty TreeSet.
     */
    public static <E> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }

    /**
     * Constructs an empty Vector.
     */
    public static <E> Vector<E> newVector() {
        return new Vector<E>();
    }

    /**
     * Constructs an empty WeakHashMap.
     */
    public static <k, v> WeakHashMap<k, v> newWeakHashMap() {
        return new WeakHashMap<k, v>();
    }

    /**
     * Constructs an empty HashMap.
     */
    public static <k, v> Map<k, v> newHashMap(k key, v value) {
        Map<k, v> map = newHashMap();
        map.put(key, value);
        return map;
    }

    /**
     * Constructs an empty ConcurrentHashMap.
     */
    public static <k, v> ConcurrentHashMap<k, v> newConcurrentHashMap() {
        return new ConcurrentHashMap<k, v>();
    }
}

