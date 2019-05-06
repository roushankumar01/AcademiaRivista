package com.collegeproject.model.staticModel;

public class QueryModel {

    private String ques;
    private String date;
    private String queryId;
    private String postedBy;

    public QueryModel(String ques, String date, String queryId, String postedBy) {
        this.ques = ques;
        this.date = date;
        this.queryId = queryId;
        this.postedBy = postedBy;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}
