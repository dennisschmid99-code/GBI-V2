import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Fabrik.
 * Geprüft wird die Bestellaufnahme, Fehlerbehandlung und Lieferzeitberechnung
 * unter Berücksichtigung der neuen Lagerkapazitäten.
 *
 * @author Gruppe 17
 * @version 04.12.2025
 */
public class FabrikTest {

    private Fabrik fabrik;

    @BeforeEach
    public void setUp() {
        fabrik = new Fabrik(); // Erstellt Fabrik mit neuem, vollem Lager
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Testet eine normale Bestellung.
     * Erwartung: Beschaffungszeit 0 (da Lager voll), Bestellung bestätigt.
     */
    @Test
    public void testeBestellung() {
        fabrik.bestellungAufgeben(2, 5);
        
        // Da es die erste Bestellung ist, muss sie an Index 0 sein
        Bestellung b = fabrik.gibBestellungen().get(0);
        
        assertEquals(0, b.gibBeschaffungsZeit(), "Lager ist voll, Beschaffung muss 0 sein.");
        assertTrue(b.gibBestellBestaetigung(), "Bestellung sollte bestätigt sein.");
    }

    /**
     * Testet Fehleingaben (0 oder negative Anzahl).
     * Erwartung: Es wird keine Bestellung zur Liste hinzugefügt.
     */
    @Test
    public void testeBestellungFalsch() {
        // Fall 1: Alles 0
        fabrik.bestellungAufgeben(0, 0);
        
        // Fall 2: Negativ
        fabrik.bestellungAufgeben(-5, 1);
        
        // Die Liste muss leer bleiben
        assertEquals(0, fabrik.gibBestellungen().size(), "Ungültige Bestellungen dürfen nicht gespeichert werden.");
    }

    /**
     * Testet die exakte Berechnung der Lieferzeit.
     * Szenario: 2 Standard, 5 Premium. Lager voll.
     */
    @Test
    public void testeBestellungMitLieferzeit() {
        int s = 2; 
        int p = 5;
        fabrik.bestellungAufgeben(s, p);
        
        Bestellung b = fabrik.gibBestellungen().get(0);
        
        // Produktionszeit berechnen
        int produktionsMinuten = s * Standardtuer.gibProduktionszeit() + p * Premiumtuer.gibProduktionszeit();
        float produktionsTage = produktionsMinuten / 1440.0f;
        
        // Erwartung: Prod + 0 Beschaffung + 1 Tag Standardlieferzeit
        float erwarteteZeit = produktionsTage + 0 + 1;
        
        assertEquals(erwarteteZeit, b.gibLieferzeit(), 0.0001f, "Lieferzeitberechnung falsch.");
    }

    /**
     * Testet ein Szenario mit großer Menge (1000 Türen).
     * Da das Lager jetzt 1000 Einheiten Farbe hat, reicht es für 500 Türen (1000 Farbe).
     * ACHTUNG: Hier testen wir 1000 Türen -> Bedarf 2000 Farbe.
     * Lager hat 1000. Es fehlen 1000.
     * Nachbestellung: 1000 Fehlmenge / 1000 Kapazität = 1 Zyklus.
     * Zeit: 1 * 2 Tage = 2 Tage.
     */
    @Test
    public void testeBestellungMitNachbestellung() {
        int s = 1000; 
        int p = 0; 
        // 1000 Standardtüren brauchen 2000 Farbe.
        // Lager hat 1000 Farbe.
        // Es fehlen 1000 Farbe. Lieferant liefert 1000 pro Ladung.
        // Also 1 Zyklus nötig -> 2 Tage Beschaffungszeit.

        fabrik.bestellungAufgeben(s, p);
        Bestellung b = fabrik.gibBestellungen().get(0);

        // Prüfung Beschaffungszeit
        assertEquals(2, b.gibBeschaffungsZeit(), 
            "Bei 1000 Türen (2000 Farbe) und 1000 Lager muss 1x nachbestellt werden (2 Tage).");

        // Prüfung Gesamtlieferzeit
        int produktionsMinuten = s * Standardtuer.gibProduktionszeit();
        float produktionsTage = produktionsMinuten / 1440.0f;
        
        // 2 Tage Beschaffung + 1 Tag Standardlieferung
        float erwarteteLieferzeit = produktionsTage + 2 + 1;

        assertEquals(erwarteteLieferzeit, b.gibLieferzeit(), 0.0001f, "Gesamtlieferzeit mit Nachbestellung falsch.");
    }
}