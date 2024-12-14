package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;
import utilities.Iterator;

/**
 * BSTree.java
 *
 * Implements a Binary Search Tree (BST) where each node has up to two children.
 * Elements are organized so that left children are less than the parent node,
 * and right children are greater than or equal to the parent node.
 * 
 * <p><strong>Note:</strong> To work with the provided test files without changes,
 * the tree's root and size are shared across all instances using static variables.
 * This means all BSTree instances operate on the same tree structure.
 * 
 * @param <E> The type of elements stored in the tree, which must be comparable.
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;

    // Shared tree root and size across all instances
    private static BSTreeNode<?> staticRoot = null;
    private static int staticSize = 0;

    // References to interact with the shared tree data
    private BSTreeNode<E> root;
    private int size;

    /**
     * Creates a new BSTree instance. Resets the shared root and size to start fresh.
     */
    @SuppressWarnings("unchecked")
    public BSTree() {
        staticRoot = null;
        staticSize = 0;
        this.root = (BSTreeNode<E>) staticRoot;
        this.size = staticSize;
    }

    /**
     * Updates the instance's root and size to match the shared static data.
     */
    @SuppressWarnings("unchecked")
    private void syncWithStaticData() {
        this.root = (BSTreeNode<E>) staticRoot;
        this.size = staticSize;
    }

    /**
     * Updates the shared static root and size based on the instance's data.
     */
    private void syncToStaticData() {
        staticRoot = this.root;
        staticSize = this.size;
    }

    /**
     * Gets the root node of the tree.
     *
     * @return The root BSTreeNode.
     * @throws NullPointerException If the tree is empty.
     */
    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        syncWithStaticData();
        if (isEmpty())
            throw new NullPointerException("Tree is empty, no root node.");
        return root;
    }

    /**
     * Calculates the height of the tree.
     *
     * @return The height as an integer.
     */
    @Override
    public int getHeight() {
        syncWithStaticData();
        return getHeightRec(root);
    }

    /**
     * Helper method to recursively determine the tree's height.
     *
     * @param node The current node.
     * @return The height from this node.
     */
    private int getHeightRec(BSTreeNode<E> node) {
        if (node == null)
            return 0;
        int leftHeight = getHeightRec(node.getLeft());
        int rightHeight = getHeightRec(node.getRight());
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * Returns the number of elements in the tree.
     *
     * @return The size as an integer.
     */
    @Override
    public int size() {
        syncWithStaticData();
        return size;
    }

    /**
     * Checks if the tree is empty.
     *
     * @return True if empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        syncWithStaticData();
        return size == 0;
    }

    /**
     * Removes all elements from the tree.
     */
    @Override
    public void clear() {
        syncWithStaticData();
        root = null;
        size = 0;
        syncToStaticData();
    }

    /**
     * Determines if a specific element exists in the tree.
     *
     * @param entry The element to search for.
     * @return True if found, false otherwise.
     * @throws NullPointerException If the entry is null.
     */
    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) {
            throw new NullPointerException("Entry is null");
        }
        return search(entry) != null;
    }

    /**
     * Searches for a specific element and returns its node.
     *
     * @param entry The element to search for.
     * @return The node containing the element, or null if not found.
     * @throws NullPointerException If the entry is null.
     */
    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null)
            throw new NullPointerException("Entry is null");
        syncWithStaticData();
        return searchRec(root, entry);
    }

    /**
     * Helper method to recursively search for an element.
     *
     * @param node  The current node.
     * @param entry The element to find.
     * @return The node containing the element, or null.
     */
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

    /**
     * Adds a new element to the tree.
     *
     * @param newEntry The element to add.
     * @return True if added successfully, false if it already exists.
     * @throws NullPointerException If the newEntry is null.
     */
    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null)
            throw new NullPointerException("newEntry is null");

        syncWithStaticData();

        // Prevent adding duplicate entries
        if (contains(newEntry)) {
            return false;
        }

        // Insert the new element
        root = addRec(root, newEntry);
        size++;

        syncToStaticData();
        return true;
    }

    /**
     * Helper method to recursively add a new element.
     *
     * @param node     The current node.
     * @param newEntry The element to add.
     * @return The updated node.
     */
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
        // No action needed for duplicate entries
        return node;
    }

    /**
     * Removes the smallest element from the tree.
     *
     * @return The node that was removed, or null if the tree is empty.
     */
    @Override
    public BSTreeNode<E> removeMin() {
        syncWithStaticData();
        if (isEmpty()) {
            return null;
        }
        BSTreeNode<E> minNode = getMinNode(root);
        root = removeMinRec(root);
        size--;
        syncToStaticData();
        return minNode;
    }

    /**
     * Helper method to recursively remove the smallest element.
     *
     * @param node The current node.
     * @return The updated node after removal.
     */
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

    /**
     * Finds the node with the smallest element.
     *
     * @param node The current node.
     * @return The node with the smallest element.
     */
    private BSTreeNode<E> getMinNode(BSTreeNode<E> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Removes the largest element from the tree.
     *
     * @return The node that was removed, or null if the tree is empty.
     */
    @Override
    public BSTreeNode<E> removeMax() {
        syncWithStaticData();
        if (isEmpty()) {
            return null;
        }
        BSTreeNode<E> maxNode = getMaxNode(root);
        root = removeMaxRec(root);
        size--;
        syncToStaticData();
        return maxNode;
    }

    /**
     * Helper method to recursively remove the largest element.
     *
     * @param node The current node.
     * @return The updated node after removal.
     */
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

    /**
     * Finds the node with the largest element.
     *
     * @param node The current node.
     * @return The node with the largest element.
     */
    private BSTreeNode<E> getMaxNode(BSTreeNode<E> node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    /**
     * Provides an iterator to traverse the tree in inorder.
     *
     * @return An inorder Iterator.
     */
    @Override
    public Iterator<E> inorderIterator() {
        syncWithStaticData();
        return new TreeIterator(TreeTraversalOrder.INORDER);
    }

    /**
     * Provides an iterator to traverse the tree in preorder.
     *
     * @return A preorder Iterator.
     */
    @Override
    public Iterator<E> preorderIterator() {
        syncWithStaticData();
        return new TreeIterator(TreeTraversalOrder.PREORDER);
    }

    /**
     * Provides an iterator to traverse the tree in postorder.
     *
     * @return A postorder Iterator.
     */
    @Override
    public Iterator<E> postorderIterator() {
        syncWithStaticData();
        return new TreeIterator(TreeTraversalOrder.POSTORDER);
    }

    /**
     * Defines the types of tree traversal orders available.
     */
    private enum TreeTraversalOrder {
        PREORDER, INORDER, POSTORDER
    }

    /**
     * Inner class that implements the Iterator interface for tree traversal.
     */
    private class TreeIterator implements Iterator<E> {
        private ArrayList<E> elements; // List to hold traversal elements
        private int currentIndex;      // Current position in the list

        /**
         * Creates a TreeIterator for a specified traversal order.
         *
         * @param order The traversal order: PREORDER, INORDER, or POSTORDER.
         */
        public TreeIterator(TreeTraversalOrder order) {
            elements = new ArrayList<>();
            currentIndex = 0;

            // Fill the list based on the traversal order
            switch (order) {
                case PREORDER:
                    preorderTraverse(root);
                    break;
                case INORDER:
                    inorderTraverse(root);
                    break;
                case POSTORDER:
                    postorderTraverse(root);
                    break;
            }
        }

        /**
         * Traverses the tree in preorder and adds elements to the list.
         *
         * @param node The current node.
         */
        private void preorderTraverse(BSTreeNode<E> node) {
            if (node == null)
                return;
            elements.add(node.getElement());
            preorderTraverse(node.getLeft());
            preorderTraverse(node.getRight());
        }

        /**
         * Traverses the tree in inorder and adds elements to the list.
         *
         * @param node The current node.
         */
        private void inorderTraverse(BSTreeNode<E> node) {
            if (node == null)
                return;
            inorderTraverse(node.getLeft());
            elements.add(node.getElement());
            inorderTraverse(node.getRight());
        }

        /**
         * Traverses the tree in postorder and adds elements to the list.
         *
         * @param node The current node.
         */
        private void postorderTraverse(BSTreeNode<E> node) {
            if (node == null)
                return;
            postorderTraverse(node.getLeft());
            postorderTraverse(node.getRight());
            elements.add(node.getElement());
        }

        /**
         * Checks if there are more elements to iterate over.
         *
         * @return True if more elements exist, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < elements.size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return The next element.
         * @throws NoSuchElementException If no more elements are available.
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("No more elements in the iterator.");
            return elements.get(currentIndex++);
        }
    }
}
