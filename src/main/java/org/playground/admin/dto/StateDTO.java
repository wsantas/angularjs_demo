package org.playground.admin.dto;

import java.io.Serializable;

/**
 * Created by wsantasiero on 9/22/14.
 */
public class StateDTO implements Serializable {
    private static final long serialVersionUID = 3643831483486829132L;

    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
