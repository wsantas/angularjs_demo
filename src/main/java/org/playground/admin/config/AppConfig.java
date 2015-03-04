package org.playground.admin.config;

/**
 * Created by wsantasiero on 8/27/14.
 */

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@ComponentScan("org.playground.admin")
@PropertySource(value={"classpath:application.properties","classpath:hibernate.cfg.properties","file:${user.home}/application.properties","file:${app_properties}/application.properties"}, ignoreResourceNotFound = true)
@EnableWebMvc
@EnableTransactionManagement
@EnableJpaRepositories("org.playground.admin.dao")
public class AppConfig extends WebMvcConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(AppConfig.class);
    @Autowired
    Environment env;

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/resources/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registry.addResourceHandler("/images/**").addResourceLocations("/static/images/");
        registry.addResourceHandler("/scripts/**").addResourceLocations("/static/scripts/");
        registry.addResourceHandler("/styles/**").addResourceLocations("/static/styles/");
        registry.addResourceHandler("/partials/**").addResourceLocations("/WEB-INF/resources/partials/");
        registry.addResourceHandler("/templates/**").addResourceLocations("/WEB-INF/resources/templates/");
        registry.addResourceHandler("/files/**").addResourceLocations("/static/files/");
    }


    @Bean
    public DataSource dataSource() {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass(env.getProperty("spring.datasource.driverClassName"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        logger.info("Loading database from "+env.getProperty("spring.datasource.url"));
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUser(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setMinPoolSize(3);
        dataSource.setMaxPoolSize(15);
        dataSource.setDebugUnreturnedConnectionStackTraces(true);

        return dataSource;
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException, PropertyVetoException {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("org.playground.admin.model");
        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(va);

        Properties ps = new Properties();
        ps.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        ps.put("hibernate.ejb.naming_strategy", env.getProperty("hibernate.ejb.naming_strategy"));
        ps.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        ps.put("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
        ps.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        ps.put("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));

        emf.setJpaProperties(ps);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws ClassNotFoundException, PropertyVetoException{
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactory().getObject() );
        return transactionManager;
    }

    @Bean
    public OpenEntityManagerInViewFilter buildOpenEntityManagerInViewFilter() {
        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
        openEntityManagerInViewFilter.setEntityManagerFactoryBeanName("entityManagerFactory");
        return openEntityManagerInViewFilter;
    }

    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(env.getProperty("mail.server.host"));
        sender.setPort(Integer.parseInt(env.getProperty("mail.server.port")));
        if (Boolean.parseBoolean(env.getProperty("mail.auth"))) {
            logger.debug("mail auth true");
            sender.setUsername(env.getProperty("mail.server.api"));
            sender.setPassword(env.getProperty("mail.server.api"));
        }
        sender.setProtocol(env.getProperty("mail.server.protocol"));


        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", env.getProperty("mail.auth"));
        props.setProperty("mail.smtp.from", env.getProperty("mail.from"));
        props.setProperty("mail.from", env.getProperty("mail.from"));
        props.setProperty("mail.host", env.getProperty("mail.server.host"));
        props.setProperty("mail.transport.protocol", env.getProperty("mail.server.protocol"));

        sender.setJavaMailProperties(props);
        return sender;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine engine=new SpringTemplateEngine();
        engine.setTemplateResolver(emailTemplateResolver());
        return engine;
    }

    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver(){
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);

        return templateResolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver mr = new CommonsMultipartResolver();
        return mr;
    }
}
