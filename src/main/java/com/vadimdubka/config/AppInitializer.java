package com.vadimdubka.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/*  To configure Spring MVC we can use:
- web.xml file or
- AbstractAnnotationConfigDispatcherServletInitializer Java class.

AbstractAnnotationConfigDispatcherServletInitializer:
    0) defines that Spring MVC configuration will be in Java instead of XML;
    1) setup/creates DispatcherServlet;
    2) setup/creates ContextLoaderListener.

In Spring web application there’s often 2 APPLICATION CONTEXT:
    1) first is created by DispatcherServlet.
    2) second is created by ContextLoaderListener.

CONFIGURATION CLASSes of application (using Java configuration):
    1) DispatcherServlet in the first APPLICATION CONTEXT loads BEANS declared in the CONFIGURATION CLASS (WebConfig), that contain web components such as:
        - controllers,
        - view resolvers,
        - handler mappings.
    2) ContextLoaderListener in the second APPLICATION CONTEXT loads the other BEANS declared in other CONFIGURATION CLASSes (RootConfig, DataConfig, DBConfig) of your application. These beans are typically the middle-tier and data-tier components that drive the back end of the application.
*/

/*  DispatcherServlet = servlet container = web container. DispatcherServlet is the centerpiece of Spring MVC. It’s where the request first hits the framework, and it’s responsible for routing the request through all the other components.*/


public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    /* Map DispatcherServlet to /. */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
        
    }
    
    /*The @Configuration classes returned from getServletConfigClasses() will define beans for DispatcherServlet’s application context.*/
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
        
    }
    
    /*The @Configuration classes returned from getRootConfigClasses() will define beans for ContextLoaderListener's  application context.*/
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{SpringDBConfig.class, RootConfig.class, DataConfig.class};
    }
    
    /* ******Additional methods********** */
    
    /*By overriding customizeRegistration(), you can apply additional configuration to DispatcherServlet.*/
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        /** Configure constraints on StandardServletMultipartResolver bean from {@link WebConfig#multipartResolver()}:
         Enable multipart requests to temporarily store uploaded files at /tmp/spittr/uploads,
         Limits files to no more than 2 MB, to limit the entire request to no more than 4 MB, and to write all files to disk.
         * location - absolute path to the directory location where files will be stored
         * maxFileSize - the maximum size allowed for uploaded files
         * maxRequestSize - the maximum size allowed for multipart/form-data requests
         * fileSizeThreshold - the size threshold after which files will be written to disk
         * */
        registration.setMultipartConfig(new MultipartConfigElement("С:/99_SANDBOX/",
                                                                   4194304,
                                                                   4194304,
                                                                   0));
    }
    
    /*To register one or more filters and map them to DispatcherServlet*/
    @Override
    protected Filter[] getServletFilters() {
        Filter[] filters = null;
        /*Filter[] filters = new Filter[]{new MyFilter()};*/
        return filters;
    }
}
