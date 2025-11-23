package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileMapper {

    public static Map<String, String> getMap(String filename) {
       Map<String, String> map = new HashMap<>();
       try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
           String line;
           // i dont really like this syntax so much but it seems like a good way to do it
           while ((line = reader.readLine()) != null){
               String[] keyValuePair = line.split("=");
               map.put(keyValuePair[0], keyValuePair[1]);
           }
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       return map;
    }
}
