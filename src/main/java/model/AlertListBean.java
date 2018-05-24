package model;

import java.util.List;

public class AlertListBean {
    public int numResults;
    public List<ALertInfoBean> results;

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public List<ALertInfoBean> getaLertInfoBeanList() {
        return results;
    }

    public void setaLertInfoBeanList(List<ALertInfoBean> results) {
        this.results = results;
    }

    public AlertListBean(){}
}
