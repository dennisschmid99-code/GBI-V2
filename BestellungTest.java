import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Die Test-Klasse BestellungTest.
 * Sie überprüft, ob die Klasse Bestellung korrekt initialisiert wird
 * und die Produkte korrekt zählt und verwaltet.
 * (Kapitel 9: Fehler vermeiden)
 *
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class BestellungTest
{
    // "Testgerüst" (Kapitel 9.3)
    // Wir deklarieren eine Variable für das Objekt, das wir testen wollen.
    private Bestellung bestellung1;

    /**
     * Konstruktor für die Test-Klasse BestellungTest
     */
    public BestellungTest()
    {
    }

    /**
     * Setzt das Testgerüst für jeden Test neu auf.
     * Diese Methode wird vor jeder einzelnen @Test-Methode ausgeführt.
     * (Kapitel 9.3)
     *
     * Wir erstellen hier eine Standard-Bestellung mit 2 Standardtüren
     * und 3 Premiumtüren für die Tests.
     */
    @BeforeEach
    public void setUp()
    {
        // Erzeuge eine neue Bestellung mit Nr. 101, 2 Standard, 3 Premium
        bestellung1 = new Bestellung(101, 2, 3);
    }

    /**
     * Gibt das Testgerüst wieder frei.
     * Wird nach jeder Testmethode aufgerufen.
     */
    @AfterEach
    public void tearDown()
    {
        // Hier ist nichts zu tun, da die Objekte automatisch
        // vom "Garbage Collector" entfernt werden.
    }

    /**
     * Testet den Initialzustand der Bestellung direkt nach dem Konstruktoraufruf.
     */
    @Test
    public void testInitialisierung()
    {
        // "assertEquals" prüft, ob der erwartete Wert (1. Parameter)
        // mit dem tatsächlichen Wert (2. Parameter) übereinstimmt.
        
        // 1. Wurde die Bestellnummer korrekt gesetzt?
        assertEquals(101, bestellung1.gibBestellungsNr(), "Bestellnummer falsch initialisiert");
        
        // 2. Ist die Bestätigung initial auf 'false'?
        assertEquals(false, bestellung1.gibBestellBestaetigung(), "Bestätigung muss anfangs false sein");
        
        // 3. Ist die Beschaffungszeit initial auf -1?
        // HINWEIS: In deiner Klasse Bestellung wird sie auf -1 gesetzt, nicht auf 0.
        assertEquals(-1, bestellung1.gibBeschaffungsZeit(), "Beschaffungszeit muss initial -1 sein");
    }

    /**
     * Testet, ob die Zähler für die Türen korrekt gesetzt wurden.
     * (Prüft die Parameterübergabe im Konstruktor an die Datenfelder).
     */
    @Test
    public void testAnzahlTueren()
    {
        assertEquals(2, bestellung1.gibAnzahlStandardTueren(), "Anzahl Standardtüren falsch");
        assertEquals(3, bestellung1.gibAnzahlPremiumTueren(), "Anzahl Premiumtüren falsch");
    }
    
    /**
     * Testet, ob die while-Schleifen im Konstruktor korrekt
     * die ArrayList 'bestellteProdukte' befüllt haben.
     * (Kapitel 4: Objektsammlungen)
     */
    @Test
    public void testProduktListenErzeugung()
    {
        // Wir holen die interne Liste über die Hilfsmethode
        ArrayList<Produkt> produkte = bestellung1.gibAlleProdukte();
        
        // Sind insgesamt 5 Produkte (2 Standard + 3 Premium) in der Liste?
        // Die Methode .size() gibt die Anzahl der Elemente in einer ArrayList zurück.
        assertEquals(5, produkte.size(), "Die Liste sollte 5 Produkte enthalten");
    }

    /**
     * Testet die "verändernden Methoden" (Mutatoren) der Klasse.
     */
    @Test
    public void testVeranderndeMethoden()
    {
        // 1. Teste bestellungBestaetigen()
        bestellung1.bestellungBestaetigen();
        assertEquals(true, bestellung1.gibBestellBestaetigung(), "Bestätigung hat nicht funktioniert");
        
        // 2. Teste setzeBeschaffungsZeit() (Gültiger Wert)
        bestellung1.setzeBeschaffungsZeit(14);
        assertEquals(14, bestellung1.gibBeschaffungsZeit(), "Setzen der Zeit hat nicht funktioniert");
    }
    
    /**
     * Testet die "Fehlerbehandlung" (Kapitel 2.13) in setzeBeschaffungsZeit.
     */
    @Test
    public void testFehlerbehandlungZeit()
    {
        // Versuch, einen ungültigen Wert zu setzen
        bestellung1.setzeBeschaffungsZeit(-5);
        
        // Der Wert muss immer noch auf dem Initialwert (-1) sein,
        // da der ungültige Wert ignoriert werden sollte.
        assertEquals(-1, bestellung1.gibBeschaffungsZeit(), "Ungültige Zeit wurde fälschlicherweise übernommen");
    }
}