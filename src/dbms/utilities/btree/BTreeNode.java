package dbms.utilities.btree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BTreeNode<K extends Comparable<K>, O> {

    BTreeNode<K, O> parent;
    List<BTreeKey<K, O>> keys = new ArrayList<>();
    List<BTreeNode<K, O>> children = new ArrayList<>();

    /* attempt to insert a key into this node or its children with the assumption that this node is not full */
    protected void insert(BTreeKey<K, O> key) {
        if (isLeafNode()) {
            // attempt to insert the key into self
            this.keys.add(this.getInsertionIndex(key), key);
            if (this.keys.size() == BTree.MAX_DEGREE) split();

        } else {
            // attempt to insert the key into the child
            this.children.get(this.getInsertionIndex(key)).insert(key);
        }
    }

    /* split the node and its children and promote the middle key */
    protected void split() {
        // determine the correct middle key index
        int middleKeyIndex = this.getLeftMiddleKeyIndex();

        // split the original child keys and children into parts
        BTreeKey<K, O> middleKey = this.keys.get(middleKeyIndex);
        List<BTreeKey<K, O>> leftKeys = this.getKeysBeforeIndex(middleKeyIndex);
        List<BTreeKey<K, O>> rightKeys = this.getKeysAfterIndex(middleKeyIndex);
        List<BTreeNode<K, O>> leftChildren = this.getChildrenBeforeIndex(middleKeyIndex + 1);
        List<BTreeNode<K, O>> rightChildren = this.getChildrenAfterIndex(middleKeyIndex);

        // create new parent if it does not exist
        if (this.parent == null) {
            this.parent = new BTreeNode<>();
            this.parent.children.add(this);
        };

        // promote the middle key to the parent
        int promotionIndex = this.parent.getInsertionIndex(middleKey);
        this.parent.keys.add(promotionIndex, middleKey);

        // update current node
        this.keys = leftKeys;
        this.children = leftChildren;
        this.children.forEach(c -> c.parent = this);

        // create adjacent node
        BTreeNode<K, O> adjacentNode = new BTreeNode<>();
        adjacentNode.parent = this.parent;
        adjacentNode.keys = rightKeys;
        adjacentNode.children = rightChildren;
        adjacentNode.children.forEach(c -> c.parent = adjacentNode);
        this.parent.children.add(promotionIndex + 1, adjacentNode);

        // parent must be split if full
        if (this.parent.keys.size() == BTree.MAX_DEGREE) this.parent.split();
    }

    /* returns the root of the tree */
    protected BTreeNode<K, O> getRoot() {
        if (this.parent == null) return this;
        return this.parent.getRoot();
    }

    /* the node is a leaf node if it has no children */
    protected boolean isLeafNode() {
        return this.children.isEmpty();
    }

    /* returns the left-leaning middle key index if the number of keys is even, has no bearing if the number of keys is odd */
    private int getLeftMiddleKeyIndex() {
        return (int) Math.ceil((double) this.keys.size() / 2.0) - 1;
    }

    /* gets a sublist of the keys in the keys list before the specified index */
    private List<BTreeKey<K, O>> getKeysBeforeIndex(int index) {
        return new ArrayList<>(IntStream.range(0, index)
                .mapToObj(i -> this.keys.get(i)).toList());
    }

    /* gets a sublist of the keys in the keys list after the specified index */
    private List<BTreeKey<K, O>> getKeysAfterIndex(int index) {
        return new ArrayList<>(IntStream.range(index + 1, this.keys.size())
                .mapToObj(i -> this.keys.get(i)).toList());
    }

    /* gets a sublist of the children in the children list before the specified index */
    private List<BTreeNode<K, O>> getChildrenBeforeIndex(int index) {
        if (this.children.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(IntStream.range(0, index)
                .mapToObj(i -> this.children.get(i)).toList());
    }

    /* gets a sub-list of the children in the children list after the specified index */
    private List<BTreeNode<K, O>> getChildrenAfterIndex(int index) {
        if (this.children.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(IntStream.range(index + 1, this.children.size())
                .mapToObj(i -> this.children.get(i)).toList());
    }

    /* returns the index to which the key should be inserted, the index of the first value >= key */
    private int getInsertionIndex(BTreeKey<K, O> key) {
        return IntStream.range(0, this.keys.size())
            .filter(i -> this.keys.get(i).greaterThanOrEqual(key))
            .findFirst().orElse(this.keys.size());
    }

    /* returns the keys of the child matching the specified key value */
    private List<BTreeKey<K, O>> searchKeysOfChild(int childIndex, K keyValue) {
        if (isLeafNode()) return new ArrayList<>();
        return this.children.get(childIndex).searchKeys(keyValue);
    }

    /* returns all keys in the node matching the specified key value */
    public List<BTreeKey<K, O>> searchKeys(K keyValue) {
        List<BTreeKey<K, O>> keys = new ArrayList<>();

        // add all keys matching the specified value and search their left child
        for (int i = 0; i < this.keys.size(); i++) {
            if (this.keys.get(i).getKeyValue().compareTo(keyValue) == 0) {
                keys.addAll(this.searchKeysOfChild(i, keyValue));
                keys.add(this.keys.get(i));
            }
        }

        // get index of the first key greater than specified value
        int idx = IntStream.range(0, this.keys.size())
            .filter(i -> this.keys.get(i).getKeyValue().compareTo(keyValue) > 0)
            .findFirst().orElse(this.keys.size());

        // search the left child of that key
        keys.addAll(this.searchKeysOfChild(idx, keyValue));
        return keys;
    }

    /* returns the keys of the child, returns empty list if node is a leaf node */
    private List<BTreeKey<K, O>> getKeysOfChild(int childIndex) {
        if (isLeafNode()) return new ArrayList<>();
        return this.children.get(childIndex).getKeys();
    }

    /* returns all the keys in this node and its children in ascending order */
    public List<BTreeKey<K, O>> getKeys() {
        List<BTreeKey<K, O>> keys = new ArrayList<>();

        for (int i = 0; i < this.keys.size(); i++) {
            keys.addAll(this.getKeysOfChild(i));
            keys.add(this.keys.get(i));
        }

        keys.addAll(getKeysOfChild(this.keys.size()));
        return keys;
    }
}
