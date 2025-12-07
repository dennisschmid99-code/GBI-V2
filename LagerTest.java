import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Die Test-Klasse LagerTest.
 * Sie prüft die Funktionalität der Lagerverwaltung, insbesondere die Berechnung
 * der Beschaffungszeit bei ausreichenden und unzureichenden Beständen.
 *
 * @author Gruppe 17
 * @version 04.12.2025
 */
public class LagerTest
{
    private Lager lager;

    /**
     * Konstruktor für die Test-Klasse LagerTest
     */
    public LagerTest()
    {
    }

    /**
     * Setzt das Testgerüst vor jedem Test neu auf.
     * Es wird ein neues Lager mit vollen Anfangsbeständen erzeugt.
     */
    @BeforeEach
    public void setUp()
    {
        lager = new Lager(); 
    }

    /**
     * Gibt das Testgerüst wieder frei.
     * Wird nach jeder Testmethode aufgerufen.
     */
    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Testet den Fall, dass genügend Material für die Bestellung vorhanden ist.
     * Erwartung: Die Beschaffungszeit beträgt 0 Tage.
     */
    @Test
    public void testBeschaffungszeitOhneNachbestellung()
    {
        // Eine kleine Bestellung, die problemlos aus dem Lager bedient werden kann.
        Bestellung b = new Bestellung(1, 0, 1); 
        int zeit = lager.gibBeschaffungsZeit(b);

        assertEquals(0, zeit, "Bei ausreichendem Lagerbestand muss die Beschaffungszeit 0 sein.");
    }

    /**
     * Testet den Fall, dass Material fehlt (Engpass).
     * Szenario: 500 Standardtüren benötigen 1000 Einheiten Farbe.
     * Das Lager hat eine maximale Kapazität von 1000 Einheiten Farbe.
     * Da der Bestand (1000) exakt dem Bedarf (1000) entspricht, ist noch keine Nachbestellung nötig.
     * Erwartung: 0 Tage Beschaffungszeit (Grenzfall).
     */
    @Test
    public void testBeschaffungszeitGrenzfall()
    {
        // 500 Standardtüren verbrauchen genau 1000 Einheiten Farbe.
        // Das entspricht exakt der Lagerkapazität für Farbe.
        Bestellung b = new Bestellung(500, 0, 2);

        int zeit = lager.gibBeschaffungsZeit(b);

        assertEquals(0, zeit, "Bestand deckt Bedarf exakt ab. Zeit muss 0 sein.");
    }

    /**
     * Testet, ob die Materialbestände nach einer Bestellung korrekt reduziert werden.
     * Wir prüfen dies exemplarisch am Holzbestand.
     */
    @Test
    public void testMaterialAbzug()
    {
        int vorherHolz = lager.gibHolzBestand();

        // Bestellung auslösen, um Material zu verbrauchen
        Bestellung b = new Bestellung(1, 0, 3);
        lager.gibBeschaffungsZeit(b); 

        // Eine Standardtür verbraucht 2 Einheiten Holz.
        // Der neue Bestand muss also (Alter Bestand - 2) sein.
        assertEquals(vorherHolz - Standardtuer.gibHolzeinheiten(),
                     lager.gibHolzBestand(),
                     "Der Holzbestand wurde nach der Bestellung nicht korrekt reduziert.");
    }

    /**
     * Testet die Methode lagerAuffuellen.
     * Erwartung: Alle Bestände müssen wieder auf ihren Maximalwert gesetzt werden.
     */
    @Test
    public void testLagerAuffuellen()
    {
        // Zuerst Material verbrauchen
        Bestellung b = new Bestellung(1, 0, 4);
        lager.gibBeschaffungsZeit(b);

        // Dann das Lager wieder auffüllen
        lager.lagerAuffuellen();

        // Prüfen, ob der Holzbestand wieder auf dem Maximum (1000) ist.
        assertEquals(1000, lager.gibHolzBestand(), 
                     "Nach dem Auffüllen muss der Holzbestand wieder 1000 betragen.");
    }
}