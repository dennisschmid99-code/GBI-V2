import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse ProduktTest
 * Testet die abstrakte Klasse Produkt sowie deren Ableitungen und den Produktionsablauf.
 * Angepasst für Aufgabe 3 (ohne Montage_Roboter).
 *
 * @author GBI Gruppe 17
 * @version 21.12.2025
 */
public class ProduktTest {
    String nameTestClasse = "ProduktTest";

    // Wir brauchen Referenzen für den Test
    private Produkt produkt;
    private Roboter roboterDummy;

    public ProduktTest() {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("Testlauf " + nameTestClasse + " Start");
        
        // Da Produkt abstrakt ist, nutzen wir eine konkrete Implementierung für den Test
        produkt = new Standardtuer();
        
        // Ein Roboter zum Testen des Ablaufs
        roboterDummy = new Holzbearbeitungs_Roboter();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Testlauf " + nameTestClasse + " Ende");
        System.out.println("------------------------");
    }

    @Test
    public void testeInitialisierung() {
        assertEquals(Produkt.BESTELLT, produkt.gibAktuellerZustand(), "Neues Produkt muss Zustand BESTELLT (0) haben");
        assertFalse(produkt.istFertig(), "Neues Produkt darf nicht fertig sein");
    }

    @Test
    public void testePremiumTuerInitialisierung() {
        Produkt premium = new Premiumtuer();
        assertEquals(Produkt.BESTELLT, premium.gibAktuellerZustand());
        assertTrue(premium instanceof Premiumtuer);
    }

    @Test
    public void testeZustandManuellAendern() {
        produkt.zustandAendern(Produkt.IN_PRODUKTION);
        assertEquals(Produkt.IN_PRODUKTION, produkt.gibAktuellerZustand());
        
        produkt.zustandAendern(Produkt.FERTIG);
        assertEquals(Produkt.FERTIG, produkt.gibAktuellerZustand());
        assertTrue(produkt.istFertig());
    }

    @Test
    public void testeWorkflowStart() {
        produkt.fuegeRoboterHinzu(roboterDummy);
        produkt.naechsteProduktionsStation();
        
        assertEquals(Produkt.IN_PRODUKTION, produkt.gibAktuellerZustand(), 
            "Sobald die erste Station erreicht wird, muss Zustand IN_PRODUKTION sein.");
        assertFalse(produkt.istFertig());
    }

    @Test
    public void testeWorkflowAbschluss() {
        produkt.fuegeRoboterHinzu(roboterDummy);
        
        // 1. Aufruf: Produkt geht zum Roboter
        produkt.naechsteProduktionsStation();
        assertEquals(Produkt.IN_PRODUKTION, produkt.gibAktuellerZustand());
        
        // 2. Aufruf: Roboter ist fertig, Produkt wird weitergereicht (Liste leer)
        produkt.naechsteProduktionsStation();
        
        assertEquals(Produkt.FERTIG, produkt.gibAktuellerZustand(), 
            "Wenn keine Stationen mehr da sind, muss Zustand FERTIG sein.");
        assertTrue(produkt.istFertig());
    }
    
    @Test
    public void testeWorkflowMehrereStationen() {
        // Szenario: 2 Roboter hintereinander (Kette simulieren)
        // Da wir Montage_Roboter gelöscht haben, nutzen wir einfach
        // eine zweite Instanz des Holzbearbeitungs_Roboter. 
        // Für den Test der Liste ist der Typ egal.
        Roboter roboter2 = new Holzbearbeitungs_Roboter(); 
        
        produkt.fuegeRoboterHinzu(roboterDummy); // 1. Station
        produkt.fuegeRoboterHinzu(roboter2);     // 2. Station
        
        // Start
        produkt.naechsteProduktionsStation(); // Geht zu Station 1
        assertEquals(Produkt.IN_PRODUKTION, produkt.gibAktuellerZustand());
        assertFalse(produkt.istFertig());
        
        // Weiter
        produkt.naechsteProduktionsStation(); // Geht zu Station 2
        assertEquals(Produkt.IN_PRODUKTION, produkt.gibAktuellerZustand());
        assertFalse(produkt.istFertig());
        
        // Ende
        produkt.naechsteProduktionsStation(); // Liste leer -> Fertig
        assertTrue(produkt.istFertig());
    }
}