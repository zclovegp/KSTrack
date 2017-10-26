package com.bonc.hbaserestful.trackv2.until;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaoc on 2017/8/29.
 */
public class AddTag {
    public static List<HashMap<String, Object>> addTag(List<HashMap<String, Object>> list){
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        HashMap<String,String> speedMap = GetSpeed.getSpeedMap();
        for(int i=0;i<list.size();i++){
            HashMap<String, Object> tmpHm = new HashMap<>();
            //目前没有发现省份id为空的
            String provinceId = list.get(i).get("provinceId").toString();
            //1：高速，2：铁路，3：地铁
            String baseType = list.get(i).get("baseType").toString();
            //驻留点 1驻留 0非驻留
            String is_stay = list.get(i).get("is_stay").toString();
            double speedStandard = Double.parseDouble(speedMap.get(provinceId));
            String type = "";
            String highSpeed = "";
            double speed = Double.parseDouble(list.get(i).get("speed").toString());
            //1步行 2驾车 3公交 0驻留
            if(speed<=speedStandard){
                type="1";
            }else{
                //普通开车出行
                type="2";
            }
            //基站判断的优先级判断高于速度判断
            if(baseType.equals("1")){
                //走高速出行
                type="222";
                //highSpeed="222";
            }
            if(baseType.equals("2")||baseType.equals("3")){
                type="3";
            }
            //驻留点的优先级最高
            if(is_stay.equals("1")){
                type="0";
            }
            tmpHm.put("lng",list.get(i).get("lng"));
            tmpHm.put("lat",list.get(i).get("lat"));
            tmpHm.put("currentTime",list.get(i).get("currentTime"));
            tmpHm.put("speed",list.get(i).get("speed"));
            //1室外 2室内
            tmpHm.put("inOut",list.get(i).get("inOut").equals("1")?"室外":"室内");
            tmpHm.put("type",type);
            tmpHm.put("order",i+1);
            //tmpHm.put("highSpeed",highSpeed);

            resultList.add(tmpHm);
        }

        //当天的第一个点和最后一个点设置成为停驻点
        if(list.size()>1){
            HashMap<String, Object> tmpHm1 = resultList.get(0);
            tmpHm1.put("type","0");
            HashMap<String, Object> tmpHm2 = resultList.get(resultList.size()-1);
            tmpHm2.put("type","0");
            resultList.set(0,tmpHm1);
            resultList.set(resultList.size()-1,tmpHm2);
        }

        return resultList;
    }
}
