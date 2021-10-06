package chat.config;

public enum Configuration {

    LOCALHOST("localhost", 12345);

    String ip;
    int port;

    Configuration(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
