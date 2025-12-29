import java.util.ArrayList;

public class Fabrik {

    private Lager lager;
    private Lieferant lieferant;
    private Produktions_Manager produktionsManager;
    private ArrayList<Bestellung> alleBestellungen;
    private int bestellungsNrCounter;

    public Fabrik() {
        this.alleBestellungen = new ArrayList<Bestellung>();
        this.bestellungsNrCounter = 1;

        this.lager = new Lager();
        this.lieferant = new Lieferant(lager);
        this.lager.setzeLieferant(lieferant);
        this.lieferant.start(); 

        this.produktionsManager = new Produktions_Manager(lager);
        this.produktionsManager.start();

        System.out.println("Fabrik: System hochgefahren.");
    }

    public void bestellungAufgeben(int anzahlPremium, int anzahlStandard) {
        Bestellung neueBestellung = new Bestellung(bestellungsNrCounter++);
        
        for (int i = 0; i < anzahlStandard; i++) neueBestellung.fuegeProduktHinzu(new Standardtuer());
        for (int i = 0; i < anzahlPremium; i++) neueBestellung.fuegeProduktHinzu(new Premiumtuer());

        alleBestellungen.add(neueBestellung);
        
        // Smart Logistics Trigger
        lager.meldeBedarf(neueBestellung);

        produktionsManager.fuegeZuVerarbeitendeBestellungenHinzu(neueBestellung);
    }
    
    // --- Neue Steuerungs-Methoden ---
    
    public void setzeProduktionPausiert(boolean pausiert) {
        if(produktionsManager != null) {
            produktionsManager.setzePausiert(pausiert);
        }
    }
    
    public void stornierePendenteBestellungen() {
        // 1. Die Warteschlange im Manager leeren
        if(produktionsManager != null) {
            produktionsManager.storniereWartendeBestellungen();
        }
        
        // 2. Die laufende Bestellung abbrechen
        // Das geschieht implizit, indem wir das Flag der aktuellen Bestellung setzen.
        // Dafür müssten wir wissen, welche gerade läuft.
        // ABER: In unserer Logik haben wir Zugriff auf "alleBestellungen".
        // Wir können einfach ALLE Bestellungen, die noch nicht fertig sind, stornieren.
        // Der Manager wird dann im Loop merken "Oh, storniert -> Stop".
        
        for(Bestellung b : alleBestellungen) {
            if(!b.istAbgeschlossen()) {
                b.stornieren();
            }
        }
    }

    public ArrayList<Bestellung> gibBestellungen() { return alleBestellungen; }
    public Lieferant gibLieferant() { return lieferant; }
    public Lager gibLager() { return this.lager; }
}