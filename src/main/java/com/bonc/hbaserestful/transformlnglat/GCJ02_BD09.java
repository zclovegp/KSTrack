package com.bonc.hbaserestful.transformlnglat;

/**
 * Created by zhaoc on 2016/9/22.
 */
public class GCJ02_BD09 {
    static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    /*
     * 将 GCJ-02 坐标转换成 BD-09 坐标
     */
    public static void GCJ02toBD09(double gg_lat, double gg_lon)
    {
        double bd_lat;
        double bd_lon;
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        bd_lon = z * Math.cos(theta) + 0.0065;
        bd_lat = z * Math.sin(theta) + 0.006;
        System.out.println(bd_lat+","+bd_lon);
    }
/*    public static void main(String[] args) {
        GCJ02toBD09(25.667604605395884,100.15099744474743);
    }*/
}
