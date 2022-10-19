package org.nefure.tools.entity;

import java.util.Comparator;

/**
 * @author nefure
 * @date 2022/10/17 18:56
 */
public class TreeSet<T> extends RedBlackTree<TreeSet<T>.Node,T>{

    @SuppressWarnings("unchecked")
    public TreeSet() {
        super((a,b) -> {
            if (a instanceof Comparable<?>){
                return ((Comparable<T>)a).compareTo(b);
            }
            return a.hashCode() - b.hashCode();
        });
    }

    public TreeSet(Comparator<T> comparator){
        super(comparator);
    }

    public class Node extends RedBlackTree<Node,T>.Node{

        public Node(T key) {
            super(key);
        }

        @Override
        protected void cover(Node newNode) {
            key(newNode.key());
        }
    }

    public void add(T value){
        super.put(new Node(value));
    }


    @Override
    public void put(Node node) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(T value){
        return get(value) != null;
    }

}
