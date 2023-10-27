package com.ascklrt.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


public class GenerateNum {

    private static final AtomicLong account = new AtomicLong();
    private static final String dtMilliseconds = "yyyyMMddHHmmssSSS";
    public static Integer RefundApply = 1;
    public static Integer tradeSource = 03;
    /**
     * 结算单号生成类型
     */
    public static Integer settle = 4;
    /**
     * 付款单号生成类型
     */
    public static Integer payment = 5;

    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return
     * 以yyyyMMddHHmmss为格式的当前系统时间
     * @throws InterruptedException
     */
    public static   String getNum(){
        return getNum(null, dtMilliseconds);
    }
    public static String getNum(Integer bizType) {
        return getNum(bizType, dtMilliseconds);
    }
    public static String getNum(Integer bizType, String dateFormat) {
        Date date=new Date();
        DateFormat df=new SimpleDateFormat(dateFormat);
        if(account.get()==9999){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
            date=new Date();
            account.set(0);
        }
        String stringFormat = "%s%04d%s";
        if (bizType != null) {
            stringFormat = "%s%04d%s%d";
        }
        return String.format(stringFormat, df.format(date),
                account.incrementAndGet(), getRandomNum(2), bizType);
    }


    //获取4位数字随机验证码
    public static String getRandomNum(int num){

        String[] digits = {"1","2","3","4","5","6","7","8","9","0"};
        Random rnum = new Random(System.nanoTime());
        for(int i=0; i<digits.length;i++)
        {     int index = Math.abs(rnum.nextInt(num));
            String tmpDigit=digits[index];
            digits[index]=digits[i];
            digits[i]=tmpDigit;
        }
        String returnStr=digits[0];
        for(int i=1;i<num;i++)
        {
            returnStr=digits[i]+returnStr;
        }
        return   returnStr;
    }


}
