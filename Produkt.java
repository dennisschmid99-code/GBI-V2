/**
 * Die Klasse Produkt ist eine Superklasse für Standardtüren und Premiumtüren.
 * Sie hält den Produktionszustand eines Produkts fest.
 *
 * Mögliche Zustände:
 * 0 = Bestellt
 * 1 = In Produktion
 * 2 = Bereit für Auslieferung
 * 3 = Ausgeliefert
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class Produkt {
    
    // Zustand des Produkts (siehe oben)
    private int zustand;

    /**
     * Konstruktor der Klasse Produkt.
     * Ein neues Produkt ist standardmäßig im Zustand "bestellt" (0).
     */
    public Produkt() {
        this.zustand = 0;
    }

    /**
     * Gibt den aktuellen Zustand des Produkts zurück.
     *
     * @return aktueller Zustand des Produkts
     */
    public int gibAktuellerZustand() {
        return zustand;
    }

    /**
     * Ändert den Zustand des Produkts.
     *
     * @param zustand neuer Zustand
     */
    public void zustandAendern(int zustand) {
        this.zustand = zustand;
    }
}
