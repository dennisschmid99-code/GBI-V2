import java.util.ArrayList;

/**
 * Die Klasse Lager verwaltet die Rohstoffe für die Produktion.
 * Sie ist thread-sicher implementiert, da Lieferant und Produktion
 * gleichzeitig darauf zugreifen.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Lager {

    // Konstanten für maximale Lagerkapazität
    private static final int MAX_HOLZEINHEITEN = 1000;
    private static final int MAX_SCHRAUBEN = 5000;
    private static final int MAX_FARBEINHEITEN = 1000;
    private static final int MAX_KARTONEINHEITEN = 1000;
    private static final int MAX_GLASEINHEITEN = 100;

    // Schwellenwert für Nachbestellung (z.B. 20% der Kapazität)
    private static final double BESTELLSCHWELLE = 0.2;

    // Aktuelle Bestände
    private int vorhandeneHolzeinheiten;
    private int vorhandeneSchrauben;
    private int vorhandeneFarbeinheiten;
    private int vorhandeneKartoneinheiten;
    private int vorhandeneGlaseinheiten;

    // Referenz zum Lieferanten
    private Lieferant lieferant;

    /**
     * Konstruktor der Klasse Lager.
     * Initialisiert das Lager voll (oder mit Startwerten).
     */
    public Lager() {
        // Wir starten mit einem gefüllten Lager
        this.vorhandeneHolzeinheiten = MAX_HOLZEINHEITEN;
        this.vorhandeneSchrauben = MAX_SCHRAUBEN;
        this.vorhandeneFarbeinheiten = MAX_FARBEINHEITEN;
        this.vorhandeneKartoneinheiten = MAX_KARTONEINHEITEN;
        this.vorhandeneGlaseinheiten = MAX_GLASEINHEITEN;
    }

    /**
     * Setzt den Lieferanten (wird von der Fabrik aufgerufen).
     * @param lieferant Der Lieferanten-Thread
     */
    public void setzeLieferant(Lieferant lieferant) {
        this.lieferant = lieferant;
    }

    /**
     * Entnimmt die notwendigen Materialien für eine komplette Bestellung.
     * Wird vom Produktions_Manager aufgerufen.
     *
     * @param bestellung Die Bestellung, für die Material benötigt wird.
     */
    public synchronized void materialEntnehmen(Bestellung bestellung) {
        ArrayList<Produkt> produkte = bestellung.liefereBestellteProdukte();

        System.out.println("Lager: Entnehme Material für Bestellung " + bestellung.gibBestellungsNr() + "...");

        for (Produkt produkt : produkte) {
            if (produkt instanceof Standardtuer) {
                // Materialbedarf Standardtür
                vorhandeneHolzeinheiten -= 2;
                vorhandeneSchrauben -= 10;
                vorhandeneFarbeinheiten -= 2;
                vorhandeneKartoneinheiten -= 1;
            } else if (produkt instanceof Premiumtuer) {
                // Materialbedarf Premiumtür
                vorhandeneHolzeinheiten -= 4;
                vorhandeneSchrauben -= 5;
                vorhandeneGlaseinheiten -= 5;
                vorhandeneFarbeinheiten -= 1;
                vorhandeneKartoneinheiten -= 5;
            }
        }

        lagerBestandAusgeben();
        pruefeBestandUndBestelle();
    }

    /**
     * Prüft, ob Bestände kritisch niedrig sind und löst ggf. eine Bestellung aus.
     */
    private void pruefeBestandUndBestelle() {
        if (lieferant == null) return;

        boolean bestellungNoetig = false;
        
        // Berechnung der Nachbestellmengen (auffüllen bis Max)
        int bestellHolz = 0;
        int bestellSchrauben = 0;
        int bestellFarbe = 0;
        int bestellKarton = 0;
        int bestellGlas = 0;

        // Prüfen ob Schwellenwerte unterschritten sind
        if (vorhandeneHolzeinheiten < MAX_HOLZEINHEITEN * BESTELLSCHWELLE) {
            bestellHolz = MAX_HOLZEINHEITEN - vorhandeneHolzeinheiten;
            bestellungNoetig = true;
        }
        if (vorhandeneSchrauben < MAX_SCHRAUBEN * BESTELLSCHWELLE) {
            bestellSchrauben = MAX_SCHRAUBEN - vorhandeneSchrauben;
            bestellungNoetig = true;
        }
        // ... (Logik kann für andere Materialien erweitert werden) ...
        // Vereinfacht bestellen wir hier alles Fehlende nach, wenn IRGENDWAS knapp wird:
        if (bestellungNoetig) {
             bestellHolz = MAX_HOLZEINHEITEN - vorhandeneHolzeinheiten;
             bestellSchrauben = MAX_SCHRAUBEN - vorhandeneSchrauben;
             bestellFarbe = MAX_FARBEINHEITEN - vorhandeneFarbeinheiten;
             bestellKarton = MAX_KARTONEINHEITEN - vorhandeneKartoneinheiten;
             bestellGlas = MAX_GLASEINHEITEN - vorhandeneGlaseinheiten;

             // Bestellung an Lieferant senden
             boolean akzeptiert = lieferant.wareBestellen(bestellHolz, bestellSchrauben, bestellFarbe, bestellKarton, bestellGlas);
             
             if (akzeptiert) {
                 System.out.println("Lager: Kritischer Bestand! Nachbestellung ausgelöst.");
             }
        }
    }

    /**
     * Nimmt gelieferte Ware entgegen und füllt die Bestände auf.
     * Wird vom Lieferanten-Thread aufgerufen.
     */
    public synchronized void wareLiefern(int holz, int schrauben, int farbe, int karton, int glas) {
        vorhandeneHolzeinheiten += holz;
        vorhandeneSchrauben += schrauben;
        vorhandeneFarbeinheiten += farbe;
        vorhandeneKartoneinheiten += karton;
        vorhandeneGlaseinheiten += glas;
        
        System.out.println("Lager: Wareneingang verbucht!");
        lagerBestandAusgeben();
    }

    /**
     * Gibt den aktuellen Lagerbestand auf der Konsole aus.
     */
    public void lagerBestandAusgeben() {
        System.out.println("--- Aktueller Lagerbestand ---");
        System.out.println("Holz: " + vorhandeneHolzeinheiten + " / " + MAX_HOLZEINHEITEN);
        System.out.println("Schrauben: " + vorhandeneSchrauben + " / " + MAX_SCHRAUBEN);
        System.out.println("Farbe: " + vorhandeneFarbeinheiten + " / " + MAX_FARBEINHEITEN);
        System.out.println("Karton: " + vorhandeneKartoneinheiten + " / " + MAX_KARTONEINHEITEN);
        System.out.println("Glas: " + vorhandeneGlaseinheiten + " / " + MAX_GLASEINHEITEN);
        System.out.println("------------------------------");
    }

    /**
     * Berechnet die Beschaffungszeit für eine Bestellung.
     * (Aus Aufgabe 2 übernommen und angepasst)
     */
    public int gibBeschaffungsZeit(Bestellung kundenBestellung) {
        // Hier könnte man prüfen, ob genug Material für DIESE Bestellung da ist.
        // Falls ja: 0 Tage. Falls nein: 2 Tage (Lieferzeit).
        // Vereinfacht für Task 3: Wir geben 0 zurück, da der Manager das Material einfach entnimmt
        // und wir davon ausgehen, dass genug da ist bzw. nachbestellt wird.
        return 0; 
    }
    
    // Alte Methode aus Aufgabe 2, falls noch benötigt, kann hier bleiben oder angepasst werden.
    public void lagerAuffuellen() {
        // Leere Implementierung oder Aufruf an Lieferant, falls manuell gewünscht
    }
}