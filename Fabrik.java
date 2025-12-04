import java.util.ArrayList;

/**
 * Die Klasse Fabrik verwaltet Bestellungen und koordiniert
 * die Materialprüfung über das Lager.
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class Fabrik {

    // Liste aller Bestellungen
    private ArrayList<Bestellung> bestellungen;

    // Laufende Bestellnummer
    private int bestellungsNr;

    // Lager der Fabrik
    private Lager lager;

    // Standardlieferzeit laut Aufgabenstellung (1 Tag)
    private static final int STANDARD_LIEFERZEIT = 1;

    /**
     * Konstruktor: Fabrik wird mit leerer Bestellliste und neuem Lager erzeugt.
     */
    public Fabrik() {
        bestellungen = new ArrayList<>();
        bestellungsNr = 0;
        lager = new Lager();   // Lager erzeugt intern seinen Lieferanten
    }

    /**
     * Gibt eine neue Bestellung auf und berechnet die Gesamtlieferzeit.
     *
     * @param standardTueren Anzahl Standardtüren
     * @param premiumTueren Anzahl Premiumtüren
     */
    public void bestellungAufgeben(int standardTueren, int premiumTueren) {

        // Fehlerbehandlung
        if (standardTueren < 0 || premiumTueren < 0) {
            System.out.println("Ungültige Bestellmenge. Kann nicht negativ sein.");
            return;
        }
        if (standardTueren == 0 && premiumTueren == 0) {
            System.out.println("Die Bestellung muss mindestens ein Produkt enthalten.");
            return;
        }
        if (standardTueren > 10000 || premiumTueren > 10000) {
            System.out.println("Bestellmenge ist zu gross. Maximal 10'000 pro Artikel.");
            return;
        }

        // Bestellung erstellen
        bestellungsNr++;
        Bestellung neueBestellung = new Bestellung(standardTueren, premiumTueren, bestellungsNr);

        // Beschaffungszeit vom Lager ermitteln
        int beschaffungsZeit = lager.gibBeschaffungsZeit(neueBestellung);
        neueBestellung.setzeBeschaffungsZeit(beschaffungsZeit);

        // Produktionszeit berechnen (in Tagen)
        int produktionsMinuten =
                standardTueren * Standardtuer.gibProduktionszeit() +
                premiumTueren * Premiumtuer.gibProduktionszeit();

        float produktionsTage = produktionsMinuten / 1440.0f; // 1440 Minuten = 1 Tag

        // Gesamtlieferzeit
        float lieferZeit = produktionsTage + beschaffungsZeit + STANDARD_LIEFERZEIT;
        neueBestellung.setzeLieferzeit(lieferZeit);

        // Bestellung bestätigen
        neueBestellung.bestellungBestaetigen();

        // Bestellung speichern
        bestellungen.add(neueBestellung);
    }

    /**
     * Gibt alle Bestellungen der Fabrik aus.
     */
    public void bestellungenAusgeben() {
        System.out.println("\nAktuelle Bestellungen:");
        for (Bestellung bestellung : bestellungen) {
            System.out.println(
                "Bestellung Nr. " + bestellung.gibBestellungsNr() +
                " | Standardtüren: " + bestellung.gibAnzahlStandardTueren() +
                " | Premiumtüren: " + bestellung.gibAnzahlPremiumTueren() +
                " | Beschaffung: " + bestellung.gibBeschaffungsZeit() +
                " | Lieferzeit: " + bestellung.gibLieferzeit() +
                " | Bestätigt: " + bestellung.gibBestellBestaetigung()
            );
        }
    }

    /**
     * Gibt die Bestellliste zurück (für Tests).
     */
    public ArrayList<Bestellung> gibBestellungen() {
        return bestellungen;
    }

    /**
     * Füllt das Lager wieder vollständig auf.
     */
    public void lagerAuffuellen() {
        lager.lagerAuffuellen();
    }
}
