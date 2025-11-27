//Import der ArrayList-Bibliothek. Notwendig, da die Fabrik eine Sammlung von Bestellungen verwaltet.(Kapitel 4.4)
import java.util.ArrayList;

/**
 * Die Klasse Fabrik bildet die zentrale Steuerung der Anwendung.
 * Sie verwaltet alle eingegangenen Bestellungen und startet das
 * gesamte Programm über ihre main-Methode.
 *
 * @author Gruppe 17
 * @version 4.0 (23.11.2025)
 */
public class Fabrik
{
    // Datenfelder definieren.
    // Wir nutzen 'private', um den direkten Zugriff von aussen zu verhindern.

    private ArrayList<Bestellung> bestellungen; //Speichert alle Bestellungen in einer Sammlung-

    private int bestellungsNr; //Ein Zähler, um sicherzustellen, dass jede Bestellung eine eindeutige Nummer erhält.
    //sollte sein private int bestellungsNr

    /**
     * Konstruktor für Objekte der Klasse Fabrik.
     * Dient der Initialisierung der Instanzvariabeln in einen gültigen Startzustand.
     */
    public Fabrik()
    {
        bestellungen = new ArrayList<Bestellung>(); //Initialisierung der Sammlung 
        bestellungsNr = 1; //Startnummer setzen
    }

    /**
     * Die "main"-Methode ist der Startpunkt für jede Java-Anwendung.
     * (Kapitel 6.16: "Programmausführung ohne BlueJ")
     *
     * Wir führen hier eine komplexere Simulation durch, um die
     * "Objektinteraktion" (Kapitel 3) zu demonstrieren.
     */
    public static void main(String[] args)
    {
        // --- 1. SETUP ---
        System.out.println("--- Fabrik-Simulation wird gestartet ---");
        
        // "Objekterzeugung" (Kapitel 3.10): Wir brauchen ein Fabrik-Objekt.
        Fabrik meineFabrik = new Fabrik();
        System.out.println("Fabrik wurde erstellt. Aktueller Status:");
        meineFabrik.bestellungenAusgeben(); // Sollte "leer" anzeigen
        System.out.println(); // Leere Zeile

        // --- 2. SIMULATION: BESTELLEINGANG ---
        System.out.println("--- Phase 1: Neue Bestellungen ---");
        
        // "Methodenaufruf" (Kapitel 1.3)
        meineFabrik.bestellungAufgeben(3, 1); // 3 Standard, 1 Premium
        System.out.println("Bestellung 1 (3S, 1P) wurde aufgegeben.");
        
        meineFabrik.bestellungAufgeben(5, 0); // 5 Standard, 0 Premium
        System.out.println("Bestellung 2 (5S, 0P) wurde aufgegeben.");
        System.out.println();
        
        // --- 3. SIMULATION: STATUSPRÜFUNG 1 ---
        System.out.println("--- Statusbericht nach Phase 1 ---");
        meineFabrik.bestellungenAusgeben(); // Beide Bestellungen sollten "OFFEN" sein
        System.out.println();

        // --- 4. SIMULATION: AUFTRAGSBEARBEITUNG ---
        System.out.println("--- Phase 2: Bearbeitung von Bestellung 100 ---");
        
        // Um eine Bestellung zu bearbeiten, holen wir sie aus der Liste.
        // Wir nutzen unsere Test-Hilfsmethode.
        // (Kapitel 4.7: "Nummerierung in Sammlungen", .get(index))
        ArrayList<Bestellung> alle = meineFabrik.gibAlleBestellungen();
        
        if(!alle.isEmpty()) {
            Bestellung ersteBestellung = alle.get(0); // Holt das Objekt für BestellNr. 1
            
            // "Externe Methodenaufrufe" (Kapitel 3.12.2) auf dem Bestell-Objekt
            ersteBestellung.bestellungBestaetigen();
            ersteBestellung.setzeBeschaffungsZeit(5); // z.B. 5 Tage
            
            System.out.println("Bestellung 1 wurde bestätigt und Zeit gesetzt.");
            
            System.out.println();
            System.out.println(">>> Produktion startet für Bestellung 1...");

            // 1. Wir holen uns die "Kiste" mit den Türen aus der Mappe
            ArrayList<Produkt> dieTueren = ersteBestellung.gibAlleProdukte();

            // 2. Wir nehmen die allererste Tür heraus (Index 0). 
            // Das müsste eine Standardtür sein.
            Produkt tuerEins = dieTueren.get(0);
            
            // Wir ändern den Zustand auf 1 (z.B. "In Produktion")
            tuerEins.zustandAendern(1);
            System.out.println("Tür 1 (Standard) Status geändert auf: " + tuerEins.aktuellerZustand() + " (In Produktion)");

            // 3. Wir nehmen die vierte Tür heraus (Index 3).
            // Da wir 3 Standardtüren bestellt haben (Index 0, 1, 2), ist Index 3 die Premiumtür.
            Produkt tuerVier = dieTueren.get(3);
            
            // Wir ändern den Zustand auf 2 (z.B. "Fertiggestellt")
            tuerVier.zustandAendern(2);
            System.out.println("Tür 4 (Premium)  Status geändert auf: " + tuerVier.aktuellerZustand() + " (Fertiggestellt)");
        }
        System.out.println();
        
        // --- 5. SIMULATION: STATUSPRÜFUNG 2 ---
        System.out.println("--- Finaler Statusbericht ---");
        meineFabrik.bestellungenAusgeben(); // Jetzt sollte Bst-Nr 100 "BESTÄTIGT" sein
        System.out.println();
        
        // --- 6. SIMULATION: FEHLERFALL-TEST ---
        System.out.println("--- Phase 3: Test der Sicherheitsmechanismen ---");
        System.out.println("Versuche, eine ungültige Bestellung (-5 Türen) aufzugeben...");

        try {
            // Wir versuchen absichtlich etwas Verbotenes
            meineFabrik.bestellungAufgeben(-5, 2); 
        } catch (IllegalArgumentException e) {
            // Wir fangen den Fehler ab, damit das Programm nicht abstürzt
            System.out.println("ERFOLG: Der Fehler wurde korrekt erkannt!");
            System.out.println("Meldung des Systems: " + e.getMessage());
        }
        
        System.out.println(); 
        System.out.println("--- Fabrik-Simulation beendet ---");
    }

