package shared_data.entities;

import shared_data.helper.Pair;

import java.util.*;

public class Group {

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