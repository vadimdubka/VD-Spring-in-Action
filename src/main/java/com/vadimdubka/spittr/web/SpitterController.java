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

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    @RequestMapping(value = "/register", method = RequestMethod.GET)
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
    @RequestMapping(value = "/thymeleaf/register", method = RequestMethod.GET)
    public String thymeleafShowRegistrationForm(Model model) {
        logger.debug("thymeleafShowRegistrationForm()");
        model.addAttribute(new Spitter("th-name",
                                       "th-password",
                                       "th-fname",
                                       "th-lname",
                                       "th-email@gmail.com"));
        return "thymeleaf/registerForm";
    }
    
    /*5.4.1	Writing a form-handling controller*/
    /*5.4.2	Validating forms*/
    /*7.2.2	Handling multipart requests*/
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistration(@RequestParam("profilePicture") MultipartFile file, @Valid Spitter spitter, Errors errors) {
        logger.debug("processRegistration()");
        if (errors.hasErrors()) {
            logger.error(errors.getAllErrors().toString());
            return "spittles/registerForm"; // Return to form on validation errors
        }
        
        // Save a Spitter
        spitterRepository.save(spitter);
        // Save file
        logger.debug("file name and size: " + file.getOriginalFilename() + "  ::  " + file.getSize());
        if (file.getOriginalFilename().isEmpty()) {
            logger.debug("Please select a valid file..");
        } else {
            writeFile(file, FOLDER_FOR_DOWNLOADS);
        }
        return "redirect:/spitter/" + spitter.getUsername(); // Redirect to profile page . Also recognizes the forward: prefix
    }
    
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public String showSpitterProfile(@PathVariable String username, Model model) {
        logger.debug("showSpitterProfile()");
        Spitter spitter = spitterRepository.findByUsername(username);
        model.addAttribute(spitter);
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
