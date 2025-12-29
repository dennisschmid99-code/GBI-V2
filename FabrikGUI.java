import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Modernes Management-Dashboard für die Aeki Türenfabrik.
 * UPDATE: Multi-Lieferungs-Anzeige im Statusfeld.
 * * @author GBI Gruppe 17
 * @version 29.12.25
 */
public class FabrikGUI extends JFrame {

    private final Fabrik fabrik;
    private final Lager lager;

    // UI Komponenten
    private JSpinner spinStandard, spinPremium;
    private JProgressBar barHolz, barSchrauben, barFarbe, barKarton, barGlas;
    private DefaultTableModel orderTableModel;
    private JTextArea logArea;
    
    // Statusanzeige
    private JLabel lblStatusIcon;
    private JLabel lblStatusText;
    private JPanel statusPanel;

    public FabrikGUI(Fabrik fabrik) {
        this.fabrik = fabrik;
        this.lager = fabrik.gibLager(); 

        initUI();
        redirectSystemStreams(); 
        startDashboardUpdateLoop(); 
        setVisible(true);
    }

    private void initUI() {
        setTitle("Aeki Smart Manufacturing | Executive Dashboard");
        setSize(1200, 900); // Etwas mehr Platz für mehrere LKWs
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Header ---
        JLabel header = new JLabel("AEKI FABRIK STATUS MONITOR", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        header.setOpaque(true);
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        add(header, BorderLayout.NORTH);

        // --- Left Panel: Controls ---
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);

        // --- Center Panel ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(createStockPanel());
        centerPanel.add(createOrderTablePanel());
        add(centerPanel, BorderLayout.CENTER);

        // --- South Panel ---
        add(createLogPanel(), BorderLayout.SOUTH);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(280, 0));

        // 1. Status Panel (jetzt dynamisch)
        panel.add(createStatusDisplay());
        panel.add(Box.createVerticalStrut(20));

        // 2. Auftrags-Panel
        JPanel inputGroup = new JPanel();
        inputGroup.setLayout(new BoxLayout(inputGroup, BoxLayout.Y_AXIS));
        inputGroup.setBorder(BorderFactory.createTitledBorder("Neuer Auftrag"));
        inputGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        inputGroup.add(Box.createVerticalStrut(10));
        JLabel lblStd = new JLabel("Standardtüren:");
        lblStd.setFont(labelFont);
        lblStd.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputGroup.add(lblStd);
        
        spinStandard = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        spinStandard.setMaximumSize(new Dimension(150, 30));
        inputGroup.add(spinStandard);

        inputGroup.add(Box.createVerticalStrut(15));

        JLabel lblPrem = new JLabel("Premiumtüren:");
        lblPrem.setFont(labelFont);
        lblPrem.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputGroup.add(lblPrem);

        spinPremium = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        spinPremium.setMaximumSize(new Dimension(150, 30));
        inputGroup.add(spinPremium);

        inputGroup.add(Box.createVerticalStrut(25));

        JButton btnOrder = new JButton("PRODUKTION STARTEN");
        btnOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOrder.setBackground(new Color(39, 174, 96));
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOrder.setFocusPainted(false);
        btnOrder.addActionListener(e -> aufgebenBestellung());
        inputGroup.add(btnOrder);
        
        inputGroup.add(Box.createVerticalStrut(10));
        panel.add(inputGroup);

        // 3. Demo Button
        panel.add(Box.createVerticalStrut(20));
        JButton btnDemo = new JButton("DEMO SZENARIO LAUFEN");
        btnDemo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDemo.setBackground(new Color(52, 152, 219));
        btnDemo.setForeground(Color.WHITE);
        btnDemo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDemo.setFocusPainted(false);
        btnDemo.addActionListener(e -> starteDemoSzenario());
        panel.add(btnDemo);

