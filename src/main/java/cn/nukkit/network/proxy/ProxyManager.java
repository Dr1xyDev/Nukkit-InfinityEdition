/*
 * nukkit Infinity by @Dr1xyDev 
 */
package cn.nukkit.network.proxy;

import cn.nukkit.raknet.server.UDPServerSocket;
import cn.nukkit.utils.ThreadedLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyManager {

    private static ProxyManager instance;

    private final Map<String, ProxySession> byClient = new ConcurrentHashMap<>();
    private final Map<String, ProxySession> byTarget = new ConcurrentHashMap<>();

    private UDPServerSocket serverSocket;
    private ThreadedLogger logger;

    private ProxyManager() {}

    public static ProxyManager getInstance() {
        if (instance == null) {
            instance = new ProxyManager();
        }
        return instance;
    }

    public void init(UDPServerSocket serverSocket, ThreadedLogger logger) {
        this.serverSocket = serverSocket;
        this.logger = logger;
    }

    public boolean startProxy(String clientAddress, int clientPort, String targetAddress, int targetPort) {
        if (serverSocket == null || logger == null) {
            return false;
        }
        stopProxy(clientAddress, clientPort);

        ProxySession session = new ProxySession(clientAddress, clientPort, targetAddress, targetPort, serverSocket, logger);
        byClient.put(session.getIdentifier(), session);
        byTarget.put(session.getTargetIdentifier(), session);
        logger.info("[Proxy] Started: " + session.getIdentifier() + " -> " + session.getTargetIdentifier());
        return true;
    }

    public void stopProxy(String clientAddress, int clientPort) {
        String key = clientAddress + ":" + clientPort;
        ProxySession session = byClient.remove(key);
        if (session != null) {
            byTarget.remove(session.getTargetIdentifier());
            session.close();
        }
    }

    public boolean isClientProxied(String clientAddress, int clientPort) {
        String key = clientAddress + ":" + clientPort;
        ProxySession session = byClient.get(key);
        if (session != null && !session.isActive()) {
            byClient.remove(key);
            byTarget.remove(session.getTargetIdentifier());
            return false;
        }
        return session != null;
    }

    public boolean isTargetSource(String address, int port) {
        return byTarget.containsKey(address + ":" + port);
    }

    public void handleClientPacket(String clientAddress, int clientPort, byte[] data) {
        ProxySession session = byClient.get(clientAddress + ":" + clientPort);
        if (session != null && session.isActive()) {
            session.relayClientToTarget(data);
        }
    }

    public void handleTargetPacket(String targetAddress, int targetPort, byte[] data) {
        ProxySession session = byTarget.get(targetAddress + ":" + targetPort);
        if (session != null && session.isActive()) {
            session.relayTargetToClient(data);
        }
    }

    public void shutdown() {
        for (ProxySession session : byClient.values()) {
            session.close();
        }
        byClient.clear();
        byTarget.clear();
    }

    public boolean isInitialized() {
        return serverSocket != null && logger != null;
    }
}

