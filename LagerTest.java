
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows; //optional

/**
 * Klasse LagerTest
 *
 * @author Alex Marchese
 * @version 08.12.2025
 */
public class LagerTest {
    String nameTestClasse = "LagerTest"; // Name der Testklasse

    /**
     * Konstruktor von LagerTest
     */
    public LagerTest() {
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
     * Testet gibBeschaffungsZeit()
     */
    public void testeGibBeschaffungsZeit() {

        
        // Instanzierung eines Lagers und einer Bestellung
        Lager testLager = new Lager();
        Bestellung testBestellung1 = new Bestellung(5, 7, 2); // Genügende Materialien auf Lager
        Bestellung testBestellung2 = new Bestellung(5, 21, 3); // Ungenügende Materialien auf Lager

        assertEquals(0, testLager.gibBeschaffungsZeit(testBestellung1));
        assertEquals(2, testLager.gibBeschaffungsZeit(testBestellung2));

        System.out.println(
                "Test Methode gibBeschaffungsZeit erfolgreich.");

    }

    

}
