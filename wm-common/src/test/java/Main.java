import com.keepthinker.wavemessaging.core.Constants;
import com.keepthinker.wavemessaging.core.PropertiesUtils;

public class Main {

	public static void main(String[] args) {
		System.out.println(PropertiesUtils.getString("zookeeper.connectionString"));
		
		System.out.println(Constants.PRIVATE_IP);
	}

}
