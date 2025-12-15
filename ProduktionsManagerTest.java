import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für Produktions_Manager.
 * Prüft den Ablauf mit dem Holzroboter und die asynchrone Verarbeitung.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class ProduktionsManagerTest {
    
    private Lager lager;
    private Lieferant lieferant;
    private Produktions_Manager manager;

    public ProduktionsManagerTest() {
    }

    @BeforeEach
    public void setUp() {
        // Infrastruktur aufbauen
        lager = new Lager();
        lieferant = new Lieferant(lager);
        lager.setzeLieferant(lieferant);
        
        // Manager erstellen (startet automatisch den Holzroboter)
        manager = new Produktions_Manager(lager);
    }

    @AfterEach
    public void tearDown() {
        manager = null; // Referenz löschen
    }

    @Test
    public void testInitialisierung() {
        assertNotNull(manager, "Manager sollte existieren.");
    }

    @Test
    public void testThreadStart() {
        manager.start();
        assertTrue(manager.isAlive(), "Manager-Thread sollte laufen.");
    }

    @Test
    public void testBestellungHinzufuegen() {
        Bestellung b = new Bestellung(1);
        try {
            manager.fuegeZuVerarbeitendeBestellungenHinzu(b);
            assertTrue(true);
        } catch (Exception e) {
            fail("Fehler beim Hinzufügen: " + e.getMessage());
        }
    }

    @Test
    public void testVerarbeitungEinerBestellung() {
        System.out.println("Test: Eine Standardtür produzieren");
        manager.start();
        
        Bestellung b = new Bestellung(10);
        b.fuegeProduktHinzu(new Standardtuer()); // Dauer ~166ms
        
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b);
        
        // Wir warten 2 Sekunden (Puffer für Thread-Start + Produktion)
        pause(2000);
        
        assertTrue(b.istAbgeschlossen(), "Bestellung sollte nach 2s fertig sein (Check Holzroboter-Code!).");
    }

    @Test
    public void testVerarbeitungMehrereProdukte() {
        System.out.println("Test: Mehrere Produkte (Gemischt)");
        manager.start();
        
        Bestellung b = new Bestellung(20);
        b.fuegeProduktHinzu(new Premiumtuer()); // ~500ms
        b.fuegeProduktHinzu(new Standardtuer()); // ~166ms
        
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b);
        
        pause(3000); // 3 Sekunden Puffer
        
        assertTrue(b.istAbgeschlossen(), "Gemischte Bestellung sollte fertig sein.");
    }
    
    @Test
    public void testVerarbeitungMehrereBestellungen() {
        System.out.println("Test: Zwei Bestellungen hintereinander");
        manager.start();
        
        Bestellung b1 = new Bestellung(31);
        b1.fuegeProduktHinzu(new Standardtuer());
        
        Bestellung b2 = new Bestellung(32);
        b2.fuegeProduktHinzu(new Premiumtuer());
        
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b1);
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b2);
        
        pause(4000); // 4 Sekunden Puffer
        
        assertTrue(b1.istAbgeschlossen(), "Bestellung 1 nicht fertig.");
        assertTrue(b2.istAbgeschlossen(), "Bestellung 2 nicht fertig.");
    }
    
    @Test
    public void testLeerlauf() {
        System.out.println("Test: Leerlauf-Verhalten");
        manager.start();
        pause(500); // Kurz leerlaufen lassen
        
        Bestellung b = new Bestellung(99);
        b.fuegeProduktHinzu(new Standardtuer());
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b);
        
        pause(2000);
        assertTrue(b.istAbgeschlossen(), "Sollte auch nach Leerlauf arbeiten.");
    }
    
    // Hilfsmethode zum Warten
    private void pause(int ms) {
        try { 
            Thread.sleep(ms); 
        } catch (Exception e) {}
    }
}