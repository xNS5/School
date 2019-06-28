import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Sort{
    private static File[] Sort(File[] init){

        if (init.length <= 1) {
            return init;
        }

        File[] first = new File[init.length/2];
        File[] second = new File[init.length - first.length];
        System.arraycopy(init, 0, first, 0, first.length );
        System.arraycopy(init, first.length, second, 0, second.length);

        Sort(first);
        Sort(second);

        merge(first, second, init);
        return init;
    }

    private static void merge(File[] first, File[] second, File[] result)
    {

        int iFirst = 0;
        int iSecond = 0;
        int iMerged = 0;

        while (iFirst < first.length && iSecond < second.length)
        {

            if (first[iFirst].split("") < 0)
            {
                result[iMerged] = first[iFirst];
                iFirst++;
            }
            else
            {
                result[iMerged] = second[iSecond];
                iSecond++;
            }
            iMerged++;
        }

        System.arraycopy(first, iFirst, result, iMerged, first.length - iFirst);
        System.arraycopy(second, iSecond, result, iMerged, second.length - iSecond);
    }
}
}