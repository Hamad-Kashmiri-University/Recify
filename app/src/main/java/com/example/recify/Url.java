package com.example.recify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// retrieves data by reading url using string buffer
public class Url {

    public String urlReader(String urlForPlaces) throws IOException{
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        // insert url passed in as url using URL
        // try catches stop errors
        try {
            URL url = new URL(urlForPlaces);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            //read the data
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            // read each row with while until no lines left i.e row is null
           String row = "";

           while( (row = bufferedReader.readLine()) != null ){
               stringBuffer.append(row);
           }
           // set data to the read data and stop reading
           data = stringBuffer.toString();
           bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // if exception happens then continue here
        finally {
            inputStream.close();
            connection.disconnect();

        }
        return data;
    }
}
