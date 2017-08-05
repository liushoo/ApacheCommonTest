/**
 * Created by liush on 17-8-5.
 */
public class SystemProperty {
    public static void main(String args[]) {
        //Java 运行时环境版本
        System.out.println("java.version:" + System.getProperty("java.version"));
        //Java 运行时环境供应商,
        System.out.println("java_vendor:" + System.getProperty("java.vendor"));
        // Java 供应商的 URL
        System.out.println("java_vendor_url:"
                + System.getProperty("java.vendor.url"));
        //Java 安装目录
        System.out.println("java_home:" + System.getProperty("java.home"));
        //Java 类格式版本号
        System.out.println("java_class_version:"
                + System.getProperty("java.class.version"));
        //Java 类路径
        System.out.println("java_class_path:"
                + System.getProperty("java.class.path"));
        //操作系统的名称
        System.out.println("os_name:" + System.getProperty("os.name"));
        //操作系统的架构
        System.out.println("os_arch:" + System.getProperty("os.arch"));
        //操作系统的版本
        System.out.println("os_version:" + System.getProperty("os.version"));
        //用户的账户名称
        System.out.println("user_name:" + System.getProperty("user.name"));
        //用户的主目录
        System.out.println("user_home:" + System.getProperty("user.home"));
        //用户的当前工作目录
        System.out.println("user_dir:" + System.getProperty("user.dir"));
        //Java 虚拟机规范版本
        System.out.println("java_vm_specification_version:"
                + System.getProperty("java.vm.specification.version"));
        //Java 虚拟机规范供应商
        System.out.println("java_vm_specification_vendor:"
                + System.getProperty("java.vm.specification.vendor"));
        //Java 虚拟机规范名称
        System.out.println("java_vm_specification_name:"
                + System.getProperty("java.vm.specification.name"));
        //Java 虚拟机实现版本
        System.out.println("java_vm_version:"
                + System.getProperty("java.vm.version"));
        //Java 虚拟机实现供应商
        System.out.println("java_vm_vendor:"
                + System.getProperty("java.vm.vendor"));
        //Java 虚拟机实现名称
        System.out
                .println("java_vm_name:" + System.getProperty("java.vm.name"));
        //一个或多个扩展目录的路径
        System.out.println("java_ext_dirs:"
                + System.getProperty("java.ext.dirs"));
        //文件分隔符
        System.out.println("file_separator:"
                + System.getProperty("file.separator"));
        //路径分隔符
        System.out.println("path_separator:"
                + System.getProperty("path.separator"));
        //行分隔符
        System.out.println("line_separator:"
                + System.getProperty("line.separator"));
        //java.class.path  Java 类路径
        System.out.println("java.class.path:"
                + System.getProperty("java.class.path"));
        //加载库时搜索的路径列表
        System.out.println("java.library.path:"
                + System.getProperty("java.library.path"));
        //默认的临时文件路径
        System.out.println("java.io.tmpdir:"
                + System.getProperty("java.io.tmpdir"));


    }
}
