

/**
 * Die Klasse Premiumtuer (Subklasse) repräsentiert eine spezialisierte Form eines Produkts (Superklasse).
 * Sie erbt von der Superklasse Produkt und erweitert sie um spezifische Materialeigenschaften.
 * * Design-Entscheidung:
 * Da sich die Materialanforderungen (z.B. Anzahl Schrauben) für Premiumtueren
 * nicht von Tuer zu Tuer unterscheiden, werden diese als Klassenkonstanten (static final)
 * definiert.
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class Premiumtuer extends Produkt
{
    // --- Konstanten für Materialbedarf (Klassenvariablen) ---
    // Wir verwenden 'private', um den externen Zugriff nur über Methoden zu gewährleisten.
    // Wir nutzen 'static final', da diese Werte für die Herstellung der "Premiumtuer"
    // feststehen und nicht für jedes Objekt einzeln gespeichert werden müssen.
    // Konvention: Konstanten werden in GROSSBUCHSTABEN geschrieben.
    
    private static final int HOLZEINHEITEN    = 4;  // Höherer Holzbedarf als Standard
    private static final int SCHRAUBEN        = 5; 
    private static final int GLASEINHEITEN    = 5;  // Besonderheit der Premiumtür
    private static final int FARBEINHEITEN    = 1;
    private static final int KARTONEINHEITEN  = 5;  // Aufwendigere Verpackung
    private static final int PRODUKTIONSZEIT  = 30; // Längere Dauer in Minuten

    /**
     * Konstruktor für Objekte der Klasse Premiumtuer.
     * Erzeugt eine neue Premiumtür und initialisiert den geerbten Zustand.
     */
    public Premiumtuer()
    {
        // Aufruf des Konstruktors der Superklasse (Produkt).
        // Dies stellt sicher, dass die geerbte Variable 'zustand' korrekt 
        // auf 0 (bestellt) initialisiert wird, bevor wir die Tür verwenden.
        super();
    }

    // --- Getter-Methoden für die statischen Klassenvariabeln ---
    
    /**
     * Gibt die Anzahl der benötigten Holzeinheiten für eine Premiumtuer zurück.
     * @return Die Anzahl der Holzeinheiten.
     */
    public static int gibHolzeinheiten()
    {
        return HOLZEINHEITEN;
    }

    /**
     * Gibt die Anzahl der benötigten Schrauben für eine Premiumtuer zurück.
     * @return Die Anzahl der Schrauben.
     */
    public static int gibSchrauben()
    {
        return SCHRAUBEN;
    }

    /**
     * Gibt die Anzahl der Glaseinheiten zurück.
     * Dies ist ein spezifisches Merkmal der Premiumtuer und existiert
     * nicht in der Standardtuer.
     * * @return Die Anzahl der Glaseinheiten.
     */
    public static int gibGlaseinheiten()
    {
        return GLASEINHEITEN;
    }

    /**
     * Gibt die Anzahl der benötigten Farbeinheiten für eine Premiumtuer zurück.
     * @return Die Anzahl der Farbeinheiten.
     */
    public static int gibFarbeinheiten()
    {
        return FARBEINHEITEN;
    }

    /**
     * Gibt die Anzahl der benötigten Kartoneinheiten für eine Premiumtür zurück.
     * @return Die Anzahl der Kartoneinheiten.
     */
    public static int gibKartoneinheiten()
    {
        return KARTONEINHEITEN;
    }

    /**
     * Gibt die Produktionszeit in Minuten für eine Premiumtür zurück.
     * * @return Die Produktionszeit in Minuten.
     */
    public static int gibProduktionszeit()
    {
        return PRODUKTIONSZEIT;
    }
}