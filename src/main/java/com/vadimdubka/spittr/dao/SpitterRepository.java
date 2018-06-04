package com.vadimdubka.spittr.dao;

import com.vadimdubka.spittr.model.Spitter;

import java.util.List;

public interface SpitterRepository {
    long count();
    
    Spitter save(Spitter spitter);
    
    Spitter findOne(long id);
    
    Spitter findByUsername(String username);
    
    List<Spitter> findAll();
}
