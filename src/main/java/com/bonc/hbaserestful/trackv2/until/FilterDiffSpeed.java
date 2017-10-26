package com.bonc.hbaserestful.trackv2.until;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaoc on 2017/8/31.
 */
public class FilterDiffSpeed {
    public static List<HashMap<String, Object>> filterDiffSpeed(List<HashMap<String, Object>> list){
        //那些type不能改，要去掉
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        int start = 0;
        int end = 0;
        int firstDiff = 0;
        int count = 0;
        List<String> splitIndex = new ArrayList<>();
        //找驻留点的下标
        for(int i = 0;i<list.size();i++){
            //进入0开始或者结束范围
            if(list.get(i).get("type").toString().equals("0")){
                if(start!=0||firstDiff==1){
                    end = i;
                    splitIndex.add(start+","+end+","+list.get(start).get("currentTime")+","+list.get(end).get("currentTime"));
                    start=0;
                    end = 0;
                }
                if(end==0){
                    start=i;
                    if(i==0){
                        firstDiff = 1;
                    }
                }
                //记录有几个停驻点
                count++;
            }
        }
        System.out.println("$$$$$停驻点的索引下标"+splitIndex);

        //分割list,如果有超过1个的停驻点才分割，否则直接返回原来的list不分割
        if(count>1){
            if(list.get(0).get("type").toString().equals("0")||list.get(list.size()-1).get("type").toString().equals("0")){
                //0在开头
                if (list.get(0).get("type").toString().equals("0")&&!list.get(list.size()-1).get("type").toString().equals("0")){
                    //System.out.println("在开头");
                    resultList.addAll(SplitListType0.splitListType0(splitIndex, list));
                    String[] last = splitIndex.get(splitIndex.size()-1).split(",");
                    //最后一个0后的元素
                    int l = Integer.parseInt(last[1]);
                    for(int i=l+1;i<list.size();i++){
                        resultList.add(list.get(i));
                    }
                    return resultList;
                    //0在结尾
                }else if(list.get(list.size()-1).get("type").toString().equals("0")&&!list.get(0).get("type").toString().equals("0")){
                    //System.out.println("在结尾");
                    String[] first = splitIndex.get(0).split(",");
                    int f = Integer.parseInt(first[0]);
                    for(int i=0;i<f;i++){
                        resultList.add(list.get(i));
                    }
                    resultList.addAll(SplitListType0.splitListType0(splitIndex, list));
                    return resultList;
                    //0是开头又是结尾的
                }else{
                    //System.out.println("在两边");
                    //目前的结果集头尾都有停驻点
                    if(list.size()==2){
                        return list;
                    }
                    resultList.addAll(SplitListType0.splitListType0(splitIndex, list));
                    return resultList;
                }
                //0在中间的
            }else{
                //第一个0前的元素
                String[] first = splitIndex.get(0).split(",");
                int f = Integer.parseInt(first[0]);
                for(int i=0;i<f;i++){
                    resultList.add(list.get(i));
                }
                resultList.addAll(SplitListType0.splitListType0(splitIndex, list));
                String[] last = splitIndex.get(splitIndex.size()-1).split(",");
                //最后一个0后的元素
                int l = Integer.parseInt(last[1]);
                for(int i=l+1;i<list.size();i++){
                    resultList.add(list.get(i));
                }
                return resultList;
            }
        }else{
            return list;
        }
    }
}
