package model.WarnNum;

import java.util.List;

public class Results {
    public Results(){}
    private List<Integer> engineNum;
    private List<Integer> outTempNum;
    private List<Integer> inTempNum;
    private List<Integer> gasPaNum;
    private List<Integer> waterPaNum;
    private List<Integer> sumNum;
    public void setSumNum(List<Integer> sumNum){
        this.sumNum = sumNum;
    }
    public List<Integer> getSumNum(){
        return this.sumNum;
    }
    public void setEngineNum(List<Integer> engineNum) {
        this.engineNum = engineNum;
    }
    public List<Integer> getEngineNum() {
        return engineNum;
    }

    public void setOutTempNum(List<Integer> outTempNum) {
        this.outTempNum = outTempNum;
    }
    public List<Integer> getOutTempNum() {
        return outTempNum;
    }

    public void setInTempNum(List<Integer> inTempNum) {
        this.inTempNum = inTempNum;
    }
    public List<Integer> getInTempNum() {
        return inTempNum;
    }

    public void setGasPaNum(List<Integer> gasPaNum) {
        this.gasPaNum = gasPaNum;
    }
    public List<Integer> getGasPaNum() {
        return gasPaNum;
    }

    public void setWaterPaNum(List<Integer> waterPaNum) {
        this.waterPaNum = waterPaNum;
    }
    public List<Integer> getWaterPaNum() {
        return waterPaNum;
    }
}
