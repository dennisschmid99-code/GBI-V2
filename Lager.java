import java.util.ArrayList;

/**
 * Klasse Lager
 * Verwaltet Bestände, prüft Verfügbarkeit und löst Nachbestellungen aus.
 * * @author Gruppe 17
 * @version 1.0
 */
public class Lager {

    // --- Konstanten gemäss Aufgabenstellung ---
    private static final int MAXHOLZEINHEITEN = 200;
    private static final int MAXSCHRAUBEN = 1000;
    private static final int MAXFARBEINHEITEN = 100;
    private static final int MAXKARTONEINHEITEN = 100;
    private static final int MAXGLASEINHEITEN = 50;

    // --- Zeit-Konstanten (in Minuten) ---
    // 1 Tag = 1440 Minuten. 2 Tage = 2880 Minuten.
    private static final int STRAFZEIT_MINUTEN = 2880; 

    // --- Aktuelle Bestände ---
    private int holzEinheiten;
    private int schrauben;
    private int farbEinheiten;
    private int kartonEinheiten;
    private int glasEinheiten;

    private Lieferant lieferant;

    /**
     * Konstruktor
     */
    public Lager() {
        this.lieferant = new Lieferant();
        lagerAuffuellen(); // Startet mit vollem Lager
    }

    /**
     * Füllt das Lager auf Maximalwerte auf und ruft den Lieferanten.
     */
    public void lagerAuffuellen() {
        lieferant.bestellungAufgeben();
        this.holzEinheiten = MAXHOLZEINHEITEN;
        this.schrauben = MAXSCHRAUBEN;
        this.farbEinheiten = MAXFARBEINHEITEN;
        this.kartonEinheiten = MAXKARTONEINHEITEN;
        this.glasEinheiten = MAXGLASEINHEITEN;
        // System.out.println("LAGER: Aufgefüllt."); // Optional für Übersicht
    }

    /**
     * Prüft Materialverfügbarkeit und berechnet die Beschaffungszeit.
     * Wenn Material fehlt: 2 Tage Strafe (in Minuten) UND Nachbestellung.
     * * @param bestellung Die zu prüfende Bestellung
     * @return Beschaffungszeit in Minuten (0 oder 2880)
     */
    public int gibBeschaffungsZeit(Bestellung bestellung) {
        int benoetigtHolz = 0;
        int benoetigtSchrauben = 0;
        int benoetigtFarbe = 0;
        int benoetigtKarton = 0;
        int benoetigtGlas = 0;

        // 1. Bedarf ermitteln
        for (Produkt p : bestellung.gibBestellteProdukte()) {
            if (p instanceof Standardtuer) {
                benoetigtHolz += Standardtuer.gibHolzeinheiten();
                benoetigtSchrauben += Standardtuer.gibSchrauben();
                benoetigtFarbe += Standardtuer.gibFarbeinheiten();
                benoetigtKarton += Standardtuer.gibKartoneinheiten();
            } else if (p instanceof Premiumtuer) {
                benoetigtHolz += Premiumtuer.gibHolzeinheiten();
                benoetigtSchrauben += Premiumtuer.gibSchrauben();
                benoetigtFarbe += Premiumtuer.gibFarbeinheiten();
                benoetigtKarton += Premiumtuer.gibKartoneinheiten();
                benoetigtGlas += Premiumtuer.gibGlaseinheiten();
            }
        }

        int zeitAufschlag = 0;

        // 2. Check
        boolean bestandReicht = (holzEinheiten >= benoetigtHolz) &&
                                (schrauben >= benoetigtSchrauben) &&
                                (farbEinheiten >= benoetigtFarbe) &&
                                (kartonEinheiten >= benoetigtKarton) &&
                                (glasEinheiten >= benoetigtGlas);

        if (!bestandReicht) {
            // Logik: Bestände niedrig -> Nachbestellen -> Zeitstrafe
            lagerAuffuellen(); 
            zeitAufschlag = STRAFZEIT_MINUTEN;
        }

        // 3. Material abziehen
        holzEinheiten -= benoetigtHolz;
        schrauben -= benoetigtSchrauben;
        farbEinheiten -= benoetigtFarbe;
        kartonEinheiten -= benoetigtKarton;
        glasEinheiten -= benoetigtGlas;

        return zeitAufschlag;
    }

    /**
     * Gibt Lagerbestand aus.
     */
    public void lagerBestandAusgeben() {
        System.out.println("Lagerbestand: Holz: " + holzEinheiten + ", Schrauben: " + schrauben + 
                           ", Farbe: " + farbEinheiten + ", Karton: " + kartonEinheiten + 
                           ", Glas: " + glasEinheiten);
    }
    
    // Getter für Tests (damit wir prüfen können, ob Bestand sinkt)
    public int gibHolzBestand() { return holzEinheiten; }
}