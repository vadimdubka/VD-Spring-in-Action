package com.vadimdubka.spittr.dao;


import com.vadimdubka.spittr.model.Spittle;

import java.util.List;

public interface SpittleRepository {
    List<Spittle> findRecentSpittles();
    
    /**
     * The max parameter is a Spittle ID that represents the maximum ID of any Spittle that should
     * be returned. As for the count parameter, it indicates how many Spittle objects to return.
     */
    List<Spittle> findSpittles(long max, int count);
    
    Spittle findOne(long i);
    
    void save(Spittle spittle);
}
