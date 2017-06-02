package com.softtek.idtc.gemfire.server;

import java.util.Date;
import java.util.Properties;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import com.gemstone.gemfire.pdx.PdxInstance;

public class LoggingCacheListener extends CacheListenerAdapter<String, PdxInstance> implements Declarable {

    @Override
    public void afterCreate(EntryEvent<String, PdxInstance> event) {
        //The new value will be a PdxInstance, because the cache
        //has read-serialized set to true in gemfire-server.xml.
        //PdxInstance is a wrapper around the serialized object.
        PdxInstance session = event.getNewValue();

        Integer id = (Integer) session.getField("id");
        Date createdAt = (Date) session.getField("createdAt");
        Date updatetAt = (Date) session.getField("updateddAt");
        Boolean status = (Boolean) session.getField("status");

        //print out the id and creation time of the portfolio.
        System.out.println("LoggingCacheListener: - " + id + " created at " + createdAt);
    }

    /**
     * Initialize any properties in the specified in the cache.xml file.
     */
    public void init(Properties props) {
        // do nothing
    }
}

