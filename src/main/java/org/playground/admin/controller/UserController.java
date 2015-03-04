package org.playground.admin.controller;

import org.playground.admin.dto.UserDTO;
import org.playground.admin.exception.UploadMultipleUserException;
import org.playground.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wsantasiero on 9/1/14.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public List<UserDTO> users(@RequestParam int institutionId, @RequestParam String sortedBy, @RequestParam String sortOrder, @RequestParam String filteredBy) {
        return userService.findByInstitutionIdAndFilteredBy(institutionId, sortedBy, sortOrder, filteredBy);
    }

    @RequestMapping(value = "users/save", method = RequestMethod.POST)
    public UserDTO saveUser(@RequestBody UserDTO userDTO){
        userService.saveUserWithAuthorities(userDTO);
        return userDTO;
    }

    @RequestMapping(value = "users/upload", method = RequestMethod.POST)
    public Map<String, List<UserDTO>> uploadUsers(@RequestParam("file") MultipartFile file, @RequestParam int institutionId) throws IOException{
        Map<String, List<UserDTO>> temp = new HashMap<String, List<UserDTO>>();
        try {
            temp.put("SUCCESS", userService.processBulkUserFile(file, institutionId));
            return temp;
        }catch(UploadMultipleUserException umue){
            temp.put("FAILED", umue.getUserExceptionList());
            return temp;
        }
    }

    @RequestMapping(value = "users/authorizations", method = RequestMethod.POST)
    public Map<String, String> saveAuthorizations(@RequestBody UserDTO[] userDTOArray, @RequestParam int institutionId) throws IOException{
        userService.processAuthorizations(Arrays.asList(userDTOArray),institutionId);
        Map<String, String> temp = new HashMap<String, String>();
        temp.put("status","success");
        return temp;
    }

    @RequestMapping(value = "users/{userId}", method = RequestMethod.GET)
    public UserDTO getUser(@PathVariable("userId") int userId, @RequestParam int institutionId) {
        return userService.findById(userId, institutionId);
    }

}
