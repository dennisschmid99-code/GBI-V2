import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Erweiterte Integrationstests für die Fabrik.
 * Prüft den Produktionsablauf, asynchrone Lieferzeiten und das Verhalten bei leerem Lager.
 *
 * @author GBI Gruppe 17
 * @version 16.12.2025
 */
public class FabrikTest {
    
    private Fabrik fabrik;

    public FabrikTest() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("### TEST START ###");
        fabrik = new Fabrik();
        
        // WICHTIG: Wir setzen die Lieferzeit für Tests auf 2 Sekunden (2000ms),
        // damit wir das Zeitverhalten prüfen können, ohne 48 Sekunden zu warten.
        if (fabrik.gibLieferant() != null) {
            fabrik.gibLieferant().setzeLieferzeitFuerTests(2000);
        }
    }

    @AfterEach
    public void tearDown() {
        // Threads sauber beenden
        if (fabrik.gibLieferant() != null) {
            fabrik.gibLieferant().stoppen();
        }
        fabrik = null; 
        System.out.println("### TEST ENDE ###\n");
    }

    @Test
    public void testStandardProduktionsDurchlauf() {
        System.out.println("Szenario: Normale Bestellung (Lager gefüllt)");
        
        fabrik.bestellungAufgeben(0, 1); // 1 Standardtür
        
        // Wir warten 2.5 Sekunden (ausreichend für Produktion, da Lager voll ist)
        pause(2500);
        
        ArrayList<Bestellung> liste = fabrik.gibBestellungen();
        assertEquals(1, liste.size());
        assertTrue(liste.get(0).istAbgeschlossen(), "Bestellung sollte fertig sein.");
    }

    /**
     * Dieser Test prüft den kritischen Edge Case:
     * 1. Lager wird komplett geleert.
     * 2. Neue Bestellung kommt rein.
     * 3. Manager muss warten (Produktion stoppt).
     * 4. Lieferung kommt an.
     * 5. Produktion läuft weiter.
     */
    @Test
    public void testWartezeitBeiLeeremLager() {
        System.out.println("Szenario: Lager leer -> Warten auf Lieferant -> Weiterproduzieren");
        
        // 1. Lager leeren
        // Wir bestellen 250 Premiumtüren. Jede braucht 4 Holz. 250 * 4 = 1000 (Max Kapazität).
        System.out.println("-> Entleere Lager...");
        fabrik.bestellungAufgeben(250, 0);
        
        // Kurz warten, damit der Manager das Lager leer räumen kann
        pause(2000);
        
        // 2. Neue Bestellung aufgeben, die blockieren muss (da 0 Holz da ist)
        System.out.println("-> Gebe blockierende Bestellung auf...");
        fabrik.bestellungAufgeben(0, 1); // Braucht 2 Holz
        
        // 3. Prüfung der Blockade
        // Die Lieferzeit ist auf 2000ms eingestellt.
        // Wir prüfen nach 1000ms: Die Bestellung darf noch NICHT fertig sein.
        pause(1000);
        
        ArrayList<Bestellung> liste = fabrik.gibBestellungen();
        Bestellung letzteBestellung = liste.get(liste.size() - 1);
        
        assertFalse(letzteBestellung.istAbgeschlossen(), 
            "FEHLER: Bestellung ist zu früh fertig! Manager hat nicht auf Lieferung gewartet.");
            
        // 4. Prüfung nach Lieferung
        // Wir warten den Rest der Zeit ab (insgesamt > 2000ms)
        pause(2500); 
        
        assertTrue(letzteBestellung.istAbgeschlossen(), 
            "FEHLER: Bestellung wurde nach Lieferung nicht fertiggestellt (Deadlock?).");
            
        System.out.println("-> Erfolg: System hat korrekt gewartet und fortgesetzt.");
    }

    /**
     * Hilfsmethode zum Warten.
     */
    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}