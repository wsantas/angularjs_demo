package org.playground.admin.dto;


import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

/**
 * Created by wsantasiero on 8/22/14.
 */
public class UserDTO {

    private static final long serialVersionUID = -5737585926953245483L;

    private int id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String[] authorities;

    private int institutionId;

    private String institutionName;

    private String sendEmail;

    private String sortableName;

    private String setPasswordUrl;

    private String UUID;

    private String userInstitutionProfileStatus;

    private String password;

    private boolean userExists = false;

    public List<String> exceptions = Lists.newArrayList();

    private String loginUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getSortableName() {
        return sortableName;
    }

    public void setSortableName(String sortableName) {
        this.sortableName = sortableName;
    }

    public String getSetPasswordUrl() {
        return setPasswordUrl;
    }

    public void setSetPasswordUrl(String setPasswordUrl) {
        this.setPasswordUrl = setPasswordUrl;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUserInstitutionProfileStatus() {
        return userInstitutionProfileStatus;
    }

    public void setUserInstitutionProfileStatus(String userInstitutionProfileStatus) {
        this.userInstitutionProfileStatus = userInstitutionProfileStatus;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }

    public boolean isUserExists() {
        return userExists;
    }

    public void setUserExists(boolean userExists) {
        this.userExists = userExists;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
