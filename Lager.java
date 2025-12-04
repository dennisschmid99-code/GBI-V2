import java.util.ArrayList;

/**
 * Das Lager verwaltet die verfügbaren Materialbestände und prüft,
 * ob eine Bestellung vollständig produziert werden kann.
 * 
 * Wenn Material fehlt, wird über den Lieferanten nachbestellt.
 * In diesem Fall beträgt die Beschaffungszeit 2 Tage.
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class Lager {

    // Maximale Bestände gemäss UML
    private static final int MAXHOLZEINHEITEN = 200;
    private static final int MAXSCHRAUBEN = 1000;
    private static final int MAXFARBEEINHEITEN = 100;
    private static final int MAXKARTONEINHEITEN = 100;
    private static final int MAXGLASEINHEITEN = 50;

    // Aktuelle Bestände
    private int vorhandeneHolzeinheiten;
    private int vorhandeneSchrauben;
    private int vorhandeneFarbeeinheiten;
    private int vorhandeneKartoneinheiten;
    private int vorhandeneGlaseinheiten;

    private Lieferant lieferant;

    /**
     * Konstruktor: Lager wird initial komplett gefüllt.
     */
    public Lager() {
        lieferant = new Lieferant();
        lagerAuffuellen();
    }

    /**
     * Füllt das Lager vollständig auf die Maximalwerte auf.
     * Dazu wird eine Bestellung beim Lieferanten ausgelöst.
     */
    public void lagerAuffuellen() {
        // Lieferant liefert immer erfolgreich laut UML
        lieferant.wareBestellen(
            MAXHOLZEINHEITEN,
            MAXSCHRAUBEN,
            MAXFARBEEINHEITEN,
            MAXKARTONEINHEITEN,
            MAXGLASEINHEITEN
        );

        vorhandeneHolzeinheiten = MAXHOLZEINHEITEN;
        vorhandeneSchrauben = MAXSCHRAUBEN;
        vorhandeneFarbeeinheiten = MAXFARBEEINHEITEN;
        vorhandeneKartoneinheiten = MAXKARTONEINHEITEN;
        vorhandeneGlaseinheiten = MAXGLASEINHEITEN;
    }

    /**
     * Prüft den Materialbedarf und bestimmt die Beschaffungszeit.
     * Clean Code Ansatz: Trennt Lagerentnahme von Nachbestellung.
     * * @param kundenBestellung Die Bestellung
     * @return 0 (alles da) oder 2 (Nachbestellung nötig)
     */
    public int gibBeschaffungsZeit(Bestellung kundenBestellung) {

        int benoetigtHolz = 0;
        int benoetigtSchrauben = 0;
        int benoetigtFarbe = 0;
        int benoetigtKarton = 0;
        int benoetigtGlas = 0;

        // 1. Bedarf ermitteln
        for (Produkt p : kundenBestellung.liefereBestellteProdukte()) {
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

        // 2. Prüfen: Reicht der Bestand?
        // WICHTIG: Variable heisst vorhandeneFarbeeinheiten (doppeltes e) laut Lager.java
        boolean bestandReicht = 
            vorhandeneHolzeinheiten >= benoetigtHolz &&
            vorhandeneSchrauben >= benoetigtSchrauben &&
            vorhandeneFarbeeinheiten >= benoetigtFarbe &&
            vorhandeneKartoneinheiten >= benoetigtKarton &&
            vorhandeneGlaseinheiten >= benoetigtGlas;

        if (bestandReicht) {
            // Fall A: Alles da -> Einfach abziehen
            vorhandeneHolzeinheiten -= benoetigtHolz;
            vorhandeneSchrauben -= benoetigtSchrauben;
            vorhandeneFarbeeinheiten -= benoetigtFarbe;
            vorhandeneKartoneinheiten -= benoetigtKarton;
            vorhandeneGlaseinheiten -= benoetigtGlas;
            
            return 0; // Keine Verzögerung
        } else {
            // Fall B: Nicht genug da -> Nachbestellung
            // Strategie: Lager auffüllen, dann bedienen.
            
            lagerAuffuellen(); 
            
            // Abziehen mit Math.max(0, ...), falls Bestellung > Lagerkapazität
            vorhandeneHolzeinheiten = Math.max(0, vorhandeneHolzeinheiten - benoetigtHolz);
            vorhandeneSchrauben = Math.max(0, vorhandeneSchrauben - benoetigtSchrauben);
            vorhandeneFarbeeinheiten = Math.max(0, vorhandeneFarbeeinheiten - benoetigtFarbe);
            vorhandeneKartoneinheiten = Math.max(0, vorhandeneKartoneinheiten - benoetigtKarton);
            vorhandeneGlaseinheiten = Math.max(0, vorhandeneGlaseinheiten - benoetigtGlas);
            
            return 2; // 2 Tage Verzögerung
        }
    }

    /**
     * Gibt die aktuellen Lagerbestände auf der Konsole aus.
     */
    public void lagerBestandAusgeben() {
        System.out.println(
            "Holz: " + vorhandeneHolzeinheiten +
            ", Schrauben: " + vorhandeneSchrauben +
            ", Farbe: " + vorhandeneFarbeeinheiten +
            ", Karton: " + vorhandeneKartoneinheiten +
            ", Glas: " + vorhandeneGlaseinheiten
        );
    }

    /**
     * Getter für den Holzbestand.
     * Wird für Unit Tests benötigt.
     *
     * @return aktueller Holzbestand
     */
    public int gibHolzBestand() {
        return vorhandeneHolzeinheiten;
    }
}
