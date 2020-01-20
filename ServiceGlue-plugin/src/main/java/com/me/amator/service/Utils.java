package com.me.amator.service;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public class Utils {

    public static boolean isEmpty(String value) {
        return length(value) < 0;
    }

    public static int length(String value) {
        return value == null ? -1 : value.length();
    }

    public static <K, V> boolean isEmpty(Map<K, V> c) {
        return length(c) <= 0;
    }

    public static <K, V> int length(Map<K, V> c) {
        return c == null ? -1 : c.size();
    }

    public static <T> boolean isEmpty(Collection<T> c) {
        return length(c) <= 0;
    }

    public static <T> int length(Collection<T> c) {
        return c == null ? -1 : c.size();
    }


    public static boolean equel(String s1, String s2) {
        return (isEmpty(s1) && isEmpty(s2) || s1 != null && s1.equals(s2));
    }

    public static String path2Current(boolean start, String className) {
        if (isEmpty(className)) {
            return className;
        }
        StringBuilder builder = new StringBuilder(className);
        int lastIndexOf = builder.lastIndexOf(File.separator);
        if (start) {
            return builder.substring(0, lastIndexOf);
        } else {
            return builder.substring(lastIndexOf + 1);
        }
    }

}
