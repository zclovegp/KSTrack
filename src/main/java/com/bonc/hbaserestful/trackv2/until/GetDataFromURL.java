package com.bonc.hbaserestful.trackv2.until;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.hbaserestful.transformlnglat.WGStoBD;
import com.bonc.hbaserestful.util.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bonc.hbaserestful.transformlnglat.WGStoBD.transform;

/**
 * Created by zhaoc on 2017/8/28.
 */
public class GetDataFromURL {
    public static List<HashMap<String,Object>> getOneDay(String phoneNumber, String startDate,String startTime, String endDate,String endTime, HashMap<String,String> baseStationCodeMap) {
        String orData = null;
        List<HashMap<String,Object>> resultList = new ArrayList<>();
        List dataList = new ArrayList<>();
        try {

            orData = HttpUtils.doGet("http://10.162.20.30:8090/api/v1/track?keyId=Bs7I67Nn4uc=&token=c2e18e7d2164fe4a87187918f6c8668b&phone="+ phoneNumber +"&timestamp=1503651859838&startDate="+ startDate +"&startHour="+startTime+"&&endDate=" + endDate +"&endHour="+endTime);

        } catch (Exception e) {
        }
        JSONObject jsonData = JSON.parseObject(orData);
        dataList = (List) jsonData.get("data");
        int i;
        int base=0;
        for (i = 0; i < dataList.size(); i++) {
            JSONObject finalJson = JSON.parseObject(dataList.get(i).toString());
            //清理脏数据
            if(!(finalJson.get("latitude").equals("")||finalJson.get("longitude").equals("")||
                    Double.parseDouble(finalJson.get("latitude").toString())>Double.parseDouble(finalJson.get("longitude").toString())
                    ||finalJson.get("cityId").equals("")||finalJson.get("provinceId").equals(""))){

                HashMap<String,Object> hm = new HashMap<>();
                String lng = finalJson.get("longitude").toString();
                String lat = finalJson.get("latitude").toString();
                //基站类型
                String baseType = "";
                //室内室外
                String inOut = "";
                String createTime = finalJson.get("createTime").toString();
                String distance = finalJson.get("distance").toString();
                String speed = finalJson.get("speed").toString();
                String is_stay = finalJson.get("is_stay").toString();
                String cityId = finalJson.get("cityId").toString();
                String provinceId = finalJson.get("provinceId").toString();
                //一个基站的唯一标识（lac+ci）
                String lac = finalJson.get("lac").toString();
                String ci = finalJson.get("ci").toString();


                //判断是否在基站码表中存在
                if(baseStationCodeMap.get(lac+"|"+ci)!=null){
                    String[] lngLatType = baseStationCodeMap.get(lac+"|"+ci).split("\\|",-1);
                    lng = lngLatType[0];
                    lat = lngLatType[1];
                    baseType = lngLatType[2];
                    inOut=lngLatType[3];
                    //如果匹配到的码表中的道路上的经纬度是""，那么按照基站经纬度来，但是要转换坐标！
                    if(lngLatType[0].equals("")||lngLatType[1].equals("")){
                        lng =  finalJson.get("longitude").toString();
                        lat = finalJson.get("latitude").toString();
                        //坐标转换
                        double wgLon = Double.parseDouble(lng);
                        double wgLat = Double.parseDouble(lat);

                        double[] latLonBD = WGStoBD.GCJ02toBD09(transform(wgLat, wgLon));
                        if (latLonBD == null) {
                            String b = null;
                            System.out.println("wgLon==================="+wgLon);
                            System.out.println("wgLat==================="+wgLat);
                            System.out.println("输入的坐标为空！或者超出中国范围了");
                            continue;
                        }else{
                            lng = String.valueOf(latLonBD[1]);
                            lat = String.valueOf(latLonBD[0]);
                        }
                    }
                    //如果是地铁的室内基站那么经纬度就用地铁内的那个点，但是要转换坐标
                    if(inOut.equals("2")&&baseType.equals("3")){
                        //lng =  lngLatType[4];
                        //lat = lngLatType[5];
                        lng =  finalJson.get("longitude").toString();
                        lat = finalJson.get("latitude").toString();
                        //坐标转换
                        double wgLon = Double.parseDouble(lng);
                        double wgLat = Double.parseDouble(lat);

                        double[] latLonBD = WGStoBD.GCJ02toBD09(transform(wgLat, wgLon));
                        if (latLonBD == null) {
                            System.out.println("wgLon==================="+wgLon);
                            System.out.println("wgLat==================="+wgLat);
                            String c = null;
                            System.out.println("输入的坐标为空！或者超出中国范围了");
                            continue;
                        }else{
                            lng = String.valueOf(latLonBD[1]);
                            lat = String.valueOf(latLonBD[0]);
                        }
                    }
                    base++;
                }else{
                    //坐标转换
                    double wgLon = Double.parseDouble(lng);
                    double wgLat = Double.parseDouble(lat);

                    double[] latLonBD = WGStoBD.GCJ02toBD09(transform(wgLat, wgLon));
                    if (latLonBD == null) {
                        System.out.println("wgLon==================="+wgLon);
                        System.out.println("wgLat==================="+wgLat);
                        System.out.println("输入的坐标为空！或者超出中国范围了");
                        continue;
                    }else{
                        lng = String.valueOf(latLonBD[1]);
                        lat = String.valueOf(latLonBD[0]);
                    }
                }

                hm.put("lng",lng);
                hm.put("lat",lat);
                hm.put("currentTime",createTime);
                hm.put("distance",distance);
                hm.put("speed",speed);
                hm.put("is_stay",is_stay);
                hm.put("cityId",cityId);
                hm.put("baseType",baseType);
                hm.put("provinceId",provinceId);
                hm.put("inOut",inOut);

                resultList.add(hm);
            }
        }
        System.out.println("基站码表命中"+base);
        return resultList;
    }

