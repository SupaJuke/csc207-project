package entity;

import java.util.SortedMap;

public interface Taggable {
    /**
     * Returns a Map that maps a tag name to its information.
     * @return A Map mapping a tag name to its information.
     */
    public SortedMap<String, String> getTags();
}
