package cn.wzgzs.springboot.utils;



import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Seven on 16/2/23.
 */




import com.google.common.net.InetAddresses;

public abstract class IpAddressUtils {
    private static final Logger logger = LoggerFactory.getLogger(IpAddressUtils.class);
    private static final Pattern IP_PATTERN = Pattern.compile("^[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}$");
    private static final Pattern PRIVATE_IP_PATTERN = Pattern.compile("127\\.0\\.0\\.1");
    private static String localIp = doGetLocalIp();
    private static final List<IpAddressUtils.AddressRange> validAddressRanges = new ArrayList();

    public IpAddressUtils() {
    }

    public static String getLocalIp() {
        return localIp;
    }

    public static String doGetLocalIp() {
        try {
            InetAddress e = getInetAddress();
            if(e != null) {
                return e.getHostAddress();
            }
        } catch (SocketException var1) {
            logger.warn("could not get local ip", var1);
        }

        return null;
    }

    public static String getRequestUserIp(HttpServletRequest request) {
        String ip = parseRequestIp(request.getHeader("HTTP_CDN_SRC_IP"));
        if(StringUtils.isBlank(ip)) {
            ip = parseRequestIp(request.getHeader("X-Forwarded-For"));
        }

        if(StringUtils.isBlank(ip)) {
            ip = parseRequestIp(request.getHeader("HTTP_X_FORWARDED_FOR"));
        }

        if(StringUtils.isBlank(ip)) {
            ip = parseRequestIp(request.getHeader("HTTP_CLIENT_IP"));
        }

        if(StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    private static String parseRequestIp(String s) {
        if(!StringUtils.isBlank(s) && !"unknown".equalsIgnoreCase(s)) {
            String[] splits = s.split(",");
            if(splits != null && splits.length != 0) {
                for(int i = 0; i < splits.length; ++i) {
                    String split = splits[i];
                    if(StringUtils.isNotBlank(split)) {
                        String ip = split.trim();
                        Matcher matcher = IP_PATTERN.matcher(ip);
                        if(matcher.matches() && !isPrivateIp(ip)) {
                            return ip;
                        }
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static boolean isPrivateIp(String s) {
        if(StringUtils.isBlank(s)) {
            return false;
        } else {
            Matcher matcher = PRIVATE_IP_PATTERN.matcher(s);
            return matcher.matches();
        }
    }

    public static boolean isInternalIp(String ip) {
        if(StringUtils.isBlank(ip)) {
            return false;
        } else {
            Matcher matcher = IP_PATTERN.matcher(ip);
            if(!matcher.matches()) {
                return false;
            } else if(isPrivateIp(ip)) {
                return true;
            } else {
                InetAddress address = InetAddresses.forString(ip);
                Iterator var3 = validAddressRanges.iterator();

                IpAddressUtils.AddressRange range;
                do {
                    if(!var3.hasNext()) {
                        return false;
                    }

                    range = (IpAddressUtils.AddressRange)var3.next();
                } while(!range.isInRange(address));

                return true;
            }
        }
    }

    public static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0L;
        byte[] var4 = octets;
        int var5 = octets.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            byte octet = var4[var6];
            result <<= 8;
            result |= (long)(octet & 255);
        }

        return result;
    }

    public static InetAddress getInetAddress(NetworkInterface networkInterface) {
        Enumeration inetAddressEnumeration = networkInterface.getInetAddresses();

        InetAddress inetAddress;
        do {
            if(!inetAddressEnumeration.hasMoreElements()) {
                return null;
            }

            inetAddress = (InetAddress)inetAddressEnumeration.nextElement();
        } while(inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress());

        return inetAddress;
    }

    public static InetAddress getInetAddress() throws SocketException {
        Enumeration networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();

        InetAddress inetAddress;
        do {
            if(!networkInterfaceEnumeration.hasMoreElements()) {
                return null;
            }

            inetAddress = getInetAddress((NetworkInterface)networkInterfaceEnumeration.nextElement());
        } while(inetAddress == null);

        return inetAddress;
    }

    public static InetAddress getInetAddress(String interfaceName) throws SocketException {
        return getInetAddress(NetworkInterface.getByName(interfaceName));
    }

    static {
        validAddressRanges.add(new IpAddressUtils.AddressRange("192.168.0.0", "192.168.255.255"));
        validAddressRanges.add(new IpAddressUtils.AddressRange("10.0.0.0", "10.255.255.255"));
        validAddressRanges.add(new IpAddressUtils.AddressRange("172.16.0.0", "172.31.255.255"));
    }

    private static class AddressRange {
        private long low;
        private long high;

        public AddressRange(String low, String high) {
            InetAddress lowAddress = InetAddresses.forString(low);
            InetAddress highAddress = InetAddresses.forString(high);
            this.low = IpAddressUtils.ipToLong(lowAddress);
            this.high = IpAddressUtils.ipToLong(highAddress);
        }

        public boolean isInRange(InetAddress address) {
            long ip = IpAddressUtils.ipToLong(address);
            return ip >= this.low && ip <= this.high;
        }
    }
}
