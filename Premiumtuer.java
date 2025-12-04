/**
 * Die Klasse Premiumtuer repräsentiert eine Premiumtür und beinhaltet
 * alle materialspezifischen Informationen, die für deren Produktion benötigt werden.
 * 
 * Diese Werte sind für alle Premiumtüren identisch und werden daher als
 * Klassenvariablen (static final) geführt.
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class Premiumtuer extends Produkt {

    // Materialbedarf pro Premiumtür
    private static final int HOLZEINHEITEN = 4;
    private static final int SCHRAUBEN = 5;
    private static final int GLASEINHEITEN = 5;
    private static final int FARBEINHEITEN = 1;
    private static final int KARTONEINHEITEN = 5;

    // Produktionszeit in Minuten (wird in Aufgabe 3 in ms umgerechnet)
    private static final int PRODUKTIONSZEIT = 30;

    /**
     * Konstruktor für eine neue Premiumtür.
     * Ruft den Konstruktor der Superklasse Produkt auf.
     */
    public Premiumtuer() {
        super(); // setzt Zustand = 0
    }

    /** 
     * @return benötigte Holzeinheiten pro Premiumtür 
     */
    public static int gibHolzeinheiten() {
        return HOLZEINHEITEN;
    }

    /** 
     * @return benötigte Schrauben pro Premiumtür 
     */
    public static int gibSchrauben() {
        return SCHRAUBEN;
    }

    /** 
     * @return benötigte Glaseinheiten pro Premiumtür 
     */
    public static int gibGlaseinheiten() {
        return GLASEINHEITEN;
    }

    /** 
     * @return benötigte Farbeinheiten pro Premiumtür 
     */
    public static int gibFarbeinheiten() {
        return FARBEINHEITEN;
    }

    /** 
     * @return benötigte Kartoneinheiten pro Premiumtür 
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
