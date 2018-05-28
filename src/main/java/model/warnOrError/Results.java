package model.warnOrError;

import java.util.List;

public class Results {
    public Results(){}
    private List<Integer> warnNum;
    private List<Integer> errorNum;
    private List<Integer> sumNum;
    public void setSumNum(List<Integer> sumNum){
        this.sumNum = sumNum;
    }
    public List<Integer> getSumNum(){
        return this.sumNum;
    }
    public void setWarnNum(List<Integer> warnNum) {
        this.warnNum = warnNum;
    }
    public List<Integer> getWarnNum() {
        return warnNum;
    }

    public void setErrorNum(List<Integer> errorNum) {
        this.errorNum = errorNum;
    }
    public List<Integer> getErrorNum() {
        return errorNum;
    }
}
