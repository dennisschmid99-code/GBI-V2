
/**
 * Klasse Standardtuer beinhaltet die spezifischen Informationen einer Standardtuer
 *
 * @author Alex Marchese
 * @version 26.11.2025
 */
public class Standardtuer extends Produkt 
{
    // Diese Klassenvariablen beschreiben die Einheiten, die für die Herstellung 
    // jeder Standardtuer gebraucht werden, deswegen werden sie nicht als Instanzvariablen
    // sondern als Klassenvariablen deklariert.
    private static final int HOLZEINHEITEN = 2;
    private static final int SCHRAUBEN = 10;
    private static final int FARBEINHEITEN = 2;
    private static final int KARTONEINHEITEN = 1;
    
    // Für die Produktion einer Standardtür werden 10 Minuten gebraucht
    private static final int PRODUKTIONSZEIT = 10;

    /**
     * Konstruktor ruft den Konstruktor der Superklasse auf
     */
    public Standardtuer() 
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
