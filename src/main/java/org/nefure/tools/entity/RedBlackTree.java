package org.nefure.tools.entity;

import java.util.Comparator;

/**
 * @author nefure
 * @date 2022/10/16 11:05
 */
public class RedBlackTree<T extends RedBlackTree<T,K>.Node,K> implements Tree<T,K> {

    public abstract class Node extends TreeNode<T,K> {

        private boolean black;

        public Node(K key) {
            super(key);
        }

        protected void toRed(){
            black = false;
        }

        protected void toBlack(){
            black = true;
        }

        public boolean isBlack(){
            return black;
        }

        protected void exchangeColor(Node b){
            boolean tmp = b.black;
            b.black = black;
            black = tmp;
        }
    }

    private boolean isRed(Node node){
        return node != null && !node.isBlack();
    }

    private T root;

    private int size;

    private final Comparator<K> cmp;

    public RedBlackTree(Comparator<K> comparator){
        this.cmp = comparator;
    }

    @Override
    public T getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T next(K idx) {
        return null;
    }

    @Override
    public T prev(K key) {
        return null;
    }

    @Override
    public T get(K key) {
        return getNode(key);
    }

    private T getNode(K key){
        T idx = root;
        while (idx != null){
            int rs = cmp.compare(idx.key(), key);
            if (rs < 0){
                idx = idx.right();
            }else if (rs == 0){
                return idx;
            }else {
                idx = idx.left();
            }
        }
        return null;
    }

    @Override
    public T first() {
        if (root == null){
            return null;
        }
        T idx = root;
        while (idx.left() != null){
            idx = idx.left();
        }
        return idx;
    }

    @Override
    public T last() {
        if (root == null){
            return null;
        }
        T idx = root;
        while (idx.right() != null){
            idx = idx.right();
        }
        return idx;
    }

    @Override
    public void put(T node) {
        if (root == null){
            root = node;
            root.toBlack();
            size++;
        }else {
            T idx = root;
            int rs;
            do{
                rs = cmp.compare(idx.key(), node.key());
                if (rs == 0){
                    idx.cover(node);
                    ++size;
                    return;
                }else if (rs < 0){
                    if (idx.right() == null){
                        T tmp;
                        node.next(tmp = idx.next());
                        if (tmp != null){
                            tmp.prev(node);
                        }
                        node.prev(idx);
                        idx.next(node);
                        idx.right(node);
                        node.parent(idx);
                        node.toRed();
                        ++size;
                        break;
                    }
                    idx = idx.right();
                }else {
                    if (idx.left() == null){
                        T tmp;
                        node.prev(tmp = idx.prev());
                        if (tmp != null){
                            tmp.next(node);
                        }
                        node.next(idx);
                        idx.prev(node);
                        idx.left(node);
                        node.parent(idx);
                        node.toRed();
                        ++size;
                        break;
                    }
                    idx = idx.left();
                }
            }while (idx != null);
            fixAfterInsert(node);
            root.toBlack();
            root.parent(null);
        }
    }

    private void fixAfterInsert(T node) {
        T parent = node.parent();
        if (isRed(parent)){
            T grandpa = parent.parent();
            if (parent.isLeft()){
                //rotate left if in need
                if (node.isRight()){
                    grandpa.left(node);
                    node.parent(grandpa);
                    rotateLeft(parent,node);
                    parent = node;
                }
                //if parent's brother is red,make the red node up
                if (isRed(grandpa.right())){
                    redUp(grandpa);
                    fixAfterInsert(grandpa);
                }else {
                    //if parent's brother is black rotate right
                    sonReplace(parent,grandpa);
                    rotateRight(grandpa,parent);
                    parent.toBlack();
                    grandpa.toRed();
                    if (grandpa == root){
                        root = parent;
                    }
                }
            }
            else {
                //rotate right if in need
                if (node.isLeft()){
                    grandpa.right(node);
                    node.parent(grandpa);
                    rotateRight(parent,node);
                    parent = node;
                }
                if (isRed(grandpa.left())){
                    redUp(grandpa);
                    fixAfterInsert(grandpa);
                }else {
                    //if parent's brother is black rotate left
                    sonReplace(parent,grandpa);
                    rotateLeft(grandpa,parent);
                    grandpa.toRed();
                    parent.toBlack();
                    if (grandpa == root){
                        root = parent;
                    }
                }
            }
        }
        else if (parent == null){
            root = node;
            root.toBlack();
        }
    }

    private void rotateLeft(T parent, T right){
        T tmp = right.left();
        parent.right(tmp);
        if (tmp != null){
            tmp.parent(parent);
        }
        parent.parent(right);
        right.left(parent);
    }

    private void rotateRight(T parent, T left){
        T tmp = left.right();
        parent.left(tmp);
        if (tmp != null){
            tmp.parent(parent);
        }
        left.right(parent);
        parent.parent(left);
    }

    private void redUp(T parent){
        parent.toRed();
        parent.left().toBlack();
        parent.right().toBlack();
    }

