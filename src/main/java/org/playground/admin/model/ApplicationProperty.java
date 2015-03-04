package org.playground.admin.model;

import javax.persistence.*;

/**
 * Created by wsantasiero on 7/8/14.
 */
@Entity
public class ApplicationProperty {


    private int id;

    private String key;

    private String value;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="`key`",columnDefinition = "nvarchar(max)")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(columnDefinition = "nvarchar(max)")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
