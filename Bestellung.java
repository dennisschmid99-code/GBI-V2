import java.util.ArrayList;

/**
 * Klasse Bestellung beinhaltet die bestellten Produkte
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Bestellung {
    // Die Liste bestellteProdukte enthält alle Produkte, welche bestellt worden sind
    private ArrayList<Produkt> bestellteProdukte;
    // Die bestellBestaetigung gibt an, ob eine Bestellung bestätigt wurde oder nicht
    private boolean bestellBestaetigung;
    // Die Beschaffungszeit gibt an, wie lange die Lieferzeit (in Tage) ist
    private int beschaffungsZeit;
    // Jede Bestellung erhält eine Bestellnummer
    private int bestellungsNr;
    // Anzahl der bestellten Standardtueren
    private int anzahlStandardTueren;
    // Anzahl der bestellten Premiumtueren
    private int anzahlPremiumTueren;

    // Die lieferZeit gibt an, wie lange es dauert, bis die Ware rausgeschickt wird.
    private float lieferZeit;
    
    // WICHTIG FÜR AUFGABE 3: Status, ob Produktion abgeschlossen ist
    private boolean alleProdukteProduziert;

    /**
     * Konstruktor für Klasse Bestellung.
     * Hier werden alle globalen Variablen initialisiert
     * * @param anzahlStandardTueren Anzahl bestellter Standardtüren
     * @param anzahlPremiumTueren  Anzahl bestellter Premiumtüren
     * @param bestellungsNr  Zugeordnete Bestellnummer
     */
    public Bestellung(int anzahlStandardTueren, int anzahlPremiumTueren, int bestellungsNr) {

        if (anzahlStandardTueren < 0 || anzahlPremiumTueren < 0) {
            throw new IllegalArgumentException("Ungültige Bestellmenge. Kann nicht negativ sein.");
        } else if (anzahlStandardTueren == 0 && anzahlPremiumTueren == 0) {
            throw new IllegalArgumentException("Die Bestellung muss mindestens ein Produkt enthalten.");
        } else if (anzahlStandardTueren > 10_000 || anzahlPremiumTueren > 10_000) {
            throw new IllegalArgumentException("Bestellmenge ist zu gross. Maximal 10 Tausend pro Artikel.");
        } else {
            this.bestellungsNr = bestellungsNr;
            beschaffungsZeit = -1;
            lieferZeit = -1;
            bestellteProdukte = new ArrayList<Produkt>();
            bestellBestaetigung = false;
            alleProdukteProduziert = false;
            
            this.anzahlStandardTueren = anzahlStandardTueren;
            this.anzahlPremiumTueren = anzahlPremiumTueren;
            fuelleBestellteprodukte(anzahlStandardTueren, anzahlPremiumTueren);
        }
    }
    
    /**
     * Konstruktor nur mit Bestellnummer (wird von Fabrik in Aufgabe 3 genutzt)
     */
    public Bestellung(int bestellungsNr) {
        this.bestellungsNr = bestellungsNr;
        this.bestellteProdukte = new ArrayList<Produkt>();
        this.alleProdukteProduziert = false;
    }

    /**
     * Fügt ein Produkt zur Bestellung hinzu (Wichtig für Aufgabe 3).
     */
    public void fuegeProduktHinzu(Produkt p) {
        bestellteProdukte.add(p);
    }

    /**
     * Mit dieser Methode werden die entsprechenden Standardtüren und Premiumtüren
     * erstellt und zur Liste der bestellten Produkte hinzugefügt.
     */
    private void fuelleBestellteprodukte(int anzahlStandardTueren, int anzahlPremiumTueren) {
        for(int i=0; i<anzahlStandardTueren; i++) {
            bestellteProdukte.add(new Standardtuer());
        }
        for(int i=0; i<anzahlPremiumTueren; i++) {
            bestellteProdukte.add(new Premiumtuer());
        }
    }

    /**
     * Mit dieser Methode wird der Zustand einer Bestellung abgefragt
     * @return bestellBestaetigung Zustand der Bestellbestätigung
     */
    public boolean gibBestellBestaetigung() {
        return bestellBestaetigung; 
    }

    public int gibBeschaffungsZeit() {
        return beschaffungsZeit;
    }

    public void setzeBeschaffungsZeit(int beschaffungsZeit) {
        this.beschaffungsZeit = beschaffungsZeit;
    }

    public int gibBestellungsNr() {
        return bestellungsNr;
    }

    public int gibAnzahlStandardTueren() {
        return anzahlStandardTueren;
    }

    public int gibAnzahlPremiumTueren() {
        return anzahlPremiumTueren;
    }

    public void bestellungBestaetigen() {
        bestellBestaetigung = true;
    }

    /**
     * Gibt die Liste der Produkte zurück.
     * Wurde umbenannt zu 'liefereBestellteProdukte' passend zum UML und Aufruf im Lager/Manager.
     */
    public ArrayList<Produkt> liefereBestellteProdukte() {
        return bestellteProdukte;
    }
    
    // Alte Methode zur Kompatibilität (kann bleiben oder weg)
    public ArrayList<Produkt> gibBestellteProdukte() {
        return bestellteProdukte;
    }

    public void setzeLieferzeit(float lieferZeit) {
        this.lieferZeit = lieferZeit;
    }

    public float gibLieferzeit() {
        return lieferZeit;
    }
    
    // Neu für Aufgabe 3:
    public void setzeAlleProdukteProduziert() {
        this.alleProdukteProduziert = true;
    }
    
    public boolean istAbgeschlossen() {
        return alleProdukteProduziert;
    }
}