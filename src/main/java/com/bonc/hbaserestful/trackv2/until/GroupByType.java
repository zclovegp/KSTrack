package com.bonc.hbaserestful.trackv2.until;

import java.util.*;

/**
 * Created by zhaoc on 2017/8/30.
 */
public class GroupByType {
    public static List<List<HashMap<String,Object>>> groupByType(List<HashMap<String, Object>> list){
        HashMap<Integer,List<HashMap<String,Object>>> hm = new HashMap<>();
        int mark = 0;

        for(int i = 0;i<list.size()-1;i++){
            //判断类型是否一致
            String type = list.get(i).get("type").toString();
            String typeNext = list.get(i+1).get("type").toString();


            if (null != hm.get(mark)){
                List tmpList = hm.get(mark);
                tmpList.add(list.get(i));
                hm.put(mark,tmpList);
            }else{
                List tmpList = new ArrayList<>();
                tmpList.add(list.get(i));
                hm.put(mark,tmpList);
            }

            //如果类型不一致mark+1，这样下次不一样的数据就在mark+1的key中了
            if(!type.equals(typeNext)){
                mark++;
            }

            //对于最后一个元素，把他放在他应该在的mark中
            if(i==list.size()-2){
                if (null != hm.get(mark)){
                    String a = null;
                    List tmpList = hm.get(mark);
                    tmpList.add(list.get(i+1));
                    hm.put(mark,tmpList);
                }else{
                    List tmpList = new ArrayList<>();
                    tmpList.add(list.get(i+1));
                    hm.put(mark,tmpList);
                }
            }

        }

        //System.out.println("分组后的map"+hm);

        List<Map.Entry<Integer,List<HashMap<String,Object>>>> groupList = new ArrayList<Map.Entry<Integer,List<HashMap<String,Object>>>>(hm.entrySet());
        Collections.sort(groupList, new Comparator<Map.Entry<Integer,List<HashMap<String,Object>>>>() {
            public int compare(Map.Entry<Integer,List<HashMap<String,Object>>> o1, Map.Entry<Integer,List<HashMap<String,Object>>> o2) {
                //匹配搜索字多的在前面
                return (o1.getKey() - o2.getKey());
            }
        });
        //System.out.println("map变list"+groupList);

        //使list中增加混合的元素(里面就两个值)
        List<List<HashMap<String,Object>>> resultList = new ArrayList<>();
        for (int i = 0;i<groupList.size()-1;i++){
            //获取到type不同的list
            List<HashMap<String,Object>> tmpList = groupList.get(i).getValue();
            List<HashMap<String,Object>> tmpList2 = groupList.get(i+1).getValue();
            //混合部分的list，是新添加的元素，拿两个list的头和尾
            List<HashMap<String,Object>> tmpMix = new ArrayList<>();
            tmpMix.add(tmpList.get(tmpList.size()-1));
            tmpMix.add(tmpList2.get(0));
            //如果size是1的就不要了
            if(tmpList.size()!=1&&tmpList.size()!=0){
                resultList.add(tmpList);
            }
            //添加混合的
            resultList.add(tmpMix);
            //对于最后一个如果size是1的就不要了
            if(i==groupList.size()-2&&tmpList2.size()!=1&&tmpList2.size()!=0){
                resultList.add(tmpList2);
            }
        }
        //System.out.println(resultList);
            return resultList;
    }
}
