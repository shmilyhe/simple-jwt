package io.shmilyhe.utils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import io.shmilyhe.utils.encryption.HMAC;
import io.shmilyhe.utils.encryption.Hash;
import io.shmilyhe.utils.encryption.ISignature;
import io.shmilyhe.utils.encryption.Sha256;
import io.shmilyhe.utils.encryption.Sha512;
import io.shmilyhe.utils.encryption.SignatureFactory;

/**
 * JWT 实现，目前只实现了HS256,HS512
 */
public class JWT {
    static Charset utf8 = Charset.forName("utf-8");
    private Map<String,Object> head;
    private Map<String,Object> payload;
    private String sign;
    private String headString;
    private String payloadString;
    private boolean vaild;


    public boolean isVaild() {
        return vaild;
    }

    public void setVaild(boolean vaild) {
        this.vaild = vaild;
    }

    /**
     * 
     * @param key
     * @return
     */
    public Object getHeader(String key){
        if(head==null)return null;
        return head.get(key);
    }

    public JWT addHeader(String key,String value){
        if(head==null)head = new HashMap<String,Object>();
        head.put(key, value);
        return this;
    }

    public JWT addPayload(String key,Object value){
        if(value==null)return this;
        if(payload==null)payload = new HashMap<String,Object>();
        payload.put(key, value);
        return this;
    }

    /**
     * 
     * @param key
     * @return
     */
    public Object getPayload(String key){
        if(payload==null)return null;
        return payload.get(key);
    }

    public JWT fromJwt(String jwt){
        String[] jwtp = jwt.split("\\.");
        headString=jwtp[0];
        payloadString=jwtp[1];
        sign=jwtp[2];
        head= getMap(headString);
        payload= getMap(payloadString);
        return this;
    }

    public boolean check(String key){
        if(headString==null||payloadString==null||sign==null)return false;
        //return sign.equals(sign(key,headString,payloadString,head));
        ISignature sginser = SignatureFactory.getSignature(this.alg());
        return sginser.verify(join(headString,".",payloadString), sign, key);
    }


    protected Map<String,Object> getMap(String pStr){
        String str =new String(URL64.decode(pStr),utf8);
        return (Map<String, Object>) SimpleJson.parse(str).getRoot();
    }

    protected String toUrl64(Map map){
        return URL64.encode(JsonString.asJsonString(map).getBytes(utf8)).replaceAll("=", "");
    }


    public String toJwt(String key){
        if(payload==null)return null;
        if(getPayload("iat")==null){
            addPayload("iat", System.currentTimeMillis());
        }
        if(getHeader("typ")==null){
            addHeader("typ","JWT");
        }
        if(getHeader("alg")==null){
            addHeader("alg","HS256");
        }
        headString=toUrl64(head);
        payloadString=toUrl64(payload);
        sign=sign(key,headString,payloadString,head);
        return join(headString,".",payloadString,".",sign);
    }

    public String alg(){
        String alg =(String)this.getHeader("alg");
        return alg==null?"HS256":alg;
    }

    protected Hash getHash(String alg){
        if("HS256".equals(alg()))return  new Sha256();
        if("HS512".equals(alg()))return  new Sha512();
        return  new Sha256();
    }

    protected String sign(String key,String headString,String payloadString,Map<String,Object> head){
        /*
        Hash hash =getHash(this.alg());
        HMAC hmac = new HMAC(key.getBytes(),hash);
        byte[] b= hmac.sum(join(headString,".",payloadString).getBytes(utf8));
        return URL64.encode(b).replaceAll("=", "");*/
        ISignature sginser = SignatureFactory.getSignature(this.alg());
        return sginser.sgin(join(headString,".",payloadString), key);
    }

    protected String join(String ...str){
        StringBuilder sb = new StringBuilder();
        for(String s:str){
            sb.append(s);
        }
        return sb.toString();
    } 


    public String toString(){
        Map root = new HashMap();
        root.put("head", head);
        root.put("payload", payload);
        root.put("sign", sign);
        return JsonString.asJsonString(root);
    }

}
