package net.jonaskf.eatable.product;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jonas on 25.02.2016.
 */
//Class for ingredient source
public class Source implements Serializable {
    private static final long serialVersionUID = -324168761687654686L;

    private String source;
    public static HashMap<String, Source> list = new HashMap<>();

    public Source(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
