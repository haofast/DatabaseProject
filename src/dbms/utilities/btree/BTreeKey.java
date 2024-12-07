package dbms.utilities.btree;


public class BTreeKey<K extends Comparable<K>, O> {

    private final K keyValue;
    private final O objectValue;

    public BTreeKey(K keyValue, O objectValue) {
        this.keyValue = keyValue;
        this.objectValue = objectValue;
    }

    public K getKeyValue() {
        return this.keyValue;
    }

    public O getObjectValue() {
        return this.objectValue;
    }

    public boolean greaterThanOrEqual(BTreeKey<K, O> key) {
        return this.keyValue.compareTo(key.keyValue) >= 0;
    }

    public String toString() {
        return "[ " + this.keyValue + " | " + this.objectValue + " ]";
    }
}
