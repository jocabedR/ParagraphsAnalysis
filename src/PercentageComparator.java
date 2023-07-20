import java.util.*;

// Esta clase se realizó tomando el ejemplo de:
// https://hireamir.com/blog/sort-list-of-objects-by-field-java
class PercentageComparator implements Comparator<WordPercentage> {
    @Override
    public int compare(WordPercentage wp1, WordPercentage wp2) {
        // Comparar los valores de percentage en orden descendente
        return Float.compare(wp2.getPercentage(), wp1.getPercentage());
    }
}