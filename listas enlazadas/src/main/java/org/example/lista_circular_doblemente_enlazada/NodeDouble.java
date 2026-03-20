package org.example.lista_circular_doblemente_enlazada;

public class NodeDouble<T> {
    private T data;
    private NodeDouble<T> next;
    private NodeDouble<T> prev;

    public NodeDouble(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public NodeDouble<T> getNext() { return next; }
    public void setNext(NodeDouble<T> next) { this.next = next; }

    public NodeDouble<T> getPrev() { return prev; }
    public void setPrev(NodeDouble<T> prev) { this.prev = prev; }
}