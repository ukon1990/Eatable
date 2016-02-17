package net.jonaskf.eatable;


import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jonas on 17.02.2016.
 */
public class GetFromAPI {

    public static String getURL(String source){
        String updateFile = null;
        try{
            URL url = new URL("http://www.wah.jonaskf.net/updates/latestupdate.json");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            updateFile = new String(baos.toByteArray(), encoding);

        }catch(Exception e){
            e.printStackTrace();
        }
        return updateFile;
    }

}
