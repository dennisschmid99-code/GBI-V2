/**
 * Die Klasse Standardtuer repräsentiert eine Standardtür und beinhaltet
 * alle materialspezifischen Informationen, die für deren Produktion benötigt werden.
 * 
 * Diese Werte sind für alle Standardtüren identisch und werden daher als
 * Klassenvariablen (static final) geführt.
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class Standardtuer extends Produkt {

    // Materialbedarf pro Standardtür
    private static final int HOLZEINHEITEN = 2;
    private static final int SCHRAUBEN = 10;
    private static final int FARBEINHEITEN = 2;
    private static final int KARTONEINHEITEN = 1;

    // Produktionszeit in Minuten (wird in Aufgabe 3 in ms umgerechnet)
    private static final int PRODUKTIONSZEIT = 10;

    /**
     * Konstruktor für eine neue Standardtür.
     * Ruft den Konstruktor der Superklasse Produkt auf.
     */
    public Standardtuer() {
        super();  // setzt Zustand = 0
    }

    /** 
     * @return benötigte Holzeinheiten pro Standardtür 
     */
    public static int gibHolzeinheiten() {
        return HOLZEINHEITEN;
    }

    /** 
     * @return benötigte Schrauben pro Standardtür 
     */
    public static int gibSchrauben() {
        return SCHRAUBEN;
    }

    /** 
     * @return benötigte Farbeinheiten pro Standardtür 
     */
    public static int gibFarbeinheiten() {
        return FARBEINHEITEN;
    }

    /** 
     * @return benötigte Kartoneinheiten pro Standardtür 
     */
    public static int gibKartoneinheiten() {
        return KARTONEINHEITEN;
    }

    /** 
     * @return Produktionszeit in Minuten 
     */
    public static int gibProduktionszeit() {
        return PRODUKTIONSZEIT;
    }
}
