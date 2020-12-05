package com.asiainfo.data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "solr-query")
public class SolrQueryInfo {

    private String cron;

    private String core;

    private String rangeCol;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getRangeCol() {
        return rangeCol;
    }

    public void setRangeCol(String rangeCol) {
        this.rangeCol = rangeCol;
    }
}
