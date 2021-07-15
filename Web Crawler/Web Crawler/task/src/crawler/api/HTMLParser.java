package crawler.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HTMLParser {

    public static final Pattern patternTitle = Pattern.compile("\\<title.*?\\>.*?\\<\\/title\\>");
    public static final Pattern patternForTag = Pattern.compile("\\<a.*?href=['\\\"].*?['\\\"].*?\\>");
    public static final Pattern patternForURL = Pattern.compile("href=['\\\"].*?['\\\"]");
    public static final Pattern titleTagName = Pattern.compile("\\<.*?\\>");

    public static Map<String, String> getAllPageURLs(String htmlPage, URL domain) {

        return patternForTag.matcher(htmlPage)
                .results()
                .map(e -> e.group())
                .flatMap(el -> patternForURL.matcher(el)
                        .results()
                        .map(e -> e.group()))
                .map(link -> link.replaceAll("href=", "")
                        .replaceAll("\"", "")
                        .replaceAll("'", ""))
                .map(link -> {
                    try {
                        URL url = URLFetcher.formCorrectURL(link, domain);
                        if (URLFetcher.getURLConnection(url) != null) {
                            return url.toString();
                        }
                    } catch (java.io.IOException | NullPointerException e) {
                        System.out.println(link + " page does not really exist!");
                    }
                    return null;
                })
                .filter(link -> link != null)
                .collect(Collectors.toMap(e -> e.toString(),
                        e -> {
                            try {
                                return getTitle(URLFetcher.fetchFromURL(new URL(e)));
                            } catch (MalformedURLException malformedURLException) {
                                return "";
                            }
                        }));
    }

    public static String getTitle(String text) {
        String title = "";
        Matcher matcher = patternTitle.matcher(text);
        if (matcher.find()) {
            title = titleTagName.matcher(matcher.group()).replaceAll("");
        }
        return title;
    }
}
