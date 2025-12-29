import java.util.ArrayList;

/**
 * Die Klasse Lager verwaltet die Rohstoffe für die Produktion.
 * UPDATE: Unterstützt nun atomare Materialentnahme pro Produkt (Just-in-Time).
 *
 * @author GBI Gruppe 17
 * @version 29.12.25
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
     * VERALTET (Legacy): Entnimmt Material für ganze Bestellung. 
     * Wird beibehalten für Kompatibilität, aber intern nutzen wir nun die feinere Methode.
     */
    public synchronized void materialEntnehmen(Bestellung bestellung) {
        for (Produkt p : bestellung.liefereBestellteProdukte()) {
            materialEntnehmen(p);
        }
    }

    /**
     * NEU & ELEGANT: Entnimmt Material für genau EIN Produkt.
     * Blockiert, wenn für dieses eine Produkt nicht genug da ist.
     */
    public synchronized void materialEntnehmen(Produkt produkt) {
        int bedarfHolz = 0;
        int bedarfSchrauben = 0;
        int bedarfFarbe = 0;
        int bedarfKarton = 0;
        int bedarfGlas = 0;

        // Bedarfsermittlung
        if (produkt instanceof Standardtuer) {
            bedarfHolz = Standardtuer.gibHolzeinheiten();
            bedarfSchrauben = Standardtuer.gibSchrauben();
            bedarfFarbe = Standardtuer.gibFarbeinheiten();
            bedarfKarton = Standardtuer.gibKartoneinheiten();
        } else if (produkt instanceof Premiumtuer) {
            bedarfHolz = Premiumtuer.gibHolzeinheiten();
            bedarfSchrauben = Premiumtuer.gibSchrauben();
            bedarfFarbe = Premiumtuer.gibFarbeinheiten();
            bedarfKarton = Premiumtuer.gibKartoneinheiten();
            bedarfGlas = Premiumtuer.gibGlaseinheiten();
        }

        // Guarded Block: Warten bis genug Material für DIESES EINE Produkt da ist
        while (!istGenugMaterialDa(bedarfHolz, bedarfSchrauben, bedarfFarbe, bedarfKarton, bedarfGlas)) {
            System.out.println("[Lager] Engpass bei Produkt! Warte auf Lieferung...");
            pruefeBestandUndBestelle(true); // Bestellung erzwingen
            try {
                wait(); 
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Entnahme
        vorhandeneHolzeinheiten -= bedarfHolz;
        vorhandeneSchrauben -= bedarfSchrauben;
        vorhandeneFarbeinheiten -= bedarfFarbe;
        vorhandeneKartoneinheiten -= bedarfKarton;
        vorhandeneGlaseinheiten -= bedarfGlas;

        // Checken, ob wir DURCH diese Entnahme unter die Schwelle gefallen sind -> Nachbestellen
        pruefeBestandUndBestelle(false);
    }

    private boolean istGenugMaterialDa(int h, int s, int f, int k, int g) {
        return vorhandeneHolzeinheiten >= h && vorhandeneSchrauben >= s &&
               vorhandeneFarbeinheiten >= f && vorhandeneKartoneinheiten >= k &&
               vorhandeneGlaseinheiten >= g;
    }

    private void pruefeBestandUndBestelle(boolean erzwingen) {
        if (lieferant == null) return;

        // Wir prüfen exemplarisch Holz und Schrauben für die Nachbestell-Logik
        boolean knapp = vorhandeneHolzeinheiten < MAX_HOLZEINHEITEN * BESTELLSCHWELLE ||
                        vorhandeneSchrauben < MAX_SCHRAUBEN * BESTELLSCHWELLE;

        if (erzwingen || knapp) {
             int h = Math.max(0, MAX_HOLZEINHEITEN - vorhandeneHolzeinheiten);
             int s = Math.max(0, MAX_SCHRAUBEN - vorhandeneSchrauben);
             int f = Math.max(0, MAX_FARBEINHEITEN - vorhandeneFarbeinheiten);
             int k = Math.max(0, MAX_KARTONEINHEITEN - vorhandeneKartoneinheiten);
             int g = Math.max(0, MAX_GLASEINHEITEN - vorhandeneGlaseinheiten);

             if (h > 0 || s > 0 || f > 0 || k > 0 || g > 0) {
                 lieferant.wareBestellen(h, s, f, k, g);
             }
        }
    }

    public synchronized void wareLiefern(int h, int s, int f, int k, int g) {
        vorhandeneHolzeinheiten = Math.min(MAX_HOLZEINHEITEN, vorhandeneHolzeinheiten + h);
        vorhandeneSchrauben = Math.min(MAX_SCHRAUBEN, vorhandeneSchrauben + s);
        vorhandeneFarbeinheiten = Math.min(MAX_FARBEINHEITEN, vorhandeneFarbeinheiten + f);
        vorhandeneKartoneinheiten = Math.min(MAX_KARTONEINHEITEN, vorhandeneKartoneinheiten + k);
        vorhandeneGlaseinheiten = Math.min(MAX_GLASEINHEITEN, vorhandeneGlaseinheiten + g);
        
        System.out.println("[Lager] Wareneingang! Bestände aufgefüllt.");
        notifyAll(); 
    }

    // Getter für GUI
    public int gibAnzahlHolz() { return vorhandeneHolzeinheiten; }
    public int gibAnzahlSchrauben() { return vorhandeneSchrauben; }
    public int gibAnzahlFarbe() { return vorhandeneFarbeinheiten; }
    public int gibAnzahlKarton() { return vorhandeneKartoneinheiten; }
    public int gibAnzahlGlas() { return vorhandeneGlaseinheiten; }

    public int gibMaxHolz() { return MAX_HOLZEINHEITEN; }
    public int gibMaxSchrauben() { return MAX_SCHRAUBEN; }
    public int gibMaxFarbe() { return MAX_FARBEINHEITEN; }
    public int gibMaxKarton() { return MAX_KARTONEINHEITEN; }
    public int gibMaxGlas() { return MAX_GLASEINHEITEN; }
}