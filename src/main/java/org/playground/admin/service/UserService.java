package org.playground.admin.service;

import org.playground.admin.dto.UserDTO;
import org.playground.admin.exception.ExpiredException;
import org.playground.admin.exception.UploadMultipleUserException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by wsantasiero on 8/23/14.
 */
public interface UserService {

    List<UserDTO> findByInstitutionIdAndFilteredBy(int institutionId, String sortedBy, String sortOrder, String filteredBy);
    UserDTO saveUserWithAuthorities(UserDTO userDTO);
    List<UserDTO> processBulkUserFile(MultipartFile file, int institutionId) throws IOException, UploadMultipleUserException;
    void processAuthorizations(List<UserDTO> userDTOs, int institutionId);
    public UserDTO findById(int id, int institutionId);
}


