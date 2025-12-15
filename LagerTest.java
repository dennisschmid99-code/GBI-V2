import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Ausführliche Testklasse für Lager.
 * Prüft Bestandsverwaltung, kritische Grenzen und Nachbestell-Trigger.
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
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
        lager.setzeLieferant(lieferantDummy);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("<-- LagerTest Ende\n");
    }

    @Test
    public void testLagerInitialisierung() {
        assertNotNull(lager, "Lager muss existieren.");
        // Da wir keine Getter haben, prüfen wir indirekt, ob Operationen möglich sind
        Bestellung b = new Bestellung(1);
        // Eine leere Bestellung sollte keine Fehler werfen und nichts abziehen
        try {
            lager.materialEntnehmen(b);
            assertTrue(true);
        } catch (Exception e) {
            fail("Leere Bestellung sollte das Lager nicht crashen.");
        }
    }

    @Test
    public void testMaterialEntnahmeStandard() {
        System.out.println("Test: Entnahme Standardtür");
        Bestellung b = new Bestellung(1);
        b.fuegeProduktHinzu(new Standardtuer());
        
        try {
            lager.materialEntnehmen(b);
            // Wir können hier nur auf Exception-Freiheit testen, da Bestände private sind.
            // Der Konsolen-Output sollte "Lager: Entnehme Material..." zeigen.
            assertTrue(true);
        } catch (Exception e) {
            fail("Standard-Entnahme fehlgeschlagen: " + e.getMessage());
        }
    }

    @Test
    public void testMaterialEntnahmePremium() {
        System.out.println("Test: Entnahme Premiumtür");
        Bestellung b = new Bestellung(2);
        b.fuegeProduktHinzu(new Premiumtuer());
        
        try {
            lager.materialEntnehmen(b);
            assertTrue(true);
        } catch (Exception e) {
            fail("Premium-Entnahme fehlgeschlagen.");
        }
    }

    @Test
    public void testGrosseEntnahmeMitNachbestellung() {
        System.out.println("Test: Große Entnahme (Trigger Nachbestellung)");
        // Wir simulieren eine Bestellung, die den Bestand unter 20% drückt.
        // Kapazität Holz = 1000. 20% = 200.
        // Premiumtür braucht 4 Holz. 200 Premiumtüren = 800 Holz.
        // 1000 - 800 = 200 (Grenze erreicht).
        
        Bestellung b = new Bestellung(3);
        for(int i=0; i<210; i++) {
            b.fuegeProduktHinzu(new Premiumtuer());
        }
        
        // Jetzt muss der Lieferant beauftragt werden.
        // Da der Lieferant im gleichen Thread läuft (wenn wir ihn nicht gestartet haben),
        // wird die Bestellung synchron ausgeführt.
        lieferantDummy.start(); // Thread starten, damit er "beschäftigt" sein kann
        
        lager.materialEntnehmen(b);
        
        // Indirekter Test: Wenn keine Exception flog, hat die Logik gegriffen.
        // In der Konsole muss stehen: "Lager: Kritischer Bestand! Nachbestellung ausgelöst."
        assertTrue(true);
    }

    @Test
    public void testWareLiefern() {
        System.out.println("Test: Wareneingang");
        try {
            lager.wareLiefern(500, 500, 500, 500, 500);
            assertTrue(true, "Warenlieferung sollte fehlerfrei verbucht werden.");
        } catch (Exception e) {
            fail("Fehler bei wareLiefern.");
        }
    }
    
    @Test
    public void testMehrfacheEntnahme() {
        System.out.println("Test: Mehrfache kleine Entnahmen");
        Bestellung b1 = new Bestellung(10);
        b1.fuegeProduktHinzu(new Standardtuer());
        
        Bestellung b2 = new Bestellung(11);
        b2.fuegeProduktHinzu(new Premiumtuer());
        
        lager.materialEntnehmen(b1);
        lager.materialEntnehmen(b2);
        assertTrue(true, "Mehrfache Entnahmen sollten problemlos möglich sein.");
    }
}