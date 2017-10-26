package com.bonc.hbaserestful.trackv2.until;

/**
 * Created by zhaoc on 2016/9/18.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class BaseStationCode {

    public static HashMap<String,String> mm = new HashMap<String,String>();

    public static HashMap<String,String> getMap() throws IOException {
        //String path = "/home/all_signal_dev/zc/code";
        String path = "C:\\Users\\zhaoc\\Desktop\\code";
        //String path = "C:\\Users\\zhan\\Desktop\\code";
        FileReader fr = null;
        BufferedReader br = null;


            for (File file : new File(path).listFiles()) {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                String line = null;

                while ((line = br.readLine()) != null) {
                    String[] s=line.split("\\|",-1);
                    //lac+"|"+ci
                    String key=s[0]+"|"+s[1];
                    //lng道路+lat道路+基站类型+室内外+lng基站+lat基站
                    String value=s[8]+"|"+s[7]+"|"+s[10]+"|"+s[2]+"|"+s[4]+"|"+s[3];
                    mm.put(key,value);
            }

        }
        br.close();
        fr.close();
        System.out.println("基站码表的大小是"+mm.size());

        return mm;
    }

}
