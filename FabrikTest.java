import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Fabrik.
 * Überprüft das Erstellen, Speichern und Berechnen von Bestellungen.
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class FabrikTest {

    @BeforeEach
    public void setUp() {
        // optional: kann leer bleiben
    }

    @AfterEach
    public void tearDown() {
        // optional
    }

    /**
     * Test 1:
     * Normale Bestellung (2 Standard, 5 Premium)
     */
    @Test
    public void testeBestellung() {

        Fabrik fabrik = new Fabrik();
        fabrik.bestellungAufgeben(2, 5);

        // Eine Bestellung wurde gespeichert
        assertEquals(1, fabrik.gibBestellungen().size());

        Bestellung b = fabrik.gibBestellungen().get(0);

        // 7 Produkte insgesamt
        assertEquals(7, b.liefereBestellteProdukte().size());

        // Prüfen der Produktarten
        int s = 0;
        int p = 0;
        for (Produkt produkt : b.liefereBestellteProdukte()) {
            if (produkt instanceof Standardtuer) s++;
            if (produkt instanceof Premiumtuer) p++;
        }

        assertEquals(2, s);
        assertEquals(5, p);

        // Lager war voll -> Beschaffungszeit = 0
        assertEquals(0, b.gibBeschaffungsZeit());

        // Bestellung ist bestätigt
        assertTrue(b.gibBestellBestaetigung());

        // Lieferzeit muss > 1 Tag sein (wegen Produktion + 1 Tag Standardlieferzeit)
        assertTrue(b.gibLieferzeit() > 1.0f);
    }

    /**
     * Test 2:
     * Fehlerhafte Bestellungen dürfen NICHT gespeichert werden.
     */
    @Test
    public void testeBestellungFalsch() {

        Fabrik fabrik = new Fabrik();

        fabrik.bestellungAufgeben(0, 0);      // ungültig
        fabrik.bestellungAufgeben(-5, 1);     // ungültig
        fabrik.bestellungAufgeben(1, -3);     // ungültig
        fabrik.bestellungAufgeben(20000, 1);  // ungültig

        assertEquals(0, fabrik.gibBestellungen().size());
    }

    /**
     * Test 3:
     * Lieferzeitberechnung OHNE Nachbestellung
     */
    @Test
    public void testeBestellungMitLieferzeit() {

        Fabrik fabrik = new Fabrik();

        int s = 2;
        int p = 5;

        fabrik.bestellungAufgeben(s, p);

        Bestellung b = fabrik.gibBestellungen().get(0);

        // Produktionszeit in Minuten
        int produktionsMinuten =
                s * Standardtuer.gibProduktionszeit()
              + p * Premiumtuer.gibProduktionszeit();

        float produktionsTage = produktionsMinuten / 1440.0f;

        float erwarteteLieferzeit = produktionsTage + 0 + 1; // 0 Beschaffung + 1 Standardlieferzeit

        assertEquals(0, b.gibBeschaffungsZeit());
        assertEquals(erwarteteLieferzeit, b.gibLieferzeit(), 0.0001f);
    }

    /**
     * Test 4:
     * Bestellung so groß, dass das Lager nachbestellen MUSS
     */
    @Test
    public void testeBestellungMitNachbestellung() {

        Fabrik fabrik = new Fabrik();

        int s = 1000;   // garantiert zu groß für das Lager
        int p = 0;

        fabrik.bestellungAufgeben(s, p);

        Bestellung b = fabrik.gibBestellungen().get(0);

        // Nachbestellung nötig
        assertEquals(2, b.gibBeschaffungsZeit());

        // Produktionszeit berechnen
        int produktionsMinuten =
                s * Standardtuer.gibProduktionszeit()
              + p * Premiumtuer.gibProduktionszeit();

        float produktionsTage = produktionsMinuten / 1440.0f;
        float erwarteteLieferzeit = produktionsTage + 2 + 1;

        assertEquals(erwarteteLieferzeit, b.gibLieferzeit(), 0.0001f);

        // Bestellung muss bestätigt sein
        assertTrue(b.gibBestellBestaetigung());
    }
}
