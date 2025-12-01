
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Klasse FabrikTest
 *
 * @author Alex Marchese
 * @version 26.11.2025
 */
public class FabrikTest {
    String nameTestClasse = "FabrikTest"; // Name der Testklasse

    /**
     * Konstruktor von FabrikTest
     */
    public FabrikTest() {
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
     * Testet die Aufgabe einer Bestellung mit zugelassenen Werten
     */
    public void testeBestellung() {

        // Instanzierung einer Fabrik
        Fabrik testFabrik = new Fabrik();
        testFabrik.bestellungAufgeben(2, 5);

        // Überprüfung, dass 3 gültige Bestellungen getätigt wurden
        assertEquals(1, testFabrik.gibBestellungen().size());

        Bestellung ersteBestellung = testFabrik.gibBestellungen().get(0);

        // Überprüfung, dass die Arraylist die Produkte enthält. Es müssen genau 7 sein
        assertEquals(7, ersteBestellung.gibBestellteProdukte().size());

        // Kontrolle, dass es genau 2 Standartüren und 5 Premiumtüren sind
        int anzahlStandardTueren = 0;
        int anzahlPremiumtueren = 0;

        for (Produkt produkt : ersteBestellung.gibBestellteProdukte()) {
            if (produkt instanceof Standardtuer) {
                anzahlStandardTueren++;
            } else if (produkt instanceof Premiumtuer) {
                anzahlPremiumtueren++;
            }
        }

        assertEquals(2, anzahlStandardTueren);
        assertEquals(5, anzahlPremiumtueren);

         // Beschaffungszeit: Lager ist zu Beginn voll -> 0 Tage
        assertEquals(0, ersteBestellung.gibBeschaffungsZeit());

        // Bestellbestätigung: Bestellung muss bestätigt sein
        assertTrue(ersteBestellung.gibBestellBestaetigung());

        // Lieferzeit: muss mindestens grösser als nur Standardlieferzeit (1 Tag) sein
        assertTrue(ersteBestellung.gibLieferzeit() > 1.0f);
        
        System.out.println(
                "Test Bestellung mit erlaubten Werten. Produkte wurden bestellt");

    }

    @Test
    /**
     * Testet, dass bei der Eingabe von unzulässigen Werten, keine Bestellung
     * aufgegeben wird
     */
    public void testeBestellungFalsch() {

        // Instanzierung einer Fabrik
        Fabrik testFabrik = new Fabrik();
        // Beide Werte von Türen Null
        testFabrik.bestellungAufgeben(0, 0);
        // Zu hohe Bestellmenge
        testFabrik.bestellungAufgeben(15_000, 0);
        // Ein Negativwert
        testFabrik.bestellungAufgeben(-5, 0);
        // Zweiter Negativfall
        testFabrik.bestellungAufgeben(0, -3);
        
        // Kontrolle, dass keine Bestellung durchgegangen ist
        assertEquals(0, testFabrik.gibBestellungen().size());

        System.out.println(
                "Test Bestellung mit unerlaubten Argumenten. Nichts wurde bestellt");

    }
    
    @Test
    /**
     * Testet die Berechnung der Lieferzeit bei einer Bestellung mit normalen Werten.
     * 
     * Erwartet:
     * - Beschaffungszeit = 0 Tage (Lager ist voll)
     * - Lieferzeit = Produktionszeit (in Tagen) + Beschaffungszeit (0) + Standardlieferzeit (1 Tag)
     */
    public void testeBestellungMitLieferzeit() {

        // Instanzierung einer Fabrik
        Fabrik testFabrik = new Fabrik();

        int standardTueren = 2;
        int premiumTueren = 5;

        testFabrik.bestellungAufgeben(standardTueren, premiumTueren);

    assertEquals(1, testFabrik.gibBestellungen().size());

        Bestellung ersteBestellung = testFabrik.gibBestellungen().get(0);

        // Produktionszeit in Minuten:
        int produktionsZeitMinuten =
                standardTueren * Standardtuer.gibProduktionszeit()
                        + premiumTueren * Premiumtuer.gibProduktionszeit();

        // Umrechnung in Tage
        float produktionsZeitTage = produktionsZeitMinuten / 1440.0f;

        // Erwartete Lieferzeit (Beschaffungszeit = 0, Standardlieferzeit = 1)
        float erwarteteLieferzeit = produktionsZeitTage + 0 + 1;

        assertEquals(0, ersteBestellung.gibBeschaffungsZeit());
        assertEquals(erwarteteLieferzeit, ersteBestellung.gibLieferzeit(), 0.0001f);

        System.out.println(
                "Test Berechnung der Lieferzeit ohne Nachbestellung erfolgreich.");
    }

    @Test
    /**
     * Testet eine grosse Bestellung, bei der das Lager nachbestellen muss.
     * 
     * Erwartet:
     * - Beschaffungszeit = 2 Tage (Nachbestellung notwendig)
     * - Lieferzeit = Produktionszeit (Tage) + 2 + 1
     */
    public void testeBestellungMitNachbestellung() {

        // Instanzierung einer Fabrik
        Fabrik testFabrik = new Fabrik();

        int standardTueren = 1000;
        int premiumTueren = 0;

        testFabrik.bestellungAufgeben(standardTueren, premiumTueren);

        assertEquals(1, testFabrik.gibBestellungen().size());

        Bestellung grosseBestellung = testFabrik.gibBestellungen().get(0);

        // Bei einer so grossen Bestellung sind die Lagerbestände ungenügend,
        // daher muss nachbestellt werden -> Beschaffungszeit = 2
        assertEquals(2, grosseBestellung.gibBeschaffungsZeit());

        // Produktionszeit in Minuten:
        int produktionsZeitMinuten =
                standardTueren * Standardtuer.gibProduktionszeit()
                        + premiumTueren * Premiumtuer.gibProduktionszeit();

        float produktionsZeitTage = produktionsZeitMinuten / 1440.0f;

        // Erwartete Lieferzeit = Produktionszeit + 2 Tage Beschaffung + 1 Tag Standardlieferzeit
        float erwarteteLieferzeit = produktionsZeitTage + 2 + 1;

        assertEquals(erwarteteLieferzeit, grosseBestellung.gibLieferzeit(), 0.0001f);

        // Bestellung muss bestätigt sein
        assertTrue(grosseBestellung.gibBestellBestaetigung());

        System.out.println(
                "Test grosse Bestellung mit Nachbestellung und korrekter Lieferzeit erfolgreich.");
    }
}

