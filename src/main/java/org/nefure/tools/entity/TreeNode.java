package org.nefure.tools.entity;

/**
 * @author nefure
 * @date 2022/10/16 9:14
 */
public abstract class TreeNode<T extends TreeNode<T,K>,K> {

    private K key;

    private T prev;

    private T next;

    private T left;

    private T right;

    private T parent;

    public TreeNode(K key){
        this.key = key;
    }

    /**
     * this node's key
     * @return key
     */
    public K key(){
        return key;
    }

    /**
     * set a key for the node
     */
    protected void key(K key){
        this.key = key;
    }

    /**
     * to get previous node，if exists and supported
     * @return previous node
     * @throws UnsupportedOperationException when not supported
     */
    public T prev(){
        return prev;
    }

    /**
     * to set previous node，if supported
     * @param prev previous node
     * @throws UnsupportedOperationException when not supported
     */
    protected void prev(T prev){
        this.prev = prev;
    }

    /**
     * to get next node, if exists and supported
     * @return next node
     * @throws UnsupportedOperationException when not supported
     */
    public T next(){
        return next;
    }

    /**
     * to set next node, if supported
     * @param next  next node
     * @throws UnsupportedOperationException when not supported
     */
    protected void next(T next){
        this.next = next;
    }

    /**
     * get the left child
     * @return left child
     */
    public T left(){
        return left;
    }

    /**
     * set the left child
     * @param left left child
     */
    protected void left(T left){
        this.left = left;
    }

    /**
     * get the right children
     * @return right children
     */
    public T right(){
        return right;
    }

    /**
     * get the right child
     * @param right  right child
     */
    protected void right(T right){
        this.right = right;
    }

    /**
     * to get the node's parent
     * @return parent node
     */
    public T parent(){
        return parent;
    }

    /**
     * to set the node's parent
     * @param parent  parent node
     */
    protected void parent(T parent){
        this.parent = parent;
    }

    /**
     * use a new node to update this
     * but shouldn't update pointers
     * you can only update node's key、value
     * @param newNode new node
     */
    protected abstract void cover(T newNode);

    public boolean isLeft(){
        return parent.left() == this;
    }

    public boolean isRight(){
        return parent.right() == this;
    }
}
