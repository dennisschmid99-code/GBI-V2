import java.util.ArrayList;

/**
 * Die Klasse Lager verwaltet die Rohstoffe für die Produktion.
 * Sie nutzt ein Monitor-Pattern (wait/notify), um die Produktion bei 
 * Materialmangel zu pausieren, statt negative Bestände zu erzeugen.
 *
 * @author GBI Gruppe 17
 * @version 16.12.2025
 */
public class Lager {

    // Maximale Lagerkapazität
    private static final int MAX_HOLZEINHEITEN = 1000;
    private static final int MAX_SCHRAUBEN = 5000;
    private static final int MAX_FARBEINHEITEN = 1000;
    private static final int MAX_KARTONEINHEITEN = 1000;
    private static final int MAX_GLASEINHEITEN = 100;

    // Schwellenwert für automatische Nachbestellung (20%)
    private static final double BESTELLSCHWELLE = 0.2;

    // Aktuelle Bestände
    private int vorhandeneHolzeinheiten;
    private int vorhandeneSchrauben;
    private int vorhandeneFarbeinheiten;
    private int vorhandeneKartoneinheiten;
    private int vorhandeneGlaseinheiten;

    private Lieferant lieferant;

    public Lager() {
        // Initialisierung mit vollem Lager
        this.vorhandeneHolzeinheiten = MAX_HOLZEINHEITEN;
        this.vorhandeneSchrauben = MAX_SCHRAUBEN;
        this.vorhandeneFarbeinheiten = MAX_FARBEINHEITEN;
        this.vorhandeneKartoneinheiten = MAX_KARTONEINHEITEN;
        this.vorhandeneGlaseinheiten = MAX_GLASEINHEITEN;
    }

    public synchronized void setzeLieferant(Lieferant lieferant) {
        this.lieferant = lieferant;
    }

    /**
     * Entnimmt die notwendigen Materialien für eine Bestellung.
     * WICHTIG: Blockiert den Thread, falls nicht genug Material da ist.
     *
     * @param bestellung Die Bestellung, für die Material benötigt wird.
     */
    public synchronized void materialEntnehmen(Bestellung bestellung) {
        ArrayList<Produkt> produkte = bestellung.liefereBestellteProdukte();

        // 1. Gesamtbedarf berechnen
        int bedarfHolz = 0;
        int bedarfSchrauben = 0;
        int bedarfFarbe = 0;
        int bedarfKarton = 0;
        int bedarfGlas = 0;

        for (Produkt produkt : produkte) {
            if (produkt instanceof Standardtuer) {
                bedarfHolz += Standardtuer.gibHolzeinheiten();
                bedarfSchrauben += Standardtuer.gibSchrauben();
                bedarfFarbe += Standardtuer.gibFarbeinheiten();
                bedarfKarton += Standardtuer.gibKartoneinheiten();
            } else if (produkt instanceof Premiumtuer) {
                bedarfHolz += Premiumtuer.gibHolzeinheiten();
                bedarfSchrauben += Premiumtuer.gibSchrauben();
                bedarfFarbe += Premiumtuer.gibFarbeinheiten();
                bedarfKarton += Premiumtuer.gibKartoneinheiten();
                bedarfGlas += Premiumtuer.gibGlaseinheiten();
            }
        }

        // 2. Prüfen ob genug da ist (Guarded Block)
        while (!istGenugMaterialDa(bedarfHolz, bedarfSchrauben, bedarfFarbe, bedarfKarton, bedarfGlas)) {
            System.out.println("[Lager] WARNUNG: Zu wenig Material für Best. " + bestellung.gibBestellungsNr() + 
                               ". Warte auf Lieferung...");
            
            // Bevor wir warten, müssen wir sicherstellen, dass Ware bestellt ist!
            // 'true' erzwingt die Bestellung, auch wenn wir knapp über der 20% Schwelle wären aber für DIESE Bestellung zu wenig haben.
            pruefeBestandUndBestelle(true); 
            
            try {
                wait(); // Thread legt sich schlafen und gibt Lock frei
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("[Lager] Warten unterbrochen.");
                return;
            }
        }

        // 3. Entnahme (Sicher möglich, da Bedingung erfüllt)
        vorhandeneHolzeinheiten -= bedarfHolz;
        vorhandeneSchrauben -= bedarfSchrauben;
        vorhandeneFarbeinheiten -= bedarfFarbe;
        vorhandeneKartoneinheiten -= bedarfKarton;
        vorhandeneGlaseinheiten -= bedarfGlas;

        System.out.println("[Lager] Material für Best. " + bestellung.gibBestellungsNr() + " entnommen. Rest Holz: " + vorhandeneHolzeinheiten);
        
        // 4. Prüfen ob wir durch die Entnahme unter die Schwelle gefallen sind
        pruefeBestandUndBestelle(false);
    }

