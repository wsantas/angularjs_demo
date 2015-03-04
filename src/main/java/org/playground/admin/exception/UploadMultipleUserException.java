package org.playground.admin.exception;

import org.playground.admin.dto.UserDTO;

import java.util.List;

/**
 * Created by wsantasiero on 10/13/14.
 */
public class UploadMultipleUserException extends Exception {

    private List<UserDTO> userExceptionList;

    public UploadMultipleUserException(List<UserDTO> userExceptionList){
        this.userExceptionList=userExceptionList;
    }
    public List<UserDTO> getUserExceptionList() {
        return userExceptionList;
    }
}
