# 一个简单的JSON 解析器
这是一个很简单，高效的JSON 解析和操作的组件。它不依赖其它的组件，在一些轻度使用的JSON的场景非常适用。

## 快还开始
```
String js = "{\"name\":\"eric\",\"profile\":{\"company\":\"tylw\",\"birthday\":\"1984-05-17\"},\"data\":[1,2,3,4,5,6,7.9]}";

        Json json = Json.parse(js);
		//设置
        json.set("js[0]", new Date());
        json.set("js[1]", new Date().getTime());
        System.out.println(json);
		
		//读取值
        System.out.println(json.Q("name").asString());
        System.out.println(json.Q("profile"));
        System.out.println(json.Q("profile.company").getRaw());
        System.out.println(json.Q("profile.company"));
        System.out.println(json.Q("profile.company").asString());
        System.out.println(json.Q("profile.company").asDate());
        System.out.println(json.Q("profile.birthday").asDate());
        System.out.println(json.Q("data[6]").asDate());
		System.out.println(json.Q("data[1]").asInt());
		
		//移除值
        json.remove("profile");
        System.out.println(json);
		
		//转为JSON string
		String jsonString =json.toString();
		 System.out.println(jsonString);
		
		
```