    /**
     * Prüft, ob für den aktuellen Bedarf genug Rohstoffe da sind.
     */
    private boolean istGenugMaterialDa(int h, int s, int f, int k, int g) {
        return vorhandeneHolzeinheiten >= h && vorhandeneSchrauben >= s &&
               vorhandeneFarbeinheiten >= f && vorhandeneKartoneinheiten >= k &&
               vorhandeneGlaseinheiten >= g;
    }

    /**
     * Prüft Bestände und löst Bestellung beim Lieferanten aus.
     * @param erzwingen Wenn true, wird bestellt auch wenn Schwelle nicht unterschritten (z.B. bei Leerstand).
     */
    private void pruefeBestandUndBestelle(boolean erzwingen) {
        if (lieferant == null) return;

        boolean holzKnapp = vorhandeneHolzeinheiten < MAX_HOLZEINHEITEN * BESTELLSCHWELLE;
        // (Zur Vereinfachung prüfen wir hier primär auf Holz, im echten Leben auf alle)

        if (erzwingen || holzKnapp) {
             // Strategie: Lager komplett auffüllen
             int h = Math.max(0, MAX_HOLZEINHEITEN - vorhandeneHolzeinheiten);
             int s = Math.max(0, MAX_SCHRAUBEN - vorhandeneSchrauben);
             int f = Math.max(0, MAX_FARBEINHEITEN - vorhandeneFarbeinheiten);
             int k = Math.max(0, MAX_KARTONEINHEITEN - vorhandeneKartoneinheiten);
             int g = Math.max(0, MAX_GLASEINHEITEN - vorhandeneGlaseinheiten);

             // Nur bestellen, wenn > 0
             if (h > 0 || s > 0) {
                 lieferant.wareBestellen(h, s, f, k, g);
             }
        }
    }

    /**
     * Nimmt Ware entgegen. Wird vom Lieferanten aufgerufen.
     * Weckt wartende Threads auf.
     */
    public synchronized void wareLiefern(int h, int s, int f, int k, int g) {
        vorhandeneHolzeinheiten = Math.min(MAX_HOLZEINHEITEN, vorhandeneHolzeinheiten + h);
        vorhandeneSchrauben = Math.min(MAX_SCHRAUBEN, vorhandeneSchrauben + s);
        vorhandeneFarbeinheiten = Math.min(MAX_FARBEINHEITEN, vorhandeneFarbeinheiten + f);
        vorhandeneKartoneinheiten = Math.min(MAX_KARTONEINHEITEN, vorhandeneKartoneinheiten + k);
        vorhandeneGlaseinheiten = Math.min(MAX_GLASEINHEITEN, vorhandeneGlaseinheiten + g);
        
        System.out.println("[Lager] Wareneingang! Neuer Holzbestand: " + vorhandeneHolzeinheiten);
        
        // WICHTIG: Weckt den Manager auf, falls er im materialEntnehmen() wartet.
        notifyAll(); 
    }
}