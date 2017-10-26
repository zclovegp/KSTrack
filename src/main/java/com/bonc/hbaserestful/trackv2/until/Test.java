package com.bonc.hbaserestful.trackv2.until;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import static com.bonc.hbaserestful.transformlnglat.WGStoBD.GCJ02toBD09;
import static com.bonc.hbaserestful.transformlnglat.WGStoBD.transform;


/**
 * Created by zhaoc on 2017/9/1.
 */



public class Test {
    public static void main(String[] args) throws ParseException, IOException {

        double[] latlon_bd= GCJ02toBD09(transform(39.79763,116.46485));

        if (latlon_bd==null) {System.out.println("输入坐标不正确！"); }
        else {
            System.out.println(latlon_bd[0]+","+latlon_bd[1]);
        }

    }
}


