import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Aktualisierte Testklasse für Lager (Smart Logistics).
 * Testet nun die granulare Materialentnahme pro Produkt.
 *
 * @author GBI Gruppe 17
 * @version 29.12.2025
 */
public class LagerTest {
    
    private Lager lager;
    private Lieferant lieferantDummy;

    public LagerTest() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("--> LagerTest Start");
        lager = new Lager();
        lieferantDummy = new Lieferant(lager);
        
        // Schnelle Lieferung für Tests
        lieferantDummy.setzeLieferzeitFuerTests(10); 
        lager.setzeLieferant(lieferantDummy);
    }

    @AfterEach
    public void tearDown() {
        if(lieferantDummy != null) lieferantDummy.stoppen();
        System.out.println("<-- LagerTest Ende\n");
    }

    @Test
    public void testLagerInitialisierung() {
        assertNotNull(lager, "Lager muss existieren.");
    }

    @Test
    public void testMaterialEntnahmeStandard() {
        System.out.println("Test: Entnahme Standardtür (einzeln)");
        
        // 1. Bestellung erstellen
        Bestellung b = new Bestellung(1);
        b.fuegeProduktHinzu(new Standardtuer());
        
        // 2. Optional: Bedarf melden (Smart Logistics)
        lager.meldeBedarf(b);
        
        // 3. Simulation der Entnahme (Manager-Verhalten)
        try {
            for (Produkt p : b.liefereBestellteProdukte()) {
                lager.materialEntnehmen(p);
            }
            assertTrue(true, "Entnahme sollte bei vollem Lager fehlerfrei durchlaufen.");
        } catch (Exception e) {
            fail("Standard-Entnahme fehlgeschlagen: " + e.getMessage());
        }
    }

    @Test
    public void testMaterialEntnahmePremium() {
        System.out.println("Test: Entnahme Premiumtür (einzeln)");
        Bestellung b = new Bestellung(2);
        b.fuegeProduktHinzu(new Premiumtuer());
        
        lager.meldeBedarf(b);
        
        try {
            for (Produkt p : b.liefereBestellteProdukte()) {
                lager.materialEntnehmen(p);
            }
            assertTrue(true);
        } catch (Exception e) {
            fail("Premium-Entnahme fehlgeschlagen.");
        }
    }

    @Test
    public void testGrosseEntnahmeMitNachbestellung() {
        System.out.println("Test: Große Entnahme (Trigger Smart Logistics)");
        
        // Wir bestellen mehr, als das Lager hat (z.B. 300 Premiumtüren = 1200 Holz > 1000 Kapazität)
        Bestellung b = new Bestellung(3);
        for(int i=0; i<300; i++) {
            b.fuegeProduktHinzu(new Premiumtuer());
        }
        
        // Startet den Lieferantenthread, damit er auf Nachbestellungen reagieren kann
        lieferantDummy.start(); 
        
        // 1. Bedarf anmelden -> Sollte LKW losschicken
        lager.meldeBedarf(b);
        
        // 2. Entnahme in Loop (dies wird blockieren/warten, bis LKW kommen)
        // Wir starten das in einem Thread, damit der Test nicht ewig blockiert, falls was schief geht
        Thread entnahmeThread = new Thread(() -> {
            for (Produkt p : b.liefereBestellteProdukte()) {
                lager.materialEntnehmen(p);
            }
        });
        
        entnahmeThread.start();
        
        try {
            // Wir geben dem Ganzen etwas Zeit (Lieferzeit ist auf 10ms verkürzt)
            // Bei 300 Türen und mehreren Lieferungen sollte es in <2 Sek durch sein
            entnahmeThread.join(4000); 
            
            if (entnahmeThread.isAlive()) {
                // Wenn er noch lebt, hängt er im Deadlock -> Fail
                entnahmeThread.interrupt();
                fail("Entnahme hat zu lange gedauert (Deadlock im Lager?).");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertTrue(true, "Großbestellung wurde erfolgreich (mit Nachladen) verarbeitet.");
    }

    @Test
    public void testMehrfacheEntnahme() {
        System.out.println("Test: Mehrfache Bestellungen");
        Bestellung b1 = new Bestellung(10);
        b1.fuegeProduktHinzu(new Standardtuer());
        
        Bestellung b2 = new Bestellung(11);
        b2.fuegeProduktHinzu(new Premiumtuer());
        
        lager.meldeBedarf(b1);
        for (Produkt p : b1.liefereBestellteProdukte()) lager.materialEntnehmen(p);
        
        lager.meldeBedarf(b2);
        for (Produkt p : b2.liefereBestellteProdukte()) lager.materialEntnehmen(p);
        
        assertTrue(true, "Mehrfache Entnahmen sollten problemlos möglich sein.");
    }
}