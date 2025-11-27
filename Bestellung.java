//Dieses Import-Statement ist notwendig, um die Klasse ArrayList zu verwenden.
//Die ArrayList ist eine "Sammlung", die es uns erlaubt, eine flexible Anzahl von Objekten zu speichern (Kapitel 4: Objektsammlungen) 
import java.util.ArrayList;

/**
 * Die Klasse Bestellung repräsentiert einen einzelnen Auftrag in der Fabrik.
 * Sie verwaltet eine Liste der zu produzierenden Produkte (Türen) und
 * speichert Daten wie Bestellnummer und Status.
 *
 * @author Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class Bestellung
{
    // Datenfelder definieren.
    // Wir nutzen 'private', um den direkten Zugriff von aussen zu verhindern.
    private ArrayList<Produkt> bestellteProdukte; //Speichert Produkte (Standard- & Premiumtüren).

    private boolean bestellBestaetigung; // Speichert, ob die Bestellung bestätigt ist.
    private int bestellungsNr;           // Eindeutige ID, von der Fabrik vergeben.
    private int beschaffungsZeit;        // Geschätzte Zeit, z.B. in Tagen.

    // Diese Felder speichern die Anzahl, die im Konstruktor übergeben wurde.
    private int anzahlStandardTueren;
    private int anzahlPremiumTueren;

    /**
     * Konstruktor für Objekte der Klasse Bestellung.
     * Dient der Initialisierung der Instanzvariabeln in einen gültigen Startzustand.
     * @param neueBestellNr Die Bestellnummer (von der Fabrik).
     * @param anzahlStandard Die Anzahl der zu erzeugenden Standardtüren.
     * @param anzahlPremium Die Anzahl der zu erzeugenden Premiumtüren.
     */
    public Bestellung(int bestellungsNr, int anzahlStandardTueren, int anzahlPremiumTueren) 
    {
        // Initialisiere Instanzvariablen

        this.bestellungsNr = bestellungsNr;
        this.beschaffungsZeit = -1;
        this.bestellteProdukte = new ArrayList<Produkt>();
        this.bestellBestaetigung = false;

        if (anzahlStandardTueren < 0 || anzahlPremiumTueren < 0) {
            throw new IllegalArgumentException("Ungültige Bestellmenge. Kann nicht negativ sein.");
            // System.out.println("Ungültige Bestellmenge. Kann nicht negativ sein.");
        } else if (anzahlStandardTueren == 0 && anzahlPremiumTueren == 0) {
            throw new IllegalArgumentException("Die Bestellung muss mindestens ein Produkt enthalten.");
            // System.out.println("Die Bestellung muss mindestens ein Produkt enthalten.");
        } else if (anzahlStandardTueren > 10_000 || anzahlPremiumTueren > 10_000) {
            throw new IllegalArgumentException("Die maximale Bestellmenge von 10.000 Stück wurde überschritten. Bitte kontaktieren Sie den Grosshandel für Sonderaufträge.");
            // System.out.println("Die maximale Bestellmenge von 10.000 Stück wurde überschritten. Bitte kontaktieren Sie den Grosshandel für Sonderaufträge.");
        } else {
            this.anzahlStandardTueren = anzahlStandardTueren;
            this.anzahlPremiumTueren = anzahlPremiumTueren;
            // Die eigentliche Erzeugung der Tür-Objekte lagern wir in eine private Hilfsmethode aus
            // Das hält den Konstruktor schlank und übersichtlich (Separation of Concerns)
            fuelleBestellteprodukte(anzahlStandardTueren, anzahlPremiumTueren);
        }

    }

    /**
     * Mit dieser Methode werden die entsprechenden Standardtueren und Premiumtueren erstellt und zur Liste der Bestellten Produkte hinzugefügt 
     * 
     * @param anzahlStandardTueren Anzahl bestellter Standardtüren
     * @param anzahlPremiumTueren Anzahl bestellter Premiumtüren
     */ 
    private void fuelleBestellteprodukte(int anzahlStandardTueren, int anzahlPremiumTueren) 
    {

        int standardTueren = 0;
        int premiumTueren = 0;

        while (standardTueren < anzahlStandardTueren) {
            // Erzeugt eine neue Instanz (Objekt) und fügt sie direkt der Liste hinzu.
            // Durch das 'new' in der Schleife ist jede Tür im Speicher ein eigenständiges Objekt.
            bestellteProdukte.add(new Standardtuer());
            standardTueren++;
        }

        while (premiumTueren < anzahlPremiumTueren) {
            bestellteProdukte.add(new Premiumtuer());
            premiumTueren++;
        }
    }
    // Verändernde Methoden: Mutatoren
    // Diese Methoden ändern den Zustand des Objekts.

    /**
     * Setzt den Status der Bestellung auf 'bestätigt'.
     * Dies ist eine verändernde Methode ohne "Parameter" und ohne
     * "Ergebniswert"
     */
    public void bestellungBestaetigen()
    {
        this.bestellBestaetigung = true;
    }

    /**
     * Setzt die geschätzte Beschaffungszeit für die Bestellung.
     * Enthält eine "Fehlerbehandlung" mittels "bedingter
     * Anweisung" (if-else), um ungültige Werte zu verhindern.
     *
     * @param zeit Die geschätzte Zeit (muss positiv sein).
     */
    public void setzeBeschaffungsZeit(int zeit)
    {
        if(zeit >= 0) {
            this.beschaffungsZeit = zeit;
        }
        else {
            // Feedback an den Benutzer bei ungültiger Eingabe.
            System.out.println("Fehler: Die Beschaffungszeit darf nicht negativ sein.");
        }
    }

    // Sondierende Methoden: Accessoren
    // Diese Methoden liefern Informationen über den Zustand des Objekts zurück.

    /**
     * Gibt zurück, ob die Bestellung bereits bestätigt wurde.
     * @return true, wenn bestätigt, sonst false.
     */
    public boolean gibBestellBestaetigung()
    {
        return this.bestellBestaetigung;
    }

    /**
     * Gibt die geschätzte Beschaffungszeit zurück.
     * @return Die Zeit.
     */
    public int gibBeschaffungsZeit()
    {
        return this.beschaffungsZeit;
    }

    /**
     * Gibt die eindeutige Bestellnummer zurück.
     * @return Die Bestellnummer.
     */
    public int gibBestellungsNr()
    {
        return this.bestellungsNr;
    }

    /**
     * Gibt die Anzahl der Standardtüren zurück.
     * @return Anzahl der Standardtüren.
     */
    public int gibAnzahlStandardTueren()
    {
        return this.anzahlStandardTueren;
    }

    /**
     * Gibt die Anzahl der Premiumtüren zurück.
     * @return Anzahl der Premiumtüren.
     */
    public int gibAnzahlPremiumTueren()
    {
        return this.anzahlPremiumTueren;
    }
    
    /**
     * Zusätzliche Hilfsmethode
     * Gibt die gesamte Liste der Produkte zurück, falls die Fabrik
     * den Zustand aller Produkte ändern muss.
     * @return Die Liste aller Produkte in dieser Bestellung.
     */
    public ArrayList<Produkt> gibAlleProdukte()
    {
        return this.bestellteProdukte;
    }
}