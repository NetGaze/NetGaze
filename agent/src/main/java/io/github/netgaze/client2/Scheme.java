package io.github.netgaze.client2;

public enum Scheme {
    HTTP, HTTPS, TCP;

    public static Scheme fromString(String connectionType) {
        try {
            return Scheme.valueOf(connectionType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown connection type: " + connectionType, e);
        }
    }
}
