package controller;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.regexp.internal.RE;
import config.GlobalCorsConfig;
import db.DbAlertManeger;
import db.DbDeviceManage;
import db.DbManager;
import interfaces.ResultInfoInterface;
import model.*;
import model.AddPerson.AddPersonBean;
import model.AddSite.AddSiteBean;
import model.AssociaPerson.AssociaPersonBean;
import model.AssociaPerson.Metadata;
import model.CreateDevice.CreateDeviceBean;
import model.DeviceList.Data;
import model.DeviceList.DeviceList;
import model.Measure.MeasureBean;
import model.ResDeviceList.ResDeviceListBean;
import model.ResDeviceList.ResultBean;
import model.SpecToken.SpecTokenBean;
import model.SpecToken.SpecTokensBean;
import model.WarnNum.*;
import model.warnOrError.WarnErrorNums;
import network.NetworkUtils;
import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sun.nio.ch.Net;

import javax.jws.soap.SOAPBinding;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
@ComponentScan
@Configuration
@EnableAutoConfiguration
@RestController
@RequestMapping("/history")
@Import(GlobalCorsConfig.class)
public class HistoryAlertController {

    private String StartMeasuresResult = null;
    private String StartEndAlertResult = null;
    private String StartEndErrorResult = null;
    private String  getAllErrorOrWarn = null;
    private String   getAllDeviceErrorOrWarn = null;
    private String  getAllDeviceAlertInfo = null;
    private String getAlertInfoExcPage = null;
    private String[] startMonths = {"2018-01-01","2018-02-01","2018-03-01","2018-04-01","2018-05-01","2018-06-01","2018-07-01","2018-08-01","2018-09-01","2018-10-01","2018-11-01","2018-12-01"};
    private String[] endMonths = {"2018-01-31","2018-02-28","2018-03-31","2018-04-30","2018-05-31","2018-06-30","2018-07-31","2018-08-31","2018-09-30","2018-10-31","2018-11-30","2018-12-31"};
    private String getWarnInfo = null;
    private String getErrorInfo = null;
    private String getAllDeviceErrorInfo = null;


