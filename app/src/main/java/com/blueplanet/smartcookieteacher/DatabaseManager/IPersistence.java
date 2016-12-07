package com.blueplanet.smartcookieteacher.DatabaseManager;

import java.util.ArrayList;

/**
 * super class that serves as type to be instantiated for factory method pattern for storing and fetch data from
 * Persistence storage
 * @author dhanashree.ghayal
 */
public interface IPersistence {
    /**
     * This method is used to store data to persistence storage.
     * @param object of respective model class
     */
    public void save(Object object);

    /**
     * This method is used to load/read data from database.
     * @param object of respective model class
     * @return ArrayList<Object>
     */
    public Object load(Object object);


    public Object getData();

    /**
     * This method delete all data from respective table of persistence storage.
     */
    public void delete(Object object);
    
    /**
     * This method updates data of respective table of persistence storage.
     */
    public void update(Object object);
}
