import com.keepthinker.wavemessaging.core.PropertiesUtils;

public class Main {

	public static void main(String[] args) {
		System.out.println(PropertiesUtils.getString("zookeeper.connectionString"));
	}

}
