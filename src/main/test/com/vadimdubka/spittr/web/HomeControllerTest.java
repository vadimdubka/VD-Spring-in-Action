package com.vadimdubka.spittr.web;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class HomeControllerTest {
    // Old variant
    @Test
    public void testHomePage() throws Exception {
        HomeController controller = new HomeController();
        assertEquals("home", controller.home());
    }
    
    //new Spring MVC testing features
    @Test
    public void testHomePage2() throws Exception {
        HomeController controller = new HomeController();
        /*It starts by passing an instance of HomeController to MockMvcBuilders
.standaloneSetup() and calling build() to set up the MockMvc instance. Then it asks
the MockMvc instance to perform a GET request for / and sets an expectation for the view name.
*/
        // Set up MockMvc
        MockMvc mockMvc = standaloneSetup(controller).build();
        //Perform GET /homepage
        mockMvc.perform(get("/homepage")).andExpect(view().name("home")); //Expect home view
    }
    
    
    
    
}
