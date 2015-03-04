package org.playground.admin.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by wsantasiero on 9/22/14.
 */
@Entity
public class State {

    private int id;
    private String name;
    private String code;
    private String countryCode;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
