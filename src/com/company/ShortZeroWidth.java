package com.company;

public class ShortZeroWidth {

    /*
    A
    ﻿
    

    ﻿
    B


        str = str.replace("\\u200a","00");
        str = str.replace("\\u200b","01");
        str = str.replace("\\u200c","10");
        str = str.replace("\\u200d","11");
     */

    public static void start() {
        //System.out.println("\u200a");
        //System.out.println("\u200b");
        //System.out.println("\u200c");
        //System.out.println("\u200d");

        String data = "To be or not to be– that is the question\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                " And, by opposing, end them.";
        String enc = ZeroWidthWrdEncryption(data);
        String str = "A|" + enc + "|B";
        System.out.println("Enc:" + str);
        System.out.println("Dec:" + ZeroWidthWordDecryption(str));
    }

    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    public static String strToBinStr(String str) {
        char[] chars=str.toCharArray();
        StringBuffer result = new StringBuffer();
        for(int i=0; i<chars.length; i++) {
            result.append(Integer.toBinaryString(chars[i]));
            result.append(" ");
        }
        return result.toString();
    }

    public static String BinStrTostr(String binary) {
        String[] tempStr=binary.split(" ");
        char[] tempChar=new char[tempStr.length];
        for(int i=0;i<tempStr.length;i++) {
            tempChar[i]=BinstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }

    public static int[] BinstrToIntArray(String binStr) {
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }

    public static char BinstrToChar(String binStr){
        int[] temp=BinstrToIntArray(binStr);
        int sum=0;
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }
        return (char)sum;
    }

    public static String ZeroWidthWrdEncryption(String str){
        char[] chars = gbEncoding(str).toCharArray();
        StringBuffer result = new StringBuffer();
        StringBuffer sb = new StringBuffer("\\uFEFF");
        for(int i=0; i<chars.length; i++) {
            if(Integer.toBinaryString(chars[i]).length()==7){
                result.append("0");
                result.append(Integer.toBinaryString(chars[i]));
            }else {
                result.append("00");
                result.append(Integer.toBinaryString(chars[i]));
            }
        }
        for(int i=0; i<result.toString().length(); i++) {
            if(i%2==0){
                switch(result.toString().substring(i,i+2)){
                    case "00" :
                        sb.append("\\u2060");
                        break;
                    case "01" :
                        sb.append("\\u200b");
                        break;
                    case "10" :
                        sb.append("\\u200c");
                        break;
                    case "11" :
                        sb.append("\\u200d");
                        break;
                }
            }
        }
        sb.append("\\uFEFF");
        return decodeUnicode(sb.toString());
    }

    public static String ZeroWidthWordDecryption(String txt){
        String text = gbEncoding(txt);
        String str = text.substring(text.indexOf("\\ufeff")+6, text.lastIndexOf("\\ufeff"));
        str = str.replace("\\u2060","00");
        str = str.replace("\\u200b","01");
        str = str.replace("\\u200c","10");
        str = str.replace("\\u200d","11");


        char[] tempChar=new char[str.length()/8];
        for(int i=0;i<str.length();i++) {
            if(i%8==0){
                tempChar[i/8]=BinstrToChar(str.substring(i,i+8));
            }
        }
        return decodeUnicode(String.valueOf(tempChar));
    }
}