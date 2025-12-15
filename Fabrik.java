import java.util.ArrayList;

/**
 * Die Klasse Fabrik ist der Haupt-Einstiegspunkt. Sie initialisiert die Infrastruktur
 * (Lager, Lieferant, Manager) und ermöglicht die Eingabe von Bestellungen.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Fabrik {

    // Die zentralen Komponenten der Fabrik
    private Lager lager;
    private Lieferant lieferant;
    private Produktions_Manager produktionsManager;

    // Liste aller jemals getätigten Bestellungen (Historie)
    private ArrayList<Bestellung> alleBestellungen;
    
    // Zähler für eindeutige Bestellnummern
    private int bestellungsNrCounter;

    /**
     * Konstruktor der Fabrik.
     * Erstellt die Komponenten, verknüpft sie und startet die Threads.
     */
    public Fabrik() {
        // 1. Listen und Zähler initialisieren
        this.alleBestellungen = new ArrayList<Bestellung>();
        this.bestellungsNrCounter = 1;

        // 2. Lager erstellen
        this.lager = new Lager();

        // 3. Lieferant erstellen (benötigt Referenz aufs Lager zum Abliefern)
        this.lieferant = new Lieferant(lager);
        
        // WICHTIG: Das Lager muss auch den Lieferanten kennen, um bestellen zu können.
        this.lager.setzeLieferant(lieferant);
        
        // Thread des Lieferanten starten (damit er auf Bestellungen warten kann)
        this.lieferant.start(); 

        // 4. Produktionsmanager erstellen (benötigt Lager für Materialentnahme)
        this.produktionsManager = new Produktions_Manager(lager);
        
        // Thread des Managers starten (damit er Bestellungen abarbeiten kann)
        this.produktionsManager.start();

        System.out.println("Fabrik: System hochgefahren. Threads gestartet.");
    }

    /**
     * Nimmt eine Bestellung entgegen, erzeugt die Produkte und übergibt sie an den Manager.
     * * @param anzahlPremium   Anzahl der gewünschten Premiumtüren
     * @param anzahlStandard  Anzahl der gewünschten Standardtüren
     */
    public void bestellungAufgeben(int anzahlPremium, int anzahlStandard) {
        // Neue Bestellung mit eindeutiger ID anlegen
        Bestellung neueBestellung = new Bestellung(bestellungsNrCounter++);
        
        // Standardtüren hinzufügen (benötigt Klasse Standardtuer aus Aufgabe 1/2)
        for (int i = 0; i < anzahlStandard; i++) {
            neueBestellung.fuegeProduktHinzu(new Standardtuer());
        }
        
        // Premiumtüren hinzufügen (benötigt Klasse Premiumtuer aus Aufgabe 1/2)
        for (int i = 0; i < anzahlPremium; i++) {
            neueBestellung.fuegeProduktHinzu(new Premiumtuer());
        }

        // Zur Historie hinzufügen
        alleBestellungen.add(neueBestellung);

        System.out.println("Fabrik: Bestellung " + neueBestellung.gibBestellungsNr() + 
                           " aufgegeben (" + anzahlStandard + " Std, " + anzahlPremium + " Prem).");

        // Bestellung an den Produktionsmanager zur asynchronen Verarbeitung übergeben
        produktionsManager.fuegeZuVerarbeitendeBestellungenHinzu(neueBestellung);
    }

    /**
     * Gibt die Liste aller Bestellungen zurück.
     * @return ArrayList mit Bestellungen
     */
    public ArrayList<Bestellung> gibBestellungen() {
        return alleBestellungen;
    }

    /**
     * Main-Methode zum Starten und Testen der Anwendung.
     */
    public static void main(String[] args) {
        Fabrik meineFabrik = new Fabrik();

        // Szenario 1: Eine normale Bestellung aufgeben
        meineFabrik.bestellungAufgeben(2, 3); // 2 Premium, 3 Standard

        // Wir warten kurz, um zu sehen, wie die Produktion startet
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        // Szenario 2: Eine weitere Bestellung nachschieben
        System.out.println("\n--- Neue Bestellung kommt rein ---");
        meineFabrik.bestellungAufgeben(1, 1);
        
        // Hinweis: Das Programm läuft weiter, da die Threads (Manager, Lieferant) noch leben.
        // Beenden Sie es manuell in BlueJ oder über die Konsole.
    }
}