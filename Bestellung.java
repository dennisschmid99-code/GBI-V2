import java.util.ArrayList;

/**
 * Klasse Bestellung.
 * UPDATE: Unterstützt nun Stornierung (Status ABGEBROCHEN).
 *
 * @author GBI Gruppe 17
 * @version 29.12.2025
 */
public class Bestellung {
    private ArrayList<Produkt> bestellteProdukte;
    private boolean bestellBestaetigung;
    private int beschaffungsZeit;
    private int bestellungsNr;
    private int anzahlStandardTueren;
    private int anzahlPremiumTueren;
    private float lieferZeit;
    
    // Status-Flags
    private boolean alleProdukteProduziert;
    private boolean istStorniert; // NEU

    public Bestellung(int anzahlStandardTueren, int anzahlPremiumTueren, int bestellungsNr) {
        if (anzahlStandardTueren < 0 || anzahlPremiumTueren < 0) {
            throw new IllegalArgumentException("Ungültige Bestellmenge.");
        } else if (anzahlStandardTueren == 0 && anzahlPremiumTueren == 0) {
            throw new IllegalArgumentException("Bestellung muss Produkte enthalten.");
        } else if (anzahlStandardTueren > 10_000 || anzahlPremiumTueren > 10_000) {
            throw new IllegalArgumentException("Zu viele Artikel.");
        } else {
            this.bestellungsNr = bestellungsNr;
            this.beschaffungsZeit = -1;
            this.lieferZeit = -1;
            this.bestellteProdukte = new ArrayList<Produkt>();
            this.alleProdukteProduziert = false;
            this.istStorniert = false;
            
            this.anzahlStandardTueren = anzahlStandardTueren;
            this.anzahlPremiumTueren = anzahlPremiumTueren;
            fuelleBestellteprodukte(anzahlStandardTueren, anzahlPremiumTueren);
        }
    }
    
    public Bestellung(int bestellungsNr) {
        this.bestellungsNr = bestellungsNr;
        this.bestellteProdukte = new ArrayList<Produkt>();
        this.alleProdukteProduziert = false;
        this.istStorniert = false;
    }

    public void fuegeProduktHinzu(Produkt p) {
        bestellteProdukte.add(p);
    }

    private void fuelleBestellteprodukte(int std, int prem) {
        for(int i=0; i<std; i++) bestellteProdukte.add(new Standardtuer());
        for(int i=0; i<prem; i++) bestellteProdukte.add(new Premiumtuer());
    }

    // --- Getter & Setter ---
    
    public boolean gibBestellBestaetigung() { return bestellBestaetigung; }
    public int gibBeschaffungsZeit() { return beschaffungsZeit; }
    public void setzeBeschaffungsZeit(int t) { this.beschaffungsZeit = t; }
    public int gibBestellungsNr() { return bestellungsNr; }
    public int gibAnzahlStandardTueren() { return anzahlStandardTueren; }
    public int gibAnzahlPremiumTueren() { return anzahlPremiumTueren; }
    public void bestellungBestaetigen() { bestellBestaetigung = true; }
    public ArrayList<Produkt> liefereBestellteProdukte() { return bestellteProdukte; }
    public ArrayList<Produkt> gibBestellteProdukte() { return bestellteProdukte; }
    public void setzeLieferzeit(float t) { this.lieferZeit = t; }
    public float gibLieferzeit() { return lieferZeit; }
    
    public void setzeAlleProdukteProduziert() { this.alleProdukteProduziert = true; }
    public boolean istAbgeschlossen() { return alleProdukteProduziert; }

    // --- NEU: Abbruch-Logik ---
    public void stornieren() {
        this.istStorniert = true;
    }
    
    public boolean istStorniert() {
        return istStorniert;
    }
}