import java.util.ArrayList;

/**
 * Das Lager verwaltet die Materialbestände.
 * Implementiert eine performante mathematische Berechnung (O(1)) statt 
 * Simulationsschleifen, um Engpässe (Bottlenecks) und Lieferzeiten zu ermitteln.
 *
 * @author Gruppe 17 (Refactored by Senior Dev)
 * @version 04.12.2025
 */
public class Lager {

    // Konstanten für maximale Lagerkapazität
    private static final int MAX_HOLZ = 200;
    private static final int MAX_SCHRAUBEN = 1000;
    private static final int MAX_FARBE = 100;
    private static final int MAX_KARTON = 100;
    private static final int MAX_GLAS = 50;

    // Aktuelle Bestände
    private int vorhandeneHolzeinheiten;
    private int vorhandeneSchrauben;
    private int vorhandeneFarbeinheiten;
    private int vorhandeneKartoneinheiten;
    private int vorhandeneGlaseinheiten;

    private Lieferant lieferant;

    /**
     * Konstruktor: Lager wird initial gefüllt.
     */
    public Lager() {
        lieferant = new Lieferant();
        lagerAuffuellen();
    }

    /**
     * Setzt das Lager auf die Maximalwerte zurück.
     */
    public void lagerAuffuellen() {
        lieferant.wareBestellen(MAX_HOLZ, MAX_SCHRAUBEN, MAX_FARBE, MAX_KARTON, MAX_GLAS);
        vorhandeneHolzeinheiten = MAX_HOLZ;
        vorhandeneSchrauben = MAX_SCHRAUBEN;
        vorhandeneFarbeinheiten = MAX_FARBE;
        vorhandeneKartoneinheiten = MAX_KARTON;
        vorhandeneGlaseinheiten = MAX_GLAS;
    }

    /**
     * Prüft Materialbedarf, berechnet Nachfüllzyklen basierend auf dem Engpass-Material
     * und aktualisiert Bestände.
     * @param kundenBestellung Die zu verarbeitende Bestellung
     * @return Beschaffungszeit in Tagen (Zyklen * 2)
     */
    public int gibBeschaffungsZeit(Bestellung kundenBestellung) {
        
        // 1. Gesamtbedarf ermitteln
        int bedarfHolz = 0;
        int bedarfSchrauben = 0;
        int bedarfFarbe = 0;
        int bedarfKarton = 0;
        int bedarfGlas = 0;

        for (Produkt p : kundenBestellung.liefereBestellteProdukte()) {
            if (p instanceof Standardtuer) {
                bedarfHolz += Standardtuer.gibHolzeinheiten();
                bedarfSchrauben += Standardtuer.gibSchrauben();
                bedarfFarbe += Standardtuer.gibFarbeinheiten();
                bedarfKarton += Standardtuer.gibKartoneinheiten();
            } else if (p instanceof Premiumtuer) {
                bedarfHolz += Premiumtuer.gibHolzeinheiten();
                bedarfSchrauben += Premiumtuer.gibSchrauben();
                bedarfFarbe += Premiumtuer.gibFarbeinheiten();
                bedarfKarton += Premiumtuer.gibKartoneinheiten();
                bedarfGlas += Premiumtuer.gibGlaseinheiten();
            }
        }

        // 2. Engpass-Berechnung (Mathematisch)
        int zyklenHolz = berechneZyklen(vorhandeneHolzeinheiten, bedarfHolz, MAX_HOLZ);
        int zyklenSchrauben = berechneZyklen(vorhandeneSchrauben, bedarfSchrauben, MAX_SCHRAUBEN);
        int zyklenFarbe = berechneZyklen(vorhandeneFarbeinheiten, bedarfFarbe, MAX_FARBE);
        int zyklenKarton = berechneZyklen(vorhandeneKartoneinheiten, bedarfKarton, MAX_KARTON);
        int zyklenGlas = berechneZyklen(vorhandeneGlaseinheiten, bedarfGlas, MAX_GLAS);

        // Das Material mit den meisten Zyklen ist der Flaschenhals (Bottleneck).
        int maxZyklen = Math.max(zyklenHolz, Math.max(zyklenSchrauben, 
                        Math.max(zyklenFarbe, Math.max(zyklenKarton, zyklenGlas))));

        // 3. Bestände aktualisieren
        vorhandeneHolzeinheiten = berechneRestbestand(vorhandeneHolzeinheiten, bedarfHolz, MAX_HOLZ, maxZyklen);
        vorhandeneSchrauben = berechneRestbestand(vorhandeneSchrauben, bedarfSchrauben, MAX_SCHRAUBEN, maxZyklen);
        vorhandeneFarbeinheiten = berechneRestbestand(vorhandeneFarbeinheiten, bedarfFarbe, MAX_FARBE, maxZyklen);
        vorhandeneKartoneinheiten = berechneRestbestand(vorhandeneKartoneinheiten, bedarfKarton, MAX_KARTON, maxZyklen);
        vorhandeneGlaseinheiten = berechneRestbestand(vorhandeneGlaseinheiten, bedarfGlas, MAX_GLAS, maxZyklen);

        // Konsolenausgabe simulieren
        if (maxZyklen > 0) {
            lieferant.wareBestellen(bedarfHolz, bedarfSchrauben, bedarfFarbe, bedarfKarton, bedarfGlas);
        }

        // 4. Rückgabe der Zeit (2 Tage pro Zyklus)
        return maxZyklen * 2;
    }

    /**
     * Berechnet mathematisch die nötigen Nachfüllzyklen.
     */
    private int berechneZyklen(int bestand, int bedarf, int kapazitaet) {
        if (bestand >= bedarf) return 0;
        int fehlmenge = bedarf - bestand;
        // Integer-Arithmetic für Ceil: (a + b - 1) / b
        return (fehlmenge + kapazitaet - 1) / kapazitaet;
    }

    /**
     * Berechnet den neuen Lagerbestand nach Verarbeitung der Zyklen.
     */
    private int berechneRestbestand(int bestand, int bedarf, int kapazitaet, int zyklen) {
        long verfuegbarGesamt = bestand + ((long) zyklen * kapazitaet);
        return (int) (verfuegbarGesamt - bedarf);
    }

    // --- Getter für Tests ---
    public int gibHolzBestand() { return vorhandeneHolzeinheiten; }
    
    public void lagerBestandAusgeben() {
        System.out.println("Lagerbestand [H:" + vorhandeneHolzeinheiten + 
                           " S:" + vorhandeneSchrauben + 
                           " F:" + vorhandeneFarbeinheiten + 
                           " K:" + vorhandeneKartoneinheiten + 
                           " G:" + vorhandeneGlaseinheiten + "]");
    }
}