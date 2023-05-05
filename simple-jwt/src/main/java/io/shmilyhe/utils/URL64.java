package io.shmilyhe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base64 URL风格的编码
 */
public class URL64 {

    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_=".toCharArray();  
    /** 
     * data[]进行编码 
     * @param data 
     * @return 
     */  
        public static String encode(byte[] data) {  
            int start = 0;  
            int len = data.length;  
            StringBuffer buf = new StringBuffer(data.length * 3 / 2);  
  
            int end = len - 3;  
            int i = start;  
            int n = 0;  
  
            while (i <= end) {  
                int d = ((((int) data[i]) & 0x0ff) << 16)  
                        | ((((int) data[i + 1]) & 0x0ff) << 8)  
                        | (((int) data[i + 2]) & 0x0ff);  
  
                buf.append(legalChars[(d >> 18) & 63]);  
                buf.append(legalChars[(d >> 12) & 63]);  
                buf.append(legalChars[(d >> 6) & 63]);  
                buf.append(legalChars[d & 63]);  
  
                i += 3;  
                /* 
                if (n++ >= 14) {  
                    n = 0;  
                    buf.append(" ");  
                }  */
            }  
  
            if (i == start + len - 2) {  
                int d = ((((int) data[i]) & 0x0ff) << 16)  
                        | ((((int) data[i + 1]) & 255) << 8);  
  
                buf.append(legalChars[(d >> 18) & 63]);  
                buf.append(legalChars[(d >> 12) & 63]);  
                buf.append(legalChars[(d >> 6) & 63]);  
                //buf.append(legalChars[64]);  
            } else if (i == start + len - 1) {  
                int d = (((int) data[i]) & 0x0ff) << 16;  
  
                buf.append(legalChars[(d >> 18) & 63]);  
                buf.append(legalChars[(d >> 12) & 63]);  
                //buf.append(legalChars[64]); 
                //buf.append(legalChars[64]); 
            }  
  
            return buf.toString();  
        }  
  
        private static int decode(char c) { 
            if (c >= 'A' && c <= 'Z')  
                return ((int) c) - 65;  
            else if (c >= 'a' && c <= 'z')  
                return ((int) c) - 97 + 26;  
            else if (c >= '0' && c <= '9')  
                return ((int) c) - 48 + 26 + 26;  
            else  
            {   
                if(c==legalChars[62])return 62;
                if(c==legalChars[63])return 63;
                if(c==legalChars[64])return 0;
                
                for(int i=0;i<65;i++){
                    System.out.println(legalChars[i]+":"+i);
                }
                System.out.println(legalChars[61]+":"+c);
                throw new RuntimeException("unexpected code: " + c); 

            }
                
        }  
  
        /** 
         * Decodes the given Base64 encoded String to a new byte array. The byte 
         * array holding the decoded data is returned. 
         */  
  
        public static byte[] decode(String s) {  
  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            try {  
                decode(s, bos);  
            } catch (IOException e) {  
                throw new RuntimeException();  
            }  
            byte[] decodedBytes = bos.toByteArray();  
            try {  
                bos.close();  
                bos = null;  
            } catch (IOException ex) {  
                System.err.println("Error while decoding BASE64: " + ex.toString());  
            }  
            return decodedBytes;  
        }  
  
        private static void decode(String s, OutputStream os) throws IOException {  
            int i = 0;  
  
            int len = s.length();  
  
            while (true) {  
                while (i < len && s.charAt(i) <= ' ')  
                    i++;  
  
                if (i >= len)  
                    break;  
                int b1=0;
                int b2=0;
                int b3=0;
                int b4=0;
                if(i<len){
                    b1=(decode(s.charAt(i)) << 18) ;
                }
                if(i+1<len){
                    b2=(decode(s.charAt(i + 1)) << 12) ;
                }
                if(i+2<len){
                    b3= (decode(s.charAt(i + 2)) << 6);
                }
                if(i+3<len){
                    b4= (decode(s.charAt(i + 3))); 
                }

                /*int tri = (decode(s.charAt(i)) << 18)  
                        + (decode(s.charAt(i + 1)) << 12)  
                        + (decode(s.charAt(i + 2)) << 6)  
                        + (decode(s.charAt(i + 3)));  */
                int tri=b1+b2+b3+b4;
                os.write((tri >> 16) & 255);  
                if (i+2>=len||s.charAt(i + 2) == '=')  
                    break;  
                os.write((tri >> 8) & 255);  
                if (i+3>=len||s.charAt(i + 3) == '=')  
                    break;  
                os.write(tri & 255);  
  
                i += 4;  
            }  
        }  
        
        public static void main(String[] agrs){
        	String str ="Basic YWRtaW46YWRtaW4=";
            //String bcode  =encode("最a国a中a人bi我c爱j！%$@r^&*()_".getBytes());
        	//System.out.println(bcode);
            //System.out.println(new String(decode(bcode)));
        	//System.out.println(new String(decode("YWRtaW46YWRtaW4=")));
        }
    }
