package net.jonaskf.eatable;


import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

    public static void urlText(){
        try {
            // Create a URL for the desired page
            URL url = new URL("http://frigg.hiof.no/android_v165/api.php");

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                // str is one line of text; readLine() strips the newline character(s)
                Log.d("urltest", str);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    };

}
