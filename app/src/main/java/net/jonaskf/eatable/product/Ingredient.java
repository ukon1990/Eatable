package net.jonaskf.eatable.product;

import java.util.HashMap;

/**
 * Created by jonas on 22.02.2016.
 */
public class Ingredient {
    private String name;
    private String comment;
    private String sourceID;
    private String typeID;
    private String allergenID;

    public Ingredient(String name, String comment, String sourceID, String typeID) {
        this.name = name;
        this.comment = comment;
        this.sourceID = sourceID;
        this.typeID = typeID;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getSourceID() {
        return sourceID;
    }

    public String getTypeID() {
        return typeID;
    }
}
