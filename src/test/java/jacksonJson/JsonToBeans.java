package jacksonJson;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
/**
 * Created by liush on 17-8-11.
 */

import java.util.List;

 class ListBean {

    List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
public class JsonToBeans {
    public static void main(String[] args) throws Exception {

        //将json字符串转换成java对象
        String userJson = "{\"username\":\"jzx\",\"age\":10}";
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson,User.class);
        System.out.println(user+":"+user.getUsername()+","+ user.getAge());
        //打印结果：com.Jtianlin.test.User@c2a132:jzx,10

        //将json字符串数组转换成java对象list集合。直接转换成list集合较为复杂，本例通过一个过渡对象
        //该对象里面存有一个list的java对象集合。通过转换成对象来实现集合对象的转换。
        String userListJson = "{\"userList\":[{\"username\":\"sss\",\"age\":11},{\"username\":\"hsj\",\"age\":12}]}";
        ListBean listBeans = objectMapper.readValue(userListJson, ListBean.class);
        System.out.println(listBeans.getUserList());
        //打印结果：[com.Jtianlin.test.User@1e0bc08, com.Jtianlin.test.User@158b649]

        //将json字符串转换成map集合，可以通过键来取得值。
        String mapJson = "{\"username\":\"hsj\",\"age\":12}";
        Map userMap = objectMapper.readValue(mapJson, Map.class);
        System.out.println(userMap+":" + userMap.get("username")+","+userMap.get("age") );
        //打印结果：{username=hsj, age=12}:hsj,12

        //将json字符串数组转换成Map的list集合。
        String json ="[{\"username\":\"sss\",\"age\":11},{\"username\":\"hsj\",\"age\":12}]}";
        List<Map> list = objectMapper.readValue(json, List.class);
        System.out.println(list.get(0).get("username"));
        //打印结果：sss
    }
}
