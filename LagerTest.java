import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Aktualisierte Testklasse für Lager (Smart Logistics).
 * FIX: Lieferant wird nun zentral gestartet, damit alle Tests (inkl. Konfig-Änderungen) funktionieren.
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
        
        // Schnelle Lieferung für Tests (10ms)
        lieferantDummy.setzeLieferzeitFuerTests(10); 
        lager.setzeLieferant(lieferantDummy);
        
        // FIX: Thread hier zentral starten, damit er für ALLE Tests bereit ist
        lieferantDummy.start();
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
        
        Bestellung b = new Bestellung(1);
        b.fuegeProduktHinzu(new Standardtuer());
        
        lager.meldeBedarf(b);
        
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
        
        // Wir bestellen mehr, als das Lager hat (z.B. 300 Premiumtüren)
        Bestellung b = new Bestellung(3);
        for(int i=0; i<300; i++) {
            b.fuegeProduktHinzu(new Premiumtuer());
        }
        
        // Hinweis: Lieferant läuft bereits durch setUp()!
        
        // 1. Bedarf anmelden -> Sollte LKW losschicken
        lager.meldeBedarf(b);
        
        // 2. Entnahme in separatem Thread simulieren (da sie warten muss)
        Thread entnahmeThread = new Thread(() -> {
            for (Produkt p : b.liefereBestellteProdukte()) {
                lager.materialEntnehmen(p);
            }
        });
        
        entnahmeThread.start();
        
        try {
            // Wir geben dem Ganzen etwas Zeit (Lieferzeit ist auf 10ms verkürzt)
            entnahmeThread.join(4000); 
            
            if (entnahmeThread.isAlive()) {
                entnahmeThread.interrupt();
                fail("Entnahme hat zu lange gedauert (Deadlock im Lager?).");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertTrue(true, "Großbestellung wurde erfolgreich verarbeitet.");
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

    /**
     * EDGE CASE: Konfigurierbare Schwellwerte.
     * Prüft, ob eine Änderung des Min-Bestands sofort eine Bestellung auslöst.
     */
    @Test
    public void testDynamischeSchwellwerte() {
        System.out.println("Test: Dynamische Grenzwerte (GUI Spinner)");
        
        // Setup: Lager ist voll (1000 Holz)
        // Wir entnehmen etwas, um Platz zu schaffen (Bestand = 998)
        Bestellung b = new Bestellung(1);
        b.fuegeProduktHinzu(new Standardtuer()); // Braucht 2 Holz
        lager.materialEntnehmen(b.liefereBestellteProdukte().get(0));
        
        // Check: Bestand sollte gesunken sein
        assertTrue(lager.gibAnzahlHolz() < 1000, "Bestand hätte sinken müssen.");
        
        // JETZT: Wir setzen den Min-Bestand extrem hoch (999)
        // Da 998 < 999, muss SOFORT nachbestellt werden.
        lager.setzeMinBestand("Holz", 999);
        
        // Da der Lieferant läuft (setUp), sollte er kurz darauf liefern.
        try { Thread.sleep(200); } catch (Exception e) {}
        
        // Wenn nachbestellt wurde, ist das Lager wieder voll.
        assertTrue(lager.gibAnzahlHolz() > 998, 
            "Änderung des Min-Bestands hat keine automatische Nachbestellung ausgelöst!");
            
        System.out.println("-> Dynamische Schwelle erfolgreich getestet.");
    }
}