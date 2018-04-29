package com.vadimdubka.spittr.web.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*7.3.1	Mapping exceptions to HTTP status codes
    By default result in a response with a 500 (Internal Server Error) status code.
 * When SpittleNotFoundException is thrown, it’s a situation where a requested resource isn’t found.
 * The HTTP status code of 404 is precisely the appropriate response status code when a resource isn’t found.
 * So, let’s use @ResponseStatus to map SpittleNotFoundException to HTTP status code 404.
 * The response would have a status code of 404 and a reason of Spittle Not Found
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Spittle Not Found")
public class SpittleNotFoundException extends RuntimeException {

}
