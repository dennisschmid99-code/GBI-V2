import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Bestellung.
 * Testet Initialisierung, Methoden und Fehlerbehandlung.
 *
 * @author Gruppe 17 - FS
 * @version 02.12.2025
 */
public class BestellungTest {

    @BeforeEach
    public void setUp() {
        // optional
    }

    @AfterEach
    public void tearDown() {
        // optional
    }

    /**
     * Testet die korrekte Initialisierung einer Bestellung.
     */
    @Test
    public void testeBestellung() {

        Bestellung b = new Bestellung(5, 7, 2);

        assertEquals(5, b.gibAnzahlStandardTueren());
        assertEquals(7, b.gibAnzahlPremiumTueren());
        assertEquals(2, b.gibBestellungsNr());

        // Standardwerte prüfen
        assertFalse(b.gibBestellBestaetigung());
        assertEquals(-1, b.gibBeschaffungsZeit());
        assertEquals(0.0f, b.gibLieferzeit(), 0.0001f);
    }

    /**
     * Testet die Methode bestellungBestaetigen().
     */
    @Test
    public void testeBestellungBestaetigen() {

        Bestellung b = new Bestellung(5, 7, 2);

        assertFalse(b.gibBestellBestaetigung());

        b.bestellungBestaetigen();

        assertTrue(b.gibBestellBestaetigung());
    }

    /**
     * Testet setzeBeschaffungsZeit().
     */
    @Test
    public void testeSetzeBeschaffungsZeit() {

        Bestellung b = new Bestellung(5, 7, 2);

        assertEquals(-1, b.gibBeschaffungsZeit());

        b.setzeBeschaffungsZeit(2);

        assertEquals(2, b.gibBeschaffungsZeit());
    }

    /**
     * Testet setzeLieferzeit() und gibLieferzeit().
     */
    @Test
    public void testeSetzeLieferzeit() {

        Bestellung b = new Bestellung(5, 7, 2);

        assertEquals(0.0f, b.gibLieferzeit(), 0.0001f);

        b.setzeLieferzeit(3.5f);

        assertEquals(3.5f, b.gibLieferzeit(), 0.0001f);
    }

    /**
     * Testet die korrekte Erstellung der Produktliste.
     */
    @Test
    public void testeLiefereBestellteProdukte() {

        Bestellung b = new Bestellung(2, 3, 1);

        assertEquals(5, b.liefereBestellteProdukte().size());

        int standard = 0;
        int premium = 0;

        for (Produkt p : b.liefereBestellteProdukte()) {
            if (p instanceof Standardtuer) standard++;
            if (p instanceof Premiumtuer) premium++;
        }

        assertEquals(2, standard);
        assertEquals(3, premium);
    }

    /**
     * Testet die Fehlerbehandlung (Exceptions).
     */
    @Test
    public void testeFehlerbehandlung() {

        assertThrows(IllegalArgumentException.class,
                () -> new Bestellung(-1, 5, 1));

        assertThrows(IllegalArgumentException.class,
                () -> new Bestellung(5, -1, 2));

        assertThrows(IllegalArgumentException.class,
                () -> new Bestellung(0, 0, 3));

        assertThrows(IllegalArgumentException.class,
                () -> new Bestellung(11_000, 5, 4));

        assertThrows(IllegalArgumentException.class,
                () -> new Bestellung(5, 12_000, 5));
    }
}
