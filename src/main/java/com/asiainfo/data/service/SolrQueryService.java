package com.asiainfo.data.service;

import com.asiainfo.data.config.SolrQueryInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class SolrQueryService {

    private Log logger = LogFactory.getLog(SolrQueryService.class);

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private SolrClient solrClient;

    @Resource
    private SolrTemplate solrTemplate;

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Resource
    SolrQueryInfo solrQueryInfo;

    @Autowired
    public SolrQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void callSchema() {
        String response = restTemplate.getForObject("/co_audit_std/schema/fields", String.class);
        System.out.println(response);
    }

    public void postObject(){
//        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
//        postParameters.add("command", "full-import");
//        postParameters.add("verbose", "false");
//        postParameters.add("clean", "true");
//        postParameters.add("commit", "true");
//        postParameters.add("core", "ik_core");
//        postParameters.add("name", "dataimport");
//        HttpHeaders httpHeaders=new HttpHeaders();
//        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
//        HttpEntity<MultiValueMap <String, Object>> r = new HttpEntity<>(postParameters, httpHeaders);
//        String time = String.valueOf(new Date().getTime());
//        String url = "http://localhost:8983/solr/co_audit_std/dataimport?_=" + time + "&indent=on&wt=json";
//        String responseMessage = restTemplate.postForObject(url, r, String.class);
    }

    //每五秒执行一次
    @Scheduled(cron = "${solr-query.cron}")
    public void solrQuery() {
        Date now =new Date();
        Date end=DateUtils.truncate(now,Calendar.DATE);
        Date begin=DateUtils.addDays(end,-1);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginStr=simpleDateFormat.format(begin).replace(" ","T")+"Z";
        String endStr=simpleDateFormat.format(end).replace(" ","T")+"Z";

        logger.info(solrTemplate);
        logger.info(solrClient);
        SolrParams params = new SolrQuery("rec_time:["+beginStr+" TO "+endStr+"]");
        QueryResponse queryResponse= null;
        try {
            logger.info("Solr Core ==="+solrQueryInfo.getCore());
            queryResponse = solrClient.query(solrQueryInfo.getCore(),params);
        } catch (SolrServerException e) {
            logger.info("SolrServerException====["+e.getMessage()+"]");

            e.printStackTrace();
        } catch (IOException e) {
            logger.info("IOException===="+e.getMessage());

            e.printStackTrace();
        }
        logger.info("queryResponse===="+queryResponse);
        List<FacetField> list=queryResponse.getFacetFields();
        SolrDocumentList solrDocumentList=  queryResponse.getResults();
        logger.info("solrDocumentList===="+solrDocumentList.size());
        for (int i = 0; i <solrDocumentList.size() ; i++) {
            Collection<String> collection= solrDocumentList.get(i).getFieldNames();
            logger.info(solrDocumentList.get(i).getFieldValuesMap());
            for (String f:collection) {

                logger.info("f===="+f+"====value===="+solrDocumentList.get(i).getFieldValue(f).toString());

            }


        }

        logger.info("更新solr索引：返回值：{"+"12345678"+"}");
    }
}
