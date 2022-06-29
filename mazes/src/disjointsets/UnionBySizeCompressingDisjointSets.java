package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    HashMap<T, Integer> valueMap;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        valueMap = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        pointers.add(-1);
        int size = pointers.size();
        valueMap.put(item, size - 1);
    }

    @Override
    public int findSet(T item) {
        /*
            This method could have been implemented recursively, but we decided to implement
            it iteratively
         */

        ArrayList<Integer> indexList = new ArrayList<>();
        int prev = 0;
        if (valueMap.containsKey(item)) {
            if (pointers.get(valueMap.get(item)) < 0) {
                return valueMap.get(item);
            }
            int cur = pointers.get(valueMap.get(item));
            indexList.add(valueMap.get(item));
            while (cur >= 0) {
                indexList.add(cur);
                prev = cur;              // prev is used to keep track of the index of the parent node
                cur = pointers.get(cur); // updates to the next location in pointers
            }
            for (int i : indexList) {
                if (pointers.get(i) >= 0) {
                    // this is for compression, setting the pointers in parent node path to top root
                    pointers.set(i, prev);
                }
            }
            return prev;

        } else {
            throw new IllegalArgumentException("Item not found");
        }
    }

    @Override
    public boolean union(T item1, T item2) {
        int set1 = findSet(item1);
        int set2 = findSet(item2);
        if (set1 != set2) {
            int newWeight = pointers.get(set1) + pointers.get(set2);
            if (Math.abs(pointers.get(set1)) >= Math.abs(pointers.get(set2))) { // checks for the bigger size
                pointers.set(set1, newWeight);      // if set1 is bigger, it absorbs set2 and updates its size
                pointers.set(set2, set1);           // this line sets set2 parent to point to set1 parent
            } else {
                pointers.set(set2, newWeight);      // does the same thing the other way for set1 is bigger
                pointers.set(set1, set2);
            }
            return true;
        }
        return false; // returns true if given items are in different sets, false if in same set
    }
}
