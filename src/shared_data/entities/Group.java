package shared_data.entities;

import shared_data.helper.Pair;

import java.io.Serializable;
import java.util.*;

/*
 *  A classe Group contém toda a informação
 *  que permite ao programa criar objetos do
 *  tipo Group.
 *  É constituída por funções get/set para a variavel name
 *  assim coomo um construtor.
 *  A função make recebe os argumentos necessários para
 *  e criar e retornar um objeto Group.
 * */
public class Group implements Serializable {

    private String name;

    // constructor
    public Group(String name) {
        this.name = name;
    }

    // getter & setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Group make(String name) {return new Group(name);}

    @Override
    public String toString() {
        return name;
    }
}