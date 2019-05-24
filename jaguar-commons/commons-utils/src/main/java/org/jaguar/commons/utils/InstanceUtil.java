package org.jaguar.commons.utils;

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

