package com.keepthinker.wavemessaging.core;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Constants {
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	public static final String ZK_BROKER_BASE_PATH = "/brokers";
	
	public static final String ZK_HANDLER_BASE_PATH = "/handlers";
	
	public static final String SIGN_SLASH = "/";

	/**
	 * Any address in the range 192.168.xxx.xxx is a private (aka site local) IP address. 
	 * These are reserved for use within an organization. 
	 * The same applies to 10.xxx.xxx.xxx addresses, and 172.16.xxx.xxx through 172.31.xxx.xxx.<br/>
	 * Excerpts from  <a href="http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java">
	 * Getting the IP address of the current machine using Java</>
	 *  
	 */
	public static final String PRIVATE_IP;
	
	static {
		PRIVATE_IP = getIPV4Private();
	}

	/*
	 * private (aka site local) IP address						
	 */
	private static String getIPV4Private(){
		Enumeration<NetworkInterface> n;
		try {
			n = NetworkInterface.getNetworkInterfaces();
			for (; n.hasMoreElements();)
			{
				NetworkInterface e = n.nextElement();

				Enumeration<InetAddress> a = e.getInetAddresses();
				for (; a.hasMoreElements();)
				{
					InetAddress addr = a.nextElement();

					String tempAddr = addr.getHostAddress();
					if(tempAddr.startsWith("192.168.") 
							|| tempAddr.startsWith("10.")){
						return tempAddr;
					}
					if(tempAddr.startsWith("172.")){
						int num = Integer.valueOf(tempAddr.split(".")[1]);
						if(num >= 16 && num <= 31){
							return tempAddr;
						}

					}
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
