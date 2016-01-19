/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucue.main;

import edu.ucue.bptree.IndexGenerator;
import java.util.Comparator;

/**
 *
 * @author stsewd
 */
public class NombreGenerator implements IndexGenerator<Person, String>{

    @Override
    public String getKey(Person obj) {
        return obj.name + obj.lastName;
    }

    @Override
    public Comparator<String> getComparator() {
        return new ComparatorString();
    }
}
