package com.softtek.idtc.gemfire.dtm;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolManager;
import com.gemstone.gemfire.cache.query.Query;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.SelectResults;
import com.gemstone.gemfire.cache.query.Struct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateRegionData {

    final static Logger log = LogManager.getLogger(CreateRegionData.class);

    public static void main(String[] args) throws Exception{
        ClientCacheFactory cf = new ClientCacheFactory();
        cf.set("cache-xml-file", "client-cache.xml");
        log.debug("Created the GemFire Cache");

//        ClientCache cache = cf.create();
//        log.info("Created the GemFire Cache");

        // Get the example Region from the Cache which is declared in the Cache XML file.
//        Region<String, PortfolioPdx> region = cache.<String, PortfolioPdx>getRegion("Portfolios");

    }

}


