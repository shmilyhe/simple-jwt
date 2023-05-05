package io.shmilyhe.utils.encryption;

public class NotSupportAlgorithmException  extends RuntimeException{
    public NotSupportAlgorithmException(String alg){
        super("not support algorithm:"+alg);
    }
    
}
