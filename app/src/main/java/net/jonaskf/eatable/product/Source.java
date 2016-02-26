package net.jonaskf.eatable.product;

import java.util.HashMap;

/**
 * Created by jonas on 25.02.2016.
 */
//Class for ingredient source
public class Source{
    private String source;
    public static HashMap<Integer, Source> list = new HashMap<>();

    public Source(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
