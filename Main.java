/**
 * Die Klasse Main ist die Hauptklasse des Programms.
 * Sie demonstriert den Produktionsablauf über die Zeit hinweg.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Main {

    public static void main(String[] args) {

        System.out.println(">>> Start Smart Manufacturing Fabrik <<<");
        System.out.println("----------------------------------------");

        // 1. Fabrik initialisieren (startet Manager & Roboter Threads)
        Fabrik testFabrik = new Fabrik();

        try {
            // Szenario 1: Eine kleine Bestellung zum Aufwärmen
            System.out.println("\n[Main]: Gebe Bestellung 1 auf (2 Standard, 2 Premium)...");
            testFabrik.bestellungAufgeben(2, 2);
            
            // Wir warten kurz, damit man sieht, wie der Manager reagiert
            Thread.sleep(2000); 

            // Szenario 2: Nur Standardtüren (geht schneller)
            System.out.println("\n[Main]: Gebe Bestellung 2 auf (5 Standard)...");
            testFabrik.bestellungAufgeben(5, 0);

            // Szenario 3: Nur Premiumtüren (dauert länger)
            System.out.println("\n[Main]: Gebe Bestellung 3 auf (2 Premium)...");
            testFabrik.bestellungAufgeben(0, 2);

            // Jetzt geben wir dem System Zeit, alles abzuarbeiten.
            // Der Roboter und Manager laufen im Hintergrund weiter.
            System.out.println("\n[Main]: Alle Bestellungen aufgegeben. Beobachte Produktion...");
            
            // Optional: Endlosschleife, damit das Programm nicht terminiert, 
            // während die Threads noch arbeiten (falls es keine Daemon-Threads sind).
            // Bei BlueJ meist nicht nötig, aber sauberer für Standalone-Apps.
            while(true) {
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}