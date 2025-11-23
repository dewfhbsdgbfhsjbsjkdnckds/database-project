package org.example;

import java.util.HashMap;
import java.util.Map;

public class Secrets {
    public static Map<String, String> map = new HashMap<>();

    public static Map<String, String> getMap(){
        if (!map.isEmpty()) return map;
        map = FileMapper.getMap("secrets.env");
        return map;
    }

    // i dont know if this is a bad pattern
    // i dont see a better way to do this where i can just change the key name in 1 single place, instead of changing it everywhere that uses map.get()
    public static String getDBName(){
        return getMap().get("dbname");
    }
    public static String getUsername(){
        return getMap().get("username");
    }

    public static String getPassword(){
        return getMap().get("password");
    }

    public static String getUsersTable(){
        return getMap().get("usersTable");
    }

}
