package com.example.swaggerdemo.json;

public class info_data_util {
    private int start, limit, total;
    private String rows;
 
    @Override
    public String toString() {
        return "info_data_util [start=" + start + ", limit=" + limit
                + ", total=" + total + ", rows=" + rows + "]";
    }
 
    public int getStart() {
        return start;
    }
 
    public void setStart(int start) {
        this.start = start;
    }
 
    public int getLimit() {
        return limit;
    }
 
    public void setLimit(int limit) {
        this.limit = limit;
    }
 
    public int getTotal() {
        return total;
    }
 
    public void setTotal(int total) {
        this.total = total;
    }
 
    public String getRows() {
        return rows;
    }
 
    public void setRows(String rows) {
        this.rows = rows;
    }
}