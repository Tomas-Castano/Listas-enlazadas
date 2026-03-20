package org.example.lista_doblemente_enlazada;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaDoble implements Iterable<Integer> {

    private NodeDouble<Integer> first;
    private NodeDouble<Integer> last;
    private int size;

    public ListaDoble() {
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

    public void agregarInicio(int valor) {
        NodeDouble<Integer> nuevo = new NodeDouble<>(valor);
        if (estaVacia()) {
            first = nuevo;
            last  = nuevo;
        } else {
            nuevo.setNext(first);   // nuevo → antiguo first
            first.setPrev(nuevo);   // antiguo first ← nuevo
            first = nuevo;
        }
        size++;
    }

    public void agregarFinal(int valor) {
        NodeDouble<Integer> nuevo = new NodeDouble<>(valor);
        if (estaVacia()) {
            first = nuevo;
            last  = nuevo;
        } else {
            nuevo.setPrev(last);    // nuevo ← antiguo last
            last.setNext(nuevo);    // antiguo last → nuevo
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

        NodeDouble<Integer> actual;
        if (posicion <= size / 2) {
            actual = first;
            for (int i = 0; i < posicion; i++) actual = actual.getNext();
        } else {
            actual = last;
            for (int i = size - 1; i > posicion; i--) actual = actual.getPrev();
        }
        
        NodeDouble<Integer> nuevo = new NodeDouble<>(valor);
        NodeDouble<Integer> anterior = actual.getPrev();

        nuevo.setNext(actual);       // nuevo → actual
        nuevo.setPrev(anterior);     // anterior ← nuevo
        anterior.setNext(nuevo);     // anterior → nuevo
        actual.setPrev(nuevo);       // nuevo ← actual
        size++;
    }

    public int obtenerValorNodo(int index) {
        if (!indiceValido(index)) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }
        return getNodo(index).getData();
    }

    // Helper privado: obtiene el nodo en 'index' desde el extremo más cercano
    private NodeDouble<Integer> getNodo(int index) {
        NodeDouble<Integer> actual;
        if (index <= size / 2) {
            actual = first;
            for (int i = 0; i < index; i++) actual = actual.getNext();
        } else {
            actual = last;
            for (int i = size - 1; i > index; i--) actual = actual.getPrev();
        }
        return actual;
    }

    public int obtenerPosicionNodo(int valor) {
        NodeDouble<Integer> actual = first;
        int indice = 0;
        while (actual != null) {
            if (actual.getData() == valor) return indice;
            actual = actual.getNext();
            indice++;
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
            first.setPrev(null);    // el nuevo first ya no tiene anterior
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
            last = last.getPrev();
            last.setNext(null);
        }
        size--;
        return valor;
    }

    public boolean eliminar(int valor) {
        if (estaVacia()) return false;

        NodeDouble<Integer> actual = first;
        while (actual != null) {
            if (actual.getData() == valor) {
                if (actual == first) { eliminarPrimero(); return true; }
                if (actual == last)  { eliminarUltimo();  return true; }

                // Nodo intermedio: reconecta vecinos
                actual.getPrev().setNext(actual.getNext());
                actual.getNext().setPrev(actual.getPrev());
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
        getNodo(index).setData(nuevoValor);
    }

    public void ordenarLista() {
        if (size <= 1) return;

        boolean huboIntercambio;
        do {
            huboIntercambio = false;
            NodeDouble<Integer> actual = first;
            while (actual.getNext() != null) {
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
        NodeDouble<Integer> actual = first;
        while (actual != null) {
            sb.append(actual.getData());
            if (actual.getNext() != null) sb.append(" ⇌ ");
            actual = actual.getNext();
        }
        sb.append("]  (size=").append(size).append(")");
        System.out.println(sb);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private NodeDouble<Integer> actual = first;

            @Override
            public boolean hasNext() { return actual != null; }

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
        first = null;
        last  = null;
        size  = 0;
    }

    // Getters y Setters
    public NodeDouble<Integer> getFirst() { return first; }
    public void setFirst(NodeDouble<Integer> first) { this.first = first; }
    public NodeDouble<Integer> getLast() { return last; }
    public void setLast(NodeDouble<Integer> last) { this.last = last; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}