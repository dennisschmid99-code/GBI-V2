import java.util.LinkedList;

/**
 * Die Klasse Produkt ist die abstrakte Oberklasse für alle Produkte (Türen).
 * Sie verwaltet nun auch den Produktionsablauf durch die Roboter.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public abstract class Produkt {

    // Zustands-Konstanten für bessere Lesbarkeit
    public static final int BESTELLT = 0;
    public static final int IN_PRODUKTION = 1;
    public static final int FERTIG = 2;

    // Instanzvariablen
    private int zustand;
    
    // Liste der Roboter, die dieses Produkt noch bearbeiten müssen
    private LinkedList<Roboter> produktionsAblauf;

    /**
     * Konstruktor für die Klasse Produkt
     */
    public Produkt() {
        this.zustand = BESTELLT;
        this.produktionsAblauf = new LinkedList<Roboter>();
    }

    /**
     * Fügt einen Roboter zum Produktionsablauf hinzu.
     * Dies wird vom Produktions_Manager genutzt, um den Weg des Produkts zu planen.
     * @param roboter Der Roboter, der als nächstes arbeiten soll
     */
    public void fuegeRoboterHinzu(Roboter roboter) {
        produktionsAblauf.add(roboter);
    }

    /**
     * Schickt das Produkt zur nächsten Station im Ablauf.
     * Nimmt den nächsten Roboter aus der Liste und fügt das Produkt
     * in dessen Warteschlange ein.
     */
    public void naechsteProduktionsStation() {
        // Prüfen, ob noch Roboter im Ablauf sind
        if (!produktionsAblauf.isEmpty()) {
            // Zustand aktualisieren, falls das Produkt gerade erst startet
            if (zustand == BESTELLT) {
                zustand = IN_PRODUKTION;
            }

            // Den nächsten Roboter aus der Liste holen und entfernen
            Roboter naechsterRoboter = produktionsAblauf.removeFirst();
            
            // Das Produkt beim Roboter anmelden
            naechsterRoboter.fuegeProduktHinzu(this);
            
        } else {
            // Keine weiteren Stationen -> Produkt ist fertig
            zustand = FERTIG;
            System.out.println("Produkt fertiggestellt: " + this);
        }
    }

    /**
     * Gibt den aktuellen Zustand des Produkts zurück.
     * @return int Zustand (0=Bestellt, 1=In Produktion, 2=Fertig)
     */
    public int gibAktuellerZustand() {
        return zustand;
    }

    /**
     * Ändert den Zustand des Produkts manuell (falls nötig).
     * @param zustand Der neue Zustand
     */
    public void zustandAendern(int zustand) {
        this.zustand = zustand;
    }
    
    /**
     * Prüft, ob das Produkt fertig produziert ist.
     * @return true wenn fertig, sonst false
     */
    public boolean istFertig() {
        return zustand == FERTIG;
    }
}