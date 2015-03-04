package org.playground.admin.service;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.playground.admin.dao.*;
import org.playground.admin.dto.UserDTO;
import org.playground.admin.exception.ExpiredException;
import org.playground.admin.exception.UploadMultipleUserException;
import org.playground.admin.model.*;
import org.playground.admin.util.Constants;
import org.playground.admin.util.Transformers;
import org.playground.admin.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * Created by wsantasiero on 8/23/14.
 */
@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private UserInstitutionProfileRepository userInstitutionProfileRepository;

    @Autowired
    Properties applicationProperties;

    @Autowired
    private SortingService sortingService;

    @Override
    public UserDTO findById(int id, int institutionId){
        User user = userRepository.findOne(id);
        UserDTO userDTO = Transformers.transformToUserDTO(user);
        UserInstitutionProfile userInstitutionProfile = userInstitutionProfileRepository.findByUserIdAndInstitutionId(id, institutionId);
        userDTO.setUserInstitutionProfileStatus(userInstitutionProfile.getStatus());

        List<Authority> authorities = authorityRepository.findAuthorityByUserIdAndInstitutionId(userDTO.getId(), institutionId);
        if(authorities != null && authorities.size()>0) {
            List<String> authoritiesString = Lists.newArrayList();
            for (Authority authority : authorities) {
                authoritiesString.add(authority.getAuthority());
            }
            userDTO.setAuthorities(authoritiesString.toArray(new String[authoritiesString.size()]));
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> findByInstitutionIdAndFilteredBy(int institutionId, String sortedBy, String sortOrder, String filteredBy){
        List<UserDTO> userDTOList = Lists.newArrayList();

        Sort sort = buildSort(sortedBy, sortingService.getSortDirectionFromString(sortOrder));
        List<User> userList = getUsers(institutionId, sort, filteredBy);


        for (User user : userList) {
            UserDTO userDTO = Transformers.transformToUserDTO(user);
            List<Authority> authorities = authorityRepository.findAuthorityByUserIdAndInstitutionId(userDTO.getId(), institutionId);
            if(authorities != null && authorities.size()>0) {
                List<String> authoritiesString = Lists.newArrayList();
                for (Authority authority : authorities) {
                    authoritiesString.add(authority.getAuthority());
                }
                userDTO.setAuthorities(authoritiesString.toArray(new String[authoritiesString.size()]));
            }
            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    private List<User> getUsers(int institutionId, Sort sort, String filteredBy){
        if(filteredBy.equalsIgnoreCase("all"))
            return userRepository.findAllByInstitutionId(sort, institutionId);
        else if(filteredBy.equalsIgnoreCase("certified"))
            return null;
        else
            return  userRepository.findByInstitutionIdAndAuthority(sort, institutionId, filteredBy);
    }

    private Sort buildSort(String sortedBy, Sort.Direction sortDirection){
        Sort sort;
        if(sortedBy.equalsIgnoreCase("name")){
            sort = new Sort(
                    new Sort.Order(sortDirection, "lastName")
            ).and(new Sort(
                    new Sort.Order(sortDirection, "firstName")
            ));
        }else{
            sort = new Sort(
                    new Sort.Order(sortDirection, sortedBy)
            );
        }
        return sort;
    }

    @Override
    public UserDTO saveUserWithAuthorities(UserDTO userDTO){
        User user;
        if(userDTO.getId() > 0){
            user = userRepository.findOne(userDTO.getId());
        }else{
            user = userRepository.findByEmail(userDTO.getEmail());
        }

        if(user != null){
            userDTO.setUserExists(true);
            userDTO.setPassword(user.getPassword());
            userDTO.setId(user.getId());
        }

        saveUser(userDTO);
        saveUserInstitutionProfile(userDTO);



        authorityRepository.deleteByUserIdAndInstitutionId(userDTO.getId(), userDTO.getInstitutionId());
        boolean isInstructorOnly = true;
        for(String auth: userDTO.getAuthorities()){
            Authority authority = new Authority();
            authority.setAuthority(auth);
            authority.setUserId(userDTO.getId());
            authority.setInstitutionId(userDTO.getInstitutionId());
            authorityRepository.save(authority);
            if(auth.equals(Constants.Authorities.ADMIN) || auth.equals(Constants.Authorities.OBSERVER))
                isInstructorOnly = false;
        }

        if(userDTO.getSendEmail() != null && userDTO.getSendEmail().equals("true") && !isInstructorOnly){
            Institution institution = institutionRepository.findById(userDTO.getInstitutionId());
            userDTO.setInstitutionName(institution.getName());
            //generatePasswordUrlUUID(userDTO);
            //emailService.sendAddUserEmail(userDTO);
        }


        //updateElasticSearchRoster(userDTO);

        return userDTO;
    }

    private void saveUser(UserDTO userDTO){
        User user = Transformers.transformToUserModel(userDTO);

        user = userRepository.save(user);
        userDTO.setId(user.getId());
    }

    public UserDTO findById(int userId)
    {
        UserDTO userDTO= Transformers.transformToUserDTO(userRepository.findById(userId));
        return userDTO;
    }

    @Override
    public List<UserDTO> processBulkUserFile(MultipartFile file, int institutionId) throws IOException, UploadMultipleUserException{
        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
        Map<String,Integer> header = Maps.newHashMap();
        List<UserDTO> userDTOs = Lists.newArrayList();
        List<UserDTO> userExceptionList = Lists.newArrayList();
        String [] nextLine;
        boolean headerRow = true;
        while ((nextLine = csvReader.readNext()) != null) {
            if (headerRow) {
                for (int i = 0; i < nextLine.length; i++) {
                    header.put(nextLine[i], i);
                    logger.debug("Header column labeled "+nextLine[i]);
                }
                if(header.get("First Name") == null || header.get("Last Name") == null || header.get("Email Address") == null){
                    UserDTO userDTO = new UserDTO();
                    userDTO.getExceptions().add("File error: The first row should have columns named First Name, Last Name, and Email. Please adjust your file accordingly and try again.");
                    userExceptionList.add(userDTO);
                    throw new UploadMultipleUserException(userExceptionList);
                }
            }else{
                UserDTO userDTO = new UserDTO();
                userDTO.setInstitutionId(institutionId);
                userDTO.setFirstName(nextLine[header.get("First Name")]);
                userDTO.setLastName(nextLine[header.get("Last Name")]);
                userDTO.setEmail(nextLine[header.get("Email Address")]);

                validateUserDTO(userDTO);
                if(!userDTO.getExceptions().isEmpty()){
                    userExceptionList.add(userDTO);
                }else {
                    User user = userRepository.findByEmail(userDTO.getEmail());
                    if (user == null) {
                        saveUser(userDTO);
                    }else{
                        userDTO.setId(user.getId());
                    }
                    saveUserInstitutionProfile(userDTO);

                    if(userRepository.findByUserIdAndInstitutionId(userDTO.getId(), userDTO.getInstitutionId()) == null) {
                        authorityRepository.deleteByUserIdAndInstitutionId(userDTO.getId(), userDTO.getInstitutionId());
                        userDTOs.add(userDTO);
                    } else {
                        userDTO.getExceptions().add("User already exists");
                        userExceptionList.add(userDTO);
                    }
                }

            }
            if (headerRow)
                headerRow = false;
        }
        if(!userExceptionList.isEmpty()){
            throw new UploadMultipleUserException(userExceptionList);
        }

        return userDTOs;
    }

    private void validateUserDTO(UserDTO userDTO){
        if(StringUtils.isBlank(userDTO.getFirstName())){
            userDTO.getExceptions().add("First name is required");
        }
        if(StringUtils.isBlank(userDTO.getLastName())){
            userDTO.getExceptions().add("Last name is required");
        }
        if(!ValidationUtils.isEmailValid(userDTO.getEmail())){
            userDTO.getExceptions().add("A valid email address is required");
        }
    }



    @Override
    public void processAuthorizations(List<UserDTO> userDTOs, int institutionId){
        Institution institution = institutionRepository.findById(institutionId);
        for(UserDTO userDTO: userDTOs) {
            authorityRepository.deleteByUserIdAndInstitutionId(userDTO.getId(), institutionId);
            for (String auth : userDTO.getAuthorities()) {
                Authority authority = new Authority();
                authority.setAuthority(auth);
                authority.setUserId(userDTO.getId());
                authority.setInstitutionId(userDTO.getInstitutionId());
                authorityRepository.save(authority);
            }
            userDTO.setInstitutionName(institution.getName());


            User user = userRepository.findOne(userDTO.getId());
            if(StringUtils.isBlank(user.getPassword())) {
                //emailService.sendAddUserEmail(userDTO);
            }else {
                userDTO.setUserExists(true);
                //emailService.sendAddUserEmail(userDTO);
            }

            userDTO.setUserInstitutionProfileStatus(Constants.UserStatus.ACTIVE);
            //updateElasticSearchRoster(userDTO);
        }
    }


    public void saveUserInstitutionProfile(UserDTO userDTO){
        UserInstitutionProfile userInstitutionProfile = userInstitutionProfileRepository.findByUserIdAndInstitutionId(userDTO.getId(), userDTO.getInstitutionId());
        if(userInstitutionProfile == null){
            userInstitutionProfile = new UserInstitutionProfile();
            userInstitutionProfile.setDateCreated(Calendar.getInstance().getTime());
            userInstitutionProfile.setUserId(userDTO.getId());
            userInstitutionProfile.setInstitutionId(userDTO.getInstitutionId());
        }
        if(StringUtils.isBlank(userDTO.getUserInstitutionProfileStatus())) {
            userDTO.setUserInstitutionProfileStatus(Constants.UserStatus.ACTIVE);
        }

        userInstitutionProfile.setStatus(userDTO.getUserInstitutionProfileStatus());
        userInstitutionProfileRepository.save(userInstitutionProfile);
    }
}
