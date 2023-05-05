package io.shmilyhe.utils.encryption;

import io.shmilyhe.utils.URL64;

public class HSSignature  implements ISignature{
    static final int HS256=0;
    static final int HS512=1;

    int alg =HS256;
    Hash hash = new Sha256();
    public HSSignature(){

    }

    public HSSignature(String type){
        if("HS256".equals(type)){
            alg=HS256;
             hash = new Sha256();
        }else if("HS512".equals(type)){
            alg=HS512;
            hash = new Sha512();
        }else{
            throw new NotSupportAlgorithmException(type);
        }
    }

    protected Hash getHash(){
       return  hash.getHash();
    }
    protected String sgin1(String jwt,String key){
        Hash hash =getHash();
        HMAC hmac = new HMAC(key.getBytes(),hash);
        byte[] b= hmac.sum(jwt.getBytes());
        return URL64.encode(b).replaceAll("=", "");
    }

    protected String join(String ...str){
        StringBuilder sb = new StringBuilder();
        for(String s:str){
            sb.append(s);
        }
        return sb.toString();
    } 

    @Override
    public String sgin(String jwt, String key) {
        return sgin1(jwt,key);
    }

    @Override
    public boolean verify(String jwt ,String s, String key) {
        return s.equals(sgin1(jwt,key));
    }
    
}
