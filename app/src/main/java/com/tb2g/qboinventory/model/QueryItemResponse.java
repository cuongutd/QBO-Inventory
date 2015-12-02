package com.tb2g.qboinventory.model;

import com.intuit.ipp.data.Fault;
import com.intuit.ipp.data.Item;
import com.intuit.ipp.data.Warnings;

import java.util.List;

/**
 * Created by Cuong on 12/1/2015.
 */
public class QueryItemResponse {
    protected Warnings warnings;
    protected List<Item> Item;
    protected Fault fault;
    protected Integer startPosition;
    protected Integer maxResults;
    protected Integer totalCount;

    public QueryItemResponse() {
    }

    public Warnings getWarnings() {
        return warnings;
    }

    public void setWarnings(Warnings warnings) {
        this.warnings = warnings;
    }

    public List<Item> getItem() {
        return Item;
    }

    public void setItem(List<Item> item) {
        Item = item;
    }

    public Fault getFault() {
        return fault;
    }

    public void setFault(Fault fault) {
        this.fault = fault;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
