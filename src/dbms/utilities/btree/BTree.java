package dbms.utilities.btree;

import java.util.List;

public class BTree<K extends Comparable<K>, O> {

    public static final int MAX_DEGREE = 3;
    private BTreeNode<K, O> rootNode = new BTreeNode<>();

    /* attempt to insert a key into the root node */
    public void insert(BTreeKey<K, O> key) {
        this.rootNode.insert(key);
        this.rootNode = this.rootNode.getRoot();
    }

    /* returns the keys in the b-tree matching the specified key value */
    public List<BTreeKey<K, O>> searchKeys(K keyValue) {
        return this.rootNode.searchKeys(keyValue);
    }

    /* returns the keys in the b-tree in ascending order */
    public List<BTreeKey<K, O>> getKeys() {
        return this.rootNode.getKeys();
    }
}
