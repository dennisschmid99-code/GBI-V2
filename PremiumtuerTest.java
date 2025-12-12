
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse PremiumtuerTest
 *
 * @author Alex Marchese
 * @version 08.12.2025
 */
public class PremiumtuerTest {
    String nameTestClasse = "PremiumtuerTest"; // Name der Testklasse

    /**
     * Konstruktor von PremiumtuerTest
     */
    public PremiumtuerTest() {
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
    public void testePremiumtuerGetters() {
        assertEquals(4, Premiumtuer.gibHolzeinheiten());
        assertEquals(5, Premiumtuer.gibSchrauben());
        assertEquals(5, Premiumtuer.gibGlaseinheiten());
        assertEquals(1, Premiumtuer.gibFarbeinheiten());
        assertEquals(5, Premiumtuer.gibKartoneinheiten());
        assertEquals(30, Premiumtuer.gibProduktionszeit());

        System.out.println("Getters von Premiumtür erfolgreich");
    }

    @Test
    /**
     * Testet die Initialisierung und den Getter von der Oberklasse Produkt
     */
    public void testeProduktGetter() {

        Premiumtuer testPremiumtuer = new Premiumtuer();
        assertEquals(0, testPremiumtuer.gibAktuellerZustand());

        System.out.println("Test Produkt Getter und Initialisierung des Zustandes funktionieren.");

    }

    @Test
    /**
     * Testet den Setter von der Oberklasse Produkt
     */
    public void testeProduktSetter() {

        Premiumtuer testPremiumtuer = new Premiumtuer();
        testPremiumtuer.zustandAendern(2);
        assertEquals(2, testPremiumtuer.gibAktuellerZustand());

        System.out.println("Test Produkt Setter funktioniert.");

    }

}
