package com.vadimdubka.spittr.web;

import com.vadimdubka.spittr.dao.SpittleRepository;
import com.vadimdubka.spittr.model.Spittle;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class SpittleControllerTest {
    @Test
    public void shouldShowRecentSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(20);
        // Mock repository
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findSpittles(Long.MAX_VALUE, 20)).thenReturn(expectedSpittles);
        
        SpittleController controller = new SpittleController(mockRepository);
        
        //  Mock Spring MVC
        MockMvc mockMvc = standaloneSetup(controller)
                              .setSingleView(new InternalResourceView("/WEB-INF/views/spittles/spittles.jsp"))
                              .build();
        
        mockMvc.perform(get("/spittles"))
            .andExpect(view().name("spittles/spittles"))
            .andExpect(model().attributeExists("spittleList"))
            .andExpect(model().attribute("spittleList", hasItems(expectedSpittles.toArray())));
    }
    
    
    @Test
    public void shouldShowPagedSpittlesWithParameters() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(50);
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findSpittles(238900, 50)).thenReturn(expectedSpittles);
        
        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
                              .setSingleView(new InternalResourceView("/WEB-INF/views/spittles/spittles.jsp"))
                              .build();
        
        
        mockMvc.perform(get("/spittles/show?max=238900&count=50"))
            .andExpect(view().name("spittles/spittles"))
            .andExpect(model().attributeExists("spittleList"));
    }
    
    @Test
    public void testSpittleAsResource() throws Exception {
        Spittle expectedSpittle = new Spittle("Hello", new Date());
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findOne(12345)).thenReturn(expectedSpittle);
        
        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();
        
        mockMvc.perform(get("/spittles/12345")) // Request resource via path
            .andExpect(view().name("spittles/spittles"))
            .andExpect(model().attributeExists("spittle"))
            .andExpect(model().attribute("spittle", expectedSpittle));
    }
    
    
    private List<Spittle> createSpittleList(int count) {
        List<Spittle> spittles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            spittles.add(new Spittle("Spittle " + i, new Date()));
        }
        return spittles;
    }
}