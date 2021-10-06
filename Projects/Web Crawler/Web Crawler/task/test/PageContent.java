import java.util.HashMap;
import java.util.Map;

public class PageContent {
    private Map<String, String> linksNContent;
    private Map<String, String> linksNTitles;
    private Map<String, Integer> linksNSubLinks;
    public PageContent(){
        linksNContent = new HashMap<>();
        linksNTitles = new HashMap<>();
        linksNSubLinks = new HashMap<>();
        initPageContent();
    }
    
    private void initPageContent() {
        //Links
        String exampleDomainLink = "http://localhost:25555/exampleDotCom";
        String circular1Link = "http://localhost:25555/circular1";
        String circular2Link = "http://localhost:25555/circular2";
        String circular3Link = "http://localhost:25555/circular3";
    
        //Titles
        String exampleDomainTitle = "Example Domain";
        String circular1Title = "circular1tiTle";
        String circular2Title = "circular2tiTle";
        String circular3Title = "circular3tiTle";
    
        //Contents
        String exampleDomainContent = "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Example Domain</title>\n" +
                "\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" +
                "    <style type=\"text/css\">\n" +
                "    body {\n" +
                "        background-color: #f0f0f2;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        font-family: \"Open Sans\", \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" +
                "\n" +
                "    }\n" +
                "    div {\n" +
                "        width: 600px;\n" +
                "        margin: 5em auto;\n" +
                "        padding: 50px;\n" +
                "        background-color: #fff;\n" +
                "        border-radius: 1em;\n" +
                "    }\n" +
                "    a:link, a:visited {\n" +
                "        color: #38488f;\n" +
                "        text-decoration: none;\n" +
                "    }\n" +
                "    @media (max-width: 700px) {\n" +
                "        body {\n" +
                "            background-color: #fff;\n" +
                "        }\n" +
                "        div {\n" +
                "            width: auto;\n" +
                "            margin: 0 auto;\n" +
                "            border-radius: 0;\n" +
                "            padding: 1em;\n" +
                "        }\n" +
                "    }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div>\n" +
                "    <h1>Example of Example Domain</h1>\n" +
                "    <p>This domain is established to be used for illustrative examples in documents. You may use this\n" +
                "    domain in examples without prior coordination or asking for permission.</p>\n" +
                "    <p><a href=\"unavailablePage\">More information...</a></p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    
        String circular1Content = "\n" +
                "            |<!doctype html>\n" +
                "            |<html>\n" +
                "            |<head>\n" +
                "            |<title>circular1tiTle</title>\n" +
                "            |</head>\n" +
                "            |<body>\n" +
                "            |<a href=\"circular2\">link1</a>\n" +
                "            |</body>\n" +
                "            |</html>\n" +
                "        ";
    
        String circular2Content = "\n" +
                "            |<!doctype html>\n" +
                "            |<html>\n" +
                "            |<head>\n" +
                "            |<title>circular2tiTle</title>\n" +
                "            |</head>\n" +
                "            |<body>\n" +
                "            |<a href=\"circular3\">link1</a>\n" +
                "            |</body>\n" +
                "            |</html>\n" +
                "        ";
    
        String circular3Content = "\n" +
                "            |<!doctype html>\n" +
                "            |<html>\n" +
                "            |<head>\n" +
                "            |<title>circular3tiTle</title>\n" +
                "            |</head>\n" +
                "            |<body>\n" +
                "            |<a href=\"circular1\">link</a>\n" +
                "            |<a href=\"exampleDotCom\">link</a>\n" +
                "            |</body>\n" +
                "            |</html>\n" +
                "        ";
    
        linksNContent.put(exampleDomainLink, exampleDomainContent);
        linksNContent.put(circular1Link, circular1Content);
        linksNContent.put(circular2Link, circular2Content);
        linksNContent.put(circular3Link, circular3Content);
    
        linksNTitles.put(exampleDomainLink, exampleDomainTitle);
        linksNTitles.put(circular1Link, circular1Title);
        linksNTitles.put(circular2Link, circular2Title);
        linksNTitles.put(circular3Link, circular3Title);
        
        linksNSubLinks.put(exampleDomainLink, 1);
        linksNSubLinks.put(circular1Link, 2);
        linksNSubLinks.put(circular2Link, 2);
        linksNSubLinks.put(circular3Link, 3);
    }
    
    public Map<String, String> getLinksNTitles(){return linksNTitles;}
    public int getSubLinksWithLink(String link){return linksNSubLinks.getOrDefault(link, 0);}
    public String getContentWithLink(String link){
        return linksNContent.getOrDefault(link, "");
    }
    public String getTitleWithLink(String link){
        return linksNTitles.getOrDefault(link, "");
    }
}
