/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmus.atmus.token;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ntbra
 */
public class Utils {
    public static String doMD5(String text){
        String md5 = null;
        try {
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(text.getBytes(),0,text.length());
            md5 = new BigInteger(1,m.digest()).toString(16);
            md5 = checksize(md5);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return md5;
    }
    
    private static String checksize(String t){
        if(t.length()<32){
            return "0"+checksize(t);
        }else{
            return t;
        }
    }
    
    public static String toHex(String t){
        return toHex(t.getBytes());
    }
    
    public static String toHex(int i){
        return toHex(new byte[]{(byte)i});
    }
    
    public static  byte[] hexToString(String hex){
        byte b[] = new byte[hex.length()/2];
        if((hex.length()%2) == 0){
            int j = 0;
            for(int i = 0;;){
                b[j++] = (byte)Integer.parseInt(hex.substring(i, i+2), 16);
                i+=2;
                if(i>=hex.length()) break;
            }
        }
        return b;
    }
    
    public static String toHex(byte b[]) {
        String str = "";
        for(int i = 0;;i++){ 
            if (i >= b.length) {
                str = str.toUpperCase();
                break;
            } else if ((0xFF & b[i]) < 16) {
              str+="0"+Integer.toHexString(0xFF & b[i]);
            } else {
              str+=Integer.toHexString(0xFF & b[i]);
            }
        }
        return str;
    }

}
