import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Lager.
 * KORRIGIERTE VERSION: Berücksichtigt Farbe als Bottleneck.
 *
 * @author Gruppe 17 (Refactored & Verified)
 * @version 04.12.2025
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
     * Berechnete Beschaffungszeit bei Materialmangel.
     * Szenario: 500 Türen benötigen 1000 Einheiten Farbe. Lager hat 100.
     * Das ist der Engpass (Bottleneck), da Holz für 1000 Türen weniger oft nachgefüllt werden müsste.
     * * Rechnung:
     * Startbestand Farbe: 100
     * Fehlmenge: 900
     * Kapazität pro Ladung: 100
     * Nötige Nachfüllungen: 900 / 100 = 9 Zyklen.
     * Zeit: 9 * 2 Tage = 18 Tage.
     */
    @Test
    public void testBeschaffungszeitMitNachbestellung()
    {
        // Bestellung: 500 Standardtüren
        Bestellung b = new Bestellung(500, 0, 2);

        int zeit = lager.gibBeschaffungsZeit(b);

        assertEquals(18, zeit, 
            "Bottleneck Farbe (Kap 100, Bedarf 1000) erzwingt 9 Zyklen à 2 Tage = 18 Tage.");
    }

    /**
     * Test 3:
     * Material muss nach einer Bestellung korrekt abgezogen werden.
     */
    @Test
    public void testMaterialAbzug()
    {
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