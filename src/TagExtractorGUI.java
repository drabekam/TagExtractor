import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Map;
import java.util.Set;

public class TagExtractorGUI {

    // Swing components and file variables
    private JFrame frame;
    private JTextArea textArea;
    private File selectedFile;
    private File stopWordsFile;
    private TextFileProcessor fileProcessor;
    private StopWordsManager stopWordsManager;


    public TagExtractorGUI() {
        fileProcessor = new TextFileProcessor();
        stopWordsManager = new StopWordsManager();
    }

    public void createAndShowGUI() {
        // Main frame setup
        frame = new JFrame("Tag Extractor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // creates a Menu bar for file operations found this and it goes on the top bar like anormal app
        JMenuBar menuBar = new JMenuBar();

        // File menu with items
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open Text File");
        JMenuItem stopwordsItem = new JMenuItem("Load Stop Words");
        JMenuItem saveItem = new JMenuItem("Save Tags");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Action listeners for menu items
        openItem.addActionListener(this::openFileAction);
        stopwordsItem.addActionListener(this::loadStopWordsAction);
        saveItem.addActionListener(this::saveTagsAction);
        exitItem.addActionListener(e -> System.exit(0));

        // Adding menu items to the file menu and the menu bar
        fileMenu.add(openItem);
        fileMenu.add(stopwordsItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Setting the menu bar to the frame
        frame.setJMenuBar(menuBar);


        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel with a start processing button
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Processing");
        startButton.addActionListener(this::startProcessingAction);
        buttonPanel.add(startButton);
        frame.add(buttonPanel, BorderLayout.PAGE_END);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //opening a text file through a file chooser
    private void openFileAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            textArea.setText("Selected file: " + selectedFile.getName() + "\n");
        }
    }

    // loading stop words through a file chooser
    private void loadStopWordsAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            stopWordsFile = fileChooser.getSelectedFile();
            textArea.append("Loaded stop words from: " + stopWordsFile.getName() + "\n");
        }
    }

    // Action for starting the tag extraction process
    private void startProcessingAction(ActionEvent event) {

        if (selectedFile != null && stopWordsFile != null) {
            try {

                Set<String> stopWords = stopWordsManager.loadStopWords(stopWordsFile);
                Map<String, Integer> tagFrequency = fileProcessor.processFile(selectedFile, stopWords);

                textArea.setText("");
                tagFrequency.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .forEach(entry -> textArea.append(entry.getKey() + ": " + entry.getValue() + "\n"));

            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error processing files: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a text file and a stop words file first.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Action for saving the tags to a file
    private void saveTagsAction(ActionEvent event) {
        // File chooser to select where to save the tags
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Write the contents of the text area to the selected file
            try (PrintWriter out = new PrintWriter(new FileWriter(fileToSave))) {
                out.println(textArea.getText());
                // Success message upon saving the file
                JOptionPane.showMessageDialog(frame, "Tags saved successfully to " + fileToSave.getName(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                // this is error if the saving fales
                JOptionPane.showMessageDialog(frame, "Failed to save the file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
