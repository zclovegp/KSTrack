package com.bonc.hbaserestful.trackv2.until;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaoc on 2017/9/1.
 */
public class CancelSubwayStop {
    public static List<HashMap<String, Object>> cancelSubwayStop(List<HashMap<String, Object>> list){

        return null;
    }
    public static void main(String[] args){
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("time","3");
        hm.put("type","3");
        HashMap<String,Object> hm2 = new HashMap<>();
        hm2.put("time","5");
        hm2.put("type","0");
        HashMap<String,Object> hm3 = new HashMap<>();
        hm3.put("time","8");
        hm3.put("type","2");
        HashMap<String,Object> hm4 = new HashMap<>();
        hm4.put("time","9");
        hm4.put("type","2");
        HashMap<String,Object> hm5 = new HashMap<>();
        hm5.put("time","12");
        hm5.put("type","0");
        HashMap<String,Object> hm6 = new HashMap<>();
        hm6.put("time","15");
        hm6.put("type","1");
        HashMap<String,Object> hm7 = new HashMap<>();
        hm7.put("time","18");
        hm7.put("type","0");
        List<HashMap<String,Object>> list = new ArrayList<>();list.add(hm);list.add(hm2);list.add(hm3);list.add(hm4);list.add(hm5);list.add(hm6);list.add(hm7);
        System.out.println("去掉不正常停驻点前"+list);
    }
}
