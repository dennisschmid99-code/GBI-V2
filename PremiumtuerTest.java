import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse PremiumtuerTest
 *
 * @author Gruppe 17
 * @version 02.12.2025
 */
public class PremiumtuerTest {

    String nameTestClasse = "PremiumtuerTest";

    @BeforeEach
    public void setUp() {
        System.out.println("\nTestlauf " + nameTestClasse + " Start\n");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("\nTestlauf " + nameTestClasse + " Ende");
        System.out.println("------------------------");
    }

    @Test
    public void testePremiumtuerGetters() {
        assertEquals(4, Premiumtuer.gibHolzeinheiten());
        assertEquals(5, Premiumtuer.gibSchrauben());
        assertEquals(5, Premiumtuer.gibGlaseinheiten());
        assertEquals(1, Premiumtuer.gibFarbeinheiten());
        assertEquals(5, Premiumtuer.gibKartoneinheiten());
        assertEquals(30, Premiumtuer.gibProduktionszeit());
        System.out.println("Getters von Premiumtür erfolgreich.");
    }

    @Test
    public void testeProduktGetter() {
        Premiumtuer p = new Premiumtuer();
        assertEquals(0, p.gibAktuellerZustand());
        System.out.println("Zustand-Getter funktioniert.");
    }

    @Test
    public void testeProduktSetter() {
        Premiumtuer p = new Premiumtuer();
        p.zustandAendern(2);
        assertEquals(2, p.gibAktuellerZustand());
        System.out.println("Zustand-Setter funktioniert.");
    }
}
