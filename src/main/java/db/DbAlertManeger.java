package db;

import model.ALertInfoBean;
import model.AlertListBean;
import model.DeviceInfoBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbAlertManeger {
    private static DbAlertManeger dbAlertManeger;
    private Connection conn = null;
    //siteware 是数据库名，根据需要修改
    private String url = "jdbc:mysql://localhost:3306/siteware?useUnicode=true&characterEncoding=utf-8";
    private DbAlertManeger(){}
    public static DbAlertManeger getInstance(){
        if(dbAlertManeger==null){
            synchronized (DbAlertManeger.class){
                if(dbAlertManeger==null){
                    dbAlertManeger=new DbAlertManeger();
                }
            }
        }
        return dbAlertManeger;
    }
    public void init(){
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            conn = DriverManager.getConnection(url,"root","1234");
            System.out.println("连接数据库成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    //增加
    public void insert(AlertListBean alertListBean){
        String sql = "insert into alertInfo(id,eventType,siteToken,deviceAssignmentToken,assignmentType,assetModuleId,assetId,eventDate,receivedDate,source,level,type,message,sitewhereToken) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt;
        System.out.println(alertListBean);
        if(alertListBean==null||alertListBean.results.size()==0) return;
        try {
            pstmt=conn.prepareStatement(sql);
            for(int i=0;i<alertListBean.results.size();i++){
                pstmt.setString(1,alertListBean.results.get(i).getId());
                pstmt.setString(2,alertListBean.results.get(i).getEventType());
                pstmt.setString(3,alertListBean.results.get(i).getSiteToken());
                pstmt.setString(4,alertListBean.results.get(i).getDeviceAssignmentToken());
                pstmt.setString(5,alertListBean.results.get(i).getAssignmentType());
                pstmt.setString(6,alertListBean.results.get(i).getAssetModuleId());
                pstmt.setString(7,alertListBean.results.get(i).getAssetId());
                pstmt.setString(8,alertListBean.results.get(i).getEventDate());
                pstmt.setString(9,alertListBean.results.get(i).getReceivedDate());
                pstmt.setString(10,alertListBean.results.get(i).getSource());
                pstmt.setString(11,alertListBean.results.get(i).getLevel());
                pstmt.setString(12,alertListBean.results.get(i).getType());
                pstmt.setString(13,alertListBean.results.get(i).getMessage());
                pstmt.setString(14,alertListBean.results.get(i).getSitewhereToken());
                pstmt.execute();
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public List<ALertInfoBean> getAlertInfo(String sitewhereToken,String assignToken,String level,String type,String startDate,String endDate,int pageIndex,int pageSize){
        List<ALertInfoBean> aLertInfoBeans=new ArrayList<ALertInfoBean>();
        String sql = "SELECT * FROM alertInfo WHERE sitewhereToken=? and deviceAssignmentToken=? and level=? and type=? and eventDate>=? and eventDate<=? order by eventDate desc limit ?,?";    //要执行的SQL
        PreparedStatement pstmt;
        ResultSet rs=null;
        System.out.println(sql);
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,sitewhereToken);
            pstmt.setString(2,assignToken);
            pstmt.setString(3,level);
            pstmt.setString(4,type);
            pstmt.setString(5,startDate);
            pstmt.setString(6,endDate);
            pstmt.setInt(7,(pageIndex-1)*pageSize);
            pstmt.setInt(8,pageSize);
            rs = pstmt.executeQuery();//创建数据对象
            while (rs.next()){
                ALertInfoBean aLertInfoBean=new ALertInfoBean();
                aLertInfoBean.setId(rs.getString(1));
                aLertInfoBean.setEventType(rs.getString(2));
                aLertInfoBean.setSitewhereToken(rs.getString(3));
                aLertInfoBean.setSiteToken(rs.getString(4));
                aLertInfoBean.setDeviceAssignmentToken(rs.getString(5));
                aLertInfoBean.setAssignmentType(rs.getString(6));
                aLertInfoBean.setAssetModuleId(rs.getString(7));
                aLertInfoBean.setAssetId(rs.getString(8));
                aLertInfoBean.setEventDate(rs.getString(9));
                aLertInfoBean.setReceivedDate(rs.getString(10));
                aLertInfoBean.setSource(rs.getString(11));
                aLertInfoBean.setLevel(rs.getString(12));
                aLertInfoBean.setType(rs.getString(13));
                aLertInfoBean.setMessage(rs.getString(14));
                System.out.println(rs.getString(14));
                aLertInfoBeans.add(aLertInfoBean);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        }
        System.out.println(aLertInfoBeans.size());
        return aLertInfoBeans;
    }



    public String getLastTime(String sitewhereToken,String assignToken){
        String sql = "SELECT eventDate FROM alertInfo where sitewhereToken=? and deviceAssignmentToken=?  order by eventDate desc limit 1";    //要执行的SQL
        PreparedStatement pstmt;
        ResultSet rs=null;
        try {
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,sitewhereToken);
            pstmt.setString(2,assignToken);
            rs = pstmt.executeQuery();//创建数据对象
            while(rs.next()){
                return rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
