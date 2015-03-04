package org.playground.admin.service;

import org.playground.admin.dao.CountryRepository;
import org.playground.admin.dao.StateRepository;
import org.playground.admin.dto.CountryDTO;
import org.playground.admin.dto.StateDTO;
import org.playground.admin.model.Country;
import org.playground.admin.util.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wsantasiero on 9/22/14.
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<StateDTO> findStatesByCountryCode(String code){
        return Transformers.transformToStateDTOList(stateRepository.findStatesByCountryCodeOrderByNameAsc(code));
    }

    public List<CountryDTO> findAllCountries(){
       List<Country> countries = countryRepository.findAllByOrderByNameAsc();
       Country us = countryRepository.findByCode("US");
       countries.remove(us);
       countries.add(0,us);
       return Transformers.transformToCountryDTO(countries);
    }


}
