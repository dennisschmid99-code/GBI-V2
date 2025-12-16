import java.util.ArrayList;

/**
 * Die Klasse Fabrik ist der Haupt-Einstiegspunkt. Sie initialisiert die Infrastruktur
 * (Lager, Lieferant, Manager) und ermöglicht die Eingabe von Bestellungen.
 *
 * @author GBI Gruppe 17
 * @version 16.12.2025
 */
public class Fabrik {

    private Lager lager;
    private Lieferant lieferant;
    private Produktions_Manager produktionsManager;
    private ArrayList<Bestellung> alleBestellungen;
    private int bestellungsNrCounter;

    /**
     * Konstruktor der Fabrik.
     * Erstellt die Komponenten, verknüpft sie und startet die Threads.
     */
    public Fabrik() {
        this.alleBestellungen = new ArrayList<Bestellung>();
        this.bestellungsNrCounter = 1;

        this.lager = new Lager();
        this.lieferant = new Lieferant(lager);
        this.lager.setzeLieferant(lieferant);
        
        // Threads starten
        this.lieferant.start(); 

        this.produktionsManager = new Produktions_Manager(lager);
        this.produktionsManager.start();

        System.out.println("Fabrik: System hochgefahren. Threads gestartet.");
    }

    public void bestellungAufgeben(int anzahlPremium, int anzahlStandard) {
        Bestellung neueBestellung = new Bestellung(bestellungsNrCounter++);
        
        for (int i = 0; i < anzahlStandard; i++) {
            neueBestellung.fuegeProduktHinzu(new Standardtuer());
        }
        for (int i = 0; i < anzahlPremium; i++) {
            neueBestellung.fuegeProduktHinzu(new Premiumtuer());
        }

        alleBestellungen.add(neueBestellung);
        System.out.println("Fabrik: Bestellung " + neueBestellung.gibBestellungsNr() + " aufgegeben.");

        produktionsManager.fuegeZuVerarbeitendeBestellungenHinzu(neueBestellung);
    }

    public ArrayList<Bestellung> gibBestellungen() {
        return alleBestellungen;
    }

    /**
     * Getter für den Lieferanten.
     * Ermöglicht Zugriff für Tests (z.B. um Lieferzeit anzupassen).
     * @return Das Lieferanten-Objekt
     */
    public Lieferant gibLieferant() {
        return lieferant;
    }
}