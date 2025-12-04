import java.util.ArrayList;

/**
 * Das Lager verwaltet die Materialbestände.
 * * Update (Senior Dev):
 * Implementiert eine performante mathematische Berechnung (O(1)) statt 
 * Simulationsschleifen, um Engpässe und Lieferzeiten zu ermitteln.
 *
 * @author Gruppe 17 (Refactored)
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
    private int vorhandeneFarbeinheiten; // Korrigiertes Naming
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
     * Wird intern oder extern aufgerufen, um Lieferungen zu simulieren.
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
     * Prüft Materialbedarf, berechnet Nachfüllzyklen und aktualisiert Bestände.
     * * @param kundenBestellung Die zu verarbeitende Bestellung
     * @return Beschaffungszeit in Tagen (Zyklen * 2)
     */
    public int gibBeschaffungsZeit(Bestellung kundenBestellung) {
        
        // 1. Gesamtbedarf ermitteln
        // (Hier nutzen wir Arrays für eine elegantere Iteration, falls gewünscht,
        // aber der Lesbarkeit halber bleiben wir beim expliziten Summieren)
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
        // Wir berechnen für jedes Material: Wie oft muss ich nachfüllen?
        int zyklenHolz = berechneZyklen(vorhandeneHolzeinheiten, bedarfHolz, MAX_HOLZ);
        int zyklenSchrauben = berechneZyklen(vorhandeneSchrauben, bedarfSchrauben, MAX_SCHRAUBEN);
        int zyklenFarbe = berechneZyklen(vorhandeneFarbeinheiten, bedarfFarbe, MAX_FARBE);
        int zyklenKarton = berechneZyklen(vorhandeneKartoneinheiten, bedarfKarton, MAX_KARTON);
        int zyklenGlas = berechneZyklen(vorhandeneGlaseinheiten, bedarfGlas, MAX_GLAS);

        // Das Material mit den meisten nötigen Nachfüllungen bestimmt das Tempo (Bottleneck)
        int maxZyklen = Math.max(zyklenHolz, Math.max(zyklenSchrauben, 
                        Math.max(zyklenFarbe, Math.max(zyklenKarton, zyklenGlas))));

        // 3. Bestände aktualisieren
        // Neuer Bestand = (Alter Bestand + Nachgefüllt) - Verbrauch
        // Nachgefüllt = Anzahl der Zyklen * Kapazität pro Zyklus
        vorhandeneHolzeinheiten = berechneRestbestand(vorhandeneHolzeinheiten, bedarfHolz, MAX_HOLZ, maxZyklen);
        vorhandeneSchrauben = berechneRestbestand(vorhandeneSchrauben, bedarfSchrauben, MAX_SCHRAUBEN, maxZyklen);
        vorhandeneFarbeinheiten = berechneRestbestand(vorhandeneFarbeinheiten, bedarfFarbe, MAX_FARBE, maxZyklen);
        vorhandeneKartoneinheiten = berechneRestbestand(vorhandeneKartoneinheiten, bedarfKarton, MAX_KARTON, maxZyklen);
        vorhandeneGlaseinheiten = berechneRestbestand(vorhandeneGlaseinheiten, bedarfGlas, MAX_GLAS, maxZyklen);

        // Optional: Falls Zyklen nötig waren, rufen wir einmalig den Lieferanten auf, 
        // um die Konsolenausgabe zu erzeugen.
        if (maxZyklen > 0) {
            // Wir tun so, als ob wir alle fehlenden Materialien bestellen
            lieferant.wareBestellen(bedarfHolz, bedarfSchrauben, bedarfFarbe, bedarfKarton, bedarfGlas);
        }

        // 4. Rückgabe der Zeit (2 Tage pro Zyklus)
        return maxZyklen * 2;
    }

    /**
     * Berechnet mathematisch die nötigen Nachfüllzyklen.
     * Formel: Aufrunden((Bedarf - Ist) / Kapazität)
     */
    private int berechneZyklen(int bestand, int bedarf, int kapazitaet) {
        if (bestand >= bedarf) return 0;
        int fehlmenge = bedarf - bestand;
        // Trick für ceil-Division mit Integern: (a + b - 1) / b
        return (fehlmenge + kapazitaet - 1) / kapazitaet;
    }

    /**
     * Berechnet den neuen Lagerbestand nach Verarbeitung der Zyklen.
     */
    private int berechneRestbestand(int bestand, int bedarf, int kapazitaet, int zyklen) {
        // Die Gesamtmenge, die über die Zeit zur Verfügung stand:
        long verfuegbarGesamt = bestand + ((long) zyklen * kapazitaet);
        
        // Wenn wir mehr aufgefüllt haben (wegen Bottleneck bei anderem Material),
        // ist das Lager für dieses Material jetzt voller.
        // Wenn dies das Bottleneck-Material war, ist der Restbestand klein.
        return (int) (verfuegbarGesamt - bedarf);
    }

    // --- Getter für Tests ---
    public int gibHolzBestand() { return vorhandeneHolzeinheiten; }
    
    // Debug Ausgabe
    public void lagerBestandAusgeben() {
        System.out.println("Lagerbestand [H:" + vorhandeneHolzeinheiten + 
                           " S:" + vorhandeneSchrauben + 
                           " F:" + vorhandeneFarbeinheiten + 
                           " K:" + vorhandeneKartoneinheiten + 
                           " G:" + vorhandeneGlaseinheiten + "]");
    }
}