    /**
     * Erstellt eine neue Bestellung und fügt sie der Liste hinzu.
     * * @param standardTueren Anzahl der Standardtüren
     * @param premiumTueren Anzahl der Premiumtüren
     */
    public void bestellungAufgeben(int standardTueren, int premiumTueren)
    {
        // Objekterzeugung (Kapitel 3.10)
        // Wir versuchen, die Bestellung zu erstellen.
        // Falls die Zahlen ungültig sind, wirft der Konstruktor von 'Bestellung' 
        // hier automatisch eine Exception und bricht die Methode ab.
        // Das ist gut so, denn so landet kein Müll in unserer Liste.
        Bestellung neueBestellung = new Bestellung(bestellungsNr, standardTueren, premiumTueren);

        // Wenn wir hier ankommen, war die Bestellung gültig.
        // Hinzufügen zur Sammlung (Kapitel 4.4)
        bestellungen.add(neueBestellung);

        // Zähler für die nächste Bestellung erst JETZT erhöhen
        bestellungsNr = bestellungsNr + 1; 
        
        // Optional: Feedback für den User
        System.out.println("Bestellung " + (bestellungsNr - 1) + " erfolgreich aufgenommen.");
    }

    /**
     * Gibt alle Details der gespeicherten Bestellungen auf der Konsole aus.
     * Nutzt "Iteration" (Schleifen), um die Sammlung zu durchlaufen.
     * (Kapitel 4.9: Komplette Sammlungen verarbeiten)
     */
    public void bestellungenAusgeben()
    {
        // Wir verwenden eine "for-each"-Schleife
        
        // Zusatzprüfung: Was, wenn die Liste leer ist?
        if (bestellungen.size() == 0) {
            System.out.println("Aktuell keine Bestellungen im System.");
            return; // Methode hier beenden
        }
        
        for(Bestellung b : bestellungen)
        {
            // 'b' ist die "lokale Variable" (Kapitel 2.16) der Schleife.
            
            //Ausgabe auf Konsole
            System.out.println("-------------------------");
            System.out.println("Bestell-Nr:     " + b.gibBestellungsNr());
            System.out.println("Standardtüren:  " + b.gibAnzahlStandardTueren());
            System.out.println("Premiumtüren:   " + b.gibAnzahlPremiumTueren());
            
            // Beispiel für eine "bedingte Anweisung" (Kapitel 2.13)
            if(b.gibBestellBestaetigung() == true) {
                System.out.println("Status:         BESTÄTIGT");
                System.out.println("Beschaffungszeit:  " + b.gibBeschaffungsZeit() + " Tage");
            } else {
                System.out.println("Status:         OFFEN");
            }
        }
        System.out.println("-------------------------");
    }
    
    /**
     * Sondiermethode (Kapitel 2.8) zur Unterstützung von Tests.
     * Gibt die komplette Liste aller Bestellungen zurück, damit ein
     * Test-Objekt den internen Zustand der Fabrik prüfen kann.
     *
     * @return Die ArrayList, die alle Bestellungen speichert.
     */
    public ArrayList<Bestellung> gibAlleBestellungen()
    {
        return this.bestellungen;
    }
}