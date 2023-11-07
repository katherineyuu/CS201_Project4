import java.util.*;

public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
		if (terms.length != weights.length) {
			throw new IllegalArgumentException("terms and weights are not the same length");
		}
		initialize(terms,weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if (prefix.length() > MAX_PREFIX) {
            prefix = prefix.substring(0, MAX_PREFIX);
        }
        if (myMap.containsKey(prefix)) {
            List<Term> all = myMap.get(prefix);
            List<Term> list = all.subList(0, Math.min(k, all.size()));
            return list;  
        }
        else {
            List<Term> list = new ArrayList<>();
            return list;
        }
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<>();
        for (int i = 0; i < terms.length; i++) {

            String t = terms[i];
            int max = Math.min(t.length(), MAX_PREFIX);
            mySize +=BYTES_PER_DOUBLE+BYTES_PER_CHAR*t.length();

            for (int j = 0; j <= max; j++) {

                String prefix = t.substring(0, j);
                Term newTerm = new Term(t, weights[i]);
                if (!myMap.containsKey(prefix)) {
                    mySize += BYTES_PER_CHAR*prefix.length();
                }
                myMap.putIfAbsent(prefix, new ArrayList<Term>());
                myMap.get(prefix).add(newTerm);
            }
        }

        for (String k: myMap.keySet()) {
            List<Term> list = myMap.get(k);
            Collections.sort(list, Comparator.comparing(Term::getWeight).reversed());
        }

    }

    @Override
    public int sizeInBytes() {
        if(mySize == 0){
            for(String key : myMap.keySet){
                mySize += BYTES_PER_DOUBLE;
                mySize += BYTES_PER_INT;
                mySize += BYTES_PER_CHAR * key.length;
                for(Term term : myMap.get(key)){
                    mySize += BYTES_PER_CHAR * term.getWord().length;
                    mySize += BYTES_PER_DOUBLE;
                }
            }
        }
        return mySize;
    }  
}

