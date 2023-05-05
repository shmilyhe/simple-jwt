package io.shmilyhe.utils.encryption;



import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import io.shmilyhe.utils.URL64;


/**
 */
public class ESSignature implements ISignature
{
    private String curveName;
    private int signatureByteLength;
    private String javaAlgo;

    public ESSignature(String type){
        if("ES256".equals(type)){
            this.javaAlgo = "SHA256withECDSA";
            this.signatureByteLength = 64;
        }else if("ES384".equals(type)){
            this.javaAlgo = "SHA384withECDSA";
            this.signatureByteLength = 96;
        }else if("ES512".equals(type)){
            this.javaAlgo = "SHA512withECDSA";
            this.signatureByteLength = 132;
        }else{
            throw new NotSupportAlgorithmException(type);
        }
    }

    public ESSignature(String id, String javaAlgo, String curveName, int signatureByteLength)
    {
        this.javaAlgo = javaAlgo;
        this.curveName = curveName;
        this.signatureByteLength = signatureByteLength;
    }

    public boolean verifySignature(byte[] signatureBytes, String key, byte[] securedInputBytes)
    {
        byte[] derEncodedSignatureBytes;
        try
        {
            derEncodedSignatureBytes = convertConcatenatedToDer(signatureBytes);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to convert R and S as a concatenated byte array to DER encoding.", e);
        }

        return doVerifySignature(derEncodedSignatureBytes, key, securedInputBytes);
    }
    public  PrivateKey getPrivateKey(String key) throws Exception {
    	
        byte[] bytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

    public  PublicKey getPublicKey(String key) throws Exception {
    	
        byte[] bytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(keySpec);
    }
    public boolean doVerifySignature(byte[] signatureBytes, String key, byte[] securedInputBytes) {
        try
        {
            Signature sig = Signature.getInstance(this.javaAlgo);
            sig.initVerify(getPublicKey(key));
            sig.update(securedInputBytes);
            return sig.verify(signatureBytes);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        

    }


    public String sign(String key, byte[] securedInputBytes)
    {
        byte[] derEncodedSignatureBytes = doSign(key, securedInputBytes);
        try
        {
            return URL64.encode(convertDerToConcatenated(derEncodedSignatureBytes, signatureByteLength));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to convert DER encoding to R and S as a concatenated byte array.", e);
        }
    }
    public byte[] doSign(String key, byte[] securedInputBytes){
        try
        {
            Signature sig = Signature.getInstance(this.javaAlgo);
            sig.initSign(this.getPrivateKey(key));
            sig.update(securedInputBytes);
            return sig.sign();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /*
        The result of an ECDSA signature is the EC point (R, S), where R and S are unsigned (very large) integers.
        The JCA ECDSA signature implementation (sun.security.ec.ECDSASignature) produces and expects a DER encoding
        of R and S while JOSE/JWS wants R and S as a concatenated byte array. XML signatures (best I can tell) treats
        ECDSA similarly to JOSE and the code for the two methods that convert to and from DER and concatenated
        R and S was originally taken from org.apache.xml.security.algorithms.implementations.SignatureECDSA in the
        (Apache 2 licensed) Apache Santuario XML Security library. Some minor changes have been made to ensure the
        concatenated output left zero pads R & S to consistent length - i.e. the "octet sequence representations
        MUST NOT be shortened to omit any leading zero octets" per http://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-25#section-3.4

        Which seemed like a better idea than trying to write it myself or using sun.security.util.Der[Input/Output]Stream
        as sun.security.ec.ECDSASignature does or some other half-arsed approach.
     */

    // Convert the concatenation of R and S into DER encoding
    public static byte[] convertConcatenatedToDer(byte[] concatenatedSignatureBytes) throws IOException
    {
        int rawLen = concatenatedSignatureBytes.length/2;

        int i;

        for (i = rawLen; (i > 0) && (concatenatedSignatureBytes[rawLen - i] == 0); i--);

        int j = i;

        if (concatenatedSignatureBytes[rawLen - i] < 0)
        {
            j += 1;
        }

        int k;

        for (k = rawLen; (k > 0) && (concatenatedSignatureBytes[2*rawLen - k] == 0); k--);

        int l = k;

        if (concatenatedSignatureBytes[2*rawLen - k] < 0)
        {
            l += 1;
        }

        int len = 2 + j + 2 + l;
        if (len > 255)
        {
            throw new IOException("Invalid format of ECDSA signature");
        }
        int offset;
        byte derEncodedSignatureBytes[];
        if (len < 128)
        {
            derEncodedSignatureBytes = new byte[2 + 2 + j + 2 + l];
            offset = 1;
        }
        else
        {
            derEncodedSignatureBytes = new byte[3 + 2 + j + 2 + l];
            derEncodedSignatureBytes[1] = (byte) 0x81;
            offset = 2;
        }

        derEncodedSignatureBytes[0] = 48;
        derEncodedSignatureBytes[offset++] = (byte) len;
        derEncodedSignatureBytes[offset++] = 2;
        derEncodedSignatureBytes[offset++] = (byte) j;

        System.arraycopy(concatenatedSignatureBytes, rawLen - i, derEncodedSignatureBytes, (offset + j) - i, i);

        offset += j;

        derEncodedSignatureBytes[offset++] = 2;
        derEncodedSignatureBytes[offset++] = (byte) l;

        System.arraycopy(concatenatedSignatureBytes, 2*rawLen - k, derEncodedSignatureBytes, (offset + l) - k, k);

        return derEncodedSignatureBytes;
    }

    // Convert the DER encoding of R and S into a concatenation of R and S
    public static byte[] convertDerToConcatenated(byte derEncodedBytes[], int outputLength) throws IOException
    {

        if (derEncodedBytes.length < 8 || derEncodedBytes[0] != 48)
        {
            throw new IOException("Invalid format of ECDSA signature");
        }

        int offset;
        if (derEncodedBytes[1] > 0)
        {
            offset = 2;
        }
        else if (derEncodedBytes[1] == (byte) 0x81)
        {
            offset = 3;
        }
        else
        {
            throw new IOException("Invalid format of ECDSA signature");
        }

        byte rLength = derEncodedBytes[offset + 1];

        int i;
        for (i = rLength; (i > 0) && (derEncodedBytes[(offset + 2 + rLength) - i] == 0); i--);

        byte sLength = derEncodedBytes[offset + 2 + rLength + 1];

        int j;
        for (j = sLength; (j > 0) && (derEncodedBytes[(offset + 2 + rLength + 2 + sLength) - j] == 0); j--);

        int rawLen = Math.max(i, j);
        rawLen = Math.max(rawLen, outputLength/2);

        if ((derEncodedBytes[offset - 1] & 0xff) != derEncodedBytes.length - offset
            || (derEncodedBytes[offset - 1] & 0xff) != 2 + rLength + 2 + sLength
            || derEncodedBytes[offset] != 2
            || derEncodedBytes[offset + 2 + rLength] != 2)
        {
            throw new IOException("Invalid format of ECDSA signature");
        }
        
        byte concatenatedSignatureBytes[] = new byte[2*rawLen];

        System.arraycopy(derEncodedBytes, (offset + 2 + rLength) - i, concatenatedSignatureBytes, rawLen - i, i);
        System.arraycopy(derEncodedBytes, (offset + 2 + rLength + 2 + sLength) - j, concatenatedSignatureBytes, 2*rawLen - j, j);

        return concatenatedSignatureBytes;
    }

    public void validatePrivateKey(PrivateKey privateKey) 
    {
        ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
        //validateKeySpec(ecPrivateKey);
    }

    public void validatePublicKey(PublicKey publicKey) 
    {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
       // validateKeySpec(ecPublicKey);
    }

    private void validateKeySpec(ECKey ecKey) 
    {
        ECParameterSpec spec = ecKey.getParams();
        EllipticCurve curve = spec.getCurve();

        /*String name = EllipticCurves.getName(curve);

        if (!getCurveName().equals(name))
        {
            throw new InvalidKeyException(getAlgorithmIdentifier() + "/" + getJavaAlgorithm() + " expects a key using " +
                    getCurveName() + " but was " + name);
        }*/
    }

    public String getCurveName()
    {
        return curveName;
    }

    public static class EcdsaP256UsingSha256 extends ESSignature
    {
        public EcdsaP256UsingSha256()
        {
            super("ECDSA_USING_P256_CURVE_AND_SHA256", "SHA256withECDSA", "EllipticCurves.P_256", 64);
        }
    }

    public static class EcdsaP384UsingSha384 extends ESSignature
    {
        public EcdsaP384UsingSha384()
        {
            super("AlgorithmIdentifiers.ECDSA_USING_P384_CURVE_AND_SHA384", "SHA384withECDSA", "EllipticCurves.P_384", 96);
        }
    }

    public static class EcdsaP521UsingSha512 extends ESSignature
    {
        public EcdsaP521UsingSha512()
        {
            super("AlgorithmIdentifiers.ECDSA_USING_P521_CURVE_AND_SHA512", "SHA512withECDSA", "EllipticCurves.P_521", 132);
        }
    }

    @Override
    public String sgin(String jwt, String key) {
        return sign( key,jwt.getBytes());
    }

    @Override
    public boolean verify(String jwt, String sgin, String key) {
        return this.verifySignature(URL64.decode(sgin), key, jwt.getBytes());
    }
}
