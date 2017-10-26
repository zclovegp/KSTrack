package com.bonc.hbaserestful.trackv2.until;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaoc on 2017/8/31.
 */
public class SplitListType0 {
    public static List<HashMap<String, Object>> splitListType0(List<String> index,List<HashMap<String, Object>> list){
        List<HashMap<String, Object>> result = new ArrayList<>();
        //添加出行判断的参数flag
        for(int i = 0;i<index.size();i++){
            String[] se = index.get(i).toString().split(",");
            int start = Integer.parseInt(se[0]);
            int end = Integer.parseInt(se[1]);
            double type2 = 0;
            double type3 = 1;
            double type1And2And222 = 1;
            double typeHigh = 1;
            for(int j=start;j<end;j++){
                //这里需要判断两个停驻点的出行方案
                    String type = list.get(j).get("type").toString();
                    if(type.equals("3")){
                        type3++;
                    }else if(type.equals("1")||type.equals("2")||type.equals("222")){
                        type1And2And222++;
                    }
                    if(type.equals("2")){
                        type2++;
                    }
                    if(type.equals("222")){
                        typeHigh++;
                    }

            }
            //决定出行方式的flag(+2是两个停驻点也算上了)
            //大于0.4认为地铁出行
            double flag = type3/(type1And2And222+2);
            //大于0.3认为开车中的高速出行
            double flagHigh = typeHigh/(type1And2And222+2);
            System.out.println("地铁出行type点个数："+type3+",开车步行高速停驻的type点个数："+(type1And2And222+2));
            System.out.println("高速出行type点个数："+typeHigh);
            index.set(i,index.get(i)+","+flag+","+flagHigh+","+type2);
        }
        System.out.println("$$$$$停驻点的索引下标+出行方式:===>"+index);

        //使用出行方式对数据筛选!!!!!
        for(int i = 0;i<index.size();i++){
            String[] se = index.get(i).toString().split(",");
            int start = Integer.parseInt(se[0]);
            int end = Integer.parseInt(se[1]);
            double flag = Double.parseDouble(se[4]);
            double flagHigh = Double.parseDouble(se[5]);
            double type2 = Double.parseDouble(se[6]);
            for(int j=start;j<end;j++){
                //要把每个0开头的那个0加到整个结果集中
                if (j==start){
                    result.add(list.get(start));
                }
                //这里需要判断两个停驻点的出行方案(地铁比剩余=2：5)
                if(flag >= 0.4) {
                    //纯地铁
                    if (list.get(j).get("type").toString().equals("3")) {
                        result.add(list.get(j));
                    }
                }else {
                    //纯高速出行
                    if (flagHigh >= 0.3) {
                        if (list.get(j).get("type").toString().equals("222")){
                            HashMap<String, Object> tmp = list.get(j);
                            tmp.put("type", "2");
                            tmp.put("typeDesc", "这是高速出行");
                            result.add(tmp);
                        }
                        //普通开车或者步行出行
                    }else{
                    if (list.get(j).get("type").toString().equals("1") || list.get(j).get("type").toString().equals("2")) {
                        //纯开车
                        if (type2 != 0) {
                            if (list.get(j).get("type").toString().equals("2")) {
                                result.add(list.get(j));
                            } else {
                                HashMap<String, Object> tmp = list.get(j);
                                tmp.put("type", "2");
                                result.add(tmp);
                            }
                        } else {
                            //纯步行
                            result.add(list.get(j));
                        }
                    }
                }
                }
            }
            //把最后一个0加到结果集中
            if (i==index.size()-1){
                result.add(list.get(end));
            }
        }

        return result;
    }
}
