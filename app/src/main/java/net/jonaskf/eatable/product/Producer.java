package net.jonaskf.eatable.product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jonas on 26.04.16.
 */
public class Producer {
    private String id;
    private String name;

    public static HashMap<String, Producer>list = new HashMap<>();

    public Producer(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getID(){return id;}
    public String getName(){return name;}
    
    public static void addProducer(JSONObject obj){
        try{
            list.put(
                    obj.getString("id"),
                    new Producer(
                            obj.getString("id"),
                            obj.getString("produced_by")
                    )
            );
        }catch(JSONException e){e.printStackTrace();}
    }
}
