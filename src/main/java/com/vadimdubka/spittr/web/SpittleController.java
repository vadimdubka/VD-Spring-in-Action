package com.vadimdubka.spittr.web;

import com.vadimdubka.spittr.dao.SpittleRepository;
import com.vadimdubka.spittr.model.Spittle;
import com.vadimdubka.spittr.model.SpittleForm;
import com.vadimdubka.spittr.web.exeption.DuplicateSpittleException;
import com.vadimdubka.spittr.web.exeption.SpittleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
/*Any time there’s a class-level @RequestMapping on a controller class, it applies to all handler methods in the controller.
Then any @RequestMapping annotations on handler methods will complement the class-level @RequestMapping.*/
@RequestMapping("/spittles")
public class SpittleController {
    private final Logger logger = LoggerFactory.getLogger(SpittleController.class);
    
    private SpittleRepository spittleRepository;
    
    // Inject SpittleRepository
    @Autowired
    public SpittleController(SpittleRepository spittleRepository) {
        this.spittleRepository = spittleRepository;
    }
    
    /** Taking request without parameters */
    @RequestMapping(method = RequestMethod.GET)
    public String spittles(Model model) {
        /* Add spittles to model.
        Spittles() method is given a Model as a parameter.
        This is so that spittles() can populate the model with the Spittle list it retrieves from the repository.
        The Model is essentially a map (that is, a collection of key-value pairs)
        that will be handed off to the view so that the data can be rendered to the client.
        When add- Attribute() is called without specifying a key,
        the key is inferred from the type of object being set as the value.
        In this case, because it’s a List<Spittle>, the key will be inferred as spittleList.
        The equivalent: model.addAttribute("spittleList", spittleRepository.findSpittles(Long.MAX_VALUE, 20));*/
        model.addAttribute(spittleRepository.findSpittles(Long.MAX_VALUE, 20));
        return "spittles/spittles";  // Return view name
    }
    
    /**
     * 5.3.1	Taking query parameters
     * Taking request with query parameters. For example:
     * http://localhost:8080/spring-mvc-form/spittles/show?max=100&count=5
     * Model attribute isn't necessary
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String spittles(Model model,
                           @RequestParam(value = "max", defaultValue = "1000") long max,
                           @RequestParam(value = "count", defaultValue = "20") int count) {
        model.addAttribute(spittleRepository.findSpittles(max, count));
        return "spittles/spittles";
    }
    
    /**
     * 5.3.2	Taking input via path parameters
     * request example: /spittles/show_one?spittle_id=12345.
     * It's not good approach from a resource-orientation perspective
     */
    @RequestMapping(value = "/show_one", method = RequestMethod.GET)
    public String showSpittle(@RequestParam("spittle_id") long spittleId, Model model) {
        model.addAttribute(spittleRepository.findOne(spittleId));
        return "spittles/spittles";
    }
    
    /**
     * Good approach to get path as resource.
     * Notice that the phrase spittleId is repeated a few times in the example: in the @RequestMapping path,
     * as the value attribute of @PathVariable, and again as a method parameter name.
     * Because the method parameter  name  happens  to  be  the same  as  the  placeholder  name,
     * you  can  optionally  omit  the  value parameter  on @PathVariable:
     */
    @RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
    /*public String spittle(@PathVariable("spittleId") long spittleId, Model model) {*/
    public String spittle(@PathVariable long spittleId, Model model) {
        logger.debug("spittle()");
        Spittle spittle = spittleRepository.findOne(spittleId);
        if (spittle == null) {
            throw new SpittleNotFoundException();
        }
        model.addAttribute(spittle);
        return "spittles/spittles";
    }
    
    /**
     * 7.3	Handling exceptions
     * Suppose that SpittleRepository’s save() method throws a DuplicateSpittleException
     * if a user attempts to create a Spittle with text identical to one they’ve already created.
     * {@link DuplicateSpittleException}, {@link #handleNotFound()}
     */
    @RequestMapping(method = RequestMethod.POST)
    public String saveSpittle(SpittleForm form, Model model) {
        /** Thanks to {@link #handleNotFound()} we can shorten exception handling.*/
        /*try {
            spittleRepository.save(new Spittle(null, form.getMessage(), new Date(),
                                               form.getLongitude(), form.getLatitude()));
            return "redirect:/spittles";
        } catch (DuplicateSpittleException e) {
            return "error/duplicate";
        }*/
        spittleRepository.save(new Spittle(null, form.getMessage(), new Date(),
                                           form.getLongitude(), form.getLatitude()));
        return "redirect:/spittles";
    }
    
    /**
     * The @ExceptionHandler annotation has been applied to the handleDuplicateSpittle() method,
     * designating it as the go-to method when a DuplicateSpittleException is thrown.
     * It returns a String, which, just as with the request-handling method,
     * specifies the logical name of the view to render, telling the user that they attempted to create a duplicate entry.
     * What’s especially interesting about @ExceptionHandler methods is that they handle their exceptions from any handler method in the same controller. So although you created the handleDuplicateSpittle() method from the code extracted from saveSpittle(), it will handle a DuplicateSpittleException thrown from any method in SpittleController.
     * If @ExceptionHandler methods can handle exceptions thrown from any handler method in the same controller class, you might be wondering if there’s a way they can handle exceptions thrown from handler methods in any controller.
     * As of Spring 3.2 they certainly can, but only if they’re defined in a controller advice class {@link AppWideExceptionHandler#handleNotFound()} .
     */
    @ExceptionHandler(DuplicateSpittleException.class)
    public String handleNotFound() {
        return "error/duplicate";
    }
}
