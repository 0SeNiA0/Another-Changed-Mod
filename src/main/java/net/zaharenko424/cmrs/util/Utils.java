package net.zaharenko424.cmrs.util;

import java.text.DecimalFormat;

public class Utils {

    public static long GB = 1024 * 1024 * 1024;
    public static long MB = 1024 * 1024;
    public static long KB = 1024;
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public static String memFormat(long bytes){
        if(bytes >= GB) return FORMAT.format(bytes / (float) GB) + "GB";
        if(bytes >= MB) return FORMAT.format(bytes / (float) MB) + "MB";
        if(bytes >= KB) return FORMAT.format(bytes / (float) KB) + "KB";
        return bytes + "B";
    }
}
