package io.shmilyhe.utils;

/**
 * 进制转换
 * @author eric
 *
 */
public class AnyNunberConvert {
	
	
	/**
	 * 
	 * @param number 整数
	 * @param array N进制的表示字符，如：二进制是 "01" 10进制是“0123456789” 16进制 "0123456789abcde"
	 * @return 返回N 进制的表示法
	 */
	public static String convertToAny (int number, String array)
    {
      // int conversionPadSize = 0;
        int tableBit = tableBit(array);
        long input = Math.abs ((long)number);
        //补码
        /*if (number < 0) {
            input -= 1;
            conversionPadSize = 128 / tableBit;
            if (tableBit < 10) {
                conversionPadSize /= 2;
            }
        }*/
        
        //String result = "";
        char [] res1=new char[32];
        int i=0;
        while (true) {
            if (input >= tableBit) {
                //result = numberConversion (array, (int)(input % tableBit)) + result;
                res1[i++]=numberConversion (array, (int)(input % tableBit));
                input = (int)(input / tableBit);
            } else {
                //result = numberConversion (array, (int)input) + result;
            	res1[i++]=numberConversion (array, (int)input);
                break;
            }
        }
        char[] res2= new char[i];
        for(int j=0;i>0;i--,j++) {
        	res2[j]=res1[i-1];
        }
        if (number < 0) {
            //反转
            //String res = result;
            //result = "";
            int moveBit = tableBit - 1;
            int j=0;
            for (char c : res2) {
            //for (char c : res.toCharArray()) {
            	res2[j++]=numberConversion (array, moveBit - findConversion (array, c));
                //result += numberConversion (array, moveBit - findConversion (array, c));
            }
        }

        return new String(res2);
    }
	

	
	private static char numberConversion (String array, int n)
    {
		if(n==array.length())return array.charAt(0);
        return  array.charAt(n);
    }
	
	private static int tableBit(String tableString) {
		return  tableString.length()%2>0?tableString.length()+1:tableString.length();
	}
	
	private static int findConversion (String str, char c)
    {
        return str.indexOf(c);
    }
	
	public static int anyToNumber (String any, String array)
    {
		int num=0;
		int n=1;
		int jz=array.length()%2>0?array.length()+1:array.length();
		for(int i=any.length()-1;i>=0;i--) {
			num+=(array.indexOf(any.charAt(i)))*n;
			n*=jz;
		}
		return num;
    }
	
	static boolean isNegativeNumber(String any, String array ){
        String s = convertToAny (Integer.MAX_VALUE, array);
        if (s.length() != any.length()) {
            return false;
        } else {
            if (array.indexOf(any.charAt(0)) >array.indexOf(s.charAt(0))) {
                return true;
            }
        }
        return false;
    }
	
	public static void main(String args[]) {
		
		System.out.println(convertToAny(90,"0123456789abcde"));
		System.out.println(Integer.parseInt("5a", 16));
		System.out.println(anyToNumber("3e9","0123456789abcde"));
		String array ="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		anyToNumber("g",array);
		//String array ="0123456789abcdef";
		long time =System.currentTimeMillis();
		int conunt=10000000;
		for(int i=0;i<conunt;i++) {
			String c=convertToAny(i,array);
			int num=anyToNumber(c,array);
			if(num!=i) {
				System.err.println(num+":"+i+":"+c+":"+Integer.toHexString(i));
			}
		}
		System.out.println(conunt*1000/(System.currentTimeMillis()-time) +" ps");
	}

}
