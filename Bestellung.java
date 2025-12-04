import java.util.ArrayList;

/**
 * Klasse Bestellung beinhaltet die bestellten Produkte.
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class Bestellung {

    // Die Liste der bestellten Produkte
    private ArrayList<Produkt> bestellteProdukte;

    // Bestellbestätigung (true/false)
    private boolean bestellBestaetigung;

    // Beschaffungszeit in Tagen (0 oder 2) – Initialwert laut Aufgabenstellung = -1
    private int beschaffungsZeit;

    // Bestellnummer
    private int bestellungsNr;

    // Anzahl bestellter Türen
    private int anzahlStandardTueren;
    private int anzahlPremiumTueren;

    // Gesamtlieferzeit (Produktionszeit + Beschaffungszeit + Standardlieferzeit)
    private float lieferZeit;

    /**
     * Konstruktor für Klasse Bestellung.
     *
     * @param anzahlStandardTueren Anzahl bestellter Standardtüren
     * @param anzahlPremiumTueren  Anzahl bestellter Premiumtüren
     * @param bestellungsNr        Zugeordnete Bestellnummer
     */
    public Bestellung(int anzahlStandardTueren, int anzahlPremiumTueren, int bestellungsNr) {

        // Fehlerbehandlung
        if (anzahlStandardTueren < 0 || anzahlPremiumTueren < 0) {
            throw new IllegalArgumentException("Ungültige Bestellmenge. Kann nicht negativ sein.");
        }
        if (anzahlStandardTueren == 0 && anzahlPremiumTueren == 0) {
            throw new IllegalArgumentException("Die Bestellung muss mindestens ein Produkt enthalten.");
        }
        if (anzahlStandardTueren > 10_000 || anzahlPremiumTueren > 10_000) {
            throw new IllegalArgumentException("Bestellmenge ist zu gross. Maximal 10 Tausend pro Artikel.");
        }

        this.bestellungsNr = bestellungsNr;
        this.anzahlStandardTueren = anzahlStandardTueren;
        this.anzahlPremiumTueren = anzahlPremiumTueren;

        // Laut Aufgabe 2: Initialwert = -1 (wird später von Fabrik gesetzt)
        this.beschaffungsZeit = -1;

        // Lieferzeit wird später gesetzt
        this.lieferZeit = 0.0f;

        this.bestellBestaetigung = false;

        bestellteProdukte = new ArrayList<>();
        fuelleBestellteProdukte(anzahlStandardTueren, anzahlPremiumTueren);
    }

    /**
     * Füllt die Liste der bestellten Produkte mit Standard- und Premiumtüren.
     */
    private void fuelleBestellteProdukte(int anzahlStandardTueren, int anzahlPremiumTueren) {

        for (int i = 0; i < anzahlStandardTueren; i++) {
            bestellteProdukte.add(new Standardtuer());
        }

        for (int i = 0; i < anzahlPremiumTueren; i++) {
            bestellteProdukte.add(new Premiumtuer());
        }
    }

    /**
     * Gibt zurück, ob die Bestellung bestätigt wurde.
     *
     * @return bestellBestaetigung
     */
    public boolean gibBestellBestaetigung() {
        return bestellBestaetigung;
    }

    /**
     * Gibt die Liste aller bestellten Produkte zurück.
     *
     * @return bestellteProdukte
     */
    public ArrayList<Produkt> liefereBestellteProdukte() {
        return bestellteProdukte;
    }

    /**
     * Gibt die Beschaffungszeit zurück.
     *
     * @return beschaffungsZeit
     */
    public int gibBeschaffungsZeit() {
        return beschaffungsZeit;
    }

    /**
     * Setzt die Beschaffungszeit für die Bestellung.
     *
     * @param beschaffungsZeit
     */
    public void setzeBeschaffungsZeit(int beschaffungsZeit) {
        this.beschaffungsZeit = beschaffungsZeit;
    }

    /**
     * Gibt die gesamte Lieferzeit zurück.
     *
     * @return lieferZeit
     */
    public float gibLieferzeit() {
        return lieferZeit;
    }

    /**
     * Setzt die gesamte Lieferzeit.
     *
     * @param lieferZeit
     */
    public void setzeLieferzeit(float lieferZeit) {
        this.lieferZeit = lieferZeit;
    }

    /**
     * Gibt die Bestellnummer zurück.
     *
     * @return bestellungsNr
     */
    public int gibBestellungsNr() {
        return bestellungsNr;
    }

    /**
     * Gibt die Anzahl der Standardtüren zurück.
     *
     * @return anzahlStandardTueren
     */
    public int gibAnzahlStandardTueren() {
        return anzahlStandardTueren;
    }

    /**
     * Gibt die Anzahl der Premiumtüren zurück.
     *
     * @return anzahlPremiumTueren
     */
    public int gibAnzahlPremiumTueren() {
        return anzahlPremiumTueren;
    }

    /**
     * Bestätigt die Bestellung.
     */
    public void bestellungBestaetigen() {
        bestellBestaetigung = true;
    }
}
