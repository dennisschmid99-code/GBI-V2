import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse ProduktTest
 *
 * @version 02.12.2025
 * @author Gruppe 17
 */
public class ProduktTest {

    String nameTestClasse = "ProduktTest";

    @BeforeEach
    public void setUp() {
        System.out.println("Testlauf " + nameTestClasse + " Start\n");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("\nTestlauf " + nameTestClasse + " Ende");
        System.out.println("------------------------");
    }

    @Test
    public void testeProduktGetter() {

        Produkt testProdukt = new Produkt();
        assertEquals(0, testProdukt.gibAktuellerZustand());

        System.out.println("Initialisierung korrekt: Zustand = 0.");
    }

    @Test
    public void testeProduktSetter() {

        Produkt testProdukt = new Produkt();
        testProdukt.zustandAendern(2);

        assertEquals(2, testProdukt.gibAktuellerZustand());

        System.out.println("Setter funktioniert: Zustand wurde auf 2 gesetzt.");
    }
}
