// this is the main entry point for the applicaiton
public class TagExtractorApp {
    public static void main(String[] args) {
        // ran into thread issue so using this to prevent potential threading issues.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TagExtractorGUI gui = new TagExtractorGUI();
                gui.createAndShowGUI();
            }
        });
    }
}