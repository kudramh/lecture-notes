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
import com.softtek.idtc.gemfire.vo.SessionPdx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateRegionData {

    final static Logger log = LogManager.getLogger(CreateRegionData.class);

    public static void main(String[] args) throws Exception{
        ClientCacheFactory cf = new ClientCacheFactory();
        cf.set("cache-xml-file", "client-cache.xml");
        log.debug("Created the GemFire CacheFactory");

        ClientCache cache = cf.create();
        log.info("Created the GemFire Cache");

        Region<String, SessionPdx> region = cache.<String, SessionPdx>getRegion("IDTCSessions");
        log.info("Obtained the Region from the Cache");

        //Populate the Region with some PortfolioPdx objects.
        SessionPdx port1 = new SessionPdx(1, "TEST-1" );
        SessionPdx port2 = new SessionPdx(2, "TEST-2");
        SessionPdx port3 = new SessionPdx(3, "TEST-3");
        region.put("1", port1);
        region.put("2", port2);
        region.put("3", port3);
        log.info("Populated some PortfolioPdx Objects");

        //Find the pool
        Pool pool = PoolManager.find("IDTCPool");

        //Get the QueryService from the pool
        QueryService qrySvc = pool.getQueryService();
        log.info("Got the QueryService from the Pool");

        //Execute a Query which returns a ResultSet.
        Query qry = qrySvc.newQuery("SELECT * FROM /IDTCSessions");
        SelectResults<SessionPdx> results = (SelectResults<SessionPdx>) qry.execute();
        log.info("ResultSet Query returned " + results.size() + " rows");

        //Execute a Query which returns a StructSet.
        QueryService qrySvc1 = pool.getQueryService();
        Query qry1 = qrySvc1.newQuery("SELECT id, sessionName, lockStatus, createdAt, updatedAt FROM /IDTCSessions WHERE id > 1");
        SelectResults<Struct> results1 = (SelectResults<Struct>) qry1.execute();
        log.info("StructSet Query returned " + results1.size() + " rows");

        //Iterate through the rows of the query result.
        int rowCount = 0;
        for (Struct si : results1) {
            rowCount++;
            log.info("Row " + rowCount + " Column 0 is named " + si.getStructType().getFieldNames()[0] + ", value is " + si.getFieldValues()[0]);
            log.info("Row " + rowCount + " Column 1 is named " + si.getStructType().getFieldNames()[1] + ", value is " + si.getFieldValues()[1]);
            log.info("Row " + rowCount + " Column 2 is named " + si.getStructType().getFieldNames()[2] + ", value is " + si.getFieldValues()[2]);
            log.info("Row " + rowCount + " Column 3 is named " + si.getStructType().getFieldNames()[3] + ", value is " + si.getFieldValues()[3]);
            log.info("Row " + rowCount + " Column 4 is named " + si.getStructType().getFieldNames()[4] + ", value is " + si.getFieldValues()[4]);
        }

        // Close the GemFire Cache.
        cache.close();
        log.info("Closed the GemFire Cache");

    }

}