    public static List<HashMap<String,Object>> getOneDayOrigin(String phoneNumber, String startDate,String startTime, String endDate,String endTime, HashMap<String,String> baseStationCodeMap) {
        String orData = null;
        List<HashMap<String,Object>> resultList = new ArrayList<>();
        List dataList = new ArrayList<>();
        try {

            orData = HttpUtils.doGet("http://10.162.20.30:8090/api/v1/track?keyId=Bs7I67Nn4uc=&token=c2e18e7d2164fe4a87187918f6c8668b&phone="+ phoneNumber +"&timestamp=1503651859838&startDate="+ startDate +"&startHour="+startTime+"&&endDate=" + endDate +"&endHour="+endTime);

        } catch (Exception e) {
        }
        JSONObject jsonData = JSON.parseObject(orData);
        dataList = (List) jsonData.get("data");
        int i;
        int base=0;
        for (i = 0; i < dataList.size(); i++) {
            JSONObject finalJson = JSON.parseObject(dataList.get(i).toString());
            //清理脏数据
            if(!(finalJson.get("latitude").equals("")||finalJson.get("longitude").equals("")||
                    Double.parseDouble(finalJson.get("latitude").toString())>Double.parseDouble(finalJson.get("longitude").toString())
                    ||finalJson.get("cityId").equals("")||finalJson.get("provinceId").equals(""))){

                HashMap<String,Object> hm = new HashMap<>();
                String lng = finalJson.get("longitude").toString();
                String lat = finalJson.get("latitude").toString();
                //基站类型
                String baseType = "";
                //室内室外
                String inOut = "";
                String createTime = finalJson.get("createTime").toString();
                String distance = finalJson.get("distance").toString();
                String speed = finalJson.get("speed").toString();
                String is_stay = finalJson.get("is_stay").toString();
                String cityId = finalJson.get("cityId").toString();
                String provinceId = finalJson.get("provinceId").toString();

                //坐标转换
                double wgLon = Double.parseDouble(lng);
                double wgLat = Double.parseDouble(lat);
                double[] latLonBD = WGStoBD.GCJ02toBD09(transform(wgLat, wgLon));
                if (latLonBD == null) {
                    System.out.println("wgLon==================="+wgLon);
                    System.out.println("wgLat==================="+wgLat);
                    System.out.println("输入的坐标为空！或者超出中国范围了");
                    String a = null;
                    continue;
                }else{
                    lng = String.valueOf(latLonBD[1]);
                    lat = String.valueOf(latLonBD[0]);
                }

                hm.put("lng",lng);
                hm.put("lat",lat);
                hm.put("currentTime",createTime);
                hm.put("distance",distance);
                hm.put("speed",speed);
                hm.put("is_stay",is_stay);
                hm.put("cityId",cityId);
                hm.put("baseType",baseType);
                hm.put("provinceId",provinceId);
                hm.put("inOut",inOut);

                resultList.add(hm);
            }
        }
        return resultList;
    }
}
