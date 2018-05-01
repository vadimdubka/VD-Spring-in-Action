package com.vadimdubka.spittr.web;

import com.vadimdubka.spittr.web.exeption.DuplicateSpittleException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*7.4	Advising controllers
Suppose you want to apply the DuplicateSpittleException-handling method across all controllers in your application.
The next listing shows AppWideExceptionHandler, a @ControllerAdvice-annotated class that does just that.

A controller advice is any class that’s annotated with @ControllerAdvice
and has one or more of the following kinds of methods:
- @ExceptionHandler-annotated
- @InitBinder-annotated
- @ModelAttribute-annotated
*/
@ControllerAdvice
public class AppWideExceptionHandler {
    
    @ExceptionHandler(DuplicateSpittleException.class)
    public String handleNotFound() {
        return "error/duplicate";
    }
    
}
