package io.shmilyhe.utils.encryption;

import java.util.HashMap;

public class SignatureFactory {
    static HashMap <String,ISignature> signatureMap = new HashMap <String,ISignature>();

    /**
     * 获取签名算法，不需要线程安全。
     * @param type 算法名称
     * @return 算法实更
     */
    public static ISignature getSignature(String type){
        //减少实例个数，每种算法只生成一个实例，不需要线程安全
        ISignature s =  signatureMap.get(type);
        if(s!=null)return s;
        s=create(type);
        if(s!=null){
            signatureMap.put(type, s);
            return s;
        }else{
            throw new NotSupportAlgorithmException(type);
        }
    }
    private static ISignature create(String type){
        if(type.startsWith("HS")){
            return new HSSignature(type);
        }else if(type.startsWith("RS")){
            return new RSSignature(type);
        }else if(type.startsWith("ES")){
            return new ESSignature(type);
        }else{
            throw new NotSupportAlgorithmException(type);
        }
    }
}
