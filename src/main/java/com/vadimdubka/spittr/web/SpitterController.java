package com.vadimdubka.spittr.web;

import com.vadimdubka.spittr.dao.SpitterRepository;
import com.vadimdubka.spittr.model.Spitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/spitter")
public class SpitterController {
    /*Relative path to folders with downloaded files*/
    private static final String FOLDER_FOR_DOWNLOADS       = "uploads/";
    private static final String FOLDER_FOR_MULTI_DOWNLOADS = "uploads/multiple/";
    
    private final Logger logger = LoggerFactory.getLogger(SpitterController.class);
    
    private SpitterRepository spitterRepository;
    
    @Autowired
    public SpitterController(SpitterRepository spitterRepository) {
        this.spitterRepository = spitterRepository;
    }
    
    /*5.4	Processing forms*/
    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm(Model model) {
        logger.debug("showRegistrationForm()");
        model.addAttribute(new Spitter("name",
                                       "password",
                                       "fname",
                                       "lname",
                                       "email@gmail.com"));
        return "spittles/registerForm";
    }
    
    /*Example for thymeleaf*/
    @RequestMapping(value = "/thymeleaf/register", method = GET)
    public String thymeleafShowRegistrationForm(Model model) {
        logger.debug("thymeleafShowRegistrationForm()");
        model.addAttribute(new Spitter("th-name",
                                       "th-password",
                                       "th-fname",
                                       "th-lname",
                                       "th-email@gmail.com"));
        return "thymeleaf/registerForm";
    }
    
    /**
     * 5.4.1	Writing a form-handling controller
     * 5.4.2	Validating forms
     * 7.2.2	Handling multipart requests
     * 7.5.2	Working with flash attributes (instead of {@link Model} we use {@link RedirectAttributes}
     * to save request attributes to session before redirect and retrieve them from the session automatically.
     * Spring agrees that putting data into the session is a great way to pass information that survives a redirect.
     * But Spring doesn’t think you should be responsible for managing that data.
     * Flash attributes are stored in the session and carrying data until the next request,
     * after request they are retrieved into the model, surviving a redirect; then they go away.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistration(@RequestParam("profilePicture") MultipartFile file,
                                      @Valid Spitter spitter, Errors errors, RedirectAttributes model) {
        logger.debug("processRegistration()");
        if (errors.hasErrors()) {
            logger.error(errors.getAllErrors().toString());
            return "spittles/registerForm"; // Return to form on validation errors
        }
        
        // Save a Spitter
        spitterRepository.save(spitter);
        // Save file
        if (file.getOriginalFilename().isEmpty()) {
            logger.debug("Please select a valid file..");
        } else {
            logger.debug("file name and size: " + file.getOriginalFilename() + "  ::  " + file.getSize());
            writeFile(file, FOLDER_FOR_DOWNLOADS);
        }
        
        /*Redirect to profile page . Also recognizes prefix "forward*/
        /*First variant of redirect.
         * String concatenation is dangerous business when constructing things like URLs and SQL queries.*/
        // return "redirect:/spitter/" + spitter.getUsername();
        /*Second variant of redirect - using URL templates
        7.5.1	Redirecting with URL templates.
        All you need to do is set the value in the model.
        To do that, the processRegistration() needs to be written to accept a Model as a parameter
        and populate it with the username.
        Because it’s filled into the placeholder in the URL template instead of concatenated into the redirect String,
        any unsafe characters in the username property are escaped. This is safer than allowing the user to type in whatever they want
        for the username and then appending it to the path.
        URL templates only good for sending simple values, such as String and numeric values.
        There’s no good way to send anything more complex in a URL. But that’s where flash attributes come in to help*/
        model.addAttribute("username", spitter.getUsername());
        /*because the spitterId attribute from the model doesn’t map to any URL placeholders in the redirect,
        it’s tacked on to the redirect automatically as a query parameter.*/
        model.addAttribute("id", spitter.getId()); /*If the username attribute is habuma and the spitterId attribute is 42,
        then the resulting redirect path will be /spitter/habuma?spitterId=42.*/
        model.addFlashAttribute("spitter", spitter);
        return "redirect:/spitter/{username}";
    }
    
    /** 7.5.2	Working with flash attributes */
    @RequestMapping(value = "/{username}", method = GET)
    public String showSpitterProfile(@PathVariable String username, Model model) {
        logger.debug("showSpitterProfile()");
        if (!model.containsAttribute("spitter")) {
            logger.debug("looking for spitter in the repository");
            Spitter spitter = spitterRepository.findByUsername(username);
            model.addAttribute(spitter);
        }
        return "spittles/profile";
    }
    
    // Handling multiple files upload requesta
    @RequestMapping(value = "/multipleFileUpload", method = RequestMethod.POST)
    public String multipleFileUpload(@RequestParam("file") MultipartFile[] files, Model model) {
        logger.debug("multipleFileUpload()");
        for (MultipartFile file : files) {
            if (file.getOriginalFilename().isEmpty()) {
                model.addAttribute("msg", "Please select at least one file..");
                return "home";
            } else {
                writeFile(file, FOLDER_FOR_MULTI_DOWNLOADS);
            }
        }
        model.addAttribute("msg", "Multiple files uploaded successfully.");
        return "home";
    }
    
    private void writeFile(@RequestPart("file") MultipartFile file, String folderForDownloads) {
        /*File downloadedFile = new File(folderForDownloads + file.getOriginalFilename());*/
        File downloadedFile = new File(folderForDownloads, file.getOriginalFilename());
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadedFile))) {
            outputStream.write(file.getBytes());
            outputStream.flush();
            logger.debug("File uploaded successfully");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}