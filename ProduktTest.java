
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse ProduktTest
 *
 * @author Alex Marchese
 * @version 08.12.2025
 */
public class ProduktTest {
    String nameTestClasse = "ProduktTest"; // Name der Testklasse

    /**
     * Default constructor for test class ProduktTest
     */
    public ProduktTest() {
    }

    /**
     * Anweisungen vor jedem Testlauf
     */
    @BeforeEach
    public void setUp() {
        System.out.println("Testlauf " + nameTestClasse + " Start");
        System.out.println();
    }

    /**
     * Anweisungen nach jedem Testlauf
     */
    @AfterEach
    public void tearDown() {
        System.out.println();
        System.out.println("Testlauf " + nameTestClasse + " Ende");
        System.out.println("------------------------");
    }

    @Test
    /**
     * Testet die Initialisierung und den Getter
     */
    public void testeProduktGetter() {

        Produkt testProdukt = new Produkt();
        assertEquals(0, testProdukt.gibAktuellerZustand());

        System.out.println("Test Produkt Getter und Initialisierung des Zustandes funktionieren.");

    }

    @Test
    /**
     * Testet den Setter
     */
    public void testeProduktSetter() {

        Produkt testProdukt = new Produkt();
        testProdukt.zustandAendern(2);
        assertEquals(2, testProdukt.gibAktuellerZustand());

        System.out.println("Test Produkt Setter funktioniert.");

    }
}
