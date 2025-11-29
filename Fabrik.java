import java.util.ArrayList;

/**
 * Klasse Fabrik beinhaltet die Methoden der Fabrik
 *
 * @author Alex Marchese
 * @version 26.11.2025
 */
public class Fabrik 
{
    // Die Liste bestellteProdukte enthält alle Produkte, welche  bestellt worden sind
    private ArrayList<Bestellung> bestellungen;
    private int bestellungsNr;

    /**
     * Konstruktor der Klasse
     * Hier werden alle globale Variablen initialisiert
     */
    public Fabrik() 
    {
        bestellungen = new ArrayList<Bestellung>();
        bestellungsNr = 0; // optional
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
        } else {
            bestellungsNr++; // can be done also with the other ways
            bestellungen.add(new Bestellung(standardTueren, premiumTueren, bestellungsNr));
        }
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
}
