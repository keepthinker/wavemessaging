import com.keepthinker.wavemessaging.core.utils.WmUtils;

public class Main {

    public static void main(String[] args) {
//        System.out.println(PropertiesUtils.getString("zookeeper.connectionString"));
//        System.out.println(ZkCommonUtils.create("/a"));
//        System.out.println(ZkCommonUtils.set("/a", "hello"));
//        System.out.println(ZkCommonUtils.get("/a"));
//        System.out.println(ZkCommonUtils.delete("/a"));
        System.out.println(WmUtils.getIPV4Public());
    }

}
