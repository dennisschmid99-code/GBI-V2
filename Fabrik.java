import java.util.ArrayList;

/**
 * Klasse Fabrik beinhaltet die Methoden der Fabrik
 *
 * @author Gruppe 17
 * @version 01.12.2025
 */
public class Fabrik 
{
    // Die Liste bestellteProdukte enthält alle Produkte, welche  bestellt worden sind
    private ArrayList<Bestellung> bestellungen;
    private int bestellungsNr;

    //NEW
    private Lager lager;

    private static final int STANDARD_LIEFERZEIT = 1;
    private static final float MINUTEN_PRO_TAG = 1440.0f;
    //
    
    /**
     * Konstruktor der Klasse
     * Hier werden alle globale Variablen initialisiert
     */
    public Fabrik() 
    {
        bestellungen = new ArrayList<Bestellung>();
        bestellungsNr = 0;
        
        //Lager benötigt einen Lieferanten, der Material liefern kann
        Lieferant lieferant = new Lieferant();
        lager = new Lager(lieferant);// Lager wird mit einem Lieferanten erstellt
        
    }

    /**
     * Mit dieser Methode wird eine Bestellung aufgegeben 
     * 
     * @param standardTueren Anzahl zu bestellender Standardtüren
     * @param premiumTueren Anzahl zu bestellender Premiumtüren
     */
    public void bestellungAufgeben(int standardTueren, int premiumTueren) 
    {
        // Fehlerbehandlung
        if (standardTueren < 0 || premiumTueren < 0) { // Sobald einer der Werte negativ ist
            System.out.println("\nUngültige Bestellmenge. Kann nicht negativ sein.");
        } else if (standardTueren == 0 && premiumTueren == 0) { // Wenn beide Werte Null sind
            System.out.println("\nDie Bestellung muss mindestens ein Produkt enthalten.");
        } else if (standardTueren > 10_000 || premiumTueren > 10_000) { // Sobald einer der Werte mehr als 10k ist
            System.out.println("\nBestellmenge ist zu gross. Maximal 10 Tausend pro Artikel.");
        }
        
        
        bestellungsNr++;// Schritt 1: Neue Bestellnummer generieren
        Bestellung neueBestellung = new Bestellung(standardTueren, premiumTueren, bestellungsNr);

        // Lager berechnet, ob Material sofort verfügbar ist oder nachbestellt werden muss
        int beschaffungsZeit = lager.gibBeschaffungsZeit(neueBestellung);
        neueBestellung.setzeBeschaffungsZeit(beschaffungsZeit);

        // Produktionszeit berechnen (in Minuten)
        int produktionsZeitMin =
                standardTueren * Standardtuer.gibProduktionszeit()
                        + premiumTueren * Premiumtuer.gibProduktionszeit();

        // Produktionszeit in Tagen umrechnen
        float produktionsZeitTage = produktionsZeitMin / MINUTEN_PRO_TAG;

        // Gesamtlieferzeit wird bestimmt
        float lieferZeit = produktionsZeitTage + beschaffungsZeit + STANDARD_LIEFERZEIT;
        neueBestellung.setzeLieferzeit(lieferZeit);

        neueBestellung.bestellungBestaetigen(); //Bestellung offiziell bestätigen

        bestellungen.add(neueBestellung); //Bestellung der Liste hinzufügen

    }   

    /**
     * Mit dieser Methode werden alle Bestellungen ausgegeben 
     * 
     */
    public void bestellungenAusgeben() 
    {
        System.out.println("\nIn der Fabrik gibt es gerade folgende Bestellungen.");
        for (Bestellung bestellung : bestellungen) {
            System.out.println("Bestellung Nummer " + bestellung.gibBestellungsNr()
                    + " Standardtüren: " + bestellung.gibAnzahlStandardTueren()
                    + " Premiumtüren: " + bestellung.gibAnzahlPremiumTueren()
                    + " Beschaffungszeit: " + bestellung.gibBeschaffungsZeit()
                    + " Lieferzeit: " + bestellung.gibLieferzeit()
                    + " Bestellbestätigung: " + bestellung.gibBestellBestaetigung());
        }
    }
    
    /**
     * Mit dieser Methode wird die Arrayliste mit den Bestellungen zurückgegeben.
     * Wird für die Unit Testklasse FabrikTest verwendet
     * 
     * @return bestellteProdukte wird retourniert
     */
    public ArrayList<Bestellung> gibBestellungen()
    {
        return bestellungen;
    }
    
    /**
     * Füllt das Lager wieder vollständig auf.
     * Diese Methode ruft intern lagerAuffuellen() auf.
     */
    public void lagerAuffuellen() {
        lager.lagerAuffuellen();
    }
}