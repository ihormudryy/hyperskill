package crawler.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class URLFetcher {

    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0";

    public static URL formCorrectURL(String url, URL domain) throws MalformedURLException {
        return url.contains("http") ? new URL(url) :
                new URL(domain.getProtocol() +
                        "://" + domain.getHost() +
                        (domain.getPort() != -1 ? ":" + domain.getPort() : "") +
                        "/" + url);
    }

    public static URLConnection getURLConnection(URL link) throws IOException {
        URLConnection connection = link.openConnection();
        connection.setRequestProperty("User-Agent", userAgent);
        if (connection.getContentType()
                .contains("text/html")) {
            return link.openConnection();
        }
        throw new NullPointerException("Connection was not initialized");
    }

    public static String fetchFromURL(URL url) {
        try {
            InputStream inputStream = new BufferedInputStream(getURLConnection(url).getInputStream());
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(url + " does not exist");
            throw new RuntimeException("URL " + url + " could not be loaded");
        }
    }
}
