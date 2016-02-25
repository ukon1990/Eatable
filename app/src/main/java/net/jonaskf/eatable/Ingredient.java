package net.jonaskf.eatable;

import java.util.HashMap;

/**
 * Created by jonas on 22.02.2016.
 */
public class Ingredient {
    private String name;
    private String comment;
    private String sourceID;
    private String typeID;

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

    //Class for ingredient source
    public class Source{
        private String source;
        public HashMap<String, Source> sources = new HashMap<>();

        public Source(String source) {
            this.source = source;
        }

        public String getSource() {
            return source;
        }
    }

    //Class for ingredient type
    public class Type{
        private String ingredientType;
        public HashMap<String, Type> types = new HashMap<>();

        public Type(String ingredientType) {
            this.ingredientType = ingredientType;
        }

        public String getIngredientType() {
            return ingredientType;
        }
    }
}
