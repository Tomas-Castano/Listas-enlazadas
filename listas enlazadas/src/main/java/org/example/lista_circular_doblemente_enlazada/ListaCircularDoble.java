package org.example.lista_circular_doblemente_enlazada;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaCircularDoble implements Iterable<Integer> {

    private NodeDouble<Integer> first;
    private NodeDouble<Integer> last;
    private int size;

    public ListaCircularDoble() {
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
    private void enlazarUnico(NodeDouble<Integer> nodo) {
        nodo.setNext(nodo);
        nodo.setPrev(nodo);
        first = nodo;
        last  = nodo;
    }

    // Helper: busca el nodo en 'index' desde el extremo más cercano
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

    public void agregarInicio(int valor) {
        NodeDouble<Integer> nuevo = new NodeDouble<>(valor);
        if (estaVacia()) {
            enlazarUnico(nuevo);
        } else {
            nuevo.setNext(first);   
            nuevo.setPrev(last);     
            first.setPrev(nuevo);    
            last.setNext(nuevo);     
            first = nuevo;
        }
        size++;
    }

    public void agregarFinal(int valor) {
        NodeDouble<Integer> nuevo = new NodeDouble<>(valor);
        if (estaVacia()) {
            enlazarUnico(nuevo);
        } else {
            nuevo.setNext(first);  
            nuevo.setPrev(last);    
            last.setNext(nuevo);    
            first.setPrev(nuevo);    
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

        NodeDouble<Integer> actual = getNodo(posicion);
        NodeDouble<Integer> anterior = actual.getPrev();
        NodeDouble<Integer> nuevo = new NodeDouble<>(valor);

        nuevo.setNext(actual);     
        nuevo.setPrev(anterior);   
        anterior.setNext(nuevo);    
        actual.setPrev(nuevo);      
        size++;
    }

    public int obtenerValorNodo(int index) {
        if (!indiceValido(index)) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }
        return getNodo(index).getData();
    }

    public int obtenerPosicionNodo(int valor) {
        if (estaVacia()) return -1;

        NodeDouble<Integer> actual = first;
        for (int i = 0; i < size; i++) {    // exactamente 'size' pasos
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
            first.setPrev(last);       
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
            last = last.getPrev();  
            last.setNext(first); 
            first.setPrev(last);      
        }
        size--;
        return valor;
    }

    public boolean eliminar(int valor) {
        if (estaVacia()) return false;

        if (first.getData() == valor) { eliminarPrimero(); return true; }
        if (last.getData()  == valor) { eliminarUltimo();  return true; }

        NodeDouble<Integer> actual = first.getNext();
        for (int i = 1; i < size - 1; i++) {
            if (actual.getData() == valor) {
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
            for (int i = 0; i < size - 1; i++) {
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
        for (int i = 0; i < size; i++) {
            sb.append(actual.getData());
            if (i < size - 1) sb.append(" ⇌ ");
            actual = actual.getNext();
        }
        sb.append(" ⇌ ...first]  (size=").append(size).append(")");
        System.out.println(sb);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private NodeDouble<Integer> actual = first;
            private int recorridos = 0;

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

    public NodeDouble<Integer> getFirst() { return first; }
    public void setFirst(NodeDouble<Integer> first) { this.first = first; }
    public NodeDouble<Integer> getLast() { return last; }
    public void setLast(NodeDouble<Integer> last) { this.last = last; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}