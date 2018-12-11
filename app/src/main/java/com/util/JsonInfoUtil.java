//package com.util;
//
//import com.example.acer.letterbox4.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created by BaiXiaozhe on 2018/10/14.
// */
//
//public class JsonInfoUtil {
//    //从网络上获取指定城市名称天气预报JSon字符串的方法
//    public static String getJSonStr(String cityName)throws Exception{
//        String jSonStr=null;
//        StringBuilder sb=new StringBuilder("http://api.map.baidu.com/telematics/v3/weather?location=");
//        String str= URLEncoder.encode(cityName,"UTF-8");//城市名称
//        sb.append(str);
//        sb.append("&output=json&ak=pbTSH1uHqAEnUkPbCwwYXB1fzOHQjTyl");
//        String urlStr=null;
//        urlStr=new String(sb.toString().getBytes());//转换成字符串格式
//        URL url=new URL(urlStr);
//        URLConnection uc=url.openConnection();//打开网址
//        uc.setRequestProperty("accept-language","zh_CN");//设置为中文
//        InputStream in=uc.getInputStream();
//        int ch=0;
//        ByteArrayOutputStream baos=new ByteArrayOutputStream();
//        while((ch=in.read())!=-1){
//            baos.write(ch);//以字符串的形式读进来
//        }
//        byte[] bb=baos.toByteArray();//转换成byte数组
//        baos.close();
//        in.close();
//        jSonStr=new String(bb);//将byte数组转换成字符串
//        return UnicodeToString(jSonStr);//将unicode转换成中文
//    }
//    //解析JSon字符串，得到应用程序需要的城市名称、天气描述、温度、风力风向、图片
//    public static TQYBInfo parseJSon(String jSonStr)throws Exception{
//        TQYBInfo result=new TQYBInfo();//定义TQYBInfo对象
//        JSONObject json=new JSONObject(jSonStr);
//        JSONArray obj=json.getJSONArray("results");
//        JSONObject temp=new JSONObject(obj.getString(0));
//        result.city=temp.getString("currentCity");//得到城市名称
//        JSONArray obj3=temp.getJSONArray("weather_data");//得到天气数据
//        JSONObject temp2=new JSONObject(obj3.getString(0));
//        result.date=temp2.getString("date");//得到日期
//        result.tqms=temp2.getString("weather");//得到天气
//        result.wd=temp2.getString("temprature");//得到温度
//        result.flfx=temp2.getString("wind");//得到风力风向
//        result.pic=getChangePicID(temp2.getString("dayPictureUrl"));//得到天气图片
//        return result;
//    }
//    public static int getChangePicID(String img){
//        //将从网络上获取的图片替换成本地的图片
//        String[] group=img.split("/");
//        int id=0;
//        if(group[group.length-1].equals("qing.png")){
//            //晴
//            id=R.drawable.qing;
//        }
//        //暂时省略其他天气情况时转换天气图片的代码
//        return 0;
//    }
//    public static String UnicodeToString(String str){
//        //将unicode替换成中文
//        Pattern pattern=Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
//        Matcher matcher=pattern,matcher(str);
//        char ch;
//        while(matcher.find()){
//            ch=(char)Integer.parseInt(matcher.group(2),16);//转换成单个字符
//            str=str.replace(matcher.group(1),ch+"");//转换成字符串
//        }
//        return str;
//    }
//}
