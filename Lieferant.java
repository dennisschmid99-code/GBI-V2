
/**
 * Diese Klasse wird verwendet, um die Lieferzeiten des Lieferanten und die
 * Bestellung über diesen zu simulieren.
 *
 * @author Alex Marchese
 * @version 08.12.2025
 */
public class Lieferant {

    /**
     * Konstruktor der Klasse Lieferant
     */
    public Lieferant() {
        // nichts nötig
    }

    /**
     * Mit dieser Methode wird die Bestellung bei dem Lieferanten simuliert
     * 
     * @param holzEinheiten   Anzahl bestellter Holzeinheiten
     * @param schrauben       Anzahl bestellter Schrauben
     * @param farbEinheiten   Anzahl bestellter Farbeinheiten
     * @param kartonEinheiten Anzahl bestellter Kartoneinheiten
     * @param glasEinheiten   Anzahl bestellter Glaseinheiten
     */
    public boolean wareBestellen(int holzEinheiten, int schrauben, int farbEinheiten, int kartonEinheiten,
            int glasEinheiten) {
        return true; // Entsprechend der Aufgabenstellung gibt es keine Einschränkungen. Deshalb gibt
                     // die Methode immer true zurück. Dennoch ist dies sinnvoll, da es dem
                     // Lieferanten obliegt, zu sagen, ob Materialien verfügbar sind

    }
}
