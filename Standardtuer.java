
/**
 * Die Klasse Standardtuer (Subklasse) repräsentiert eine spezialisierte Form eines Produkts (Superklasse).
 * Sie erbt von der Superklasse Produkt und erweitert sie um spezifische Materialeigenschaften.
 * * Design-Entscheidung:
 * Da sich die Materialanforderungen (z.B. Anzahl Schrauben) für Premiumtueren
 * nicht von Tuer zu Tuer unterscheiden, werden diese als Klassenkonstanten (static final)
 * definiert.
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class Standardtuer extends Produkt
{
    // --- Klassenkonstanten für den Materialbedarf ---
    // Wir verwenden 'private', um den externen Zugriff nur über Methoden zu gewährleisten.
    // Wir nutzen 'static final', da diese Werte für die Herstellung der "Standardtür"
    // feststehen und nicht für jedes Objekt einzeln gespeichert werden müssen.
    // Konvention: Konstanten werden in GROSSBUCHSTABEN geschrieben.
    
    private static final int HOLZEINHEITEN    = 2;  // Standardbedarf an Holz
    private static final int SCHRAUBEN        = 10; // Weniger Schrauben als bei Premium
    private static final int FARBEINHEITEN    = 2;
    private static final int KARTONEINHEITEN  = 1;  // Standardverpackung
    private static final int PRODUKTIONSZEIT  = 10; // Kürzere Dauer als Premium (in Minuten)

    /**
     * Konstruktor für Objekte der Klasse Standardtuer.
     * Erzeugt eine neue Standardtuer und initialisiert den geerbten Zustand.
     * Konstruktor ruft den Konstruktor der Superklasse auf
     */
    public Standardtuer()
    {
        // Aufruf des Konstruktors der Superklasse (Produkt).
        // Dies stellt sicher, dass die geerbte Variable 'zustand' korrekt 
        // auf 0 (bestellt) initialisiert wird, bevor wir die Tür verwenden.
        super();
    }

    // --- Getter-Methoden für die statischen Klassenvariablen ---

    /**
     * Gibt die Anzahl der benötigten Holzeinheiten für eine Standardtür zurück.
     * @return Die Anzahl der Holzeinheiten.
     */
    public static int gibHolzeinheiten()
    {
        return HOLZEINHEITEN;
    }

    /**
     * Gibt die Anzahl der benötigten Schrauben für eine Standardtür zurück.
     * @return Die Anzahl der Schrauben.
     */
    public static int gibSchrauben()
    {
        return SCHRAUBEN;
    }

    /**
     * Gibt die Anzahl der benötigten Farbeinheiten für eine Standardtür zurück.
     * @return Die Anzahl der Farbeinheiten.
     */
    public static int gibFarbeinheiten()
    {
        return FARBEINHEITEN;
    }

    /**
     * Gibt die Anzahl der benötigten Kartoneinheiten für eine Standardtür zurück.
     * @return Die Anzahl der Kartoneinheiten.
     */
    public static int gibKartoneinheiten()
    {
        return KARTONEINHEITEN;
    }

    /**
     * Gibt die Produktionszeit für eine Standardtür zurück.
     * @return Die Produktionszeit in Minuten.
     */
    public static int gibProduktionszeit()
    {
        return PRODUKTIONSZEIT;
    }
}