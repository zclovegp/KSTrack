package com.bonc.hbaserestful.trackv2;

import com.bonc.hbaserestful.trackv2.until.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaoc on 2017/8/28.
 */

@RestController
@CrossOrigin(origins = "*")
@Api(value="轨迹",description="当天人物轨迹图")
@RequestMapping({"/api/tag"})
public class GetTodayTrack {

    private static HashMap<String,String> baseStationCodeMap = null;
    /**
     * 获取码表
     **/
    public static void getBaseStationCodeMap() throws IOException {
        if(baseStationCodeMap == null){
            baseStationCodeMap = BaseStationCode.getMap();
        }
    }

    @ApiOperation("调用查询参数")
    @PostMapping("/trail")
    public Object search(@RequestBody @ApiParam("参数") String param){
        System.out.println("前台的参数是:"+param);

        String[] paramArr = param.split(",",-1);
        String phoneNum = paramArr[0];
        String startDate = paramArr[1];
        String startTime = paramArr[2];
        String endDate = paramArr[3];
        String endTime = paramArr[4];

        //获取当天日期
        Calendar cal = Calendar.getInstance();
        String todayDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        //有空的那么
        if(startDate.equals("-1")||startTime.equals("-1")||endDate.equals("-1")||endTime.equals("-1")){
            startDate = todayDate;
            startTime = "00";
            endDate = todayDate;
            endTime = "24";
        }

        //获取api数据
        List<HashMap<String, Object>> list = GetDataFromURL.getOneDay(phoneNum,startDate,startTime,endDate,endTime,baseStationCodeMap);

        //添加移动方式(0:驻留 1:步行 2:驾车 3:公交)
        List<HashMap<String, Object>> listF = AddTag.addTag(list);

        //需要对出行方式判断然后过滤一些影响点（两个停驻点之间）
        List<HashMap<String, Object>> listFF = FilterDiffSpeed.filterDiffSpeed(listF);
        if(listFF.size()==2){
            List<HashMap<String,Object>> listR = new ArrayList<>();
            List<List<HashMap<String,Object>>> listGrouped2 = new ArrayList<>();
            listGrouped2.add(listFF);
            HashMap<String,Object> hmR = new HashMap<>();
            hmR.put("trail",listGrouped2);
            listR.add(hmR);
            return listR;
        }

        //按照type分组重组list
        List<List<HashMap<String,Object>>> listGrouped = GroupByType.groupByType(listFF);

        //拼接返回结果
        List<HashMap<String,Object>> listR = new ArrayList<>();
        HashMap<String,Object> hmR = new HashMap<>();
        hmR.put("trail",listGrouped);
        listR.add(hmR);
        //System.out.println(listF);

        return listR;
    }

    @ApiOperation("调用查询参数")
    @PostMapping("/trailOrigin")
    public Object searchOrigin(@RequestBody @ApiParam("参数") String param){
        System.out.println("前台的参数是:"+param);

        String[] paramArr = param.split(",",-1);
        String phoneNum = paramArr[0];
        String startDate = paramArr[1];
        String startTime = paramArr[2];
        String endDate = paramArr[3];
        String endTime = paramArr[4];

        //获取当天日期
        Calendar cal = Calendar.getInstance();
        String todayDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        //有空的那么
        if(startDate.equals("-1")||startTime.equals("-1")||endDate.equals("-1")||endTime.equals("-1")){
            startDate = todayDate;
            startTime = "00";
            endDate = todayDate;
            endTime = "24";
        }

        //获取api数据
        List<HashMap<String, Object>> list = GetDataFromURL.getOneDayOrigin(phoneNum,startDate,startTime,endDate,endTime,baseStationCodeMap);

        //拼接返回结果
        List<HashMap<String,Object>> listR = new ArrayList<>();
        HashMap<String,Object> hmR = new HashMap<>();
        hmR.put("trail",list);
        listR.add(hmR);
        //System.out.println(listF);

        return listR;
    }
}
