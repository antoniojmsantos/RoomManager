package shared_data.entities;

import shared_data.helper.Pair;

import java.io.Serializable;
import java.util.*;

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
}