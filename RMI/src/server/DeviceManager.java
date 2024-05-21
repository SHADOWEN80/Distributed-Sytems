package server;

import Remote.RemoteInterface;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// Represents the details of a connected device
class DeviceInfo {
    private String ip;
    private RemoteInterface remoteInterface;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setRemoteInterface(RemoteInterface remoteInterface) {
        this.remoteInterface = remoteInterface;
    }

    public RemoteInterface getRemoteInterface() {
        return remoteInterface;
    }
// Constructor, getters, and setters
}

public class DeviceManager {
    // Thread-safe collection to store device information
    private static ConcurrentHashMap<String, DeviceInfo> connectedDevices = new ConcurrentHashMap<>();

    public static void addDevice(String macAddress, DeviceInfo deviceInfo) {
        connectedDevices.put(macAddress, deviceInfo);
    }

    public static void removeDevice(String macAddress) {
            connectedDevices.remove(macAddress);
    }

    public static int getConnectedDeviceCount() {
        return connectedDevices.size();
    }
    public static Set getAllDevices(){return connectedDevices.keySet();}
    public static DeviceInfo getDevice(String key) {
        return connectedDevices.get(key);
    }
}
