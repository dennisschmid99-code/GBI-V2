import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Klasse BestellungTest
 * Umfassende Tests für die Klasse Bestellung inkl. neuer Funktionen aus Aufgabe 3.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class BestellungTest {
    String nameTestClasse = "BestellungTest";

    public BestellungTest() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("Testlauf " + nameTestClasse + " Start");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Testlauf " + nameTestClasse + " Ende");
        System.out.println("------------------------");
    }

    @Test
    public void testeBestellungInitialisierung() {
        // Testet den Konstruktor aus Aufgabe 1/2
        Bestellung testBestellung = new Bestellung(5, 7, 2);

        assertEquals(5, testBestellung.gibAnzahlStandardTueren());
        assertEquals(7, testBestellung.gibAnzahlPremiumTueren());
        assertEquals(2, testBestellung.gibBestellungsNr());
        assertEquals(false, testBestellung.gibBestellBestaetigung());
        assertEquals(-1, testBestellung.gibBeschaffungsZeit());
        assertFalse(testBestellung.istAbgeschlossen(), "Sollte initial nicht abgeschlossen sein.");

        // Prüfen, ob die interne Liste korrekt gefüllt wurde (5+7 = 12 Produkte)
        assertEquals(12, testBestellung.liefereBestellteProdukte().size());
    }
    
    @Test
    public void testeBestellungKonstruktorNeu() {
        // Testet den neuen Konstruktor für Aufgabe 3 (nur ID)
        Bestellung b = new Bestellung(100);
        assertEquals(100, b.gibBestellungsNr());
        assertNotNull(b.liefereBestellteProdukte());
        assertEquals(0, b.liefereBestellteProdukte().size());
        assertFalse(b.istAbgeschlossen());
    }

    @Test
    public void testeProduktHinzufuegen() {
        Bestellung b = new Bestellung(101);
        b.fuegeProduktHinzu(new Standardtuer());
        b.fuegeProduktHinzu(new Premiumtuer());
        
        ArrayList<Produkt> produkte = b.liefereBestellteProdukte();
        assertEquals(2, produkte.size());
        assertTrue(produkte.get(0) instanceof Standardtuer);
        assertTrue(produkte.get(1) instanceof Premiumtuer);
    }

    @Test
    public void testeStatusProduktion() {
        Bestellung b = new Bestellung(102);
        assertFalse(b.istAbgeschlossen());
        
        b.setzeAlleProdukteProduziert();
        assertTrue(b.istAbgeschlossen(), "Sollte nach Setzen auf true abgeschlossen sein.");
    }

    @Test
    public void testeBestellungBestaetigen() {
        Bestellung testBestellung = new Bestellung(5, 7, 2);
        assertEquals(false, testBestellung.gibBestellBestaetigung());
        testBestellung.bestellungBestaetigen();
        assertEquals(true, testBestellung.gibBestellBestaetigung());
    }

    @Test
    public void testeSetzeBeschaffungsZeit() {
        Bestellung testBestellung = new Bestellung(5, 7, 2);
        assertEquals(-1, testBestellung.gibBeschaffungsZeit());
        testBestellung.setzeBeschaffungsZeit(2);
        assertEquals(2, testBestellung.gibBeschaffungsZeit());
    }
    
    @Test
    public void testeSetzeLieferzeit() {
        Bestellung testBestellung = new Bestellung(5, 7, 2);
        assertEquals(-1, testBestellung.gibLieferzeit());
        testBestellung.setzeLieferzeit(2.5f);
        assertEquals(2.5f, testBestellung.gibLieferzeit());
    }

    @Test
    public void testeFehlerbehandlung() {
        // Negativwerte
        assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(-1, 5, 1);
        });

        // Leere Bestellung
        assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(0, 0, 3);
        });

        // Zu hohe Menge
        assertThrows(IllegalArgumentException.class, () -> {
            new Bestellung(11_000, 5, 4);
        });
    }
}