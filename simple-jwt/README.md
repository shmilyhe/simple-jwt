# simple-jwt

一个简单的JWT实现，纯JDK实现，没有依赖第三方组件，简单明了没有多余的代码。MIT协议


## 快速开始

```
//生成JWT，不指定算法默认使用HS256
        JWT j = new JWT();
        j.addPayload("sub","test" );
        j.addPayload("iat", 1681971332360L);
        String key="test";
        String js = j.toJwt(key);
        System.out.println("jwt:"+js);
//验证JWT
        JWT j2 = new JWT();
        j2.fromJwt(js);
        boolean isPass = j2.check(key)
        System.out.println(isPass);

```

## 目前支持的JWT 算法
* HS256
* HS512
* RS256
* RS384
* RS512
* ES256
* ES384
* ES512

待实现
* HS384
* PS256
* PS384
* PS512

## 其它示例
```
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
```
