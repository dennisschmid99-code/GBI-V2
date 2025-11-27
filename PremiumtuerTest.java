import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Die Test-Klasse PremiumtuerTest.
 *
 * Diese prüft:
 * 1. Ob die statischen Materialwerte korrekt sind.
 * 2. Ob die Vererbung funktioniert (Initialisierung des Zustands).
 * 3. Ob sich der Zustand korrekt ändern lässt (Grenztests der Zustände).
 *
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class PremiumtuerTest
{
    private Premiumtuer premiumTuer1;

    /**
     * Konstruktor für die Test-Klasse PremiumtuerTest
     */
    public PremiumtuerTest()
    {
    }

    /**
     * Setzt das Testgerüst fuer jeden Test neu auf.
     * Wird vor jeder @Test-Methode aufgerufen.
     */
    @BeforeEach
    public void setUp()
    {
        premiumTuer1 = new Premiumtuer();
    }

    /**
     * Gibt das Testgerüst wieder frei.
     * Wird nach jeder @Test-Methode aufgerufen.
     */
    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Testet, ob alle Material-Konstanten (statische Methoden)
     * die korrekten Werte für eine Premiumtuer zurückgeben.
     */
    @Test
    public void testMaterialKonstanten()
    {
        // Wir prüfen die statischen Getter direkt über den Klassennamen
        // Dies stellt sicher, dass die Werte exakt den Anforderungen entsprechen.
        assertEquals(4, Premiumtuer.gibHolzeinheiten(), "Holzeinheiten sollten 4 sein");
        assertEquals(5, Premiumtuer.gibSchrauben(), "Schrauben sollten 5 sein");
        assertEquals(5, Premiumtuer.gibGlaseinheiten(), "Glaseinheiten sollten 5 sein");
        assertEquals(1, Premiumtuer.gibFarbeinheiten(), "Farbeinheiten sollten 1 sein");
        assertEquals(5, Premiumtuer.gibKartoneinheiten(), "Kartoneinheiten sollten 5 sein");
        assertEquals(30, Premiumtuer.gibProduktionszeit(), "Produktionszeit sollte 30 Min sein");
    }

    /**
     * Testet die Initialisierung.
     * Eine neue Premiumtuer muss den Zustand 0 (Bestellt) haben.
     * Dies prüft, ob der super()-Aufruf im Konstruktor funktioniert.
     */
    @Test
    public void testInitialisierung()
    {
        // Erwartung: Zustand 0 direkt nach Erzeugung
        assertEquals(0, premiumTuer1.aktuellerZustand(), "Neue Tür muss Zustand 0 haben");
    }

    /**
     * Testet den Zustandsübergang (State Change).
     * Wir testen den kompletten Lebenszyklus von 0 bis 3.
     * Dies deckt die Grenzwerte der Zustandslogik ab.
     */
    @Test
    public void testZustandsAenderung()
    {
        // Test Startzustand
        assertEquals(0, premiumTuer1.aktuellerZustand());

        // Übergang zu 1: In Produktion
        premiumTuer1.zustandAendern(1);
        assertEquals(1, premiumTuer1.aktuellerZustand(), "Zustand sollte auf 1 änderbar sein");

        // Übergang zu 2: Bereit
        premiumTuer1.zustandAendern(2);
        assertEquals(2, premiumTuer1.aktuellerZustand(), "Zustand sollte auf 2 änderbar sein");

        // Übergang zu 3: Ausgeliefert (Oberer Grenzwert)
        premiumTuer1.zustandAendern(3);
        assertEquals(3, premiumTuer1.aktuellerZustand(), "Zustand sollte auf 3 änderbar sein");
    }
}