    @RequestMapping(value = "/getStartEndAlert", method = RequestMethod.GET)
    public String getStartAlert(@RequestParam(value="pageIndex",required = false) int pageIndex,@RequestParam(value="pageSize",required = false) int pageSize,@RequestParam(value="type",required = false) String type,@RequestParam(value="startDate",required = false) String startDate,@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="assignToken",required = true) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){

        DbAlertManeger.getInstance().init();
        List<ALertInfoBean> aLertInfoBeanList=DbAlertManeger.getInstance().getAlertInfo(sitewhereToken,assignToken,"Warning",type,startDate,endDate,pageIndex,pageSize);
        StartEndAlertResult=JSON.toJSONString(aLertInfoBeanList);
        /*String url = "http://localhost:8080/sitewhere/api/assignments/"+assignToken+"/alerts?startDate="+startDate+"&endDate="+endDate;
        StartEndAlertResult = NetworkUtils.doGetAsync(url, sitewhereToken);*/
        System.out.println(StartEndAlertResult);

        return StartEndAlertResult;
    }
    //获取error  全部筛选都需要
    @RequestMapping(value = "/getStartEndError", method = RequestMethod.GET)
    public String getStartEndError(@RequestParam(value="pageIndex",required = false) int pageIndex,@RequestParam(value="pageSize",required = false) int pageSize,@RequestParam(value="type",required = false) String type,@RequestParam(value="startDate",required = false) String startDate,@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="assignToken",required = true) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){

        DbAlertManeger.getInstance().init();
        List<ALertInfoBean> aLertInfoBeanList=DbAlertManeger.getInstance().getAlertInfo(sitewhereToken,assignToken,"Error",type,startDate,endDate,pageIndex,pageSize);
        StartEndErrorResult=JSON.toJSONString(aLertInfoBeanList);
        System.out.println(StartEndErrorResult);


        return StartEndErrorResult;
    }






    //获取每个设备告警warning分类别次数
    @RequestMapping(value = "/getWarnInfo", method = RequestMethod.GET)
    public String getWarnInfo(@RequestParam(value="level",required = false) String level,@RequestParam(value="type",required = false) String type,@RequestParam(value="startDate",required = false) String startDate,@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="assignToken",required = true) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){
        int size = 12;
        List<Integer> engineNum =  new ArrayList<Integer>();
        List<Integer> outTempNum = new ArrayList<Integer>();
        List<Integer> inTempNum = new ArrayList<Integer>();
        List<Integer>  gasPaNum = new ArrayList<Integer>();
        List<Integer> waterPaNum = new ArrayList<Integer>();

        List<Integer> sumNum = new ArrayList<Integer>();
        WarnNums warnNums= new WarnNums();
        Results results = new Results();
        DbAlertManeger.getInstance().init();
        Integer tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Warning","engine.overheat",startMonths[i],endMonths[i]);
            engineNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(0,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Warning","inTemp.overheat",startMonths[i],endMonths[i]);
            inTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(1,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Warning","outTemp.overheat",startMonths[i],endMonths[i]);
            outTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(2,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Warning","gasPa.overPa",startMonths[i],endMonths[i]);
            gasPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(3,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Warning","waterPa.overPa",startMonths[i],endMonths[i]);
            waterPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(4,tempSum);
        results.setEngineNum(engineNum);
        results.setInTempNum(inTempNum);
        results.setOutTempNum(outTempNum);
        results.setGasPaNum(gasPaNum);
        results.setWaterPaNum(waterPaNum);
        results.setSumNum(sumNum);

        warnNums.setResults(results);
        getWarnInfo=JSON.toJSONString(warnNums);
        return getWarnInfo;
    }

    //获取每个设备告警error分类别次数
    @RequestMapping(value = "/getErrorInfo", method = RequestMethod.GET)
    public String getErrorInfo(@RequestParam(value="level",required = false) String level,@RequestParam(value="type",required = false) String type,@RequestParam(value="startDate",required = false) String startDate,@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="assignToken",required = true) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){
        int size = 12;
        List<Integer> engineNum =  new ArrayList<Integer>();
        List<Integer> outTempNum = new ArrayList<Integer>();
        List<Integer> inTempNum = new ArrayList<Integer>();
        List<Integer>  gasPaNum = new ArrayList<Integer>();
        List<Integer> waterPaNum = new ArrayList<Integer>();
        List<Integer> sumNum = new ArrayList<Integer>();
        WarnNums warnNums= new WarnNums();
        Results results = new Results();
        DbAlertManeger.getInstance().init();
        Integer tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Error","engine.overheat",startMonths[i],endMonths[i]);
            engineNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(0,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Error","inTemp.overheat",startMonths[i],endMonths[i]);
            inTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(1,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Error","outTemp.overheat",startMonths[i],endMonths[i]);
            outTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(2,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Error","gasPa.overPa",startMonths[i],endMonths[i]);
            gasPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(3,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAlertInfoExcPage(sitewhereToken,assignToken,"Error","waterPa.overPa",startMonths[i],endMonths[i]);
            waterPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(4,tempSum);

        results.setEngineNum(engineNum);
        results.setInTempNum(inTempNum);
        results.setOutTempNum(outTempNum);
        results.setGasPaNum(gasPaNum);
        results.setWaterPaNum(waterPaNum);
        results.setSumNum(sumNum);

        warnNums.setResults(results);
        getErrorInfo=JSON.toJSONString(warnNums);
        return getErrorInfo;
    }

    //    每个设备，只区分warning error
    @RequestMapping(value = "/getAllErrorOrWarn", method = RequestMethod.GET)
    public String getAllErrorOrWarn(@RequestParam(value="assignToken",required = true) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){

        List<Integer> warnNum =  new ArrayList<Integer>();
        List<Integer> errorNum = new ArrayList<Integer>();
        List<Integer> sumNum = new ArrayList<Integer>();

        WarnErrorNums warnErrorNums = new WarnErrorNums();
        model.warnOrError.Results results = new model.warnOrError.Results();
        DbAlertManeger.getInstance().init();
        Integer tempSum = 0;

        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllAlertorErrorInfo(sitewhereToken,assignToken,"Error",startMonths[i],endMonths[i]);
            errorNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(0,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllAlertorErrorInfo(sitewhereToken,assignToken,"Warning",startMonths[i],endMonths[i]);
            warnNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(1,tempSum);

        results.setErrorNum(errorNum);
        results.setWarnNum(warnNum);
        results.setSumNum(sumNum);

        warnErrorNums.setResults(results);
        getAllErrorOrWarn=JSON.toJSONString(warnErrorNums);
        return getAllErrorOrWarn;
    }

    //所有设备告警warning分类别次数
    @RequestMapping(value = "/getAllDeviceAlertInfo", method = RequestMethod.GET)
    public String getAllDeviceAlertInfo(@RequestParam(value="type",required = false) String type,@RequestParam(value="level",required = false) String level,@RequestParam(value="startDate",required = false) String startDate,@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="assignToken",required=false) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){

        DbAlertManeger.getInstance().init();

        List<Integer> engineNum =  new ArrayList<Integer>();
        List<Integer> outTempNum = new ArrayList<Integer>();
        List<Integer> inTempNum = new ArrayList<Integer>();
        List<Integer>  gasPaNum = new ArrayList<Integer>();
        List<Integer> waterPaNum = new ArrayList<Integer>();
        List<Integer> sumNum = new ArrayList<Integer>();

        WarnNums warnNums= new WarnNums();
        Results results = new Results();
        DbAlertManeger.getInstance().init();
        Integer tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Warning","engine.overheat",startMonths[i],endMonths[i]);
            engineNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(0,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Warning","inTemp.overheat",startMonths[i],endMonths[i]);
            inTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(1,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Warning","outTemp.overheat",startMonths[i],endMonths[i]);
            outTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(2,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Warning","gasPa.overPa",startMonths[i],endMonths[i]);
            gasPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(3,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Warning","waterPa.overPa",startMonths[i],endMonths[i]);
            waterPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(4,tempSum);

        results.setEngineNum(engineNum);
        results.setInTempNum(inTempNum);
        results.setOutTempNum(outTempNum);
        results.setGasPaNum(gasPaNum);
        results.setWaterPaNum(waterPaNum);
        results.setSumNum(sumNum);

        warnNums.setResults(results);
        getAllDeviceAlertInfo=JSON.toJSONString(warnNums);

        return getAllDeviceAlertInfo;
    }

    //所有设备告警Error分类别次数
    @RequestMapping(value = "/getAllDeviceErrorInfo", method = RequestMethod.GET)
    public String getAllDeviceErrorInfo(@RequestParam(value="type",required = false) String type,@RequestParam(value="level",required = false) String level,@RequestParam(value="startDate",required = false) String startDate,@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="assignToken",required=false) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){

        DbAlertManeger.getInstance().init();

        List<Integer> engineNum =  new ArrayList<Integer>();
        List<Integer> outTempNum = new ArrayList<Integer>();
        List<Integer> inTempNum = new ArrayList<Integer>();
        List<Integer>  gasPaNum = new ArrayList<Integer>();
        List<Integer> waterPaNum = new ArrayList<Integer>();
        List<Integer> sumNum = new ArrayList<Integer>();
        WarnNums warnNums= new WarnNums();
        Results results = new Results();
        DbAlertManeger.getInstance().init();
        Integer tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Error","engine.overheat",startMonths[i],endMonths[i]);
            engineNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(0,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Error","inTemp.overheat",startMonths[i],endMonths[i]);
            inTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(1,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Error","outTemp.overheat",startMonths[i],endMonths[i]);
            outTempNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(2,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Error","gasPa.overPa",startMonths[i],endMonths[i]);
            gasPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(3,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertInfo(sitewhereToken,"Error","waterPa.overPa",startMonths[i],endMonths[i]);
            waterPaNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(4,tempSum);

        results.setEngineNum(engineNum);
        results.setInTempNum(inTempNum);
        results.setOutTempNum(outTempNum);
        results.setGasPaNum(gasPaNum);
        results.setWaterPaNum(waterPaNum);
        results.setSumNum(sumNum);

        warnNums.setResults(results);
        getAllDeviceErrorInfo=JSON.toJSONString(warnNums);

        return getAllDeviceErrorInfo;
    }

    //所有设备，区分warning   error
    @RequestMapping(value = "/getAllDeviceAlertorErrorInfo", method = RequestMethod.GET)
    public String getAllDeviceAlertorErrorInfo(@RequestParam(value="level",required = false) String level,@RequestParam(value="startDate",required = false) String startDate,@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="assignToken",required=false) String assignToken,@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){

        DbAlertManeger.getInstance().init();

        List<Integer> warnNum =  new ArrayList<Integer>();
        List<Integer> errorNum = new ArrayList<Integer>();
        List<Integer> sumNum = new ArrayList<Integer>();

        WarnErrorNums warnErrorNums = new WarnErrorNums();
        model.warnOrError.Results results = new model.warnOrError.Results();
        DbAlertManeger.getInstance().init();
        Integer tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertorErrorInfo(sitewhereToken,"Error",startMonths[i],endMonths[i]);
            errorNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(0,tempSum);
        tempSum = 0;
        for(int i=0;i<startMonths.length;i++){
            Integer temp=DbAlertManeger.getInstance().getAllDeviceAlertorErrorInfo(sitewhereToken,"Warning",startMonths[i],endMonths[i]);
            warnNum.add(i,temp);
            tempSum+=temp;
        }
        sumNum.add(1,tempSum);

        results.setErrorNum(errorNum);
        results.setWarnNum(warnNum);
        results.setSumNum(sumNum);

        warnErrorNums.setResults(results);
        getAllDeviceErrorOrWarn=JSON.toJSONString(warnErrorNums);


        return getAllDeviceErrorOrWarn;
    }


}
