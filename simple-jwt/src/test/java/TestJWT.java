import org.junit.Assert;
import org.junit.Test;

import io.shmilyhe.utils.JWT;

public class TestJWT {
    @Test
    public void test1(){
        JWT j = new JWT();
        j.addPayload("sub","test" );
        j.addPayload("iat", 1681971332360L);
        String js = j.toJwt("test");
        String testJwt ="eyJ0eXAiIDogIkpXVCIsImFsZyIgOiAiSFMyNTYifQ.eyJzdWIiIDogInRlc3QiLCJpYXQiIDogMTY4MTk3MTMzMjM2MH0.WrII_LoGY61rXpLEsw0bWl9w2hZ9QyuwfjMTnyZ88xw";
        Assert.assertEquals(testJwt, js);
        System.out.println(js);
        System.out.println(j);
        JWT j2 = new JWT();
        j2.fromJwt(testJwt);
        j2.isVaild()
        
    }    
}
