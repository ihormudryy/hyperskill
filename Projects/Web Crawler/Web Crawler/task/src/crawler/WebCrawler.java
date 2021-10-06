package crawler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class WebCrawler extends JFrame {

    private CrawlerService crawler;
    private JTextField urlTextField;
    private JToggleButton runButton;
    private JTextField workersField;
    private JTextField depthTextField;
    private JCheckBox depthCheckBox;
    private JTextField elapsedTimeTextField;
    private JCheckBox elapsedTimeCheckBox;
    private JLabel elapsedTimeLabel;
    private JLabel parsedLabel;
    private JTextField exportUrlTextField;

    public WebCrawler() {
        super("Web Crawler");
        crawler = new CrawlerService();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setBounds(5, 5, 5, 5);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height / 5;
        int width = screenSize.width / 3;
        setSize(new Dimension(width, height));
        initialize();
        setVisible(true);
    }

    private GridBagConstraints createGbc(int x, int y, double width) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = width;
        gbc.weighty = 0.0;
        return gbc;
    }

    public void initialize() {
        initSearchByURL();
        initWorkers();
        initMaxDepth();
        initTimeLimit();
        initElapsedTimeLabel();
        initParsedLabel();
        initSaveToFileField();
    }

    public void initSearchByURL() {
        JLabel start = new JLabel("Start URL: ");
        start.setName("TitleLabel");
        add(start, createGbc(0, 0, 0.05));

        urlTextField = new JTextField();
        urlTextField.setName("UrlTextField");
        urlTextField.setText("https://example.com");
        add(urlTextField, createGbc(1, 0, 0.9));

        runButton = new JToggleButton();
        runButton.setName("RunButton");
        runButton.setText("Get text!");
        add(runButton, createGbc(2, 0, 0.05));

        runButton.addActionListener(event -> {
            try {
                crawler.setMaxDepth(depthCheckBox.isSelected() ?
                        Integer.parseInt(depthTextField.getText()) : -1);
                crawler.setTimeLimit(elapsedTimeCheckBox.isSelected() ?
                        Integer.parseInt(elapsedTimeTextField.getText()) : -1);
                crawler.setWorkersNumber(Integer.parseInt(workersField.getText()));
                crawler.setSearchUrl(urlTextField.getText());
                crawler.startCrawling();

                parsedLabel.setText(String.valueOf(crawler.getVisitedPages()));
                runButton.setSelected(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Oops!");
            }
        });
    }

    public void initWorkers() {
        JLabel label = new JLabel("Workers: ");
        add(label, createGbc(0, 1, 0.05));

        workersField = new JTextField();
        workersField.setText("5");
        add(workersField, createGbc(1, 1, 0.95));
    }

    public void initMaxDepth() {
        JLabel label = new JLabel("Maximum depth: ");
        add(label, createGbc(0, 2, 0.05));

        depthTextField = new JTextField();
        depthTextField.setName("DepthTextField");
        depthTextField.setText("2");
        add(depthTextField, createGbc(1, 2, 0.8));

        depthCheckBox = new JCheckBox();
        depthCheckBox.setName("DepthCheckBox");
        depthCheckBox.setEnabled(true);
        depthCheckBox.setSelected(false);
        depthCheckBox.setText("Enabled");
        add(depthCheckBox, createGbc(2, 2, 0.1));
    }

    public void initTimeLimit() {
        JLabel label_0 = new JLabel("Time Limit: ");
        add(label_0, createGbc(0, 3, 0.05));

        elapsedTimeTextField = new JTextField();
        elapsedTimeTextField.setText("120");
        add(elapsedTimeTextField, createGbc(1, 3, 0.8));

        JLabel label_1 = new JLabel("seconds");
        add(label_1, createGbc(2, 3, 0.0));

        elapsedTimeCheckBox = new JCheckBox();
        elapsedTimeCheckBox.setEnabled(true);
        elapsedTimeCheckBox.setSelected(true);
        elapsedTimeCheckBox.setText("Enabled");
        add(elapsedTimeCheckBox, createGbc(3, 3, 0.1));
    }

    public void initElapsedTimeLabel() {
        JLabel label = new JLabel("Elapsed time: ");
        add(label, createGbc(0, 4, 0.05));
        elapsedTimeLabel = new JLabel("0:00");
        add(elapsedTimeLabel, createGbc(1, 4, 0.95));
    }

    public void initParsedLabel() {
        JLabel label = new JLabel("Parsed pages: ");
        add(label, createGbc(0, 5, 0.05));
        parsedLabel = new JLabel("0");
        parsedLabel.setName("ParsedLabel");
        add(parsedLabel, createGbc(1, 5, 0.95));
    }

    public void initSaveToFileField() {
        JLabel label = new JLabel("Export: ");
        add(label, createGbc(0, 6, 0.05));

        exportUrlTextField = new JTextField();
        exportUrlTextField.setName("ExportUrlTextField");
        exportUrlTextField.setText(System.getProperty("user.home") + "/urls.txt");
        add(exportUrlTextField, createGbc(1, 6, 0.8));

        JButton button = new JButton();
        button.setName("ExportButton");
        button.setText("Save");
        add(button, createGbc(2, 6, 0.05));
        button.addActionListener(e -> {
            try (PrintWriter fw = new PrintWriter(exportUrlTextField.getText())) {
                fw.flush();
                HashMap map = crawler.getURLsMap();
                System.out.println("writing to file");
                map.keySet().forEach(key -> {
                    fw.write(key + "\n");
                    fw.write(map.get(key) + "\n");
                    System.out.println(key + " " + map.get(key));
                });
            } catch (IOException ioException) {
                System.out.println("File can not be saved");
            }
        });
    }
}