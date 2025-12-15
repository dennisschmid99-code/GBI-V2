import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Ausführliche Testklasse für Holzbearbeitungs_Roboter.
 * Prüft Queue-Management, Typ-Erkennung und Zeitverhalten.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class Holzbearbeitungs_RoboterTest {
    
    private Holzbearbeitungs_Roboter roboter;

    public Holzbearbeitungs_RoboterTest() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("--> RoboterTest Start");
        roboter = new Holzbearbeitungs_Roboter();
        // Wir starten den Thread hier NICHT, weil wir die Methode produziereProdukt()
        // direkt und synchron testen wollen, um Zeiten zu messen.
    }

    @AfterEach
    public void tearDown() {
        System.out.println("<-- RoboterTest Ende\n");
    }

    @Test
    public void testName() {
        assertEquals("Holzbearbeitungsroboter", roboter.gibNamen());
    }

    @Test
    public void testWarteschlangeEinfuegen() {
        System.out.println("Test: Einfügen in Warteschlange");
        Produkt p = new Standardtuer();
        try {
            roboter.fuegeProduktHinzu(p);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception beim Hinzufügen zur Queue.");
        }
    }

    @Test
    public void testProduktionStandardTuerZeit() {
        System.out.println("Test: Zeitmessung Standardtür (Soll: ~166ms)");
        Produkt p = new Standardtuer();
        
        long start = System.currentTimeMillis();
        roboter.produziereProdukt(p); // Blockierender Aufruf
        long dauer = System.currentTimeMillis() - start;
        
        System.out.println("Dauer: " + dauer + "ms");
        // Toleranzbereich: Sollte mindestens die Schlafzeit sein
        assertTrue(dauer >= 160, "Zu schnell! Sleep nicht ausgeführt?");
    }

    @Test
    public void testProduktionPremiumTuerZeit() {
        System.out.println("Test: Zeitmessung Premiumtür (Soll: ~500ms)");
        Produkt p = new Premiumtuer();
        
        long start = System.currentTimeMillis();
        roboter.produziereProdukt(p);
        long dauer = System.currentTimeMillis() - start;
        
        System.out.println("Dauer: " + dauer + "ms");
        assertTrue(dauer >= 490, "Zu schnell! Sleep für Premiumtür nicht korrekt?");
    }
    
    @Test
    public void testUnbekanntesProdukt() {
        System.out.println("Test: Unbekanntes Produkt (Fallback)");
        // Anonyme Unterklasse
        Produkt fremd = new Produkt() {};
        
        try {
            roboter.produziereProdukt(fremd);
            // Sollte sofort fertig sein (0ms Sleep), da Typ nicht erkannt
            assertTrue(true);
        } catch (Exception e) {
            fail("Roboter ist bei unbekanntem Produkt abgestürzt.");
        }
    }
}