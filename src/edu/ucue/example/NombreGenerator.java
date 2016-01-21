package edu.ucue.example;

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
