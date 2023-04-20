package string_utils;

import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.net.URI;

public class StringUtils {
    public static void main(String[] args) {
        new StringUtils().run();

    }


    private void run() {
        String ip = " 127.0.0.1 ";
        System.out.println(Arrays.toString(uri2Array("ftp:///#anchor")));
        System.out.println(Arrays.toString(uri2Array("ftp:///")));
        System.out.println(Arrays.toString(uri2Array("ftp:///?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array("ftp:///pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array("ftp://1.2.3.4/pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array("ftp://1.2.3.4:25/pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array("ftp://account:pass@1.2.3.4:25/pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println();
        System.out.println(Arrays.toString(uri2Array1("ftp:///#anchor")));
        System.out.println(Arrays.toString(uri2Array1("ftp:///")));
        System.out.println(Arrays.toString(uri2Array1("ftp:///?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array1("ftp:///pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array1("ftp://1.2.3.4/pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array1("ftp://1.2.3.4:25/pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println(Arrays.toString(uri2Array1("ftp://account:pass@1.2.3.4:25/pass0/pass1/pass2?a=1&b=2#anchor")));
        System.out.println();
        System.out.println(Arrays.toString(uri2Array2("ftp:///#anchor")));
        System.out.println(toCamelcase("hELLo java     WorLD!"));
        System.out.println(Arrays.toString(ip2Bytes(ip)));
        System.out.println(Arrays.toString(ip2Bytes2(ip)));
        System.out.println(fromCamelCase("HelloJavaWorld!"));
        System.out.println(alphabet());
        System.out.println(convertIp(ip));
        System.out.println(convertIp("000.000.000.000"));
    }

    private String convertIp(String ip) {
        if (ip.trim().matches("([0-9]{1,3})(\\.([0-9]{1,3})){3}")) {
            try {
                InetAddress address = InetAddress.getByName(ip.trim());
                byte[] bytes = address.getAddress();
                return String.format("%03d.%03d.%03d.%03d", bytes[0] & 0xFF, bytes[1] & 0xFF, bytes[2] & 0xFF, bytes[3] & 0xFF);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String[] uri2Array1(String uri) {
        String[] result = new String[8];

        try {
            URI parsedUri = new URI(uri);

            result[0] = parsedUri.getScheme();

            String userInfo = parsedUri.getUserInfo();
            if (userInfo != null) {
                int colonIndex = userInfo.indexOf(":");
                if (colonIndex != -1) {
                    result[1] = userInfo.substring(0, colonIndex);
                    result[2] = userInfo.substring(colonIndex + 1);
                } else {
                    result[1] = userInfo;
                }
            }

            result[3] = parsedUri.getHost();
            int port = parsedUri.getPort();
            if (port != -1) {
                result[4] = Integer.toString(port);
            }

            String path = parsedUri.getPath();
            if (path != null && !path.isEmpty()) {
                result[5] = path.substring(1);
            }

            String query = parsedUri.getQuery();
            if (query != null) {
                result[6] = query;
            }

            String fragment = parsedUri.getFragment();
            if (fragment != null) {
                result[7] = fragment;
            }

        } catch (URISyntaxException e) {
            // URI is not valid, return an array of null values
            Arrays.fill(result, null);
        }

        return result;
    }

    public String[] uri2Array(String uri) {
        String[] result = new String[8];
        // anchor elvágása
        int anchorIndex = uri.indexOf("#");
        if (anchorIndex != -1) {
            result[7] = uri.substring(anchorIndex + 1);
            uri = uri.substring(0, anchorIndex);
        }

        // schema kezelése
        int schemaIndex = uri.indexOf("://");
        if (schemaIndex != -1) {
            result[0] = uri.substring(0, schemaIndex);
            uri = uri.substring(schemaIndex + 3);
        }

        // felhasználónév és jelszó kezelése
        int loginIndex = uri.indexOf("@");
        if (loginIndex != -1) {
            String loginPart = uri.substring(0, loginIndex);
            uri = uri.substring(loginIndex + 1);
            int passwordIndex = loginPart.indexOf(":");
            if (passwordIndex != -1) {
                result[1] = loginPart.substring(0, passwordIndex);
                result[2] = loginPart.substring(passwordIndex + 1);
            } else {
                result[1] = loginPart;
            }
        }

        // port és host kezelése
        int portIndex = uri.indexOf(":");
        int pathIndex = uri.indexOf("/");
        if (portIndex != -1 && portIndex < pathIndex) {
            result[4] = uri.substring(portIndex + 1, pathIndex);
            result[3] = uri.substring(0, portIndex);
            uri = uri.substring(pathIndex + 1);
        } else {
            result[3] = uri.substring(0, pathIndex);
            uri = uri.substring(pathIndex + 1);
        }

        // paraméterek kezelése
        int paramsIndex = uri.indexOf("?");
        if (paramsIndex != -1) {
            result[5] = uri.substring(0, paramsIndex);
            result[6] = uri.substring(paramsIndex + 1);
        }
        return result;
    }

    private String[] uri2Array2(String uri) {
        String[] result = new String[8];
        Matcher matcher = Pattern.compile("(?<scheme>[^:]+)://(?:(?<username>[^:]+):(?<password>[^@]+)@)?(?<host>[^/:]+)(?::(?<port>\\d+))?(/(?<path>[^?]*))?\\??(?<query>[^#]*)(?:#(?<fragment>.*))?").matcher(uri);
        if (matcher.matches()) {
            result[0] = matcher.group("scheme");
            result[1] = matcher.group("username");
            result[2] = matcher.group("password");
            result[3] = matcher.group("host");
            result[4] = matcher.group("port");
            result[5] = matcher.group("path");
            result[6] = matcher.group("query");
            result[7] = matcher.group("fragment");
        }
        return result;
    }

    private String toCamelcase(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : str.trim().split("\\W+")) {
            sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1).toLowerCase());
        }
        sb.setCharAt(0, str.charAt(0));
        return sb.toString();
    }

    public String fromCamelCase(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c) && s.charAt(i - 1) != ' ') {
                sb.append(" ");
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }


    private StringBuilder alphabet() {
        StringBuilder mixedCaseAlphabet = new StringBuilder();
        for (char c = 'A'; c <= 'Z'; c++) {
            mixedCaseAlphabet.append(Character.toLowerCase(c++));
            mixedCaseAlphabet.append(c);
        }
        return mixedCaseAlphabet;
    }


    private byte[] ip2Bytes2(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.getAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }


    public byte[] ip2Bytes(String ip) {
        if (ip.trim().matches("([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5])(\\.(0|([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5]))){3}")) {
            int[] ipOctets = new int[4];
            int i = 0;
            for (String octet : ip.trim().split("\\.")) {
                int temp = Integer.parseInt(octet);
                ipOctets[i++] = temp < 128 ? temp : temp - 256;
            }
            byte[] ipBytes = new byte[4];
            for (i = 0; i < ipOctets.length; i++) {
                ipBytes[i] = (byte) ipOctets[i];
            }
            return ipBytes;
        }
        return null;
    }
}
