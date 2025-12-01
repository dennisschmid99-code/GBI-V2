
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows; //optional

/**
 * Klasse BestellungTest
 *
 * @author Alex Marchese
 * @version 26.11.2025
 */
public class BestellungTest {
    String nameTestClasse = "BestellungTest"; // Name der Testklasse

    /**
     * Konstruktor von BestellungTest
     */
    public BestellungTest() {
    }

    /**
     * Anweisungen vor jedem Testlauf
     */
    @BeforeEach
    public void setUp() {
        System.out.println("Testlauf " + nameTestClasse + " Start");
        System.out.println();
    }

    /**
     * Anweisungen nach jedem Testlauf
     */
    @AfterEach
    public void tearDown() {
        System.out.println();
        System.out.println("Testlauf " + nameTestClasse + " Ende");
        System.out.println("------------------------");
    }

    @Test
    /**
     * Testet die korrekte Initialisierung einer Bestellung
     */
    public void testeBestellung() {

        // Instanzierung einer Bestellung
        Bestellung testBestellung = new Bestellung(5, 7, 2);

        assertEquals(5, testBestellung.gibAnzahlStandardTueren());
        assertEquals(7, testBestellung.gibAnzahlPremiumTueren());
        assertEquals(2, testBestellung.gibBestellungsNr());

        // Testen von automatisch initialisierten Werten
        assertEquals(false, testBestellung.gibBestellBestaetigung());
        assertEquals(-1, testBestellung.gibBeschaffungsZeit());
        assertEquals(0.0f, testBestellung.gibLieferzeit(), 0.0001f);

        System.out.println(
                "Test Bestellung mit Variableneingabe erfolgreich. Initialisierung mit Selbstdefinierten Variablen und Standardwerten funktioniert.");

    }

    @Test
    /**
     * Testet bestellungBestaetigen()
     */
    public void testeBestellungBestaetigen() {

        // Instanzierung einer Bestellung
        Bestellung testBestellung = new Bestellung(5, 7, 2);

        assertEquals(false, testBestellung.gibBestellBestaetigung());
        testBestellung.bestellungBestaetigen();
        assertEquals(true, testBestellung.gibBestellBestaetigung());

        System.out.println(
                "Test Methode bestellungBestaetigen erfolgreich.");

    }

    @Test
    /**
     * Testet setzeBeschaffungsZeit()
     */
    public void testeSetzeBeschaffungsZeit() {

        // Instanzierung einer Bestellung
        Bestellung testBestellung = new Bestellung(5, 7, 2);

        assertEquals(-1, testBestellung.gibBeschaffungsZeit());
        testBestellung.setzeBeschaffungsZeit(2);
        assertEquals(2, testBestellung.gibBeschaffungsZeit());

        System.out.println("Test Setter setzeBeschaffungsZeit erfolgreich.");

    }

     @Test
    /**
     * Testet setzeLieferzeit() und gibLieferzeit(),
     * welche in Aufgabe 2 neu hinzugekommen sind.
     */
    public void testeSetzeLieferzeit() {

        // Instanzierung einer Bestellung
        Bestellung testBestellung = new Bestellung(5, 7, 2);

        // Initialwert prüfen
        assertEquals(0.0f, testBestellung.gibLieferzeit(), 0.0001f);

        // Lieferzeit setzen und prüfen
        testBestellung.setzeLieferzeit(3.5f);
        assertEquals(3.5f, testBestellung.gibLieferzeit(), 0.0001f);

        System.out.println("Test Setter/Getter für Lieferzeit erfolgreich.");
    }

    @Test
    /**
     * Testet die neue Methode liefereBestellteProdukte().
     * 
     */
    public void testeLiefereBestellteProdukte() {

        // Instanzierung einer Bestellung
        Bestellung testBestellung = new Bestellung(2, 3, 1);

        // Es sollten insgesamt 5 Produkte in der Liste sein
        assertEquals(5, testBestellung.liefereBestellteProdukte().size());

        int anzahlStandardTueren = 0;
        int anzahlPremiumtueren = 0;

        for (Produkt produkt : testBestellung.liefereBestellteProdukte()) {
            if (produkt instanceof Standardtuer) {
                anzahlStandardTueren++;
            } else if (produkt instanceof Premiumtuer) {
                anzahlPremiumtueren++;
            }
        }

        assertEquals(2, anzahlStandardTueren);
        assertEquals(3, anzahlPremiumtueren);

        System.out.println("Test liefereBestellteProdukte erfolgreich. Produkte wurden korrekt erstellt.");
    }

    @Test //Optional
    /**
     * Test der Fehlerbehandlung (der Exceptions)
     */
    public void testeFehlerbehandlung() {

        // Optional -> wir haben Exceptions nicht zusammen gesehen
        
        
        // Negativwerte
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(-1, 5, 1); // Standardtür Negativwert
        });
        assert(exception.getMessage().contains("Ungültige Bestellmenge. Kann nicht negativ sein."));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(5, -1, 2); // Premiumtür Negativwert
        });
        assert(exception.getMessage().contains("Ungültige Bestellmenge. Kann nicht negativ sein."));
        
        
        // Beide Werte von Türen Null
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(0, 0, 3);
        });
        assert(exception.getMessage().contains("Die Bestellung muss mindestens ein Produkt enthalten."));
        
        
        // Zu hohe Bestellmenge
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(11_000, 5, 4); // Standardtür hohe Bestellmenge
        });
        assert(exception.getMessage().contains("Bestellmenge ist zu gross. Maximal 10 Tausend pro Artikel."));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(5, 12_000, 5); // Premiumtür hohe Bestellmenge
        });
        assert(exception.getMessage().contains("Bestellmenge ist zu gross. Maximal 10 Tausend pro Artikel."));

        System.out.println("Test Fehlerbehandlung (Exceptions) erfolgreich.");
    }

}
