package ru.vsu.map;

import java.util.Iterator;
import java.util.Stack;

public class SimpleRedBlackTree<T extends Comparable<? super T>> implements RBTree<T> {
    static final boolean RED = true;
    static final boolean BLACK = false;

     class RBTreeNode implements TreeNode<T> {

        public T value;
        public boolean color;
        public RBTreeNode left;
        public RBTreeNode right;
        public RBTreeNode parent;

        public RBTreeNode(T value, boolean color, RBTreeNode left, RBTreeNode right, RBTreeNode parent) {
            this.value = value;
            this.color = color;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public RBTreeNode(T value, RBTreeNode parent) {
            this(value, BLACK, null, null, parent);
        }

        public RBTreeNode(T value) {
            this(value, null);
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public TreeNode<T> getLeft() {
            return left;
        }

        @Override
        public TreeNode<T> getRight() {
            return right;
        }


         @Override
         public Iterator<T> iterator() {
             return SimpleRedBlackTree.this.dfs(this).iterator();
         }
     }

    RBTreeNode root = null;
    private int size = 0;

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public Iterator<T> iterator() {
        return this.getRoot().iterator();
    }

    @Override
    public TreeNode<T> getNode(T value) {
        return getNode(root,value);
    }

    @Override
    public T get(T value) {
        TreeNode<T> valueNode = getNode(value);
        return (valueNode == null) ? null : valueNode.getValue();
    }

    @Override
    public boolean contains(T value) {
        return getNode(value) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }
    @Override
    public RBTree.TreeNode<T> getNode(RBTree.TreeNode<T> node, T value) {
        if (node == null) {
            return null;
        }
        int cmp = node.getValue().compareTo(value);
        if (cmp == 0) {
            return node;
        } else if (cmp > 0) {
            return getNode(node.getLeft(), value);
        } else {
            return getNode(node.getRight(), value);
        }
    }
    @Override
    public T put(T value) {
        if (root == null) {
            setRoot(new RBTreeNode(value));
            size++;
            return null;
        }
        RBTreeNode node = root;
        while (true) {
            int cmp = value.compareTo(node.value);
            if (cmp == 0) {
                T oldValue = node.value;
                node.value = value;
                return oldValue;
            } else if (cmp < 0) {
                if (node.left == null) {
                    setLeft(node, new RBTreeNode(value));
                    size++;
                    correctAfterAdd(node.left);
                    return null;
                }
                node = node.left;
            } else {
                if (node.right == null) {
                    setRight(node, new RBTreeNode(value));
                    size++;
                    correctAfterAdd(node.right);
                    return null;
                }
                node = node.right;
            }
        }
    }

    private TreeNode<T> getMinNode(TreeNode<T> node) {
        return (node == null || node.getLeft() == null) ? node : getMinNode(node.getLeft());
    }
    public Iterable<T> dfs(RBTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<TreeNode<T>> stack = new Stack<>();
            RBTree.TreeNode<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    RBTree.TreeNode<T> node = stack.pop();
                    T result = node.getValue();
                    if (node.getRight() != null) {
                        node = node.getRight();
                        while (node != null) {
                            stack.push(node);
                            node = node.getLeft();
                        }
                    }
                    return result;
                }
            };
        };
    }
    @Override
    public T remove(T value) {
        RBTreeNode node = (RBTreeNode) getNode(value);
        if (node == null) {
            // в дереве нет такого значения
            return null;
        }
        T oldValue = node.value;
        if (node.left != null && node.right != null) {
            RBTreeNode nextValueNode = (RBTreeNode) getMinNode(node.right);
            node.value = nextValueNode.value;
            node = nextValueNode;
        }
        // здесь node имеет не более одного потомка
        RBTreeNode child = (node.left != null) ? node.left : node.right;
        if (child != null) {
            if (node == root) {
                setRoot(child);
                root.color = BLACK;
            } else if (node.parent.left == node) {
                // child - левый потомок node
                setLeft(node.parent, child);
            } else {
                // child - правый потомок node
                setRight(node.parent, child);
            }
            if (node.color == BLACK) {
                // если удалили черный узел, то нарушилось равновесие по черной высоте
                correctAfterRemove(child);
            }
        } else if (node == root) {
            root = null;
        } else {
            // если удалили черный узел, то нарушилось равновесие по черной высоте
            if (node.color == BLACK) {
                correctAfterRemove(node);
            }
            removeFromParent(node);
        }
        size--;
        return oldValue;
    }



    private void correctAfterAdd(RBTreeNode node) {
        if (node != null) {
            node.color = RED;
        }
        if (node != null && node != root && colorOf(node.parent) == RED) {
            if (isRed(siblingOf(parentOf(node)))) {
                setColor(parentOf(node), BLACK);
                setColor(siblingOf(parentOf(node)), BLACK);
                setColor(grandparentOf(node), RED);
                correctAfterAdd(grandparentOf(node));
            }
            else if (parentOf(node) == leftOf(grandparentOf(node))) {
                if (node == rightOf(parentOf(node))) {
                    leftRotate(node = parentOf(node));
                }
                setColor(parentOf(node), BLACK);
                setColor(grandparentOf(node), RED);
                rightRotate(grandparentOf(node));
            }
            else if (parentOf(node) == rightOf(grandparentOf(node))) {
                if (node == leftOf(parentOf(node))) {
                    rightRotate(node = parentOf(node));
                }
                setColor(parentOf(node), BLACK);
                setColor(grandparentOf(node), RED);
                leftRotate(grandparentOf(node));
            }
        }
        setColor(root, BLACK);
    }

    private void correctAfterRemove(RBTreeNode node) {
        while (node != root && isBlack(node)) {
            if (node == leftOf(parentOf(node))) {
                // Pulled up node is a left child
                RBTreeNode sibling = rightOf(parentOf(node));
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    leftRotate(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (isBlack(rightOf(sibling))) {
                        setColor(leftOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rightRotate(sibling);
                        sibling = rightOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(rightOf(sibling), BLACK);
                    leftRotate(parentOf(node));
                    node = root;
                }
            } else {
                // pulled up node is a right child
                RBTreeNode sibling = leftOf(parentOf(node));
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(node), RED);
                    rightRotate(parentOf(node));
                    sibling = leftOf(parentOf(node));
                }
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) {
                    setColor(sibling, RED);
                    node = parentOf(node);
                } else {
                    if (isBlack(leftOf(sibling))) {
                        setColor(rightOf(sibling), BLACK);
                        setColor(sibling, RED);
                        leftRotate(sibling);
                        sibling = leftOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), BLACK);
                    setColor(leftOf(sibling), BLACK);
                    rightRotate(parentOf(node));
                    node = root;
                }
            }
        }
        setColor(node, BLACK);
    }

    private void leftRotate(RBTreeNode node) {
        if (node.right == null) {
            return;
        }
        RBTreeNode right = node.right;
        setRight(node, right.left);
        if (node.parent == null) {
            setRoot(right);
        } else if (node.parent.left == node) {
            setLeft(node.parent, right);
        } else {
            setRight(node.parent, right);
        }
        setLeft(right, node);
    }

    private void rightRotate(RBTreeNode node) {
        if (node.left == null) {
            return;
        }
        RBTreeNode left = node.left;
        setLeft(node, left.right);
        if (node.parent == null) {
            setRoot(left);
        } else if (node.parent.left == node) {
            setLeft(node.parent, left);
        } else {
            setRight(node.parent, left);
        }
        setRight(left, node);
    }

    private void removeFromParent(RBTreeNode node) {
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = null;
            } else if (node.parent.right == node) {
                node.parent.right = null;
            }
            node.parent = null;
        }
    }


    private boolean colorOf(RBTreeNode node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isRed(RBTreeNode node) {
        return colorOf(node) == RED;
    }

    private boolean isBlack(RBTreeNode node) {
        return colorOf(node) == BLACK;
    }

    private void setColor(RBTreeNode node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private void setLeft(RBTreeNode node, RBTreeNode left) {
        if (node != null) {
            node.left = left;
            if (left != null) {
                left.parent = node;
            }
        }
    }

    private void setRight(RBTreeNode node, RBTreeNode right) {
        if (node != null) {
            node.right = right;
            if (right != null) {
                right.parent = node;
            }
        }
    }

    private void setRoot(RBTreeNode node) {
        root = node;
        if (node != null) {
            node.parent = null;
        }
    }

    private RBTreeNode parentOf(RBTreeNode node) {
        return node == null ? null : node.parent;
    }

    private RBTreeNode grandparentOf(RBTreeNode node) {
        return (node == null || node.parent == null) ? null : node.parent.parent;
    }

    private RBTreeNode siblingOf(RBTreeNode node) {
        return (node == null || node.parent == null)
                ? null
                : (node == node.parent.left)
                ? node.parent.right : node.parent.left;
    }

    private RBTreeNode leftOf(RBTreeNode node) {
        return node == null ? null : node.left;
    }

    private RBTreeNode rightOf(RBTreeNode node) {
        return node == null ? null : node.right;
    }

}
