import org.junit.Assert;
import org.junit.Test;

import io.shmilyhe.utils.JWT;

public class TestJWT {

    @Test
    public void test1(){
        JWT j = new JWT();
        j.addPayload("sub","test" );
        j.addPayload("iat", 1681971332360L);
        String key="test";
        String js = j.toJwt(key);
        String testJwt ="eyJ0eXAiIDogIkpXVCIsImFsZyIgOiAiSFMyNTYifQ.eyJzdWIiIDogInRlc3QiLCJpYXQiIDogMTY4MTk3MTMzMjM2MH0.WrII_LoGY61rXpLEsw0bWl9w2hZ9QyuwfjMTnyZ88xw";
        Assert.assertEquals(testJwt, js);
        System.out.println(js);
        System.out.println(j);
        JWT j2 = new JWT();
        j2.fromJwt(testJwt);
        j2.isVaild();
        Assert.assertTrue(j2.check(key));
        
    } 
    
    @Test
    public void testRS256(){
        JWT j = new JWT();
        j.addHeader("alg", "RS256");
        j.addPayload("sub","test" );
        j.addPayload("iat", 1681971332360L);
        String key="test";
        String js = j.toJwt(RS256Test.getPri());

        System.out.println(js);
        System.out.println(j);
        JWT j2 = new JWT();
        j2.fromJwt(js);
        Assert.assertTrue(j2.check(RS256Test.getPub()));
    }

    @Test
    public void testRS512(){
        JWT j = new JWT();
        j.addHeader("alg", "RS512");
        j.addPayload("sub","test" );
        j.addPayload("iat", 1681971332360L);
        String key="test";
        String js = j.toJwt(RS512Test.getPri());

        System.out.println(js);
        System.out.println(j);
        JWT j2 = new JWT();
        j2.fromJwt(js);
        Assert.assertTrue(j2.check(RS512Test.getPub()));
    }

    @Test
    public void testES512(){
        JWT j = new JWT();
        j.addHeader("alg", "ES512");
        j.addPayload("sub","test" );
        j.addPayload("iat", 1681971332360L);
        String key="test";
        String js = j.toJwt(ES512Test.getPri());

        System.out.println(js);
        System.out.println(j);
        JWT j2 = new JWT();
        j2.fromJwt(js);
        Assert.assertTrue(j2.check(ES512Test.getPub()));
    }

    @Test
    public void testES256(){
        JWT j = new JWT();
        j.addHeader("alg", "ES256");
        j.addPayload("sub","test" );
        j.addPayload("iat", 1681971332360L);
        String key="test";
        String js = j.toJwt(ES256Test.getPri());

        System.out.println(js);
        System.out.println(j);
        JWT j2 = new JWT();
        j2.fromJwt(js);
        Assert.assertTrue(j2.check(ES256Test.getPub()));
    }
}
