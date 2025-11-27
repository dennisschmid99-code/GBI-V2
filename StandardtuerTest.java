import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Die Test-Klasse StandardtuerTest.
 * Prüft die spezifischen Eigenschaften der Standardtuer und die Vererbung.
 *
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class StandardtuerTest
{
    private Standardtuer tuer;

    /**
     * Konstruktor für die Test-Klasse StandardtuerTest
     */
    public StandardtuerTest()
    {
    }

    /**
     * Setzt das Testgerüst vor jedem Test neu auf.
     */
    @BeforeEach
    public void setUp()
    {
        tuer = new Standardtuer();
    }

    /**
     * Gibt das Testgerüst wieder frei.
     */
    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Testet, ob alle statischen Materialwerte korrekt sind.
     * Hier MÜSSEN alle 5 Werte geprüft werden, Lücken sind nicht erlaubt.
     */
    @Test
    public void testMaterialKonstanten()
    {
        // Wir prüfen alle Werte gegen die Vorgaben aus der Klasse Standardtuer
        assertEquals(2, Standardtuer.gibHolzeinheiten(), "Holzeinheiten sollten 2 sein");
        assertEquals(10, Standardtuer.gibSchrauben(), "Schrauben sollten 10 sein");
        assertEquals(2, Standardtuer.gibFarbeinheiten(), "Farbeinheiten sollten 2 sein");
        assertEquals(1, Standardtuer.gibKartoneinheiten(), "Kartoneinheiten sollten 1 sein");
        assertEquals(10, Standardtuer.gibProduktionszeit(), "Produktionszeit sollte 10 Min sein");
    }

    /**
     * Testet die Initialisierung (Vererbung).
     * Eine neue Standardtuer muss den Zustand 0 (Bestellt) haben.
     */
    @Test
    public void testInitialisierung()
    {
        assertEquals(0, tuer.aktuellerZustand(), "Neue Standardtür muss Zustand 0 haben");
    }

    /**
     * Testet den gesamten Lebenszyklus (Vererbung der Setter).
     * Prüft Grenzwerte von 0 bis 3.
     */
    @Test
    public void testZustandsAenderung()
    {
        // Startzustand
        assertEquals(0, tuer.aktuellerZustand());

        // 1: In Produktion
        tuer.zustandAendern(1);
        assertEquals(1, tuer.aktuellerZustand(), "Zustandsänderung auf 1 fehlgeschlagen");

        // 2: Bereit
        tuer.zustandAendern(2);
        assertEquals(2, tuer.aktuellerZustand(), "Zustandsänderung auf 2 fehlgeschlagen");

        // 3: Ausgeliefert
        tuer.zustandAendern(3);
        assertEquals(3, tuer.aktuellerZustand(), "Zustandsänderung auf 3 fehlgeschlagen");
    }
}