package com.vadimdubka.spittr.dao;

import com.vadimdubka.spittr.model.Spitter;

public interface SpitterRepository {
    Spitter save(Spitter spitter);
    
    Spitter findByUsername(String username);
}
