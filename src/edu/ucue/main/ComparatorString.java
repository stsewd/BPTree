/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucue.main;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Santos Gallegos
 */
class ComparatorString implements Comparator<String>, Serializable {

    @Override
    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }   
}
