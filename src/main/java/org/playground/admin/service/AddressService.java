package org.playground.admin.service;

import org.playground.admin.dto.CountryDTO;
import org.playground.admin.dto.StateDTO;

import java.util.List;

/**
 * Created by wsantasiero on 9/22/14.
 */
public interface AddressService {
    List<StateDTO> findStatesByCountryCode(String code);
    List<CountryDTO> findAllCountries();
}
