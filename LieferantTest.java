
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows; //optional

/**
 * Klasse LieferantTest
 *
 * @author Alex Marchese
 * @version 08.12.2025
 */
public class LieferantTest {
    String nameTestClasse = "LieferantTest"; // Name der Testklasse

    /**
     * Konstruktor von LieferantTest
     */
    public LieferantTest() {
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
     * Testet wareBestellen()
     */
    public void testeWareBestellen() {

        
        // Instanzierung eines Lieferanten
        Lieferant testLieferant = new Lieferant();
        assertTrue(testLieferant.wareBestellen(2, 4, 50, 3, 2));
        
        
        System.out.println(
                "Test Methode wareBestellen erfolgreich.");

    }

    

}
