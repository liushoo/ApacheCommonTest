package jacksonJson;

/**
 * Created by liush on 17-8-11.
 */

public class JsonIncludeTest {
    /**
     * 1.实体上
     * @JsonInclude(Include.NON_NULL)

        //将该标记放在属性上，如果该属性为NULL则不参与序列化
        //如果放在类上边,那对这个类的全部属性起作用
        //Include.Include.ALWAYS 默认
        //Include.NON_DEFAULT 属性为默认值不序列化
        //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
        //Include.NON_NULL 属性为NULL 不序列化
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

    }
}
