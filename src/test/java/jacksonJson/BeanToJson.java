package jacksonJson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

/**
 * Created by liush on 17-8-11.
 @JsonInclude(Include.NON_NULL)

 //将该标记放在属性上，如果该属性为NULL则不参与序列化
 //如果放在类上边,那对这个类的全部属性起作用
 //Include.Include.ALWAYS 默认
 //Include.NON_DEFAULT 属性为默认值不序列化
 //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
 //Include.NON_NULL 属性为NULL 不序列化


 */

 class User {

    private String username;
    private Integer age;

    public User() {

    }

    public User(String username, Integer age) {
        this.username = username;
        this.age = age;

    }

    public String getUsername() {

        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

}

public class BeanToJson {
    public static void main(String[] args) throws Exception {

        //将对象转换成json字符串
        User user = new User();
        user.setAge(10);
        user.setUsername("jzx");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        System.out.println(userJson);
        //打印：{"username":"jzx","age":10}

        //将list集合对象转换成json字符串
        User user1 = new User();
        user1.setAge(11);
        user1.setUsername("sss");
        User user2 = new User();
        user2.setAge(12);
        user2.setUsername("hsj");
        List<User> userList = new ArrayList<User>();
        userList.add(user1);
        userList.add(user2);
        String userListJson = objectMapper.writeValueAsString(userList);
        System.out.println(userListJson);
        //打印结果：[{"username":"sss","age":11},{"username":"hsj","age":12}]

        //将map集合转换成json字符串。
        Map<String,User> userMap = new HashMap<String,User>();
        userMap.put("user1", user1);
        userMap.put("user2", user2);
        String userMapJson  = objectMapper.writeValueAsString(userMap);
        System.out.println(userMapJson);
        //打印结果：{"user2":{"username":"hsj","age":12},"user1":{"username":"sss","age":11}}

        ObjectMapper mapper = new ObjectMapper();

        ObjectMapper objectMapper1 = mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        //通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        //Include.Include.ALWAYS 默认
        //Include.NON_DEFAULT 属性为默认值不序列化
        //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
        //Include.NON_NULL 属性为NULL 不序列化
        //User userb = new User(1,"",null);
        String outJson = mapper.writeValueAsString(user);
        System.out.println(outJson);
        //只对VO起作用，Map List不起作用
        ObjectMapper mappera = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        Map map = new HashMap();
        map.put("a", null);
        map.put("b", "b");

        String ret_val = mapper.writeValueAsString(map);
        System.err.println(ret_val);
        Map m = mapper.readValue(ret_val, Map.class);
        System.err.println(m.get("a") + "|" + m.get("b"));
        //：{"b":"b","a":null} null|b


    }
}
