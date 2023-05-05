package io.shmilyhe.utils.encryption;

import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import io.shmilyhe.utils.URL64;

public class RSSignature implements ISignature {

    static final String RS256="SHA256withRSA";
    static final String RS384="SHA384withRSA";
    static final String RS512="SHA512withRSA";

    protected String alg=null;
    public RSSignature(String type){
        if(type.equals("RS256")){
            alg=RS256;
        }else if(type.equals("RS384")){
            alg=RS384;
        }else if(type.equals("RS512")){
            alg=RS512;
        }else{
            throw new NotSupportAlgorithmException(type);
        }

    }

    @Override
    public String sgin(String jwt, String key) {
        if(alg==null)return null;
        try {
            RSAPrivateKey prk = RSA.getPrivateKey(key);
            Signature signer = Signature.getInstance(alg);
            signer.initSign(prk);
            signer.update(jwt.getBytes());
            byte[] signature = signer.sign();
            return  URL64.encode(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean verify(String jwt,String s, String key) {
        if(alg==null)return false;
        try {
            RSAPublicKey puk = RSA.getPublicKey(key);
            Signature signer = Signature.getInstance(alg);
            signer.initVerify(puk);
            signer.update(jwt.getBytes());
            return  signer.verify(URL64.decode(s));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
