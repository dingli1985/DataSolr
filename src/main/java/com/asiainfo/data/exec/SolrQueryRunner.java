package com.asiainfo.data.exec;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class SolrQueryRunner implements CommandLineRunner {

    private Log logger = LogFactory.getLog(SolrQueryRunner.class);

    @Resource
    RestTemplate restTemplate;

    public String callSchema() {
       return  (String)restTemplate.getForObject("/co_audit_std/schema/fields", String.class) ;
    }


    @Override
    public void run(String... args) throws Exception {
//        RestTemplate restTemplate=restTemplateBuilder.build();
        Date now =new Date();
        Date end=DateUtils.truncate(now,Calendar.DATE);
        Date begin=DateUtils.addDays(end,-1);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginStr=simpleDateFormat.format(begin).replace(" ","T")+"Z";
        String endStr=simpleDateFormat.format(end).replace(" ","T")+"Z";
        logger.info("beginStr===="+beginStr  +"====endStr===="+endStr);

        String schemaInfo=callSchema();

        logger.info("schemaInfo===="+schemaInfo );



    }
}
