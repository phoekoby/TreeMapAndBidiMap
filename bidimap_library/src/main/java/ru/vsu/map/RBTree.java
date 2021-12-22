package ru.vsu.map;

public interface RBTree<T extends Comparable<? super T>>   extends Iterable<T>{
    interface TreeNode<T> extends Iterable<T>{
        T getValue();
        TreeNode<T> getLeft();
        TreeNode<T> getRight();
    }
    TreeNode<T> getRoot();
    TreeNode<T> getNode(T value);
    T get(T value);
    boolean contains(T value);
    TreeNode<T> getNode(TreeNode<T> node, T value);
    T put(T value);
    T remove(T value);
    void clear();
    int size();

}
