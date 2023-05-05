package io.shmilyhe.utils.encryption;

public interface ISignature {
    String sgin(String jwt,String key);
    boolean verify(String jwt,String sgin,String key);
}
