package implementations;

import java.io.Serializable;

/**
 * BSTreeNode.java
 *
 * A class representing a node in a Binary Search Tree (BST).
 * Each node contains an element and references to its left and right children.
 * 
 * @param <E> The type of element stored in this node, which must be comparable.
 */
public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private E element;               
    private BSTreeNode<E> left;      
    private BSTreeNode<E> right;     

    /**
     * Constructs a new BSTreeNode with the specified element.
     * Initializes the left and right children to null.
     * 
     * @param element The element to store in the node.
     */
    public BSTreeNode(E element) {
        this.element = element;
        this.left = null;
        this.right = null;
    }

    /**
     * Retrieves the element stored in the node.
     * 
     * @return The element stored in the node.
     */
    public E getElement() {
        return element;
    }

    /**
     * Sets the element stored in the node.
     * 
     * @param element The new element to store in the node.
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * Retrieves the left child of the node.
     * 
     * @return The left child node, or null if no left child exists.
     */
    public BSTreeNode<E> getLeft() {
        return left;
    }

    /**
     * Sets the left child of the node.
     * 
     * @param left The node to set as the left child.
     */
    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    /**
     * Retrieves the right child of the node.
     * 
     * @return The right child node, or null if no right child exists.
     */
    public BSTreeNode<E> getRight() {
        return right;
    }

    /**
     * Sets the right child of the node.
     * 
     * @param right The node to set as the right child.
     */
    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }
}
