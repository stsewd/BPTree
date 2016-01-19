/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucue.bptree;

import java.util.Comparator;

/**
 * Clase para especificar que valor tomar de indice de un objeto dado,
 * util para especificar claves secundarias del arbol B+.
 * @author Santos Gallegos
 * @param <E> Tipo de objeto
 * @param <V> Tipo de dato del objeto que va a ser indexado.
 */
public interface IndexGenerator<E, V> {
    public V getKey(E obj);
    public Comparator<V> getComparator();
}
