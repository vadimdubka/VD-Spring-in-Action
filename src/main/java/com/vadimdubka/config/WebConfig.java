package com.vadimdubka.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@EnableWebMvc // Enable Spring MVC
@ComponentScan({"com.vadimdubka.form.web",
    "com.vadimdubka.form.service",
    "com.vadimdubka.form.dao",
    "com.vadimdubka.form.exception",
    "com.vadimdubka.form.validator",
    
    "com.vadimdubka.spittr.web",
    "com.vadimdubka.spittr.dao",
    "com.vadimdubka.spittr.model"
})
public class WebConfig extends WebMvcConfigurerAdapter {
    /* *********Configure View Resolvers************/
    /*6.2.1	Configuring a JSP-ready view resolver*/
    /*The ViewResolver maps view names to actual views.*/
    /* Equivalents of View resolver:
     * InternalResourceViewResolver - for JSP
     * TilesViewResolver - Apache Tile
     * FreeMarkerViewResolver - views as FreeMarker templates
     * VelocityViewResolver - views as Velocity templates
     * */
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        // this will ensure that JSTL’s formatting and message tags will get the Locale and message sources configured in Spring
        viewResolver.setExposeContextBeansAsAttributes(true); // added by myself
        return viewResolver;
    }
    
    /****************************/
    /*6.3	Defining a layout with Apache Tiles views*/
    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tiles = new TilesConfigurer();
        /*specifies the location of tile-definition XML files*/
        tiles.setDefinitions("/WEB-INF/layout/tiles.xml",
                             "/WEB-INF/views/**/tiles.xml"); // ** look for any file named tiles.xml anywhere under /WEB-INF/views directory
        tiles.setCheckRefresh(true);
        return tiles;
    }
    
    @Bean
    public TilesViewResolver tilesViewResolver() {
        TilesViewResolver tilesViewResolver = new TilesViewResolver();
        /*we’ll need to define an order for these resolvers. The order property is used to define which is the order of invocations in the chain. The higher the order property (largest order number), the later the view resolver is positioned in the chain.
         * Be careful on the order priority as the InternalResourceViewResolver should have a higher order – because it’s intended to represent a very explicit mapping. And if other resolvers have a higher order, then the InternalResourceViewResolver might never be invoked.*/
        tilesViewResolver.setOrder(0);
        return tilesViewResolver;
    }
    
    /* *********************************/
    /*6.4.1	Configuring a Thymeleaf view resolver*/
    /*Thymeleaf view resolver: ThymeleafViewResolver resolves Thymeleaf template views from logical view names*/
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setOrder(1);
        return viewResolver;
    }
    
    /*Template engine: SpringTemplateEngine to process the templates and render the results*/
    @Bean
    public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
    
    /*Template resolver: Thymeleaf TemplateResolver that loads Thymeleaf templates */
    @Bean
    public TemplateResolver templateResolver() {
        TemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }
    
    /* ***********************************/
    
    /* *********Other configurations************/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
    /*Configure static content handling: you’re asking DispatcherServlet to forward requests for static resources to the servlet container’s default servlet and not to try to handle them itself*/
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Bean
    public MessageSource messageSource() {
        /*Optionally, you may choose ReloadableResourceBundleMessageSource, which works much like ResourceBundleMessageSource, but it has the ability to reload mes- sage properties without recompiling or restarting the application.*/
        /*The basename property can be set to look for messages in the classpath (with a classpath: prefix), in the filesystem (with a file: prefix), or at the root of the web application (with no prefix). */
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/messages",
                                   "messages/validation",
                                   "spittr/ValidationMessages",
                                   "spittr/ValidationMessages_es",
                                   "spittr/messages");
        
        /*if using ReloadableResourceBundleMessageSource*/
        /*ReloadableResourceBundleMessageSource messageSource =
            new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("file:///etc/spittr/messages");
        messageSource.setCacheSeconds(10);*/
        
        return messageSource;
    }
    
    /**
     * Resolving multipart requests
     * 1. Declare StandardServletMultipartResolver bean.
     * 2. Specify download details like the location where the uploaded files are temporarily written while they’re being uploaded.
     * It’s possible to configure constraints on StandardServletMultipartResolver.
     * But instead of configuring StandardServletMultipartResolver in your Spring configuration,
     * you must specify multipart configuration in the servlet configuration
     * (override the {@link AppInitializer#customizeRegistration} in AppInitializer class).
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver(); // Resolves multipart requests with Servlet 3.0 support (since Spring 3.1)
        // return new CommonsMultipartResolver(); // Resolves multipart requests using Jakarta Commons FileUpload if you’ll be deploying your application to a pre-Servlet 3.0 con- tainer or if you aren’t using Spring 3.1 or higher yet
    }
    
}