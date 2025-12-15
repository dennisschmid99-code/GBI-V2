/**
 * Der Holzbearbeitungs_Roboter ist ein spezifischer Roboter, der für die
 * Holzarbeiten (Zuschneiden, Bohren, Schleifen) zuständig ist.
 * Er simuliert die Bearbeitungszeit basierend auf dem Türtyp.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Holzbearbeitungs_Roboter extends Roboter {

    // Simulationszeiten basierend auf der Regel: 1 Stunde = 1 Sekunde (1000 ms)
    // 10 Minuten = 1/6 Stunde = ca. 166 ms
    private static final int BEARBEITUNGSZEIT_STANDARD = 166;
    
    // 30 Minuten = 0.5 Stunden = 500 ms
    private static final int BEARBEITUNGSZEIT_PREMIUM = 500;

    /**
     * Konstruktor für den Holzbearbeitungsroboter.
     * Ruft den Konstruktor der Superklasse mit dem entsprechenden Namen auf.
     */
    public Holzbearbeitungs_Roboter() {
        super("Holzbearbeitungsroboter");
    }

    /**
     * Simuliert die Produktion eines Produkts (Holzbearbeitung).
     * Der Thread wird für die entsprechende Bearbeitungszeit schlafen gelegt.
     * @param produkt Das zu bearbeitende Produkt
     */
    @Override
    public void produziereProdukt(Produkt produkt) {
        int schlafZeit = 0;

        // Unterscheidung des Produkttyps zur Festlegung der Bearbeitungszeit
        if (produkt instanceof Standardtuer) {
            schlafZeit = BEARBEITUNGSZEIT_STANDARD;
            System.out.println(gibNamen() + ": Starte Bearbeitung Standardtür (Dauer: 10 Min/Simuliert: " + schlafZeit + "ms)");
        } else if (produkt instanceof Premiumtuer) {
            schlafZeit = BEARBEITUNGSZEIT_PREMIUM;
            System.out.println(gibNamen() + ": Starte Bearbeitung Premiumtür (Dauer: 30 Min/Simuliert: " + schlafZeit + "ms)");
        } else {
            // Fallback, falls ein unbekannter Produkttyp auftaucht
            System.out.println(gibNamen() + ": Unbekanntes Produkt, keine Bearbeitung.");
            return;
        }

        try {
            // Simulation der Arbeit durch Warten
            Thread.sleep(schlafZeit);
        } catch (InterruptedException e) {
            // Fehlerbehandlung bei Unterbrechung des Threads
            System.err.println(gibNamen() + ": Bearbeitung wurde unterbrochen!");
            e.printStackTrace();
        }

        System.out.println(gibNamen() + ": Bearbeitung abgeschlossen für " + produkt);
        
        // WICHTIG: Das Produkt als bereit für den nächsten Schritt markieren (oder abschließen).
        // Auch wenn keine weiteren Roboter folgen, muss das Produkt wissen, 
        // dass dieser Schritt vorbei ist, damit es den Status "FERTIG" erreichen kann.
        produkt.naechsteProduktionsStation(); 
    }
}