    @Override
    public T remove(K key) {
        T node;
        if ((node = getNode(key)) != null){
            if (node == root){
                root = null;
            }else {
                remove(node);
            }
        }
        return node;
    }

    private void remove(T node){
        T tmp;
        T child = node.left();
        if (child != null && (child = node.right()) == null){
            //只有左子节点
            child = node.left();
            child.next(tmp = node.next());
            if (tmp != null){
                tmp.prev(child);
            }
            sonReplace(child,node);
            child.toBlack();
            size++;
        }else if (child == null){
            //要删除的节点只有右子节点
            child = node.right();
            if (child != null){
                child.prev(tmp = node.prev());
                if (tmp != null){
                    tmp.next(child);
                }
                sonReplace(child,node);
                child.toBlack();
                size--;
            }
            //要删除的节点无子节点的情况
            //      1.如果该节点为黑节点
            else if (node.isBlack()){
                prepareRemoveBlackLeaf(node);
                toRemove(node);
                size--;
            }
            //      2.是红节点就直接删除
            else {
                toRemove(node);
                size--;
            }
        }else {
            //左右都不为空时,用下一个节点来覆盖此节点
            tmp = node.next();
            node.cover(tmp);
            remove(tmp);
        }
    }

    /**
     * 目的为让node子树的黑高 +1，但不影响其他子树，<br/>
     * 这样就能保证删除最开始的node后树平衡
     */
    private void prepareRemoveBlackLeaf(T node) {
        if (isRed(node)){
            node.toBlack();
            return;
        }
        //在左边情
        T parent = node.parent(),tmp,child;
        if (node.isLeft()){
            //兄弟节点为红色时，此时兄弟节点子节点必为非空黑色，拿来当兄弟，以此保证兄弟是黑的
            if (isRed(child = parent.right())){
                sonReplace(child,parent);
                rotateLeft(parent,child);
                parent.toRed();
                child.toBlack();
                child = parent.right();
            }
            T l;
            //如果双黑，把问题抛给父节点
            if ((!isRed(child.left())) && !isRed(child.right())){
                child.toRed();
                prepareRemoveBlackLeaf(parent);
                return;
            }
            //兄弟左红右黑，就能把右变成红
            else if (isRed(l = child.left()) && !isRed(child.right())){
                l.toBlack();
                child.toRed();
                parent.right(l);
                l.parent(parent);
                child.parent(l);
                child.left(tmp = l.right());
                if (tmp != null){
                    tmp.parent(child);
                }
                l.right(child);
                child = l;
            }
            //此时兄弟已经保证是右红了，可以把兄弟接过来增加树高了，兄弟剩余部分也能通过红->黑补回丢失的黑高
            child.exchangeColor(parent);
            sonReplace(child,parent);
            parent.parent(child);
            parent.right(tmp = child.left());
            if (tmp != null){
                tmp.parent(parent);
            }
            child.left(parent);
            child.right().toBlack();
        }else {
            //在右边
            //兄弟节点为红色时，此时兄弟节点子节点必为非空黑色，拿来当兄弟，以此保证兄弟是黑的
            if (isRed(child = parent.left())){
                sonReplace(child,parent);
                rotateRight(parent,child);
                parent.toRed();
                child.toBlack();
                child = parent.left();
            }
            T r;
            //如果双黑，把问题抛给父节点
            if ((!isRed(child.left())) && !isRed(child.right())){
                child.toRed();
                prepareRemoveBlackLeaf(parent);
                return;
            }
            //兄弟右红左黑，就能把左变成红
            else if (isRed(r = child.right()) && !isRed(child.left())){
                r.toBlack();
                child.toRed();
                parent.left(r);
                r.parent(parent);
                child.parent(r);
                child.right(tmp = r.left());
                if (tmp != null){
                    tmp.parent(child);
                }
                r.left(child);
                child = r;
            }
            //此时兄弟已经保证是左红了，可以把兄弟接过来增加树高了，兄弟剩余部分也能通过红->黑补回丢失的黑高
            child.exchangeColor(parent);
            sonReplace(child,parent);
            parent.parent(child);
            parent.left(tmp = child.right());
            if (tmp != null){
                tmp.parent(parent);
            }
            child.right(parent);
            child.left().toBlack();
        }
    }

    private void sonReplace(T child, T origin){
        T tmp = origin.parent();
        if (tmp != null){
            if (origin.isLeft()){
                tmp.left(child);
            }else {
                tmp.right(child);
            }
        }
        child.parent(tmp);
    }

    /**
     * 直接删除节点（没有平衡操作）
     */
    private void toRemove(T node){
        T tmp = node.parent();
        if (node.isLeft()){
            tmp.left(null);
        }else {
            tmp.right(null);
        }
        if ((tmp = node.next()) != null){
            tmp.prev(node.prev());
        }
        if ((tmp = node.prev()) != null){
            tmp.next(node.next());
        }
    }
}
