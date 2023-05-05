import io.shmilyhe.utils.Bytes;
import io.shmilyhe.utils.URL64;
import io.shmilyhe.utils.encryption.ESSignature;


public class ES256Test {
    public static String getJwt(){
        return "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0";
    }

    public static String getSgin(){
        return "tyh-VfuzIxCyGYDlkBA7DfyjrqmSHu6pQ2hoZuFqUSLPNY2N0mpHb3nk5K17HWP_3cYHBw7AhHale5wky6-sVA";
    }


    public static String getPub(){
        return "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEEVs/o5+uQbTjL3chynL4wXgUg2R9"+
        "q9UU8I5mEovUf86QZ7kOBIjJwqnzD1omageEHWwHdBO6B+dFabmdT9POxg==";
    }

    public static String getPri(){
        return "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgevZzL1gdAFr88hb2"+
        "OF/2NxApJCzGCEDdfSp6VQO30hyhRANCAAQRWz+jn65BtOMvdyHKcvjBeBSDZH2r"+
        "1RTwjmYSi9R/zpBnuQ4EiMnCqfMPWiZqB4QdbAd0E7oH50VpuZ1P087G";
    }

    public static void main(String[] args){

        ESSignature rs = new ESSignature("ES256");
        String sgin = rs.sgin(getJwt(), getPri());
        System.out.println(sgin);
        System.out.println(getSgin());
        //print(sgin);
        //print(getSgin());
        System.out.println(rs.verify(getJwt(), sgin, getPub()));
        System.out.println(rs.verify(getJwt(), getSgin(), getPub()));
    }
    public static void print(String b64){
        System.out.println(Bytes.toHexString(URL64.decode(b64)));
    }
}
