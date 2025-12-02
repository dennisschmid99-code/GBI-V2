import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Lager.
 * Überprüft die Materialprüfung, Beschaffungszeit
 * und das Auffüllen des Lagers.
 *
 * @author Gruppe 17 - FS
 * @version 02.12.2025
 */
public class LagerTest
{
    private Lager lager;

    @BeforeEach
    public void setUp()
    {
        lager = new Lager();   // Lager startet voll
    }

    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Test 1:
     * Wenn genug Material vorhanden ist,
     * muss die Beschaffungszeit 0 sein.
     */
    @Test
    public void testBeschaffungszeitOhneNachbestellung()
    {
        Bestellung b = new Bestellung(1, 0, 1); // 1 Standardtür
        int zeit = lager.gibBeschaffungsZeit(b);

        assertEquals(0, zeit, 
            "Bei genug Material muss die Beschaffungszeit 0 Tage sein.");
    }

    /**
     * Test 2:
     * Wenn Material fehlt, muss die Beschaffungszeit 2 Tage sein.
     */
    @Test
    public void testBeschaffungszeitMitNachbestellung()
    {
        // Bestellung, die mehr Material braucht, als das Lager hat
        Bestellung b = new Bestellung(500, 0, 2);

        int zeit = lager.gibBeschaffungsZeit(b);

        assertEquals(2, zeit, 
            "Bei Materialmangel muss die Beschaffungszeit 2 Tage betragen.");
    }

    /**
     * Test 3:
     * Material muss nach einer Bestellung korrekt abgezogen werden.
     */
    @Test
    public void testMaterialAbzug()
    {
        // 1 Standardtür -> 2 Holz, 10 Schrauben ...
        int vorherHolz = lager.gibHolzBestand();

        Bestellung b = new Bestellung(1, 0, 3);
        lager.gibBeschaffungsZeit(b);   // löst Abzug aus

        assertEquals(vorherHolz - Standardtuer.gibHolzeinheiten(),
            lager.gibHolzBestand(),
            "Holzbestand wurde nicht korrekt reduziert.");
    }

    /**
     * Test 4:
     * LagerAuffuellen muss Bestände auf Max setzen.
     */
    @Test
    public void testLagerAuffuellen()
    {
        // Abzug erzeugen
        Bestellung b = new Bestellung(1, 0, 4);
        lager.gibBeschaffungsZeit(b);

        lager.lagerAuffuellen();

        assertEquals(200, lager.gibHolzBestand(),
            "LagerAuffuellen setzt den Holzbestand nicht korrekt auf Maximum.");
    }
}
