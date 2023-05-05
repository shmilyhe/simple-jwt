
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import io.shmilyhe.utils.encryption.RSA;
 
/**
 * @ClassName: RSATest
 * @Date: 2023/5/5
 * @Version: v1.0
 **/
public class RSATest {
 
    public static void main(String[] args) throws Exception {
 
        // 使用公钥、私钥对象加解密
        List<Key> keyList = RSA.getRSAKeyObject(1024);
        RSAPublicKey puk = (RSAPublicKey) keyList.get(0);
        RSAPrivateKey prk = (RSAPrivateKey) keyList.get(1);
        String message = "user_agent: Mozilla/4.08 [en] (Win98; I ;Nav)";
        String encryptedMsg = RSA.encryptByPublicKey(message, puk);
        String decryptedMsg = RSA.decryptByPrivateKey(encryptedMsg, prk);
        System.out.println(" message ==  decryptedMsg ? " + message.equals(decryptedMsg));
 
        System.out.println("====================");
        // 使用字符串生成公钥、私钥完成加解密
        List<String> keyStringList = RSA.getRSAKeyString(1024);
        String pukString = keyStringList.get(0);
        String prkString = keyStringList.get(1);
        System.out.println("公钥:" + pukString);
        System.out.println("私钥:" + prkString);
        // 生成公钥、私钥
        puk = RSA.getPublicKey(pukString);
        prk = RSA.getPrivateKey(prkString);
        encryptedMsg = RSA.encryptByPublicKey(message, puk);
        decryptedMsg = RSA.decryptByPrivateKey(encryptedMsg, prk);
        System.out.println("message ==  decryptedMsg ? " + message.equals(decryptedMsg));
        encryptedMsg = RSA.encryptByPrivateKey(message, prk);
        decryptedMsg = RSA.decryptByPublicKey(encryptedMsg, puk);
        System.out.println("message ==  decryptedMsg ? " + message.equals(decryptedMsg)+"|"+decryptedMsg);
 
    }
}