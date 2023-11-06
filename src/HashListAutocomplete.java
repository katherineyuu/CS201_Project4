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
        for (int i = 0; i < terms.length; i++) {
            String t = terms[i];
            mySize += BYTES_PER_CHAR*t.length();
            for (int j = 0; j <= MAX_PREFIX; j++) {
                String prefix = t.substring(0, j);
                Term newTerm = new Term(prefix, weights[i]);
                mySize += BYTES_PER_CHAR*prefix.length() + BYTES_PER_DOUBLE*weights[i];

                myMap.putIfAbsent(prefix, new ArrayList<Term>());
                myMap.get(prefix).add(newTerm);
            }
        }

        for (String k: myMap.getKeySet()) {
            List<Term> list = myMap.get(k);
            Collections.sort(list, Comparator.comparing(Term::getWeight).reversed());
        }

    }

    @Override
    public int sizeInBytes() {
        return mySize;
    }  
}

