import java.io.File;

//This is a class that implements Merge Sort to sort files according to the numbers in their names.
class Sort {
    static File[] mSort(File[] init) {
        if (init.length <= 1) {
            return init;
        }
        File[] first = new File[init.length / 2];
        File[] second = new File[init.length - first.length];
        System.arraycopy(init, 0, first, 0, first.length);
        System.arraycopy(init, first.length, second, 0, second.length);

        mSort(first);
        mSort(second);
        merge(first, second, init);
        return init;
    }

    //Merge function.
    private static void merge(File[] first, File[] second, File[] result) {
        int iFirst = 0;
        int iSecond = 0;
        int iMerged = 0;

        while (iFirst < first.length && iSecond < second.length) {
            int first_num = getNum(first[iFirst]), second_num = getNum(second[iSecond]);

            if (first_num < second_num) {
                result[iMerged] = first[iFirst];
                iFirst++;
            } else {
                result[iMerged] = second[iSecond];
                iSecond++;
            }
            iMerged++;
        }
        System.arraycopy(first, iFirst, result, iMerged, first.length - iFirst);
        System.arraycopy(second, iSecond, result, iMerged, second.length - iSecond);
    }

    //Getting the integer value from a string using regex.
    private static Integer getNum(File file) {
        String str = file.getName();
        return Integer.parseInt(str.replaceAll("[^0-9]", ""));
    }
}