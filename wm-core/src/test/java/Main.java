import com.keepthinker.wavemessaging.core.PropertiesUtils;
import com.keepthinker.wavemessaging.core.ZookeeperUtils;

public class Main {

	public static void main(String[] args) {
		System.out.println(PropertiesUtils.getString("zookeeper.connectionString"));
		System.out.println(ZookeeperUtils.create("/a"));
		System.out.println(ZookeeperUtils.set("/a", "hello"));
		System.out.println(ZookeeperUtils.get("/a"));
		System.out.println(ZookeeperUtils.delete("/a"));
	}

}
