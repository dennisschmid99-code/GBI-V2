import java.util.ArrayList;

/**
 * Klasse Bestellung beinhaltet die bestellten Produkte
 *
 * @author Alex Marchese
 * @version 26.11.2025
 */
public class Bestellung {
    // Die Liste bestellteProdukte enthält alle Produkte, welche bestellt worden
    // sind
    private ArrayList<Produkt> bestellteProdukte;
    // Die bestellBestaetigung gibt an, ob eine Bestellung bestätigt wurde oder
    // nicht (boolean)
    private boolean bestellBestaetigung;
    // Die Beschaffungszeit gibt an, wie lange die Lieferzeit (in Tage) für die
    // Produkte ist
    // -1 ist der Initialisierungswert
    private int beschaffungsZeit;
    // Jede Bestellung erhält eine Bestellnummer
    private int bestellungsNr;
    // Anzahl der bestellten Standardtueren
    private int anzahlStandardTueren;
    // Anzahl der bestellten Premiumtueren
    private int anzahlPremiumTueren;

    /**
     * Konstruktor für Klasse Bestellung.
     * Hier werden alle globalen Variablen initialisiert
     * 
     * @param standardTueren Anzahl bestellter Standardtüren
     * @param premiumTueren  Anzahl bestellter Premiumtüren
     * @param bestellungsNr  Zugeordnete Bestellnummer
     */
    public Bestellung(int anzahlStandardTueren, int anzahlPremiumTueren, int bestellungsNr) {
        // initialise instance variables

        this.bestellungsNr = bestellungsNr;
        beschaffungsZeit = -1;
        bestellteProdukte = new ArrayList<Produkt>();
        bestellBestaetigung = false; // this is optional

        if (anzahlStandardTueren < 0 || anzahlPremiumTueren < 0) {
            throw new IllegalArgumentException("Ungültige Bestellmenge. Kann nicht negativ sein.");
            // System.out.println("Ungültige Bestellmenge. Kann nicht negativ sein.");
        } else if (anzahlStandardTueren == 0 && anzahlPremiumTueren == 0) {
            throw new IllegalArgumentException("Die Bestellung muss mindestens ein Produkt enthalten.");
            // System.out.println("Die Bestellung muss mindestens ein Produkt enthalten.");
        } else if (anzahlStandardTueren > 10_000 || anzahlPremiumTueren > 10_000) {
            throw new IllegalArgumentException("Bestellmenge ist zu gross. Maximal 10 Tausend pro Artikel.");
            // System.out.println("Bestellmenge ist zu gross. Maximal 10 Tausend pro
            // Artikel.");
        } else {
            this.anzahlStandardTueren = anzahlStandardTueren;
            this.anzahlPremiumTueren = anzahlPremiumTueren;
            fuelleBestellteprodukte(anzahlStandardTueren, anzahlPremiumTueren);
        }

    }

    /**
     * Mit dieser Methode werden die entsprechenden Standardtüren und Premiumtüren
     * erstellt und zur Liste der Bestellten Produkte hinzugefügt
     * 
     * @param anzahlStandardTueren Anzahl bestellter Standardtüren
     * @param anzahlPremiumTueren  Anzahl bestellter Premiumtüren
     */
    private void fuelleBestellteprodukte(int anzahlStandardTueren, int anzahlPremiumTueren) {

        int standardTueren = 0;
        int premiumTueren = 0;

        while (standardTueren < anzahlStandardTueren) {
            bestellteProdukte.add(new Standardtuer());
            standardTueren++; // also here 3 options all fine
        }

        while (premiumTueren < anzahlPremiumTueren) {
            bestellteProdukte.add(new Premiumtuer());
            premiumTueren++; // also here 3 options all fine
        }

        // For Schleife funktioniert natürlich auch
        // for (int i = 0; i < anzahlStandardTueren; i++) {
        // bestellteProdukte.add(new Standardtuer());
        // }

        // for (int i = 0; i < anzahlPremiumTueren; i++) {
        // bestellteProdukte.add(new Premiumtuer());
        // }
    }

    /**
     * Mit dieser Methode wird der Zustand einer Bestellung abgefragt
     * 
     * @return bestellBestaetigung Zustand der Bestellbestätigung
     */
    public boolean gibBestellBestaetigung() {
        return bestellBestaetigung; // es geht auch ohne this.
    }

    /**
     * Mit dieser Methode wird die Beschaffungszeit für die Bestellung ausgegeben
     * 
     * @return beschaffungsZeit
     */
    public int gibBeschaffungsZeit() {
        return beschaffungsZeit;
    }

    /**
     * Mit dieser Methode wird die Beschaffungszeit für die Bestellung gesetzt
     * 
     *
     * @param beschaffungszeit wird übergeben
     */
    public void setzeBeschaffungsZeit(int beschaffungsZeit) {
        this.beschaffungsZeit = beschaffungsZeit;
    }

    /**
     * Mit dieser Methode wird die Bestellnummer für die Bestellung ausgegeben
     * 
     * 
     * @param bestellungsNr wird retourniert
     */
    public int gibBestellungsNr() {
        return bestellungsNr;
    }

    /**
     * Mit dieser Methode wird die Anzahl der Standardtueren für die Bestellung
     * ausgegeben
     * 
     * 
     * @return anzahlStandardTueren wird retourniert
     */
    public int gibAnzahlStandardTueren() {
        return anzahlStandardTueren;
    }

    /**
     * Mit dieser Methode wird die Anzahl der Premiumtueren für die Bestellung
     * ausgegeben
     * 
     * 
     * @return anzahlPremiumTueren wird retourniert
     */
    public int gibAnzahlPremiumTueren() {
        return anzahlPremiumTueren;
    }

    /**
     * Mit dieser Methode wird eine Bestellung bestätigt
     * 
     */
    public void bestellungBestaetigen() {
        bestellBestaetigung = true;
    }

    /**
     * Mit dieser Methode wird die Arrayliste mit den bestellten Produkten
     * zurückgegeben.
     * Wird für die Unit Testklasse FabrikTest verwendet
     * 
     * @return bestellteProdukte wird retourniert
     */
    public ArrayList<Produkt> gibBestellteProdukte() {
        return bestellteProdukte;
    }
}
