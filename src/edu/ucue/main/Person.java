/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucue.main;

import java.io.Serializable;

/**
 *
 * @author stsewd
 */
public class Person implements Serializable {
    public String name;    
    public String lastName;
    public int old;

    public Person(String name, String lastName, int old) {
        this.name = name;
        this.lastName = lastName;
        this.old = old;
    }

    @Override
    public String toString() {
        return name + " " + lastName + " " + old;
    }
}
