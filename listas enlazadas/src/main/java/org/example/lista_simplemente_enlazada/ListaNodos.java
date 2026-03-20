package org.example.lista_simplemente_enlazada;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaNodos implements Iterable<Integer> {
    private Node<Integer> first;
    private int size;

    public ListaNodos() {
        this.first = null;
        this.size = 0;
    }

    public Node<Integer> getFirst() {
        return first;
    }

    public void setFirst(Node<Integer> first) {
        this.first = first;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean estaVacia() {
        return first == null;
    }
    
    private boolean indiceValido(int index) {
        return index >= 0 && index < size;
    }

    public void agregarInicio(int valor) {
        Node<Integer> nuevo = new Node<>(valor);
        nuevo.setNext(first);   // el nuevo apunta al antiguo primero
        first = nuevo;          // first ahora es el nuevo nodo
        size++;
    }

    public void agregarFinal(int valor) {
        Node<Integer> nuevo = new Node<>(valor);
        if (estaVacia()) {
            first = nuevo;
        } else {
            Node<Integer> actual = first;
            while (actual.getNext() != null) {  // recorre hasta el último
                actual = actual.getNext();
            }
            actual.setNext(nuevo);              // el último apunta al nuevo
        }
        size++;
    }

    public void agregar(int valor, int posicion) {
        if (posicion < 0 || posicion > size) {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }
        if (posicion == 0) {
            agregarInicio(valor);
            return;
        }
        if (posicion == size) {
            agregarFinal(valor);
            return;
        }
        // Caso intermedio: busca el nodo anterior a la posición
        Node<Integer> actual = first;
        for (int i = 0; i < posicion - 1; i++) {
            actual = actual.getNext();
        }
        Node<Integer> nuevo = new Node<>(valor);
        nuevo.setNext(actual.getNext());  // nuevo → el que estaba en posicion
        actual.setNext(nuevo);            // anterior → nuevo
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
        Node<Integer> actual = first;
        int indice = 0;
        while (actual != null) {
            if (actual.getData() == valor) {
                return indice;
            }
            actual = actual.getNext();
            indice++;
        }
        return -1;
    }

    public int eliminarPrimero() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        int valor = first.getData();
        first = first.getNext();  // first avanza al siguiente
        size--;
        return valor;
    }

    public int eliminarUltimo() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        if (size == 1) {           // caso especial: un solo nodo
            return eliminarPrimero();
        }
        Node<Integer> actual = first;
        while (actual.getNext().getNext() != null) {  // para en el penúltimo
            actual = actual.getNext();
        }
        int valor = actual.getNext().getData();
        actual.setNext(null);   // el penúltimo ahora es el último
        size--;
        return valor;
    }

    public boolean eliminar(int valor) {
        if (estaVacia()) return false;

        if (first.getData() == valor) {   // está en el primero
            eliminarPrimero();
            return true;
        }
        Node<Integer> actual = first;
        while (actual.getNext() != null) {
            if (actual.getNext().getData() == valor) {
                actual.setNext(actual.getNext().getNext()); // "salta" el nodo
                size--;
                return true;
            }
            actual = actual.getNext();
        }
        return false;  // no se encontró
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
            while (actual.getNext() != null) {
                if (actual.getData() > actual.getNext().getData()) {
                    // Intercambia los datos (no los nodos)
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
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return;
        }
        StringBuilder sb = new StringBuilder("[");
        Node<Integer> actual = first;
        while (actual != null) {
            sb.append(actual.getData());
            if (actual.getNext() != null) sb.append(" → ");
            actual = actual.getNext();
        }
        sb.append("]  (size=").append(size).append(")");
        System.out.println(sb);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private Node<Integer> actual = first;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                int valor = actual.getData();
                actual = actual.getNext();
                return valor;
            }
        };
    }

    public void borrarLista() {
        first = null;   // el GC libera todos los nodos en cadena
        size = 0;
    }
}
