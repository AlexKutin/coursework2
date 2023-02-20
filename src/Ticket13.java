import java.util.Arrays;

public class Ticket13 {

    public static void main(String[] args) {
        System.out.println("********* Reverse array *********");
        Integer[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println("Init array1: " + Arrays.toString(arr1));
        reverseArray(arr1);
        System.out.println("Reverse array1: " + Arrays.toString(arr1));
        System.out.println();
        Integer[] arr2 = {10, 20, 30, 40, 50, 60, 70};
        System.out.println("Init array2: " + Arrays.toString(arr2));
        reverseArray(arr2);
        System.out.println("Reverse array2: " + Arrays.toString(arr2));
        System.out.println("***********************************");
        System.out.println();

        System.out.println("********* Replace spaces **********");
        String s1 = "   te st1   ";
        System.out.println("Initial string1: " + s1);
        System.out.println("String without spaces: " + removeSpaces(s1));
        String s2 = "=== t e s t 2 ====          =";
        System.out.println("Initial string2: " + s2);
        System.out.println("String without spaces: " + removeSpaces(s2));
    }

    private static <T> void reverseArray(T[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int j = arr.length - i -1;
            T tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    private static String removeSpaces(String s) {
        return s.replace(" ", "");
    }
}
