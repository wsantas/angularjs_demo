package org.playground.admin.dto;

import java.io.Serializable;

/**
 * Created by wsantasiero on 9/22/14.
 */
public class CountryDTO implements Serializable {
    private static final long serialVersionUID = 3276103225313865709L;

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
