package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import db.DbAlertManeger;
import db.DbDeviceManage;
import model.ALertInfoBean;
import model.AlertListBean;
import model.DeviceList.Data;
import network.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataManager {
    private static DataManager dataManager;
    private DataManager(){}
    public static DataManager getInstance(){
        if(dataManager==null){
            synchronized (DataManager.class){
                if(dataManager==null){
                    dataManager=new DataManager();
                }
            }
        }
        return dataManager;
    }

    public static void loadDataToDb(){
        DbDeviceManage.getInstance().init();
        List<String> sitewareTokens= DbDeviceManage.getInstance().getSitewareTokens();
        DbDeviceManage.getInstance().close();
        for(String token:sitewareTokens){
            System.out.println(token);
            List<String> assignTokens=new ArrayList<String>();
            String assignUrl="http://localhost:8080/sitewhere/api/devices?includeDeleted=false&excludeAssigned=false&includeSpecification=true&includeAssignment=true";
            String allDeviceList = NetworkUtils.doGetAsync(assignUrl, token);
            JSONObject object=JSON.parseObject(allDeviceList);
            JSONArray array=(JSONArray) object.get("results");
            for(int i=0;i<array.size();i++){
                JSONObject o1=(JSONObject) array.get(i);
                JSONObject o2=(JSONObject) o1.get("assignment");
                assignTokens.add((String)o2.get("token"));
            }
            System.out.println(assignTokens.size());
            for(String assignToken:assignTokens){
                System.out.println(assignToken);
                loadOneDeviceInfo(token,assignToken);
            }

        }
    }

    public static void loadOneDeviceInfo(String sitewhereToken,String assignToken){
        String currentDate=TimeUtils.getCurrentTime();
        System.out.println("data:"+currentDate);
        String url;
        AlertListBean alertListBean= null;
        DbAlertManeger.getInstance().init();
        String lastDate=DbAlertManeger.getInstance().getLastTime(sitewhereToken,assignToken);
        if(lastDate==null){
            System.out.println("is null");
            url = "http://localhost:8080/sitewhere/api/assignments/"+assignToken+"/alerts?startDate="+"2018-05-01"+"&endDate="+currentDate;
            System.out.println(url);
        }else{
            lastDate=lastDate.split("T")[0];
            System.out.println("not null");
            url = "http://localhost:8080/sitewhere/api/assignments/"+assignToken+"/alerts?startDate="+lastDate+"&endDate="+currentDate;
        }
        String result = NetworkUtils.doGetAsync(url, sitewhereToken);
        System.out.println(result);
        alertListBean= JSON.parseObject(result,AlertListBean.class);
        if(alertListBean!=null){
            for(ALertInfoBean bean:alertListBean.getaLertInfoBeanList()){
                bean.setSitewhereToken(sitewhereToken);
            }
            DbAlertManeger.getInstance().insert(alertListBean);
            System.out.println("insert");
        }
    }
    public void  startLoading(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                loadDataToDb();
            }
        };
        ScheduledExecutorService service= Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable,0,24, TimeUnit.DAYS);
    }
}
