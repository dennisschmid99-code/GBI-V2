/**
 * Die Klasse Lieferant simuliert einen externen Lieferanten, 
 * der Materialbestellungen für das Lager annimmt und immer erfolgreich liefert.
 *
 * @author Gruppe 17 - FS
 * @version 02.12.2025
 */
public class Lieferant {

    /**
     * Standard-Konstruktor für die Klasse Lieferant.
     * Keine besondere Initialisierung notwendig.
     */
    public Lieferant() {
        // nichts notwendig
    }

    /**
     * Bestellt Material beim Lieferanten.
     * In dieser Simulation liefert der Lieferant immer erfolgreich.
     *
     * @param holz  benötigte Holzeinheiten
     * @param schrauben  benötigte Schrauben
     * @param farbe benötigte Farbeinheiten
     * @param karton benötigte Kartoneinheiten
     * @param glas benötigte Glaseinheiten
     * @return true (Lieferung immer erfolgreich)
     */
    public boolean wareBestellen(int holz, int schrauben, int farbe, int karton, int glas) {
        System.out.println("LIEFERANT: Bestellung erhalten. Ware wird geliefert.");
        return true;
    }
}
