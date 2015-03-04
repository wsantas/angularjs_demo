package org.playground.admin.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by wsantasiero on 9/22/14.
 */
@Entity
public class Country {

    private String name;
    private String code;

    @Id
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
