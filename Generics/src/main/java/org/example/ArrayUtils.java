package org.example;
import java.util.Scanner;
public class ArrayUtils {
    public static <T> int findFirst(T[] array, T element) {
        if (array.equals(null)) {
            return -1;
        }

        for (int i = 0; i < array.length; i++) {
            if (element.equals(null)) {
                if (array[i].equals(null)) {
                    return i;
                }
            }
            else {
                if (element.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {

        final String[] names = {"Alice", "Charlie", "Bob"};
        final int index = ArrayUtils.findFirst(names, "Bob");
        System.out.println(index);

        final String[] names1 = {"Alice", "null", "Bob"};
        final int index1 = ArrayUtils.findFirst(names1, "null");
        System.out.println(index1);
    }
}
