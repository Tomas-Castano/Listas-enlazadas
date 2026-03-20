package org.example.lista_circular_simplemente_enlazada;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaCircular implements Iterable<Integer> {

    private Node<Integer> first;
    private Node<Integer> last;
    private int size;

    public ListaCircular() {
        this.first = null;
        this.last  = null;
        this.size  = 0;
    }

    public boolean estaVacia() {
        return first == null;
    }

    private boolean indiceValido(int index) {
        return index >= 0 && index < size;
    }

    // Helper: enlaza un único nodo consigo mismo
    private void enlazarUnico(Node<Integer> nodo) {
        nodo.setNext(nodo);
        first = nodo;
        last  = nodo;
    }

    public void agregarInicio(int valor) {
        Node<Integer> nuevo = new Node<>(valor);
        if (estaVacia()) {
            enlazarUnico(nuevo);
        } else {
            nuevo.setNext(first);  
            last.setNext(nuevo);    
            first = nuevo;
        }
        size++;
    }

    public void agregarFinal(int valor) {
        Node<Integer> nuevo = new Node<>(valor);
        if (estaVacia()) {
            enlazarUnico(nuevo);
        } else {
            nuevo.setNext(first);   
            last.setNext(nuevo);    
            last = nuevo;
        }
        size++;
    }

    public void agregar(int valor, int posicion) {
        if (posicion < 0 || posicion > size) {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }
        if (posicion == 0)    { agregarInicio(valor); return; }
        if (posicion == size) { agregarFinal(valor);  return; }

        Node<Integer> actual = first;
        for (int i = 0; i < posicion - 1; i++) {
            actual = actual.getNext();
        }
        Node<Integer> nuevo = new Node<>(valor);
        nuevo.setNext(actual.getNext());
        actual.setNext(nuevo);
        size++;
    }

    public int obtenerValorNodo(int index) {
        if (!indiceValido(index)) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }
        Node<Integer> actual = first;
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }
        return actual.getData();
    }

    public int obtenerPosicionNodo(int valor) {
        if (estaVacia()) return -1;

        Node<Integer> actual = first;
        for (int i = 0; i < size; i++) {
            if (actual.getData() == valor) return i;
            actual = actual.getNext();
        }
        return -1;
    }

    public int eliminarPrimero() {
        if (estaVacia()) throw new NoSuchElementException("La lista está vacía.");

        int valor = first.getData();
        if (size == 1) {
            first = null;
            last  = null;
        } else {
            first = first.getNext();
            last.setNext(first);
        }
        size--;
        return valor;
    }

    public int eliminarUltimo() {
        if (estaVacia()) throw new NoSuchElementException("La lista está vacía.");

        int valor = last.getData();
        if (size == 1) {
            first = null;
            last  = null;
        } else {
            Node<Integer> actual = first;
            while (actual.getNext() != last) {
                actual = actual.getNext();
            }
            actual.setNext(first);
            last = actual;
        }
        size--;
        return valor;
    }

    public boolean eliminar(int valor) {
        if (estaVacia()) return false;

        if (first.getData() == valor) { eliminarPrimero(); return true; }
        if (last.getData()  == valor) { eliminarUltimo();  return true; }

        Node<Integer> actual = first;
        for (int i = 0; i < size - 1; i++) {
            if (actual.getNext().getData() == valor) {
                actual.setNext(actual.getNext().getNext());
                size--;
                return true;
            }
            actual = actual.getNext();
        }
        return false;
    }

    public void modificarNodo(int index, int nuevoValor) {
        if (!indiceValido(index)) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }
        Node<Integer> actual = first;
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }
        actual.setData(nuevoValor);
    }

    public void ordenarLista() {
        if (size <= 1) return;

        boolean huboIntercambio;
        do {
            huboIntercambio = false;
            Node<Integer> actual = first;
            for (int i = 0; i < size - 1; i++) {   // exactamente size-1 comparaciones
                if (actual.getData() > actual.getNext().getData()) {
                    int temp = actual.getData();
                    actual.setData(actual.getNext().getData());
                    actual.getNext().setData(temp);
                    huboIntercambio = true;
                }
                actual = actual.getNext();
            }
        } while (huboIntercambio);
    }

    public void imprimirLista() {
        if (estaVacia()) { System.out.println("La lista está vacía."); return; }

        StringBuilder sb = new StringBuilder("[");
        Node<Integer> actual = first;
        for (int i = 0; i < size; i++) {
            sb.append(actual.getData());
            if (i < size - 1) sb.append(" → ");
            actual = actual.getNext();
        }
        sb.append(" → ...first]  (size=").append(size).append(")");
        System.out.println(sb);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private Node<Integer> actual = first;
            private int recorridos = 0;           // cuenta para no ciclar infinito

            @Override
            public boolean hasNext() {
                return recorridos < size;
            }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                int valor = actual.getData();
                actual = actual.getNext();
                recorridos++;
                return valor;
            }
        };
    }

    public void borrarLista() {
        first = null;
        last  = null;
        size  = 0;
    }

    public Node<Integer> getFirst() { return first; }
    public void setFirst(Node<Integer> first) { this.first = first; }
    public Node<Integer> getLast() { return last; }
    public void setLast(Node<Integer> last) { this.last = last; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}