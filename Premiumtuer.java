
/**
 * Klasse Premiumtuer beinhaltet die spezifischen Informationen einer Standardtuer
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Premiumtuer extends Produkt 
{
    // Diese Klassenvariablen beschreiben die Einheiten, die für die Herstellung 
    // jeder Premiumtuer gebraucht werden, deswegen werden sie nicht als Instanzvariablen
    // sondern als Klassenvariablen deklariert.
    private static final int HOLZEINHEITEN = 4;
    private static final int SCHRAUBEN = 5;
    private static final int GLASEINHEITEN = 5;
    private static final int FARBEINHEITEN = 1;
    private static final int KARTONEINHEITEN = 5;
    
    // Für die Produktion einer Premiumtür werden 30 Minuten gebraucht
    private static final int PRODUKTIONSZEIT = 30;

    /**
     * Konstruktor ruft den Konstruktor der Superklasse auf
     */
    public Premiumtuer() 
    {
        // constructor not needed
        // super(); //optional
    }

    /**
     * Mit dieser Methode wird die benötigte Anzahl Holzeinheiten für die Produktion ausgegeben 
     * 
     * @return HOLZEINHEITEN
     */ 
    public static int gibHolzeinheiten() 
    {
        return HOLZEINHEITEN;
    }

    /**
     * Mit dieser Methode wird die benötigte Anzahl Schrauben für die Produktion ausgegeben 
     * 
     * @return SCHRAUBEN
     */ 
    public static int gibSchrauben() 
    {
        return SCHRAUBEN;
    }

    /**
     * Mit dieser Methode wird die benötigte Anzahl Glaseinheiten für die Produktion ausgegeben 
     * 
     * @return GLASEINHEITEN
     */
    public static int gibGlaseinheiten() 
    {
        return GLASEINHEITEN;
    }

    /**
     * Mit dieser Methode wird die benötigte Anzahl Farbeinheiten für die Produktion ausgegeben 
     * 
     * @return FARBEINHEITEN
     */  
    public static int gibFarbeinheiten() 
    {
        return FARBEINHEITEN;
    }

    /**
     * Mit dieser Methode wird die benötigte Anzahl Kartoneinheiten für die Produktion ausgegeben 
     * 
     * @return KARTONEINHEITEN 
     */
    public static int gibKartoneinheiten() 
    {
        return KARTONEINHEITEN;
    }

    /**
     * Mit dieser Methode wird die benötigte Produktionszeit ausgegeben
     * 
     * @return PRODUKTIONSZEIT
     */ 
    public static int gibProduktionszeit() 
    {
        return PRODUKTIONSZEIT;
    }
}
