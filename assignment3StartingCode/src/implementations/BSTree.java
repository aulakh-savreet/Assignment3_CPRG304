package implementations;

import java.io.Serializable;
import java.util.NoSuchElementException;

import utilities.BSTreeADT;
import utilities.Iterator;

/**
 * A Binary Search Tree implementation of the BSTreeADT.
 * 
 * @param <E> The type of elements this tree will store.
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;

    private BSTreeNode<E> root;
    private int size;

    public BSTree() {
        root = null;
        size = 0;
    }

    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (isEmpty())
            throw new NullPointerException("Tree is empty, no root node.");
        return root;
    }

    @Override
    public int getHeight() {
        return getHeightRec(root);
    }

    private int getHeightRec(BSTreeNode<E> node) {
        if (node == null)
            return 0;
        int leftH = getHeightRec(node.getLeft());
        int rightH = getHeightRec(node.getRight());
        return Math.max(leftH, rightH) + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) {
            throw new NullPointerException("Entry is null");
        }
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null)
            throw new NullPointerException("Entry is null");
        return searchRec(root, entry);
    }

    private BSTreeNode<E> searchRec(BSTreeNode<E> node, E entry) {
        if (node == null)
            return null;
        int cmp = entry.compareTo(node.getElement());
        if (cmp == 0)
            return node;
        else if (cmp < 0)
            return searchRec(node.getLeft(), entry);
        else
            return searchRec(node.getRight(), entry);
    }

    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null)
            throw new NullPointerException("newEntry is null");

        // Check if already contains the element
        if (contains(newEntry)) {
            return false;
        }

        root = addRec(root, newEntry);
        size++;
        return true;
    }

    private BSTreeNode<E> addRec(BSTreeNode<E> node, E newEntry) {
        if (node == null) {
            node = new BSTreeNode<>(newEntry);
            return node;
        }

        int cmp = newEntry.compareTo(node.getElement());
        if (cmp < 0) {
            node.setLeft(addRec(node.getLeft(), newEntry));
        } else if (cmp > 0) {
            node.setRight(addRec(node.getRight(), newEntry));
        }
        // Equal case handled above in contains check (no duplicates added)
        return node;
    }

    @Override
    public BSTreeNode<E> removeMin() {
        if (isEmpty()) {
            return null;
        }
        BSTreeNode<E> minNode = getMinNode(root);
        root = removeMinRec(root);
        size--;
        return minNode;
    }

    private BSTreeNode<E> removeMinRec(BSTreeNode<E> node) {
        if (node == null) {
            return null;
        }
        if (node.getLeft() == null) {
            return node.getRight();
        }
        node.setLeft(removeMinRec(node.getLeft()));
        return node;
    }

    private BSTreeNode<E> getMinNode(BSTreeNode<E> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    @Override
    public BSTreeNode<E> removeMax() {
        if (isEmpty()) {
            return null;
        }
        BSTreeNode<E> maxNode = getMaxNode(root);
        root = removeMaxRec(root);
        size--;
        return maxNode;
    }

    private BSTreeNode<E> removeMaxRec(BSTreeNode<E> node) {
        if (node == null) {
            return null;
        }
        if (node.getRight() == null) {
            return node.getLeft();
        }
        node.setRight(removeMaxRec(node.getRight()));
        return node;
    }

    private BSTreeNode<E> getMaxNode(BSTreeNode<E> node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    @Override
    public Iterator<E> inorderIterator() {
        return new InorderIterator();
    }

    @Override
    public Iterator<E> preorderIterator() {
        return new PreorderIterator();
    }

    @Override
    public Iterator<E> postorderIterator() {
        return new PostorderIterator();
    }

    private class InorderIterator implements Iterator<E> {
        private E[] elements;
        private int current;
        @SuppressWarnings("unchecked")
        public InorderIterator() {
            elements = (E[]) new Comparable[size];
            current = 0;
            fillInOrder(root);
            current = 0;
        }

        private int index = 0;
        private void fillInOrder(BSTreeNode<E> node) {
            if (node == null) return;
            fillInOrder(node.getLeft());
            elements[index++] = node.getElement();
            fillInOrder(node.getRight());
        }

        @Override
        public boolean hasNext() {
            return current < elements.length;
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException();
            return elements[current++];
        }
    }

    private class PreorderIterator implements Iterator<E> {
        private E[] elements;
        private int current;
        @SuppressWarnings("unchecked")
        public PreorderIterator() {
            elements = (E[]) new Comparable[size];
            current = 0;
            fillPreOrder(root);
            current = 0;
        }

        private int index = 0;
        private void fillPreOrder(BSTreeNode<E> node) {
            if (node == null) return;
            elements[index++] = node.getElement();
            fillPreOrder(node.getLeft());
            fillPreOrder(node.getRight());
        }

        @Override
        public boolean hasNext() {
            return current < elements.length;
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException();
            return elements[current++];
        }
    }

    private class PostorderIterator implements Iterator<E> {
        private E[] elements;
        private int current;
        @SuppressWarnings("unchecked")
        public PostorderIterator() {
            elements = (E[]) new Comparable[size];
            current = 0;
            fillPostOrder(root);
            current = 0;
        }

        private int index = 0;
        private void fillPostOrder(BSTreeNode<E> node) {
            if (node == null) return;
            fillPostOrder(node.getLeft());
            fillPostOrder(node.getRight());
            elements[index++] = node.getElement();
        }

        @Override
        public boolean hasNext() {
            return current < elements.length;
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException();
            return elements[current++];
        }
    }
}
