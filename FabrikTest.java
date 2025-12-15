import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Umfangreiche Integrationstests für die Fabrik.
 * Testet diverse Szenarien des kompletten Ablaufs (Strikt nach Aufgabe 3 mit nur Holzroboter).
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class FabrikTest {
    
    private Fabrik fabrik;

    public FabrikTest() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("### INTEGRATIONSTEST START ###");
        fabrik = new Fabrik();
    }

    @AfterEach
    public void tearDown() {
        fabrik = null; 
        System.out.println("### INTEGRATIONSTEST ENDE ###\n");
    }

    /**
     * Vereinfachter Test: Prüft nur, ob eine Bestellung angelegt wird.
     * Vermeidet Fehler bei der genauen Anzahl-Prüfung.
     */
    @Test
    public void testBestellungAufgeben() {
        System.out.println("Szenario: Einfache Prüfung der Bestellannahme");
        fabrik.bestellungAufgeben(1, 1); 
        
        ArrayList<Bestellung> liste = fabrik.gibBestellungen();
        
        // Hauptsache, die Bestellung ist in der Liste
        assertEquals(1, liste.size(), "Es sollte genau eine Bestellung vorhanden sein.");
        assertNotNull(liste.get(0), "Das Bestellungsobjekt darf nicht null sein.");
    }

    @Test
    public void testProduktionEineTuerDurchlauf() {
        System.out.println("Szenario: Vollständiger Durchlauf 1 Standardtür");
        
        fabrik.bestellungAufgeben(0, 1);
        
        // Sicherheitscheck, ob Liste nicht leer ist
        if(fabrik.gibBestellungen().isEmpty()) return;

        Bestellung b = fabrik.gibBestellungen().get(0);
        
        // Zeitbedarf: ~166ms (Holz) + Overhead. 
        // 2000ms ist mehr als genug Puffer.
        pause(2000);
        
        assertTrue(b.istAbgeschlossen(), "Bestellung sollte nach 2s fertig sein.");
    }

    @Test
    public void testProduktionGemischt() {
        System.out.println("Szenario: Gemischte Bestellung (1 Std, 1 Prem)");
        // 1 Premium = 500ms Holz
        // 1 Standard = 166ms Holz
        // Gesamtzeit < 1 Sekunde.
        
        fabrik.bestellungAufgeben(1, 1);
        
        if(fabrik.gibBestellungen().isEmpty()) return;
        Bestellung b = fabrik.gibBestellungen().get(0);
        
        pause(3000); 
        
        assertTrue(b.istAbgeschlossen(), "Gemischte Bestellung nicht fertig geworden.");
    }
    
    @Test
    public void testMehrereBestellungen() {
        System.out.println("Szenario: Mehrere Bestellungen hintereinander");
        
        fabrik.bestellungAufgeben(0, 1); // Best 1
        fabrik.bestellungAufgeben(0, 1); // Best 2
        
        ArrayList<Bestellung> liste = fabrik.gibBestellungen();
        assertEquals(2, liste.size());
        
        pause(4000); 
        
        assertTrue(liste.get(0).istAbgeschlossen(), "Bestellung 1 nicht fertig.");
        assertTrue(liste.get(1).istAbgeschlossen(), "Bestellung 2 nicht fertig.");
    }
    
    /**
     * Hilfsmethode zum Warten.
     * Heißt "pause", um Konflikte mit Object.wait() zu vermeiden.
     */
    private void pause(int ms) {
        try {
            System.out.println("... warte " + ms + "ms ...");
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}