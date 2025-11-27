
/**
 * Die Klasse Produkt dient als Superklasse für alle spezifischen
 * Türtypen (Standardtuer und Premiumtuer) der Klasse Produkt.
 * * Design-Entscheidung:
 * Der Zustands-Mechanismus wird hier zentral verwaltet,
 * sodass Standardtuer und Premiumtuer ihn nicht doppelt implementieren müssen.
 * @author  Gruppe 17
 * @version 4.0 (23.11.2025)
 */

public class Produkt
{
    // --- Datenfelder (Instanzvariablen) ---
    // Wir nutzen 'private', um den direkten Zugriff von aussen zu verhindern.
    // Der Zustand wird als Zahl gespeichert (0=bestellt, 1=in Produktion).
    private int zustand;

    /**
     * Konstruktor für Objekte der Klasse Produkt.
     * Initialisiert ein neues Produkt mit einem definierten Startzustand.
     */
    public Produkt()
    {
        // Initialisierung: Jedes neue Produkt beginnt im Zustand 0.
        this.zustand = 0;
    }

    // --- Methoden ---

    /**
     * Verändernde Methode (mutator) zum Ändern des Produktionsstatus.
     * Wir führen eine Gültigkeitsprüfung durch (Bedingte Anweisung), 
     * bevor wir den Wert ändern. Negative Zustände sollen dadurch abgelehnt werden. 
     * * @param neuerZustand Der neue Status als ganze Zahl (muss >= 0 sein).
     */
    public void zustandAendern(int neuerZustand)
    {
        // Nur sinnvolle Werte werden akzeptiert.
        if (neuerZustand >= 0) {
            this.zustand = neuerZustand;
        }
        else {
            // Fehlermeldung auf der Konsole, damit der Benutzer Feedback erhält.
            System.out.println("Fehler: Der Zustand darf nicht negativ sein! Eingabe war: " + neuerZustand);
        }
    }

    /**
     * Sondierende Methode (Accessor), um den aktuellen Zustandsstatus abzufragen.
     * * @return Der aktuelle Zustand als int.
     */
    public int aktuellerZustand()
    {
        return this.zustand;
    }
}