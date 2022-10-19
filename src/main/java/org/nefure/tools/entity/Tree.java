package org.nefure.tools.entity;

/**
 * @author nefure
 * @date 2022/10/16 9:13
 */
public interface Tree<T extends TreeNode<T,K>,K> {

    /**
     * get the root of tree
     * @return root node
     */
    T getRoot();

    /**
     * how many keys the tree have
     * @return count of key
     */
    int size();

    /**
     * next node of idx
     * @param idx idx
     * @return next node
     */
    T next(K idx);

    /**
     * previous node of input key
     * @param key input key
     * @return previous node
     */
    T prev(K key);

    /**
     * get the node with input key,if exists
     * @param key input key
     * @return node in this tree
     */
    T get(K key);

    /**
     * to get the first node
     * @return the first node
     */
    T first();

    /**
     * to get the end key
     * @return node in the end
     */
    T last();

    /**
     * add a node to the tree
     * @param node the node's key will be us to find its place
     */
    void put(T node);

    /**
     * remove the node by key
     * @param key the removed key
     * @return the removed node
     */
    T remove(K key);
}
