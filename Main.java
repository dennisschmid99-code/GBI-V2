import javax.swing.SwingUtilities;

/**
 * Entry Point für die Smart Manufacturing Applikation.
 * Startet das Dashboard und initialisiert das Backend.
 *
 * @author GBI Gruppe 17
 * @version 29.12.2025
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(">>> Start Smart Manufacturing Fabrik v4.0 <<<");
        
        // 1. Backend initialisieren
        Fabrik fabrik = new Fabrik();

        // 2. GUI im Event-Dispatch-Thread starten (Best Practice für Swing)
        SwingUtilities.invokeLater(() -> {
            FabrikGUI gui = new FabrikGUI(fabrik);
            gui.setVisible(true);
            
            // Optional: Konsolen-Hinweis
            System.out.println("[System]: Dashboard geladen. Nutzen Sie 'Demo Szenario' für Funktionstest.");
        });
    }
}