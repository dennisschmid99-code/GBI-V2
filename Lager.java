import java.util.ArrayList;

/**
 * Die Klasse Lager verwaltet die Rohstoffe.
 * UPDATE: Konfigurierbare Mindestbestände pro Material (Safety Stock).
 *
 * @author GBI Gruppe 17
 * @version 29.12.2025
 */
public class Lager {

    // Maximale Kapazitäten
    private static final int MAX_HOLZEINHEITEN = 1000;
    private static final int MAX_SCHRAUBEN = 5000;
    private static final int MAX_FARBEINHEITEN = 1000;
    private static final int MAX_KARTONEINHEITEN = 1000;
    private static final int MAX_GLASEINHEITEN = 100; 

    // Bestände
    private int vorhandeneHolzeinheiten = MAX_HOLZEINHEITEN;
    private int vorhandeneSchrauben = MAX_SCHRAUBEN;
    private int vorhandeneFarbeinheiten = MAX_FARBEINHEITEN;
    private int vorhandeneKartoneinheiten = MAX_KARTONEINHEITEN;
    private int vorhandeneGlaseinheiten = MAX_GLASEINHEITEN;
    
    // Unterwegs (Pipeline)
    private int unterwegsHolz = 0;
    private int unterwegsSchrauben = 0;
    private int unterwegsFarbe = 0;
    private int unterwegsKarton = 0;
    private int unterwegsGlas = 0;

    // NEU: Konfigurierbare Schwellwerte (Default: 20%)
    private int minHolz = (int)(MAX_HOLZEINHEITEN * 0.2);
    private int minSchrauben = (int)(MAX_SCHRAUBEN * 0.2);
    private int minFarbe = (int)(MAX_FARBEINHEITEN * 0.2);
    private int minKarton = (int)(MAX_KARTONEINHEITEN * 0.2);
    private int minGlas = (int)(MAX_GLASEINHEITEN * 0.2);

    private int wartendeLKW = 0;
    private Lieferant lieferant;

    public Lager() {}

    public synchronized void setzeLieferant(Lieferant lieferant) {
        this.lieferant = lieferant;
    }
    
    // --- Konfiguration (Setter) ---
    public synchronized void setzeMinBestand(String typ, int wert) {
        switch(typ) {
            case "Holz": minHolz = wert; break;
            case "Schrauben": minSchrauben = wert; break;
            case "Farbe": minFarbe = wert; break;
            case "Karton": minKarton = wert; break;
            case "Glas": minGlas = wert; break;
        }
        // Nach Änderung sofort prüfen, ob wir nachbestellen müssen
        pruefeSicherheitsBestand();
    }

    // --- Smart Logistics (Auftragsbezogen) ---
    public synchronized void meldeBedarf(Bestellung b) {
        if (lieferant == null) return;
        
        int bHolz=0, bSchrauben=0, bFarbe=0, bKarton=0, bGlas=0;
        for (Produkt p : b.liefereBestellteProdukte()) {
            if (p instanceof Standardtuer) {
                bHolz += Standardtuer.gibHolzeinheiten();
                bSchrauben += Standardtuer.gibSchrauben();
                bFarbe += Standardtuer.gibFarbeinheiten();
                bKarton += Standardtuer.gibKartoneinheiten();
            } else if (p instanceof Premiumtuer) {
                bHolz += Premiumtuer.gibHolzeinheiten();
                bSchrauben += Premiumtuer.gibSchrauben();
                bFarbe += Premiumtuer.gibFarbeinheiten();
                bKarton += Premiumtuer.gibKartoneinheiten();
                bGlas += Premiumtuer.gibGlaseinheiten();
            }
        }
        
        // Wir prüfen: Haben wir genug für (Safety Stock + Bedarf)? 
        // Oder simpler: Wir sichern den Bedarf, safety stock macht die andere Methode.
        orderIfNeeded(bHolz, vorhandeneHolzeinheiten, unterwegsHolz, MAX_HOLZEINHEITEN, "Holz");
        orderIfNeeded(bSchrauben, vorhandeneSchrauben, unterwegsSchrauben, MAX_SCHRAUBEN, "Schrauben");
        orderIfNeeded(bFarbe, vorhandeneFarbeinheiten, unterwegsFarbe, MAX_FARBEINHEITEN, "Farbe");
        orderIfNeeded(bKarton, vorhandeneKartoneinheiten, unterwegsKarton, MAX_KARTONEINHEITEN, "Karton");
        orderIfNeeded(bGlas, vorhandeneGlaseinheiten, unterwegsGlas, MAX_GLASEINHEITEN, "Glas");
    }

    // --- Automatische Nachbestellung (Safety Stock Check) ---
    private void pruefeSicherheitsBestand() {
        if (lieferant == null) return;
        
        // Prüft für jedes Material: Ist (Bestand + Unterwegs) < MinBestand?
        checkSafety(minHolz, vorhandeneHolzeinheiten, unterwegsHolz, MAX_HOLZEINHEITEN, "Holz");
        checkSafety(minSchrauben, vorhandeneSchrauben, unterwegsSchrauben, MAX_SCHRAUBEN, "Schrauben");
        checkSafety(minFarbe, vorhandeneFarbeinheiten, unterwegsFarbe, MAX_FARBEINHEITEN, "Farbe");
        checkSafety(minKarton, vorhandeneKartoneinheiten, unterwegsKarton, MAX_KARTONEINHEITEN, "Karton");
        checkSafety(minGlas, vorhandeneGlaseinheiten, unterwegsGlas, MAX_GLASEINHEITEN, "Glas");
    }

