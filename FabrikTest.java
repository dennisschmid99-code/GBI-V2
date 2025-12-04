import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Fabrik.
 * KORRIGIERTE VERSION: Mit validierten mathematischen Erwartungswerten.
 *
 * @author Gruppe 17 (Refactored & Verified)
 * @version 04.12.2025
 */
public class FabrikTest {

    @BeforeEach
    public void setUp() { }

    @AfterEach
    public void tearDown() { }

    @Test
    public void testeBestellung() {
        Fabrik fabrik = new Fabrik();
        fabrik.bestellungAufgeben(2, 5);
        
        Bestellung b = fabrik.gibBestellungen().get(0);
        assertEquals(0, b.gibBeschaffungsZeit());
        assertTrue(b.gibBestellBestaetigung());
    }

    @Test
    public void testeBestellungFalsch() {
        Fabrik fabrik = new Fabrik();
        fabrik.bestellungAufgeben(0, 0);
        fabrik.bestellungAufgeben(-5, 1);
        assertEquals(0, fabrik.gibBestellungen().size());
    }

    @Test
    public void testeBestellungMitLieferzeit() {
        Fabrik fabrik = new Fabrik();
        int s = 2; int p = 5;
        fabrik.bestellungAufgeben(s, p);
        
        Bestellung b = fabrik.gibBestellungen().get(0);
        
        int produktionsMinuten = s * 10 + p * 30; // Hardcoded constants for check
        float produktionsTage = produktionsMinuten / 1440.0f;
        
        // Erwartung: Prod + 0 Beschaffung + 1 Standard
        assertEquals(produktionsTage + 1, b.gibLieferzeit(), 0.0001f);
    }

    @Test
    public void testeBestellungMitNachbestellung() {
        Fabrik fabrik = new Fabrik();
        int s = 1000; int p = 0; // 1000 Standardtüren

        fabrik.bestellungAufgeben(s, p);
        Bestellung b = fabrik.gibBestellungen().get(0);

        // KORREKTUR: Bottleneck Farbe (2000 Einheiten Bedarf / 100 Kapazität)
        // Start 100 -> 1900 fehlen -> 19 Zyklen -> 38 Tage.
        assertEquals(38, b.gibBeschaffungsZeit(), 
            "Bei 1000 Türen ist Farbe der Engpass (19 Zyklen notwendig).");

        int produktionsMinuten = s * Standardtuer.gibProduktionszeit();
        float produktionsTage = produktionsMinuten / 1440.0f;
        
        // 38 Tage Beschaffung + 1 Tag Standardlieferung
        float erwarteteLieferzeit = produktionsTage + 38 + 1;

        assertEquals(erwarteteLieferzeit, b.gibLieferzeit(), 0.0001f);
    }
}