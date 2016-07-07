/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmus.atmus.token;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ntbra
 */
public class TokenManager{
    private static TokenManager manager;
    private final Map<String, TokenGenerator> tokens;
    
    public TokenManager() {
        tokens = new HashMap<>();
    }
    
    public static Token getTokenGenerator(String key){
        if(manager == null) manager = new TokenManager();
        if(!manager.tokens.containsKey(key)) manager.tokens.put(key, new TokenGenerator(30L, key));
        return manager.tokens.get(key);
    }
    
    public static Token getTokenGenerator(String key, long timeWindow){
        if(manager == null) manager = new TokenManager();
        if(!manager.tokens.containsKey(key)) manager.tokens.put(key, new TokenGenerator(timeWindow, key));
        return manager.tokens.get(key);
    }
    
    
    private static class TokenGenerator implements Token{
        private long timeWindow;
        private final byte[] key;
        private final transient Mac hmac;

        private static final String SECRET = "YOU SHALL NOT PASS!";
        private static final String METHOD = "HMACSHA1";
        private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String NUMBERS = "0123456789";
        private long getTime(){
            return (long)((System.currentTimeMillis()/1000L));
        }
        
        private String getTimeHex(){
            return Long.toHexString((long)(getTime()/timeWindow)).toUpperCase();
        }

        @Override
        public String genToken(){
            String encode = getTimeHex()+SECRET;
            byte end[] = hmac.doFinal(encode.getBytes());
            return Utils.toHex(end);
        }

        @Override
        public String getSmallToken() {
            String bigToken = genToken();
            String smallToken = "";
            String split[] = new String[5];
            int j = 0;
            for(int i = 0; i<bigToken.length(); i+=8){
                split[j++] = bigToken.substring(i, (i+8));
            }
            for(String t: split){
                int sum = 0;
                for(char c: t.toCharArray()){
                    sum+=c;
                }
                while(sum>=(NUMBERS.length()+ALPHABET.length()+5)){
                    String x = sum+"";
                    sum = 0;
                    for(char c: x.toCharArray()){
                        sum+=Integer.parseInt(c+"");
                    }
                }
                if(sum>=NUMBERS.length()){
                    sum-=NUMBERS.length();
                    smallToken+=ALPHABET.charAt(sum);
                }else{
                    smallToken+=NUMBERS.charAt(sum);
                }
            }
            return smallToken;
        }        
        
        private Mac macInit(){
            Mac mac = null;
            try {
                mac = Mac.getInstance(METHOD);
                SecretKeySpec spec = new SecretKeySpec(this.key, METHOD);
                mac.init(spec);
            } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
                Logger.getLogger(TokenGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
            return mac;
        }

        @Override
        public void setTimeWindow(long timeWindow) {
            this.timeWindow = timeWindow;
        }

        private String sKey;
        private TokenGenerator(long timeWindow, String key) {
            this.timeWindow = timeWindow;
            this.key = (key).getBytes();
            this.hmac = macInit();
        }

        @Override
        public long getTimeWindow() {
            return timeWindow;
        }

        @Override
        public String toString() {
            return this.genToken();
        }

        @Override
        public int getReaming() {
            return (int)(timeWindow-((getTime()%60)%timeWindow));
        }

        @Override
        public String getTokenKey() {
            return this.sKey;
        }
    }
}
