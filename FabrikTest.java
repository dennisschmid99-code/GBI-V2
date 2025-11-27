import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList; // Wichtig für den Test der Liste

/**
 * Die Test-Klasse FabrikTest.
 *
 * Diese Klasse testet die Kernfunktionalität der Fabrik:
 * 1. Korrekte Initialisierung (leere Liste).
 * 2. Korrektes Erzeugen und Hinzufügen von Bestellungen.
 * 3. Korrekte Vergabe der Bestellnummern (Start bei 1).
 * 4. Sicherheitsmechanismus (Ungültige Bestellungen werden abgelehnt).
 * (Kapitel 9: Fehler vermeiden)
 *
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class FabrikTest
{
    // Das "Testgerüst"
    // Wir deklarieren die Fabrik hier, damit sie in allen Testmethoden
    // verfügbar ist.
    private Fabrik fabrik1;

    /**
     * Konstruktor für die Test-Klasse FabrikTest
     */
    public FabrikTest()
    {
    }

    /**
     * Setzt das Testgerüst für jeden Test neu auf.
     * Wird vor jeder @Test-Methode ausgeführt.
     * Hier "initialisieren" (Kapitel 2.4.2) wir die Fabrik.
     */
    @BeforeEach
    public void setUp()
    {
        fabrik1 = new Fabrik();
    }

    /**
     * Gibt das Testgerüst wieder frei.
     * (Wird nach jedem Test aufgerufen)
     */
    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Test 1: Überprüft den Zustand direkt nach dem Konstruktoraufruf.
     */
    @Test
    public void testKonstruktor()
    {
        // Wir nutzen unsere neue Sondiermethode 'gibAlleBestellungen'
        // 'assertNotNull' prüft, ob die ArrayList überhaupt erzeugt wurde.
        assertNotNull(fabrik1.gibAlleBestellungen(), "Die Liste darf nicht null sein");
        
        // 'size()' (Kapitel 4.4) prüft, ob die Liste (Sammlung) leer ist.
        assertEquals(0, fabrik1.gibAlleBestellungen().size(), "Eine neue Fabrik muss leer sein");
    }

    /**
     * Test 2: Testet die "verändernde Methode" (Kapitel 2.8) 'bestellungAufgeben'.
     */
    @Test
    public void testBestellungAufgeben()
    {
        // 1. Zustand vor dem Aufruf (sollte 0 sein)
        assertEquals(0, fabrik1.gibAlleBestellungen().size());
        
        // 2. Methode aufrufen
        fabrik1.bestellungAufgeben(2, 1); // 2 Standard, 1 Premium
        
        // 3. Zustand nach dem Aufruf (sollte 1 sein)
        assertEquals(1, fabrik1.gibAlleBestellungen().size(), "Bestellung wurde nicht hinzugefügt");
        
        // 4. Noch einen Aufruf
        fabrik1.bestellungAufgeben(5, 0);
        
        // 5. Zustand prüfen (sollte 2 sein)
        assertEquals(2, fabrik1.gibAlleBestellungen().size(), "Anzahl der Bestellungen stimmt nicht");
    }

    /**
     * Test 3: Ein detaillierter Test, der prüft, ob die Bestellungen
     * in der Liste die korrekten Daten (Bestellnummer) enthalten.
     * HINWEIS: Die Fabrik startet bei Nr. 1 (siehe Konstruktor Fabrik).
     */
    @Test
    public void testBestellnummernLogik()
    {
        fabrik1.bestellungAufgeben(1, 0); // Sollte Nr. 1 bekommen
        fabrik1.bestellungAufgeben(1, 0); // Sollte Nr. 2 bekommen
        fabrik1.bestellungAufgeben(1, 0); // Sollte Nr. 3 bekommen

        // Holen der "Sammlung" (Kapitel 4)
        ArrayList<Bestellung> alle = fabrik1.gibAlleBestellungen();
        
        // ".get(index)" holt ein Element aus der Liste (Kapitel 4.7)
        // Wir prüfen, ob die Objekte die korrekten Nummern haben.
        assertEquals(1, alle.get(0).gibBestellungsNr(), "Erste ID falsch");
        assertEquals(2, alle.get(1).gibBestellungsNr(), "Zweite ID falsch");
        assertEquals(3, alle.get(2).gibBestellungsNr(), "Dritte ID falsch");
    }
    
    /**
     * Test 4: Prüft, ob ungültige Bestellungen (negative Anzahl) abgelehnt werden.
     * Da die Klasse 'Bestellung' eine Exception wirft, darf die Fabrik
     * diese fehlerhafte Bestellung NICHT speichern.
     */
    @Test
    public void testFehlerhafteBestellung()
    {
        // 1. Eine gültige Bestellung hinzufügen
        fabrik1.bestellungAufgeben(2, 2);
        assertEquals(1, fabrik1.gibAlleBestellungen().size());
        
        // 2. Eine ungültige Bestellung versuchen (-5 Türen)
        // Wir nutzen einen try-catch Block, da wir erwarten, dass es "knallt".
        try {
            fabrik1.bestellungAufgeben(-5, 0);
            // Wenn wir hier ankommen, hat die Fabrik KEINEN Fehler geworfen -> Test gescheitert!
            fail("Es wurde keine IllegalArgumentException geworfen!");
        } catch (IllegalArgumentException e) {
            // Exception wurde gefangen -> Gut!
            // Jetzt prüfen wir, ob die Liste immer noch Größe 1 hat (kein Müll gespeichert).
            assertEquals(1, fabrik1.gibAlleBestellungen().size(), "Die ungültige Bestellung wurde fälschlicherweise gespeichert!");
        }
    }

    /**
     * Test 5: Testet die Ausgabemethode.
     * HINWEIS: Dieser Test prüft nur, ob die Methode 'bestellungenAusgeben'
     * ohne Fehler (Exception) durchläuft.
     */
    @Test
    public void testKonsolenAusgabe()
    {
        fabrik1.bestellungAufgeben(3, 3);
        // "Ausgaben in Methoden" (Kapitel 2.9)
        fabrik1.bestellungenAusgeben();
        
        // Wenn die Zeile oben keinen Fehler wirft, ist der Test erfolgreich.
        assertTrue(true); 
    }
}