import crawler.WebCrawler;
import org.assertj.swing.fixture.*;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.mocks.web.WebPage;
import org.hyperskill.hstest.mocks.web.WebServerMock;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrawlerTest extends SwingTest {

    private Map<String, String> mapOfLinksNTitles = pageContent.getLinksNTitles();
    private final String EXPORT_DIRECTORY = Paths.get("").toAbsolutePath().toString() + "/temp.txt";

    private static WebServerMock webServerMock;
    private static PageContent pageContent;
    private static final int PORT = 25555;
    private Map<String, String> mapOfTitles;
    private static List<String> parsedPages;

    public CrawlerTest() {
        super(new WebCrawler());
    }

    @BeforeClass
    public static void initWebServer() {

        System.out.println("Initializing server");
        pageContent = new PageContent();
        parsedPages = new ArrayList<>();

        WebPage exampleDotComPage = new WebPage();
        exampleDotComPage.setContent(pageContent.getContentWithLink("http://localhost:25555/exampleDotCom"));
        exampleDotComPage.setContentType("text/html");

        WebPage circular1Page = new WebPage();
        circular1Page.setContent(pageContent.getContentWithLink("http://localhost:25555/circular1"));
        circular1Page.setContentType("text/html");

        WebPage circular2Page = new WebPage();
        circular2Page.setContent(pageContent.getContentWithLink("http://localhost:25555/circular2"));
        circular2Page.setContentType("text/html");

        WebPage circular3Page = new WebPage();
        circular3Page.setContent(pageContent.getContentWithLink("http://localhost:25555/circular3"));
        circular3Page.setContentType("text/html");

        WebPage unavailablePage = new WebPage();
        unavailablePage.setContent("Web Page not found");

        webServerMock = new WebServerMock(PORT);
        webServerMock.setPage("/exampleDotCom", exampleDotComPage);
        webServerMock.setPage("/circular1", circular1Page);
        webServerMock.setPage("/circular2", circular2Page);
        webServerMock.setPage("/circular3", circular3Page);
        webServerMock.setPage("/unavailablePage", unavailablePage);

        Thread thread = new Thread(() -> {
            webServerMock.start();
            webServerMock.run();
        });

        thread.start();
    }

    @AfterClass
    public static void stopServer() {
        System.out.println("Stopping server");
        webServerMock.stop();
    }

    @After
    public void deleteFile() {
        File file = new File(EXPORT_DIRECTORY);
        if (file.exists()) {
            boolean deleted = file.delete();
        }
    }

    @SwingComponent(name = "UrlTextField")
    JTextComponentFixture textField;

    @SwingComponent(name = "RunButton")
    JToggleButtonFixture runButton;

    @SwingComponent(name = "DepthTextField")
    JTextComponentFixture depthTextField;

    @SwingComponent(name = "DepthCheckBox")
    JCheckBoxFixture depthCheckBox;

    @SwingComponent(name = "ParsedLabel")
    JLabelFixture parsedLabel;

    @SwingComponent(name = "ExportUrlTextField")
    JTextComponentFixture exportUrlTextField;

    @SwingComponent(name = "ExportButton")
    JButtonFixture exportButton;

    @DynamicTest(order = 1)
    CheckResult testComponents() {

        requireVisible(textField);
        requireVisible(runButton);
        requireVisible(exportUrlTextField);
        requireVisible(exportButton);
        requireVisible(depthTextField);
        requireVisible(depthCheckBox);
        requireVisible(parsedLabel);

        requireEnabled(textField);
        requireEnabled(runButton);
        requireEnabled(exportUrlTextField);
        requireEnabled(exportButton);
        requireEnabled(depthTextField);
        requireEnabled(depthCheckBox);
        requireEnabled(parsedLabel);

        return CheckResult.correct();
    }

    @DynamicTest(order = 2)
    CheckResult testParsedLabel() {

        String link = "http://localhost:25555/exampleDotCom";
        textField.setText(link);
        runButton.click();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println();
        }

        int numberOfSubLinks = pageContent.getSubLinksWithLink(link);

        try {
            int parsedLabelText = Integer.parseInt(parsedLabel.text());

            if (parsedLabelText != numberOfSubLinks) {
                return CheckResult.wrong("ParsedLabel shows wrong number of parsed pages");
            }

        } catch (NumberFormatException e) {
            return CheckResult.wrong("ParsedLabel shows wrong number of parsed pages");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 3)
    CheckResult testRunButtonDeselected() {

        textField.setText("http://localhost:25555/exampleDotCom");
        runButton.click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runButton = runButton.requireSelected(false);

        if (runButton == null) {
            return CheckResult.wrong("RunButton should be deselected when there are no more links to parse");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 4)
    CheckResult testFileCreation() {

        exportUrlTextField.setText(EXPORT_DIRECTORY);
        mapOfTitles = pageContent.getLinksNTitles();
        for (Map.Entry<String, String> m : mapOfTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            exportButton.click();
            boolean fileExists = checkFileExistence();
            if (!fileExists) {
                return CheckResult.wrong("Your app did not save a file after exporting.");
            }
            deleteFile();
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 5)
    CheckResult testForDuplicateLinks() {
        for (Map.Entry<String, String> m : mapOfTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            exportButton.click();
            boolean duplicateLinks = checkForDuplicateLinks();
            if (duplicateLinks) {
                return CheckResult.wrong("You should not save links that you have previously parsed.");
            }
        }
        return CheckResult.correct();
    }

    @DynamicTest(order = 6)
    CheckResult testFileNumberOfLines() {

        textField.setText("http://localhost:25555/exampleDotCom");
        runButton.click();
        exportButton.click();
        boolean checkOne = checkFileNumberOfLines(2);

        textField.setText("http://localhost:25555/circular3");
        runButton.click();
        exportButton.click();
        boolean checkTwo = checkFileNumberOfLines(8);

        if (!(checkOne && checkTwo)) {
            return CheckResult.wrong("The file your app saves contains wrong number of lines");
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 7)
    CheckResult testMaximumDepth() {
        for (int i = 1; i <= 3; i++) {
            depthTextField.setText(String.valueOf(i));
            depthCheckBox.check(true);
            textField.setText("http://localhost:25555/circular1");
            runButton.click();
            exportButton.click();

            boolean maxDepthExceeded = checkMaxDepthExceeded(i * 2);
            if (maxDepthExceeded) {
                return CheckResult.wrong("Your program parsed links deeper than the maximum depth");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 8)
    CheckResult testTitlesInFile() {

        depthTextField.setText("");
        depthCheckBox.check(false);
        for (Map.Entry<String, String> m : mapOfTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            exportButton.click();
            boolean valid = checkEvenLines();
            if (!valid) {
                return CheckResult.wrong("The file your app saves contains wrong title for it's parent url");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 9)
    CheckResult testTitlesInFileForDifferentDepths() {

        for (int i = 1; i <= 3; i++) {
            depthTextField.setText(String.valueOf(i));
            depthCheckBox.check(true);
            textField.setText("http://localhost:25555/circular1");
            runButton.click();
            exportButton.click();

            boolean valid = checkEvenLines();
            if (!valid) {
                return CheckResult.wrong("The file your app saves contains wrong title for it's parent url");
            }
        }

        return CheckResult.correct();
    }


    private boolean checkFileExistence() {
        File file = new File(EXPORT_DIRECTORY);
        return file.exists();
    }

    private boolean checkFileNumberOfLines(int expectedLineNumber) {
        int fileLines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_DIRECTORY))) {
            while (reader.readLine() != null) {
                fileLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expectedLineNumber == fileLines;
    }

    private boolean checkForDuplicateLinks() {
        parsedPages.clear();

        boolean duplicateLinks = false;
        int lineNumber = 1;
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_DIRECTORY))) {
            while ((line = reader.readLine()) != null) {
                //Every odd line contains a link
                if (lineNumber % 2 != 0) {
                    if (parsedPages.contains(line)) {
                        duplicateLinks = true;
                    }
                    parsedPages.add(line);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return duplicateLinks;
    }

    private boolean checkMaxDepthExceeded(int expectedLines) {
        int fileLines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_DIRECTORY))) {
            while (reader.readLine() != null) {
                fileLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileLines > expectedLines;
    }

    //Checks if every even line contains the correct title
    private boolean checkEvenLines() {
        boolean valid = true;
        int lineNumber = 1;
        String line;
        String originalTitle = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_DIRECTORY))) {
            while ((line = reader.readLine()) != null) {
                //Every odd line contains a link
                if (lineNumber % 2 != 0) {
                    originalTitle = pageContent.getTitleWithLink(line);
                } else {
                    //Every even line contains a title
                    if (!line.equals(originalTitle)) {
                        valid = false;
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valid;
    }
}
