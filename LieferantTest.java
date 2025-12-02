import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Lieferant.
 * Prüft, ob Materialbestellungen erfolgreich verarbeitet werden.
 *
 * @author Gruppe 17 - FS
 * @version 02.12.2025
 */
public class LieferantTest {

    private Lieferant lieferant;

    @BeforeEach
    public void setUp() {
        lieferant = new Lieferant();
    }

    @AfterEach
    public void tearDown() {
        // optional
    }

    /**
     * Test 1:
     * wareBestellen() muss immer true zurückgeben.
     */
    @Test
    public void testeWareBestellen() {

        boolean erfolg = lieferant.wareBestellen(100, 200, 50, 20, 10);

        assertTrue(erfolg, "Lieferant sollte immer erfolgreich liefern.");
    }

    /**
     * Test 2:
     * Methode muss auch bei Randwerten problemlos funktionieren.
     */
    @Test
    public void testeWareBestellenMitNullwerten() {

        boolean erfolg = lieferant.wareBestellen(0, 0, 0, 0, 0);

        assertTrue(erfolg, "Lieferant sollte auch bei 0-Einheiten erfolgreich liefern.");
    }
}
