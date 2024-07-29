package io.github.netgaze;

public enum ConnectionType {
    HTTP, HTTPS, TCP;

    public static ConnectionType fromString(String connectionType) {
        try {
            return ConnectionType.valueOf(connectionType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown connection type: " + connectionType, e);
        }
    }
}
