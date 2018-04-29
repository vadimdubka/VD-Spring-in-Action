package com.vadimdubka.spittr.web.exeption;

/*Suppose that SpittleRepository’s save() method throws a DuplicateSpittleException
if a user attempts to create a Spittle with text identical to one they’ve already created.*/
public class DuplicateSpittleException extends RuntimeException {

}
