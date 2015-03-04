package org.playground.admin.config;

import org.playground.admin.dao.ApplicationPropertiesRepository;
import org.playground.admin.model.ApplicationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by wsantasiero on 7/9/14.
 */
@Configuration
public class ApplicationPropertiesConfig {

    Logger logger = LoggerFactory.getLogger(ApplicationPropertiesConfig.class);

    @Autowired
    ApplicationPropertiesRepository applicationPropertiesRepository;

    Properties properties;

    @Bean
    public Properties applicationProperties(){
        properties = new Properties();
        for(ApplicationProperty applicationProperty: applicationPropertiesRepository.findAll()){
            logger.debug("loading " + applicationProperty.getKey() + " with " + applicationProperty.getValue());
            properties.setProperty(applicationProperty.getKey(), applicationProperty.getValue());
        }
        return properties;
    }

}
