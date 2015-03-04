package org.playground.admin.controller;

import org.playground.admin.dto.CountryDTO;
import org.playground.admin.dto.StateDTO;
import org.playground.admin.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wsantasiero on 9/22/14.
 */
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/noauth/states", method = RequestMethod.GET)
    public List<StateDTO> getAllStates(@RequestParam String code) {
        return addressService.findStatesByCountryCode(code);
    }

    @RequestMapping(value = "/noauth/countries", method = RequestMethod.GET)
    public List<CountryDTO> getAllCountries() {
        return addressService.findAllCountries();
    }
}