    private void checkSafety(int min, int lagernd, int unterwegs, int max, String typ) {
        // Verfügbar für die Zukunft = Was da ist + was schon rollt
        int futureStock = lagernd + unterwegs;
        
        if (futureStock <= min) {
            // Auffüllen bis MAX (oder mindestens bis Min, aber Auffüllen ist logistisch sinnvoller)
            int bestellMenge = max - futureStock;
            if (bestellMenge > 0) {
                System.out.println("[Lager] Auto-Refill für " + typ + " (Unter Limit " + min + ")");
                dispatch(typ, bestellMenge);
                addUnterwegs(typ, bestellMenge);
            }
        }
    }

    private void orderIfNeeded(int bedarf, int lagernd, int unterwegs, int max, String typ) {
        int defizit = bedarf - (lagernd + unterwegs);
        while (defizit > 0) {
            int menge = Math.min(defizit, max);
            dispatch(typ, menge);
            defizit -= menge;
            addUnterwegs(typ, menge);
        }
    }
    
    private void dispatch(String typ, int m) {
        int h=0,s=0,f=0,k=0,g=0;
        switch(typ) {
            case "Holz": h=m; break;
            case "Schrauben": s=m; break;
            case "Farbe": f=m; break;
            case "Karton": k=m; break;
            case "Glas": g=m; break;
        }
        lieferant.wareBestellen(h,s,f,k,g);
    }
    
    private void addUnterwegs(String typ, int m) {
        switch(typ) {
            case "Holz": unterwegsHolz+=m; break;
            case "Schrauben": unterwegsSchrauben+=m; break;
            case "Farbe": unterwegsFarbe+=m; break;
            case "Karton": unterwegsKarton+=m; break;
            case "Glas": unterwegsGlas+=m; break;
        }
    }

    // --- Entnahme ---
    public synchronized void materialEntnehmen(Produkt p) {
        int h=0, s=0, f=0, k=0, g=0;
        if (p instanceof Standardtuer) {
            h=2; s=10; f=2; k=1;
        } else if (p instanceof Premiumtuer) {
            h=4; s=5; f=1; k=5; g=5;
        }

        while (!check(h, s, f, k, g)) {
            System.out.println("[Lager] Engpass! Warte...");
            // Notfall-Check, falls wir im Deadlock sind
            pruefeSicherheitsBestand();
            try { wait(); } catch (InterruptedException ie) { return; }
        }

        vorhandeneHolzeinheiten -= h;
        vorhandeneSchrauben -= s;
        vorhandeneFarbeinheiten -= f;
        vorhandeneKartoneinheiten -= k;
        vorhandeneGlaseinheiten -= g;
        
        notifyAll(); 
        
        // Nach jeder Entnahme prüfen, ob wir unter das Limit gefallen sind
        pruefeSicherheitsBestand();
    }

    private boolean check(int h, int s, int f, int k, int g) {
        return vorhandeneHolzeinheiten >= h && vorhandeneSchrauben >= s &&
               vorhandeneFarbeinheiten >= f && vorhandeneKartoneinheiten >= k &&
               vorhandeneGlaseinheiten >= g;
    }

    // --- Lieferung ---
    public synchronized void wareLiefern(int h, int s, int f, int k, int g) {
        boolean mussWarten = (vorhandeneHolzeinheiten + h > MAX_HOLZEINHEITEN ||
                              vorhandeneSchrauben + s > MAX_SCHRAUBEN ||
                              vorhandeneFarbeinheiten + f > MAX_FARBEINHEITEN ||
                              vorhandeneKartoneinheiten + k > MAX_KARTONEINHEITEN ||
                              vorhandeneGlaseinheiten + g > MAX_GLASEINHEITEN);
                              
        if (mussWarten) wartendeLKW++;

        while (vorhandeneHolzeinheiten + h > MAX_HOLZEINHEITEN ||
               vorhandeneSchrauben + s > MAX_SCHRAUBEN ||
               vorhandeneFarbeinheiten + f > MAX_FARBEINHEITEN ||
               vorhandeneKartoneinheiten + k > MAX_KARTONEINHEITEN ||
               vorhandeneGlaseinheiten + g > MAX_GLASEINHEITEN) {
            try { wait(); } catch (InterruptedException e) { return; }
        }

        if (mussWarten) wartendeLKW--;

        vorhandeneHolzeinheiten += h;
        vorhandeneSchrauben += s;
        vorhandeneFarbeinheiten += f;
        vorhandeneKartoneinheiten += k;
        vorhandeneGlaseinheiten += g;
        
        unterwegsHolz = Math.max(0, unterwegsHolz - h);
        unterwegsSchrauben = Math.max(0, unterwegsSchrauben - s);
        unterwegsFarbe = Math.max(0, unterwegsFarbe - f);
        unterwegsKarton = Math.max(0, unterwegsKarton - k);
        unterwegsGlas = Math.max(0, unterwegsGlas - g);
        
        System.out.println("[Lager] Ware abgeladen.");
        notifyAll(); 
    }

    // Getter
    public int gibWartendeLKW() { return wartendeLKW; } 
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
    
    // Getter für GUI-Init
    public int gibMinHolz() { return minHolz; }
    public int gibMinSchrauben() { return minSchrauben; }
    public int gibMinFarbe() { return minFarbe; }
    public int gibMinKarton() { return minKarton; }
    public int gibMinGlas() { return minGlas; }
}