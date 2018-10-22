package com.booking.apartments.utility;

import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Session {

    private Map<String, Object> params = Collections.synchronizedMap(new HashMap<String, Object>());

    public void addParam(String name, Object value){
        params.put(name,value);
    }

    public Object getParam(String name){
        return params.get(name);
    }

    public void removeParam(String name){
        params.remove(name);
    }

}
