package com.vadimdubka.spittr.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller // Declared to be a controller
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    // Handle GET requests for /home
    // @RequestMapping(value = "/home", method = GET)
    @RequestMapping(value = {"/home", "/homepage"}, method = GET)
    public String home() {
        logger.debug("home()");
        return "home";// View name is home. This String will be interpreted by Spring MVC as the name of the view that will be rendered. DispatcherServlet will ask the view resolver to resolve this logical view name into an actual view.
    }
    
    /*For thymeleaf example*/
    @RequestMapping(value = "/thymeleaf/home", method = GET)
    public String thHome() {
        logger.debug("thHome()");
        return "thymeleaf/home";// View name is home. This String will be interpreted by Spring MVC as the name of the view that will be rendered. DispatcherServlet will ask the view resolver to resolve this logical view name into an actual view.
    }
}
