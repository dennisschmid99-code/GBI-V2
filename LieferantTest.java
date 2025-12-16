import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Angepasste Testklasse für den asynchronen Lieferanten.
 * Prüft nun explizit das Warteschlangen-Verhalten (Queue).
 *
 * @author GBI Gruppe 17
 * @version 16.12.2025
 */
public class LieferantTest {
    
    private Lager lagerDummy;
    private Lieferant lieferant;

    public LieferantTest() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("--> LieferantTest Start");
        lagerDummy = new Lager();
        lieferant = new Lieferant(lagerDummy);
        
        // WICHTIG: Für Tests die Lieferzeit drastisch verkürzen (10ms),
        // damit wir Resultate sehen können.
        lieferant.setzeLieferzeitFuerTests(10);
        
        lieferant.start(); // Thread starten
    }

    @AfterEach
    public void tearDown() {
        if(lieferant != null) lieferant.stoppen();
        System.out.println("<-- LieferantTest Ende\n");
    }

    @Test
    public void testThreadAktivitaet() {
        assertTrue(lieferant.isAlive(), "Lieferant-Thread sollte laufen.");
    }

    @Test
    public void testErsteBestellungAnnehmen() {
        System.out.println("Test: Erste Bestellung (Queue leer)");
        boolean akzeptiert = lieferant.wareBestellen(10, 10, 10, 10, 10);
        assertTrue(akzeptiert, "Lieferant muss erste Bestellung annehmen.");
    }

    /**
     * WICHTIGE ÄNDERUNG: Früher musste dies false sein.
     * Jetzt muss es TRUE sein, da wir eine Queue haben.
     */
    @Test
    public void testZweiteBestellungInQueue() {
        System.out.println("Test: Zweite Bestellung (Queue-Verhalten)");
        
        // 1. Bestellung feuern
        lieferant.wareBestellen(100, 100, 100, 100, 100);
        
        // 2. Bestellung sofort hinterher
        boolean akzeptiert = lieferant.wareBestellen(5, 5, 5, 5, 5);
        
        assertTrue(akzeptiert, 
            "Dank der Queue muss der Lieferant auch weitere Bestellungen annehmen (return true)!");
    }

    @Test
    public void testBestellungNullMenge() {
        boolean akzeptiert = lieferant.wareBestellen(0, 0, 0, 0, 0);
        assertTrue(akzeptiert, "Leere Bestellung ist technisch erlaubt (Queue nimmt alles).");
    }
}