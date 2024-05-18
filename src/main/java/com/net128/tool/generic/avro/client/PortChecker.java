package com.net128.tool.generic.avro.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PortChecker {
    public static List<String> parseListeners(String listeners) {
        List<String> hostPorts = new ArrayList<>();
        String[] parts = listeners.split(",");
        for (String part : parts) {
            int protocolIndex = part.indexOf("://");
            if (protocolIndex != -1) {
                hostPorts.add(part.substring(protocolIndex + 3)); // skip past the '://'
            }
        }
        return hostPorts;
    }

    public static boolean isPortAvailable(String hostPort) {
        String[] parts = hostPort.split(":");
        if (parts.length != 2) return false;
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        
        try (Socket socket = new Socket(host, port)) {
            return false; // If the connection is successful, the port is in use
        } catch (Exception e) {
            return true; // If the connection fails, the port is available
        }
    }
    
    public static boolean arePortsAvailable(String listeners) {
        List<String> hostPorts = parseListeners(listeners);
        for (String hostPort : hostPorts) {
            if (!isPortAvailable(hostPort)) {
                System.out.println(hostPort + " is already in use.");
                return false;
            }
        }
        return true;
    }
}
