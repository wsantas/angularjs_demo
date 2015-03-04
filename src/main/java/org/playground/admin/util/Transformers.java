package org.playground.admin.util;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.playground.admin.dto.*;
import org.playground.admin.model.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wsantasiero on 9/7/14.
 */
public class Transformers {
    public static User transformToUserModel(UserDTO userDTO){
        User user = new User();
        if(userDTO.getId() > 0) {
            user.setId(userDTO.getId());
        }else{
            user.setDateCreated(Calendar.getInstance().getTime());
        }

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setStatus(Constants.UserStatus.ACTIVE);
        return user;
    }

    public static UserDTO transformToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setSortableName(user.getLastName() + ", " + user.getFirstName());
        return userDTO;
    }


    public static List<StateDTO> transformToStateDTOList(Iterable<State> states){
        List<StateDTO> stateDTOs = Lists.newArrayList();

        for(State state: states) {
            StateDTO stateDTO = new StateDTO();
            stateDTO.setName(state.getName());
            stateDTO.setCode(state.getCode());
            stateDTOs.add(stateDTO);
        }
        return stateDTOs;
    }

    public static List<CountryDTO> transformToCountryDTO(Iterable<Country> countries){
        List<CountryDTO> countryDTOs = Lists.newArrayList();
        for(Country country: countries) {
            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setName(country.getName());
            countryDTO.setCode(country.getCode());
            countryDTOs.add(countryDTO);
        }
        return countryDTOs;
    }


}
