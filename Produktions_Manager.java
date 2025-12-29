import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Der Produktions_Manager steuert den Fertigungsprozess. 
 * UPDATE: Unterstützt Pause und Abbruch (Storno) von Bestellungen.
 *
 * @author GBI Gruppe 17
 * @version 29.12.2025
 */
public class Produktions_Manager extends Thread {

    private Lager meinLager;
    private Holzbearbeitungs_Roboter holzRoboter;

    private LinkedList<Bestellung> zuVerarbeitendeBestellungen;
    private LinkedList<Bestellung> bestellungenInProduktion;

    private boolean laeuft;
    
    // Steuerung
    private volatile boolean istPausiert = false;

    public Produktions_Manager(Lager lager) {
        this.meinLager = lager;
        this.zuVerarbeitendeBestellungen = new LinkedList<Bestellung>();
        this.bestellungenInProduktion = new LinkedList<Bestellung>();
        this.laeuft = true;

        this.holzRoboter = new Holzbearbeitungs_Roboter();
        this.holzRoboter.start();
        
        System.out.println("Produktions-Manager: Gestartet.");
    }

    public void run() {
        while (laeuft) {
            try {
                // PAUSE-LOGIK: Wenn pausiert, loopen wir hier
                while (istPausiert) {
                    Thread.sleep(200);
                }
                
                verarbeiteNeueBestellungen();
                pruefeFertigeBestellungen();
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // --- Steuerungsmethoden ---
    
    public void setzePausiert(boolean p) {
        this.istPausiert = p;
        if (p) System.out.println("Manager: PAUSIERT.");
        else System.out.println("Manager: WEITER.");
    }

    public synchronized void storniereWartendeBestellungen() {
        System.out.println("Manager: Storniere alle wartenden Bestellungen...");
        
        // 1. Warteschlange leeren und als storniert markieren
        for (Bestellung b : zuVerarbeitendeBestellungen) {
            b.stornieren();
        }
        zuVerarbeitendeBestellungen.clear();
        
        // Hinweis: Die Bestellung, die GERADE in Arbeit ist (lokale Variable in 'verarbeite...'),
        // wird durch das 'b.istStorniert()' Check in der Schleife unten gestoppt.
    }

    // --- Verarbeitung ---

    public synchronized void fuegeZuVerarbeitendeBestellungenHinzu(Bestellung bestellung) {
        zuVerarbeitendeBestellungen.add(bestellung);
        System.out.println("Manager: Bestellung " + bestellung.gibBestellungsNr() + " eingereiht.");
    }

    private void verarbeiteNeueBestellungen() {
        // Wir holen immer nur EINE Bestellung und verarbeiten sie,
        // damit wir zwischen den Bestellungen prüfen können.
        Bestellung aktuelleBestellung = null;
        
        synchronized (this) {
            if (!zuVerarbeitendeBestellungen.isEmpty()) {
                aktuelleBestellung = zuVerarbeitendeBestellungen.removeFirst();
            }
        }
        
        if (aktuelleBestellung != null) {
            // Check ob sie zwischenzeitlich storniert wurde
            if (aktuelleBestellung.istStorniert()) return;

            bestellungenInProduktion.add(aktuelleBestellung);
            System.out.println("Manager: Starte Produktion Best. " + aktuelleBestellung.gibBestellungsNr());

            ArrayList<Produkt> produkte = aktuelleBestellung.liefereBestellteProdukte();

            for (Produkt produkt : produkte) {
                // ABBRUCH-CHECK mitten in der Produktion
                // Wenn der User "Abbrechen" drückt, markieren wir die aktuelle Bestellung als storniert.
                if (aktuelleBestellung.istStorniert()) {
                    System.out.println("Manager: Best. " + aktuelleBestellung.gibBestellungsNr() + " ABGEBROCHEN.");
                    break; // Schleife verlassen, restliche Produkte nicht produzieren
                }
                
                // PAUSE-CHECK mitten in der Produktion (Tür für Tür)
                while (istPausiert) {
                    try { Thread.sleep(200); } catch (Exception e) {}
                }

                meinLager.materialEntnehmen(produkt);
                produkt.fuegeRoboterHinzu(holzRoboter);
                produkt.naechsteProduktionsStation();
                
                try { Thread.sleep(50); } catch (Exception e) {}
            }
        }
    }

    private void pruefeFertigeBestellungen() {
        Iterator<Bestellung> iterator = bestellungenInProduktion.iterator();
        while (iterator.hasNext()) {
            Bestellung bestellung = iterator.next();
            
            // Wenn storniert, nehmen wir sie aus der Überwachung (oder lassen sie als 'Zombie' drin,
            // aber wir müssen verhindern, dass sie als "FERTIG" markiert wird, wenn sie unvollständig ist).
            // GUI kümmert sich um Status-Anzeige.
            
            boolean alleFertig = true;
            for (Produkt p : bestellung.liefereBestellteProdukte()) {
                // Wir zählen nur Produkte, die auch gestartet wurden (Zustand > BESTELLT)
                // Aber: 'istFertig' prüft auf Zustand FERTIG.
                if (!p.istFertig()) {
                    alleFertig = false;
                    break;
                }
            }

            // Nur wenn wirklich alle Produkte fertig sind (und nicht abgebrochen),
            // setzen wir den Haken.
            if (alleFertig && !bestellung.istStorniert()) {
                bestellung.setzeAlleProdukteProduziert();
                System.out.println(">>> Manager: Bestellung " + bestellung.gibBestellungsNr() + " FERTIG. <<<");
                iterator.remove();
            } else if (bestellung.istStorniert()) {
                 // Wir entfernen sie aus der aktiven Produktion, da sie nicht mehr weiter bearbeitet wird.
                 iterator.remove();
            }
        }
    }
}