import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse StandardtuerTest
 *
 * @author Gruppe 17 - FS
 * @version 02.12.2025
 */
public class StandardtuerTest {

    String nameTestClasse = "StandardtuerTest";

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
    public void testeStandardtuerGetters() {
        assertEquals(2, Standardtuer.gibHolzeinheiten());
        assertEquals(10, Standardtuer.gibSchrauben());
        assertEquals(2, Standardtuer.gibFarbeinheiten());
        assertEquals(1, Standardtuer.gibKartoneinheiten());
        assertEquals(10, Standardtuer.gibProduktionszeit());
        System.out.println("Getters von Standardtür erfolgreich.");
    }

    @Test
    public void testeProduktGetter() {
        Standardtuer t = new Standardtuer();
        assertEquals(0, t.gibAktuellerZustand());
        System.out.println("Zustand (Getter) erfolgreich.");
    }

    @Test
    public void testeProduktSetter() {
        Standardtuer t = new Standardtuer();
        t.zustandAendern(2);
        assertEquals(2, t.gibAktuellerZustand());
        System.out.println("Setter erfolgreich.");
    }
}
