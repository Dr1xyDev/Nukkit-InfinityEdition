/*
 * nukkit Infinity by @Dr1xyDev 
 */
package cn.nukkit.network.proxy;

import cn.nukkit.raknet.server.UDPServerSocket;
import cn.nukkit.utils.ThreadedLogger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProxySession {

    private final String clientAddress;
    private final int clientPort;
    private final String targetAddress;
    private final int targetPort;
    private final UDPServerSocket serverSocket;
    private final ThreadedLogger logger;
    private final AtomicBoolean active = new AtomicBoolean(true);

    public ProxySession(String clientAddress, int clientPort, String targetAddress, int targetPort, UDPServerSocket serverSocket, ThreadedLogger logger) {
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;
        this.serverSocket = serverSocket;
        this.logger = logger;
    }

    public void relayClientToTarget(byte[] data) {
        if (!active.get()) return;
        try {
            serverSocket.writePacket(data, targetAddress, targetPort);
        } catch (IOException e) {
            if (active.get()) {
                logger.warning("[Proxy] relay client->target error: " + e.getMessage());
            }
        }
    }

    public void relayTargetToClient(byte[] data) {
        if (!active.get()) return;
        try {
            serverSocket.writePacket(data, clientAddress, clientPort);
        } catch (IOException e) {
            if (active.get()) {
                logger.warning("[Proxy] relay target->client error: " + e.getMessage());
            }
        }
    }

    public void close() {
        active.set(false);
        logger.info("[Proxy] Session closed: " + clientAddress + ":" + clientPort + " -> " + targetAddress + ":" + targetPort);
    }

    public boolean isActive() {
        return active.get();
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public String getIdentifier() {
        return clientAddress + ":" + clientPort;
    }

    public String getTargetIdentifier() {
        return targetAddress + ":" + targetPort;
    }
}

