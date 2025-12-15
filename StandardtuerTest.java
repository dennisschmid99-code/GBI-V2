
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse StandardtuerTest
 *
 * @author GBI Gruppe 17
 * @version 08.12.2025
 */
public class StandardtuerTest {
    String nameTestClasse = "StandardtuerTest"; // Name der Testklasse

    /**
     * Konstruktor von StandardtuerTest
     */
    public StandardtuerTest() {
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
     * Testet die Getters
     */
    public void testeStandardtuerGetters() {
        assertEquals(2, Standardtuer.gibHolzeinheiten());
        assertEquals(10, Standardtuer.gibSchrauben());
        assertEquals(2, Standardtuer.gibFarbeinheiten());
        assertEquals(1, Standardtuer.gibKartoneinheiten());
        assertEquals(10, Standardtuer.gibProduktionszeit());

        System.out.println("Getters von Standardtür erfolgreich");
    }

    @Test
    /**
     * Testet die Initialisierung und den Getter von der Oberklasse Produkt
     */
    public void testeProduktGetter() {

        Standardtuer testStandardtuer = new Standardtuer();
        assertEquals(0, testStandardtuer.gibAktuellerZustand());

        System.out.println("Test Produkt Getter und Initialisierung des Zustandes funktionieren.");

    }

    @Test
    /**
     * Testet den Setter von der Oberklasse Produkt
     */
    public void testeProduktSetter() {

        Standardtuer testStandardtuer = new Standardtuer();
        testStandardtuer.zustandAendern(2);
        assertEquals(2, testStandardtuer.gibAktuellerZustand());

        System.out.println("Test Produkt Setter funktioniert.");

    }
}