        return panel;
    }

    private JPanel createStatusDisplay() {
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("System Status"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        statusPanel.setBackground(new Color(230, 255, 230)); 
        // WICHTIG: Keine feste Höhe mehr, damit die Liste wachsen kann
        statusPanel.setMinimumSize(new Dimension(280, 100));
        statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblStatusIcon = new JLabel("✔"); 
        lblStatusIcon.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblStatusIcon.setForeground(new Color(39, 174, 96));
        lblStatusIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        // Icon oben ausrichten, falls Text lang wird
        lblStatusIcon.setVerticalAlignment(SwingConstants.TOP); 

        lblStatusText = new JLabel("<html>System bereit.<br>Lager gefüllt.</html>");
        lblStatusText.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        statusPanel.add(lblStatusIcon, BorderLayout.WEST);
        statusPanel.add(lblStatusText, BorderLayout.CENTER);
        
        return statusPanel;
    }

    private JPanel createStockPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Lagerbestand (Echtzeit)"));

        barHolz = createStyledProgressBar();
        barSchrauben = createStyledProgressBar();
        barFarbe = createStyledProgressBar();
        barKarton = createStyledProgressBar();
        barGlas = createStyledProgressBar();

        panel.add(layoutBarPanel("Holz", barHolz));
        panel.add(layoutBarPanel("Schrauben", barSchrauben));
        panel.add(layoutBarPanel("Farbe", barFarbe));
        panel.add(layoutBarPanel("Karton", barKarton));
        panel.add(layoutBarPanel("Glas", barGlas));

        return panel;
    }

    private JProgressBar createStyledProgressBar() {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setForeground(new Color(52, 152, 219));
        bar.setBackground(Color.WHITE);
        return bar;
    }

    private JPanel layoutBarPanel(String label, JProgressBar bar) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(80, 20));
        p.add(lbl, BorderLayout.WEST);
        p.add(bar, BorderLayout.CENTER);
        return p;
    }

    private JPanel createOrderTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Aktive Aufträge"));

        String[] columns = {"ID", "Standard", "Premium", "Fortschritt", "Status"};
        orderTableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(orderTableModel);
        table.setFillsViewportHeight(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("System Log"));
        panel.setPreferredSize(new Dimension(0, 150));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(100, 255, 100));

        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        return panel;
    }

    // --- Logik ---

    private void aufgebenBestellung() {
        int std = (Integer) spinStandard.getValue();
        int prem = (Integer) spinPremium.getValue();
        if (std == 0 && prem == 0) {
            JOptionPane.showMessageDialog(this, "Bitte Mengen > 0 wählen!", "Fehler", JOptionPane.WARNING_MESSAGE);
            return;
        }
        fabrik.bestellungAufgeben(prem, std);
        spinStandard.setValue(0);
        spinPremium.setValue(0);
    }

    private void starteDemoSzenario() {
        new Thread(() -> {
            try {
                fabrik.bestellungAufgeben(2, 2);
                Thread.sleep(2000); 
                fabrik.bestellungAufgeben(0, 5); 
                fabrik.bestellungAufgeben(2, 0);
            } catch (InterruptedException ex) { ex.printStackTrace(); }
        }).start();
    }

    private void startDashboardUpdateLoop() {
        Timer timer = new Timer(100, e -> updateDashboard());
        timer.start();
    }

    private void updateDashboard() {
        updateLagerDisplay();
        updateStatusDisplay();
        updateOrderTable();
    }
    
    /**
     * ELEGANT: Zeigt nun ALLE eintreffenden Lieferungen an.
     */
    private void updateStatusDisplay() {
        Lieferant lieferant = fabrik.gibLieferant();
        if(lieferant == null) return; 

        // Liste der ETAs (in Sekunden) holen
        List<Long> etas = lieferant.gibVerbleibendeSekundenListe();
        
        if (!etas.isEmpty()) {
            // Zustand: LIEFERUNGEN UNTERWEGS
            statusPanel.setBackground(new Color(255, 243, 205)); // Gelb
            lblStatusIcon.setText("🚛"); // LKW Icon
            lblStatusIcon.setForeground(new Color(255, 150, 0));
            
            // HTML Bauen für Mehrzeiligkeit
            StringBuilder sb = new StringBuilder("<html><b>LIEFERUNGEN UNTERWEGS: " + etas.size() + "</b><br>");
            sb.append("<table border='0' cellspacing='0' cellpadding='0'>");
            
            int count = 1;
            for (Long sek : etas) {
                sb.append("<tr><td>LKW ").append(count).append(":</td><td width='10'></td><td><b>")
                  .append(sek).append(" s</b></td></tr>");
                count++;
                if(count > 5) { // Begrenzen, damit GUI nicht platzt
                    sb.append("<tr><td>...</td><td></td><td></td></tr>");
                    break; 
                }
            }
            sb.append("</table></html>");
            
            lblStatusText.setText(sb.toString());
            
        } else {
            // Zustand: NORMAL
            statusPanel.setBackground(new Color(230, 255, 230)); 
            lblStatusIcon.setText("✔");
            lblStatusIcon.setForeground(new Color(39, 174, 96));
            lblStatusText.setText("<html>System bereit.<br>Keine offenen Lieferungen.</html>");
        }
    }

    private void updateLagerDisplay() {
        updateBar(barHolz, lager.gibAnzahlHolz(), lager.gibMaxHolz());
        updateBar(barSchrauben, lager.gibAnzahlSchrauben(), lager.gibMaxSchrauben());
        updateBar(barFarbe, lager.gibAnzahlFarbe(), lager.gibMaxFarbe());
        updateBar(barKarton, lager.gibAnzahlKarton(), lager.gibMaxKarton());
        updateBar(barGlas, lager.gibAnzahlGlas(), lager.gibMaxGlas());
    }

    private void updateOrderTable() {
        orderTableModel.setRowCount(0); 
        ArrayList<Bestellung> bestellungen = fabrik.gibBestellungen();
        
        // Wir iterieren rückwärts, damit die neuesten oben stehen
        for (int i = bestellungen.size() - 1; i >= 0; i--) {
            Bestellung b = bestellungen.get(i);
            
            // 1. Zählung der Produkttypen (Fix aus vorherigem Schritt)
            int countStd = 0;
            int countPrem = 0;
            for(Produkt p : b.liefereBestellteProdukte()) {
                if(p instanceof Standardtuer) countStd++;
                else if(p instanceof Premiumtuer) countPrem++;
            }

            // 2. Berechnung des Fortschritts (Single Source of Truth)
            int total = b.liefereBestellteProdukte().size();
            long fertigCount = b.liefereBestellteProdukte().stream()
                                .filter(Produkt::istFertig)
                                .count();
            
            String fortschritt = fertigCount + " / " + total;
            
            // 3. ELEGANT: Status rein aus den Fakten ableiten, nicht aus dem Manager-Flag
            String status;
            
            if (total > 0 && fertigCount == total) {
                // Wenn alle Produkte fertig sind, ist der Auftrag FERTIG.
                // Wir warten nicht auf den Manager-Stempel (b.istAbgeschlossen()).
                status = "FERTIG";
            } else if (fertigCount > 0) {
                status = "IN ARBEIT";
            } else {
                status = "WARTESCHLANGE";
            }

            orderTableModel.addRow(new Object[]{
                b.gibBestellungsNr(), 
                countStd, 
                countPrem, 
                fortschritt, 
                status
            });
        }
    }

    private void updateBar(JProgressBar bar, int current, int max) {
        bar.setMaximum(max);
        bar.setValue(current);
        bar.setString(current + " / " + max);
        if ((double)current / max < 0.2) bar.setForeground(Color.RED);
        else bar.setForeground(new Color(52, 152, 219));
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override public void write(int b) { updateTextArea(String.valueOf((char) b)); }
            @Override public void write(byte[] b, int off, int len) { updateTextArea(new String(b, off, len)); }
            @Override public void write(byte[] b) { write(b, 0, b.length); }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}