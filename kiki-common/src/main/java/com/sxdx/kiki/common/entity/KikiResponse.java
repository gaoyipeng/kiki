package com.sxdx.kiki.common.entity;

import java.io.Serializable;
import java.util.HashMap;

public class KikiResponse  extends HashMap<String, Object> implements Serializable {


    private static final long serialVersionUID = 967397361339698151L;

    public KikiResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public KikiResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public KikiResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public String getMessage() {
        return String.valueOf(get("message"));
    }

    public Object getData() {
        return get("data");
    }
}