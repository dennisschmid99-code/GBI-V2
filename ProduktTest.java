import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Die Test-Klasse ProduktTest.
 * Überprüft die Funktionalität der Klasse Produkt.
 * * Besonderheit: Testet auch die Fehlerbehandlung (ungültige Werte).
 *
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class ProduktTest
{
    private Produkt produkt1;

    /**
     * Konstruktor für die Test-Klasse ProduktTest
     */
    public ProduktTest()
    {
    }

    /**
     * Setzt das Testgerüst für jeden Test neu auf.
     * Wird vor jeder Testmethode aufgerufen.
     */
    @BeforeEach
    public void setUp()
    {
        produkt1 = new Produkt();
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
     * Testet, ob der Initialzustand nach Erzeugung korrekt 0 ist.
     */
    @Test
    public void testInitialZustand()
    {
        // Wir erwarten 0 direkt nach Erstellung
        assertEquals(0, produkt1.aktuellerZustand(), "Ein neues Produkt muss Zustand 0 haben.");
    }

    /**
     * Testet, ob das Ändern des Zustands auf einen gültigen Wert funktioniert.
     * Testet auch den Grenzwert 0.
     */
    @Test
    public void testZustandAendernGueltig()
    {
        // Test eines normalen Wechsels
        produkt1.zustandAendern(1);
        assertEquals(1, produkt1.aktuellerZustand(), "Änderung auf 1 fehlgeschlagen");
        
        // Test eines höheren Werts
        produkt1.zustandAendern(5);
        assertEquals(5, produkt1.aktuellerZustand(), "Änderung auf 5 fehlgeschlagen");

        // Test des unteren Grenzwerts (0 ist erlaubt)
        produkt1.zustandAendern(0);
        assertEquals(0, produkt1.aktuellerZustand(), "Änderung zurück auf 0 fehlgeschlagen");
    }

    /**
     * Testet die Fehlerbehandlung: Negative Zustände sollen ignoriert werden.
     * Strategie: 
     * 1. Zustand auf einen bekannten Wert setzen (2).
     * 2. Ungültigen Wert versuchen (-1).
     * 3. Prüfen, ob der Wert immer noch 2 ist.
     */
    @Test
    public void testZustandAendernUngueltig()
    {
        // 1. Setup: Erst auf einen gültigen Wert setzen
        produkt1.zustandAendern(2);
        
        // 2. Action: Versuchen, einen ungültigen Wert zu setzen
        produkt1.zustandAendern(-1);
        
        // 3. Assertion: Der Wert muss immer noch 2 sein (die -1 muss ignoriert worden sein)
        assertEquals(2, produkt1.aktuellerZustand(), "Negative Eingabe wurde nicht ignoriert!");
    }
}