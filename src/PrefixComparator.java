import java.util.Comparator;

/**
 * Factor pattern for obtaining PrefixComparator objects
 * without calling new. Users simply use
 *
 *     Comparator<Term> comp = PrefixComparator.getComparator(size)
 *
 * @author owen astrachan
 * @date October 8, 2020
 */
public class    PrefixComparator implements Comparator<Term> {

    private int myPrefixSize; // size of prefix

    /**
     * private constructor, called by getComparator
     * @param prefix is prefix used in compare method
     */
    private PrefixComparator(int prefix) {
        myPrefixSize = prefix;
    }


    /**
     * Factory method to return a PrefixComparator object
     * @param prefix is the size of the prefix to compare with
     * @return PrefixComparator that uses prefix
     */
    public static PrefixComparator getComparator(int prefix) {
        return new PrefixComparator(prefix);
    }


    @Override
    /**
     * Use at most myPrefixSize characters from each of v and w
     * to return a value comparing v and w by words. Comparisons
     * should be made based on the first myPrefixSize chars in v and w.
     * @return < 0 if v < w, == 0 if v == w, and > 0 if v > w
     */
    public int compare(Term v, Term w) {
        // change this to use myPrefixSize as specified,
        // replacing line below with code
        String wordV = v.getWord();
        String wordW = w.getWord();
        int newSize = 0;
        if (wordV.length() < myPrefixSize || wordW.length() < myPrefixSize) {
            newSize = Math.min(wordV.length(), wordW.length());
        } 
        else {
            newSize = myPrefixSize;
        }
        for (int i = 0; i < newSize; i++) {
            if (wordV.charAt(i) > wordW.charAt(i)) {
                return 1;
            }
            if (wordV.charAt(i) < wordW.charAt(i)) {
                return -1;
            }
        }
        if (wordV.length() > newSize && newSize != myPrefixSize) {
            return 1;
        }
        if (wordW.length() > newSize && newSize != myPrefixSize) {
            return -1;
        }
        return 0;
    }
}
