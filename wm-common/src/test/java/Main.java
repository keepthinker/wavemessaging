import org.apache.commons.net.util.SubnetUtils;
import org.junit.Test;

import com.keepthinker.wavemessaging.common.PropertiesUtils;
import com.keepthinker.wavemessaging.common.WmUtils;

public class Main {

	@Test
	public void testIpAddr() {
		System.out.println(PropertiesUtils.getString("zookeeper.connectionString"));
		System.out.println(PropertiesUtils.getString("network.prefix"));
		SubnetUtils subnetUtils = new SubnetUtils("192.168.0.0/24");
		SubnetUtils.SubnetInfo subnetInfo = subnetUtils.getInfo();
		System.out.println(subnetInfo.isInRange("192.168.0.112"));
		System.out.println(subnetInfo.isInRange("192.168.253.1"));
		System.out.println(WmUtils.getIPV4Private());
	}
	
	public static void main(String[] args){
		System.out.println(WmUtils.getIPV4Private());
	}

}
