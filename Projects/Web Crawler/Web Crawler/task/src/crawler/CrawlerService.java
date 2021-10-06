package crawler;

import crawler.api.HTMLParser;
import crawler.api.URLFetcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CrawlerService {

    private String startSiteUrl;
    private int visitedPages;
    private int maxDepth;
    private int timeLimit;
    private int workers;
    private Deque<String> queue;
    private HashMap<String, String> urlsMap;

    private void resetCrawlerState() {
        urlsMap = new HashMap<String, String>();
        queue = new LinkedList<String>();
        visitedPages = 0;
    }

    public int getVisitedPages() {
        return visitedPages;
    }

    public HashMap getURLsMap() {
        return urlsMap;
    }

    public void setMaxDepth(int depth) {
        this.maxDepth = depth;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setWorkersNumber(int workers) {
        this.workers = workers;
    }

    public void setSearchUrl(String startUrl) {
        startSiteUrl = startUrl;
    }

    public void startCrawling() throws MalformedURLException {
        resetCrawlerState();
        queue.add(startSiteUrl);
        int currentDepth = 1;
        int currentSize = queue.size();
        int leafs = queue.size();
        while (!queue.isEmpty()) {
            String link = queue.pop();
            URL url = new URL(link);
            String html = URLFetcher.fetchFromURL(url);
            urlsMap.put(link, HTMLParser.getTitle(html));
            Map<String, String> foundURLs = HTMLParser.getAllPageURLs(html, url);
            leafs += foundURLs.size();
            visitedPages++;
            if (--currentSize == 0) {
                currentDepth++;
                currentSize = leafs;
                leafs = 0;
            }
            if (currentDepth < maxDepth || maxDepth == -1) {
                foundURLs.keySet().forEach(key -> {
                    if (urlsMap.get(key) == null) {
                        try {
                            urlsMap.put(key, HTMLParser.getTitle(URLFetcher.fetchFromURL(new URL(key))));
                            queue.addLast(key);
                        } catch (MalformedURLException e) {
                            System.out.println(key + " not found");
                        }
                    }
                });
            } else {
                return;
            }
        }
    }
}
