package test;

import dbms.utilities.btree.BTree;
import dbms.utilities.btree.BTreeKey;

public class BTreeInsertionTest {
    /*
     * Description: should insert all values in the B-tree successfully
     * Expected behavior: prints [[ 5 | Hello ], [ 6 | my ], [ 6 | name ], [ 10 | is ], [ 12 | Peter ], [ 20 | L. ], [ 30 | Griffin ]]
     */
    public static void main(String[] args) {
        BTree<Integer, String> tree = new BTree<>();
        tree.insert(new BTreeKey<>(10, "10"));
        tree.insert(new BTreeKey<>(20, "20"));
        tree.insert(new BTreeKey<>(5, "5"));
        tree.insert(new BTreeKey<>(6, "6"));
        tree.insert(new BTreeKey<>(12, "12"));
        tree.insert(new BTreeKey<>(30, "30"));
        tree.insert(new BTreeKey<>(6, "6"));
        tree.insert(new BTreeKey<>(17, "17"));
        tree.insert(new BTreeKey<>(65, "65"));
        tree.insert(new BTreeKey<>(23, "23"));
        tree.insert(new BTreeKey<>(25, "25"));
        tree.insert(new BTreeKey<>(18, "18"));
        tree.insert(new BTreeKey<>(94, "94"));
        tree.insert(new BTreeKey<>(63, "63"));
        tree.insert(new BTreeKey<>(32, "32"));
        tree.insert(new BTreeKey<>(14, "14"));
        tree.insert(new BTreeKey<>(15, "15"));
        System.out.println(tree.getKeys());
    }
}
