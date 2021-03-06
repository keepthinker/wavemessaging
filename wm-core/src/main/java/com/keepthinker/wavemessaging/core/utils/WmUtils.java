package com.keepthinker.wavemessaging.core.utils;

import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Pattern;

public class WmUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String NETWORK_PREFIX = PropertiesUtils.getString("network.prefix");
    private static final SubnetUtils.SubnetInfo SUBNET_INFO;
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    static {
        System.out.println("properties: network.prefix=" + NETWORK_PREFIX);
        if (StringUtils.isNotBlank(NETWORK_PREFIX)) {
            SUBNET_INFO = new SubnetUtils(NETWORK_PREFIX).getInfo();
        } else {
            SUBNET_INFO = null;
        }
    }

    /**
     * presuming only one network card in computer working in intranet.<br/>
     * return a private (aka site local) IP address.<br/>
     * <br/>
     * if <b>network.prefix</b> is set in <b>properties file</b>, try to find a ip in that network, if not, try to
     * get first ip within private network.<br/>
     * Any address in the range 192.168.xxx.xxx is a private (aka site local) IP address.
     * These are reserved for use within an organization.
     * The same applies to 10.xxx.xxx.xxx addresses, and 172.16.xxx.xxx through 172.31.xxx.xxx.<br/>
     * Excerpts from  <a href="http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java">
     * Getting the IP address of the current machine using Java</>
     */
    public static String getIPV4Private() {

        Enumeration<NetworkInterface> n;
        try {
            n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements(); ) {
                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements(); ) {
                    InetAddress addr = a.nextElement();

                    String tempAddr = addr.getHostAddress();

                    if (!isIpv4Addr(tempAddr)) { // ignore ipv6
                        continue;
                    }
                    if (SUBNET_INFO != null) {
                        if (SUBNET_INFO.isInRange(tempAddr)) {
                            return tempAddr;
                        }
                    } else {
                        if (tempAddr.startsWith("192.168.")
                                || tempAddr.startsWith("10.")) {
                            return tempAddr;
                        }
                        if (tempAddr.startsWith("172.")) {
                            int num = Integer.valueOf(tempAddr.split(".")[1]);
                            if (num >= 16 && num <= 31) {
                                return tempAddr;
                            }

                        }
                    }
                }
            }
        } catch (SocketException e1) {
            LOGGER.error("error in getting borker intranet ip", e1);
            throw new RuntimeException(e1);
        }
        LOGGER.error("error in getting borker intranet ip");
        throw new RuntimeException();
    }

    public static String getIPV4Public() {
        Enumeration<NetworkInterface> n;
        try {
            n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements(); ) {
                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements(); ) {
                    InetAddress addr = a.nextElement();
                    String tempAddr = addr.getHostAddress();
                    LOGGER.debug(tempAddr);
                    if (!isIpv4Addr(tempAddr)) { // ignore ipv6
                        continue;
                    }

                    if  (!(addr.isSiteLocalAddress()
                            || addr.isLoopbackAddress()
                            || addr.getHostAddress().contains(":")
                            )){
                        return tempAddr;
                    }
                }
            }
        } catch (SocketException e1) {
            LOGGER.error("error in getting borker public ip", e1);
            return null;
        }
        LOGGER.error("error in getting borker public ip");
        return null;
    }

    public static boolean isIpv4Addr(final String ip) {
        return IPV4_PATTERN.matcher(ip).matches();
    }

    public static long generateUniqueId() {
        UUID uniqueKey = UUID.randomUUID();
        return Math.abs(uniqueKey.getLeastSignificantBits());
    }

    public static String getChannelRemoteAddress(Channel channel){
        InetSocketAddress address  = (InetSocketAddress)channel.remoteAddress();
        if(address != null) {
            return address.getHostName() + ":" +  address.getPort();
        }else {
            return null;
        }
    }

    public static String getChannelLocalAddress(Channel channel){
        InetSocketAddress address = (InetSocketAddress)channel.localAddress();
        if(address != null){
            return address.getHostName() + ":" + address.getPort();
        }else{
            return null;
        }
    }

    public static int clearInvalidChannel(Collection<Channel> col){
        int count = 0;
        Iterator<Channel> ite = col.iterator();
        while(ite.hasNext()){
            Channel channel = ite.next();
            if(!channel.isActive()){
                channel.close();
                ite.remove();
                count++;
            }
        }
        return count;
    }


}
