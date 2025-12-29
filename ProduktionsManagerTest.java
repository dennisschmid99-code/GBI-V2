import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * UPDATED TEST: Produktions_Manager
 * Deckt nun auch Pause- und Storno-Logik ab (Edge Cases).
 */
public class ProduktionsManagerTest {
    
    private Lager lager;
    private Lieferant lieferant;
    private Produktions_Manager manager;

    public ProduktionsManagerTest() {
    }

    @BeforeEach
    public void setUp() {
        lager = new Lager();
        lieferant = new Lieferant(lager);
        lager.setzeLieferant(lieferant);
        // Wir nutzen den echten Manager mit echten Threads
        manager = new Produktions_Manager(lager);
    }

    @AfterEach
    public void tearDown() {
        if(manager != null) manager.setzePausiert(false); // Cleanup
        manager = null;
    }

    @Test
    public void testInitialisierung() {
        assertNotNull(manager);
    }

    @Test
    public void testVerarbeitungEinerBestellung() {
        manager.start();
        Bestellung b = new Bestellung(1);
        b.fuegeProduktHinzu(new Standardtuer());
        
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b);
        pause(2000);
        
        assertTrue(b.istAbgeschlossen(), "Basis-Funktion defekt.");
    }

    /**
     * EDGE CASE 1: Pause-Funktion.
     * Der Manager darf NICHTS tun, solange pausiert ist.
     */
    @Test
    public void testPausierenUndFortsetzen() {
        System.out.println("Test: Pause-Logik");
        manager.start();
        
        // 1. Pausieren BEVOR Bestellung kommt
        manager.setzePausiert(true);
        
        Bestellung b = new Bestellung(99);
        b.fuegeProduktHinzu(new Standardtuer());
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b);
        
        // 2. Warten - Normalerweise wäre die Tür in 166ms fertig. Wir warten 1s.
        pause(1000);
        
        // ASSERT: Bestellung darf NICHT fertig sein
        assertFalse(b.istAbgeschlossen(), "Manager hat trotz Pause gearbeitet!");
        
        // 3. Fortsetzen
        manager.setzePausiert(false);
        pause(2000); // Zeit zum Abarbeiten geben
        
        // ASSERT: Jetzt muss sie fertig sein
        assertTrue(b.istAbgeschlossen(), "Manager ist nach Pause nicht wieder angelaufen.");
    }

    /**
     * EDGE CASE 2: Stornierung (Abbruch).
     * Laufende und wartende Bestellungen müssen gestoppt werden.
     */
    @Test
    public void testStornierung() {
        System.out.println("Test: Storno-Logik");
        manager.start();
        
        // Wir überfluten den Manager, damit eine Warteschlange entsteht
        Bestellung b1 = new Bestellung(101); // Wird gerade bearbeitet
        b1.fuegeProduktHinzu(new Premiumtuer()); // Dauert 500ms
        
        Bestellung b2 = new Bestellung(102); // Wartet in Queue
        b2.fuegeProduktHinzu(new Standardtuer());
        
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b1);
        manager.fuegeZuVerarbeitendeBestellungenHinzu(b2);
        
        // Kurz anlaufen lassen (b1 startet, b2 wartet)
        pause(100);
        
        // BAM! Alles abbrechen.
        manager.storniereWartendeBestellungen();
        // Wir müssen b1 manuell flaggen, wie es die Fabrik tun würde
        b1.stornieren(); 
        
        pause(2000);
        
        // ASSERT:
        // b2 war in der Queue -> muss aus Queue entfernt/ignoriert worden sein
        assertFalse(b2.istAbgeschlossen(), "Wartende Bestellung b2 wurde trotz Storno bearbeitet!");
        assertTrue(b2.istStorniert(), "b2 sollte Status storniert haben.");
        
        // b1 lief schon -> sollte abgebrochen sein (nicht fertig)
        assertTrue(b1.istStorniert());
        // Hinweis: Ob b1 "istAbgeschlossen" true/false ist, hängt von der Implementierung ab.
        // Unsere Logik setzt "alleProdukteProduziert" nur, wenn NICHT storniert.
        assertFalse(b1.istAbgeschlossen(), "Laufende Bestellung b1 wurde fälschlicherweise als 'Erfolg' markiert.");
    }
    
    private void pause(int ms) {
        try { Thread.sleep(ms); } catch (Exception e) {}
    }
}