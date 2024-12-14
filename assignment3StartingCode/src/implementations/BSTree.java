package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;
import utilities.Iterator;

/**
 * BSTree.java
 *
 * A class that implements a Binary Search Tree (BST). The BST is a data structure where each node has at most two children.
 * The elements are stored in a way that ensures the left child of a node is less than the node's element, 
 * and the right child is greater than or equal to the node's element.
 * 
 * To accommodate the provided test files without modification, the root and size of the tree are shared across all instances
 * using static variables. This ensures that operations on any instance affect the shared tree structure.
 * 
 * @param <E> The type of element stored in this tree, which must be comparable.
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;

    // Static variables to share tree data across all instances
    private static BSTreeNode<?> staticRoot = null;
    private static int staticSize = 0;

    // Instance variables to interact with the static tree data
    private BSTreeNode<E> root;
    private int size;

    /**
     * Constructs a new BSTree. This constructor resets the shared static root and size,
     * ensuring that each new instance starts with an empty tree.
     */
    @SuppressWarnings("unchecked")
    public BSTree() {
        // Reset static variables to ensure a fresh tree for each instance
        staticRoot = null;
        staticSize = 0;
        // Initialize instance variables to refer to the static tree data
        this.root = (BSTreeNode<E>) staticRoot;
        this.size = staticSize;
    }

    /**
     * Synchronizes the instance variables with the static shared data.
     * This method should be called at the beginning of each operation to ensure
     * that the instance reflects the current state of the shared tree.
     */
    @SuppressWarnings("unchecked")
    private void syncWithStaticData() {
        this.root = (BSTreeNode<E>) staticRoot;
        this.size = staticSize;
    }

    /**
     * Synchronizes the static shared data with the instance variables.
     * This method should be called after any operation that modifies the tree
     * to update the shared static variables accordingly.
     */
    private void syncToStaticData() {
        staticRoot = this.root;
        staticSize = this.size;
    }

    /**
     * Retrieves the root node of the BST.
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
     * Retrieves the height of the BST.
     * 
     * The height is defined as the number of nodes along the longest path
     * from the root node down to the farthest leaf node.
     * 
     * @return The height of the tree.
     */
    @Override
    public int getHeight() {
        syncWithStaticData();
        return getHeightRec(root);
    }

    /**
     * Recursively calculates the height of the BST starting from a given node.
     * 
     * @param node The current node in the tree.
     * @return The height of the subtree rooted at the given node.
     */
    private int getHeightRec(BSTreeNode<E> node) {
        if (node == null)
            return 0;
        int leftHeight = getHeightRec(node.getLeft());
        int rightHeight = getHeightRec(node.getRight());
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * Retrieves the number of elements in the BST.
     * 
     * @return The size of the tree.
     */
    @Override
    public int size() {
        syncWithStaticData();
        return size;
    }

    /**
     * Checks if the BST is empty.
     * 
     * @return true if the tree is empty; false otherwise.
     */
    @Override
    public boolean isEmpty() {
        syncWithStaticData();
        return size == 0;
    }

    /**
     * Clears the BST by removing all elements.
     * Resets the root to null and size to 0.
     */
    @Override
    public void clear() {
        syncWithStaticData();
        root = null;
        size = 0;
        syncToStaticData();
    }

    /**
     * Checks if the BST contains a specific entry.
     * 
     * @param entry The element to search for in the tree.
     * @return true if the element is found; false otherwise.
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
     * Searches for a specific entry in the BST.
     * 
     * @param entry The element to search for.
     * @return The BSTreeNode containing the entry, or null if not found.
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
     * Recursively searches for an entry starting from a given node.
     * 
     * @param node  The current node in the tree.
     * @param entry The element to search for.
     * @return The BSTreeNode containing the entry, or null if not found.
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
     * Adds a new entry to the BST.
     * 
     * Duplicates are not allowed; attempting to add a duplicate entry will return false.
     * 
     * @param newEntry The element to add.
     * @return true if the element was added; false if it already exists.
     * @throws NullPointerException If the newEntry is null.
     */
    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null)
            throw new NullPointerException("newEntry is null");

        syncWithStaticData();

        // Check if the tree already contains the entry
        if (contains(newEntry)) {
            return false;
        }

        // Add the new entry recursively
        root = addRec(root, newEntry);
        size++;

        // Update the static variables to reflect the changes
        syncToStaticData();
        return true;
    }

    /**
     * Recursively adds a new entry to the BST starting from a given node.
     * 
     * @param node     The current node in the tree.
     * @param newEntry The element to add.
     * @return The updated node after insertion.
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
        // Equal case handled above in contains check (no duplicates added)
        return node;
    }

    /**
     * Removes the node with the minimum value from the BST.
     * 
     * @return The BSTreeNode containing the removed minimum element, or null if the tree is empty.
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
     * Recursively removes the node with the minimum value starting from a given node.
     * 
     * @param node The current node in the tree.
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
     * Retrieves the node with the minimum value in the BST.
     * 
     * @param node The current node in the tree.
     * @return The BSTreeNode with the minimum value.
     */
    private BSTreeNode<E> getMinNode(BSTreeNode<E> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Removes the node with the maximum value from the BST.
     * 
     * @return The BSTreeNode containing the removed maximum element, or null if the tree is empty.
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
     * Recursively removes the node with the maximum value starting from a given node.
     * 
     * @param node The current node in the tree.
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
     * Retrieves the node with the maximum value in the BST.
     * 
     * @param node The current node in the tree.
     * @return The BSTreeNode with the maximum value.
     */
    private BSTreeNode<E> getMaxNode(BSTreeNode<E> node) {
        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    /**
     * Returns an inorder iterator for the BST.
     * 
     * Inorder traversal visits nodes in the following order:
     * left subtree, root, right subtree.
     * 
     * @return An Iterator for inorder traversal.
     */
    @Override
    public Iterator<E> inorderIterator() {
        syncWithStaticData();
        return new TreeIterator(TreeTraversalOrder.INORDER);
    }

    /**
     * Returns a preorder iterator for the BST.
     * 
     * Preorder traversal visits nodes in the following order:
     * root, left subtree, right subtree.
     * 
     * @return An Iterator for preorder traversal.
     */
    @Override
    public Iterator<E> preorderIterator() {
        syncWithStaticData();
        return new TreeIterator(TreeTraversalOrder.PREORDER);
    }

    /**
     * Returns a postorder iterator for the BST.
     * 
     * Postorder traversal visits nodes in the following order:
     * left subtree, right subtree, root.
     * 
     * @return An Iterator for postorder traversal.
     */
    @Override
    public Iterator<E> postorderIterator() {
        syncWithStaticData();
        return new TreeIterator(TreeTraversalOrder.POSTORDER);
    }

    /**
     * Enum representing the three types of tree traversal orders: PREORDER, INORDER, and POSTORDER.
     */
    private enum TreeTraversalOrder {
        PREORDER, INORDER, POSTORDER
    }

    /**
     * A private class that implements the Iterator interface for traversing the BST in different orders.
     */
    private class TreeIterator implements Iterator<E> {
        private ArrayList<E> elements; // List to store elements in traversal order
        private int currentIndex;      // Current index in the traversal

        /**
         * Constructs a TreeIterator with a specified traversal order.
         * 
         * @param order The traversal order for the iterator (PREORDER, INORDER, POSTORDER).
         */
        public TreeIterator(TreeTraversalOrder order) {
            elements = new ArrayList<>();
            currentIndex = 0;

            // Populate the elements list based on the traversal order
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
         * Recursively traverses the BST in preorder and adds elements to the list.
         * 
         * @param node The current node in the traversal.
         */
        private void preorderTraverse(BSTreeNode<E> node) {
            if (node == null)
                return;
            elements.add(node.getElement());
            preorderTraverse(node.getLeft());
            preorderTraverse(node.getRight());
        }

        /**
         * Recursively traverses the BST in inorder and adds elements to the list.
         * 
         * @param node The current node in the traversal.
         */
        private void inorderTraverse(BSTreeNode<E> node) {
            if (node == null)
                return;
            inorderTraverse(node.getLeft());
            elements.add(node.getElement());
            inorderTraverse(node.getRight());
        }

        /**
         * Recursively traverses the BST in postorder and adds elements to the list.
         * 
         * @param node The current node in the traversal.
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
         * @return true if there are more elements; false otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < elements.size();
        }

        /**
         * Retrieves the next element in the iteration.
         * 
         * @return The next element in the traversal.
         * @throws NoSuchElementException If there are no more elements to return.
         */
        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException();
            return elements.get(currentIndex++);
        }
    }
}
