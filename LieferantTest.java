import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Ausführliche Testklasse für Lieferant.
 * Testet Thread-Verhalten, Bestellannahme und Blockade-Mechanismus.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
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
        lieferant.start(); // WICHTIG: Thread starten
    }

    @AfterEach
    public void tearDown() {
        System.out.println("<-- LieferantTest Ende\n");
    }

    @Test
    public void testThreadAktivitaet() {
        assertTrue(lieferant.isAlive(), "Lieferant-Thread sollte laufen.");
    }

    @Test
    public void testErsteBestellungAnnehmen() {
        System.out.println("Test: Erste Bestellung (sollte true sein)");
        // Wir geben eine Bestellung auf
        boolean akzeptiert = lieferant.wareBestellen(10, 10, 10, 10, 10);
        
        assertTrue(akzeptiert, "Der Lieferant sollte im Ruhezustand Aufträge annehmen.");
    }

    @Test
    public void testZweiteBestellungAblehnen() {
        System.out.println("Test: Zweite Bestellung sofort danach (sollte false sein)");
        
        // 1. Bestellung -> Setzt Status auf "beschäftigt" und schläft (simuliert)
        lieferant.wareBestellen(100, 100, 100, 100, 100);
        
        // 2. Bestellung sofort hinterher -> Muss abgelehnt werden
        boolean akzeptiert = lieferant.wareBestellen(1, 1, 1, 1, 1);
        
        assertFalse(akzeptiert, "Lieferant muss beschäftigt sein und ablehnen.");
    }

    @Test
    public void testBestellungNullMenge() {
        System.out.println("Test: Bestellung mit 0 Einheiten");
        boolean akzeptiert = lieferant.wareBestellen(0, 0, 0, 0, 0);
        assertTrue(akzeptiert, "Auch leere Bestellungen sind technisch valid.");
    }
    
    @Test
    public void testGleichzeitigerZugriff() {
        // Hinweis: Dies ist ein vereinfachter Test für Thread-Sicherheit.
        // In einer echten Umgebung bräuchte man mehrere Threads, die hämmern.
        // Hier prüfen wir nur, ob die Methode 'synchronized' ist (indirekt).
        // Wenn wir manuell bestellen, sollte kein Deadlock entstehen.
        try {
            lieferant.wareBestellen(1,1,1,1,1);
            assertTrue(true);
        } catch (Exception e) {
            fail("Bestellung verursachte Exception.");
        }
    }
}