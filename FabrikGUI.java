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
 * FINAL VERSION: UX-Polish und Fehlerbehandlung integriert.
 *
 * @author GBI Gruppe 17
 * @version 29.12.2025
 */
public class FabrikGUI extends JFrame {

    private final Fabrik fabrik;
    private final Lager lager;

    // UI Komponenten
    private JSpinner spinStandard, spinPremium;
    private JProgressBar barHolz, barSchrauben, barFarbe, barKarton, barGlas;
    private DefaultTableModel orderTableModel;
    private JTextArea logArea;
    
    // Status Components
    private JLabel lblStatusIcon;
    private JLabel lblStatusText;
    private JPanel statusPanel;
    private JToggleButton btnPause;

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
        setSize(1250, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel header = new JLabel("AEKI FABRIK STATUS MONITOR", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        header.setOpaque(true);
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        add(header, BorderLayout.NORTH);

        // Control Panel (Links)
        add(createControlPanel(), BorderLayout.WEST);

        // Center Panel (Mitte: Lager + Tabelle)
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(createStockPanel());
        centerPanel.add(createOrderTablePanel());
        add(centerPanel, BorderLayout.CENTER);

        // Log Panel (Unten)
        add(createLogPanel(), BorderLayout.SOUTH);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(280, 0));

        // 1. Status Display
        panel.add(createStatusDisplay());
        panel.add(Box.createVerticalStrut(20));

        // 2. Auftragseingabe
        JPanel inputGroup = new JPanel();
        inputGroup.setLayout(new BoxLayout(inputGroup, BoxLayout.Y_AXIS));
        inputGroup.setBorder(BorderFactory.createTitledBorder("Neuer Auftrag"));
        inputGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputGroup.add(Box.createVerticalStrut(10));
        inputGroup.add(new JLabel("Standardtüren:"));
        spinStandard = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        spinStandard.setMaximumSize(new Dimension(150, 30));
        inputGroup.add(spinStandard);

        inputGroup.add(Box.createVerticalStrut(10));
        inputGroup.add(new JLabel("Premiumtüren:"));
        spinPremium = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        spinPremium.setMaximumSize(new Dimension(150, 30));
        inputGroup.add(spinPremium);

        inputGroup.add(Box.createVerticalStrut(20));
        JButton btnOrder = new JButton("PRODUKTION STARTEN");
        styleButton(btnOrder, new Color(39, 174, 96));
        btnOrder.addActionListener(e -> aufgebenBestellung());
        inputGroup.add(btnOrder);
        
        panel.add(inputGroup);

        // 3. Steuerung (Pause / Abbruch)
        panel.add(Box.createVerticalStrut(20));
        JPanel ctrlGroup = new JPanel();
        ctrlGroup.setLayout(new BoxLayout(ctrlGroup, BoxLayout.Y_AXIS));
        ctrlGroup.setBorder(BorderFactory.createTitledBorder("Steuerung"));
        ctrlGroup.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnPause = new JToggleButton("PRODUKTION PAUSIEREN");
        btnPause.setBackground(new Color(241, 196, 15)); // Gelb
        btnPause.setForeground(Color.BLACK);
        btnPause.setFocusPainted(false);
        btnPause.setMaximumSize(new Dimension(200, 40));
        btnPause.addActionListener(e -> togglePause());
        ctrlGroup.add(btnPause);

        ctrlGroup.add(Box.createVerticalStrut(10));

        JButton btnCancel = new JButton("ALLES ABBRECHEN");
        styleButton(btnCancel, new Color(192, 57, 43)); // Rot
        btnCancel.addActionListener(e -> storniereAlles());
        ctrlGroup.add(btnCancel);

        panel.add(ctrlGroup);

        return panel;
    }
    
    private void styleButton(JButton btn, Color bg) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 40));
    }

    private JPanel createStatusDisplay() {
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("System Status"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        statusPanel.setBackground(new Color(230, 255, 230)); 
        statusPanel.setMinimumSize(new Dimension(280, 120));
        statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblStatusIcon = new JLabel("✔"); 
        lblStatusIcon.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblStatusIcon.setForeground(new Color(39, 174, 96));
        lblStatusIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        lblStatusIcon.setVerticalAlignment(SwingConstants.TOP); 

        lblStatusText = new JLabel("<html>System bereit.</html>");
        lblStatusText.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        statusPanel.add(lblStatusIcon, BorderLayout.WEST);
        statusPanel.add(lblStatusText, BorderLayout.CENTER);
        return statusPanel;
    }

    private JPanel createStockPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Lagerbestand & Automatische Nachbestellung"));

        barHolz = createStyledProgressBar();
        barSchrauben = createStyledProgressBar();
        barFarbe = createStyledProgressBar();
        barKarton = createStyledProgressBar();
        barGlas = createStyledProgressBar();

        // Übergabe der Materialnamen und Initialwerte aus dem Lager
        panel.add(layoutBarPanel("Holz", barHolz, "Holz", lager.gibMinHolz(), lager.gibMaxHolz()));
        panel.add(layoutBarPanel("Schrauben", barSchrauben, "Schrauben", lager.gibMinSchrauben(), lager.gibMaxSchrauben()));
        panel.add(layoutBarPanel("Farbe", barFarbe, "Farbe", lager.gibMinFarbe(), lager.gibMaxFarbe()));
        panel.add(layoutBarPanel("Karton", barKarton, "Karton", lager.gibMinKarton(), lager.gibMaxKarton()));
        panel.add(layoutBarPanel("Glas", barGlas, "Glas", lager.gibMinGlas(), lager.gibMaxGlas()));

        return panel;
    }

    private JProgressBar createStyledProgressBar() {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setForeground(new Color(52, 152, 219));
        bar.setBackground(Color.WHITE);
        return bar;
    }

    private JPanel layoutBarPanel(String label, JProgressBar bar, String materialKey, int currentMin, int maxVal) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(80, 20));
        p.add(lbl, BorderLayout.WEST);
        p.add(bar, BorderLayout.CENTER);
        
        // Right: Spinner für Min-Wert
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        right.add(new JLabel("Min:"));
        JSpinner spinMin = new JSpinner(new SpinnerNumberModel(currentMin, 0, maxVal, 10));
        spinMin.setPreferredSize(new Dimension(70, 25));
        spinMin.addChangeListener(e -> {
            int newVal = (Integer) spinMin.getValue();
            lager.setzeMinBestand(materialKey, newVal);
        });
        right.add(spinMin);
        p.add(right, BorderLayout.EAST);
        
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

    // --- Actions ---

    private void aufgebenBestellung() {
        int std = (Integer) spinStandard.getValue();
        int prem = (Integer) spinPremium.getValue();

        // FIX: Benutzerfeedback bei leeren Eingaben
        if (std == 0 && prem == 0) {
            JOptionPane.showMessageDialog(this, 
                "Bitte wählen Sie mindestens eine Tür aus!", 
                "Keine Menge gewählt", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        fabrik.bestellungAufgeben(prem, std);
        spinStandard.setValue(0);
        spinPremium.setValue(0);
    }
    
    private void togglePause() {
        boolean pause = btnPause.isSelected();
        fabrik.setzeProduktionPausiert(pause);
        if(pause) {
            btnPause.setText("FORTSETZEN");
            btnPause.setBackground(new Color(230, 126, 34)); // Orange
        } else {
            btnPause.setText("PRODUKTION PAUSIEREN");
            btnPause.setBackground(new Color(241, 196, 15)); // Gelb
        }
    }
    
    private void storniereAlles() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Wirklich alle ausstehenden Bestellungen abbrechen?", "Warnung", JOptionPane.YES_NO_OPTION);
            
        if(confirm == JOptionPane.YES_OPTION) {
            fabrik.stornierePendenteBestellungen();
            JOptionPane.showMessageDialog(this, "Alle wartenden Aufträge wurden storniert.");
        }
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
    
    private void updateStatusDisplay() {
        // Priorität 1: Ist Pause?
        if (btnPause.isSelected()) {
            setStatus("⏸", "PRODUKTION PAUSIERT", new Color(255, 243, 205), Color.ORANGE);
            return;
        }

        // Priorität 2: Wartende LKW an der Rampe (Critical!)
        int wartendeLKW = lager.gibWartendeLKW();
        if (wartendeLKW > 0) {
            setStatus("⚠️", "<html><b>RAMPE VOLL!</b><br>" + wartendeLKW + " LKW warten auf Entladung.</html>", 
                      new Color(255, 200, 200), Color.RED);
            return;
        }

        // Priorität 3: Lieferungen unterwegs
        Lieferant lieferant = fabrik.gibLieferant();
        if(lieferant != null) {
            List<Long> etas = lieferant.gibVerbleibendeSekundenListe();
            if (!etas.isEmpty()) {
                StringBuilder sb = new StringBuilder("<html><b>LIEFERUNGEN UNTERWEGS: " + etas.size() + "</b><br>");
                sb.append("<table border='0' cellspacing='0' cellpadding='0'>");
                int count = 1;
                for (Long sek : etas) {
                    sb.append("<tr><td>LKW ").append(count).append(":</td><td width='10'></td><td><b>")
                      .append(sek).append(" s</b></td></tr>");
                    count++;
                    if(count > 4) { sb.append("<tr><td>...</td></tr>"); break; }
                }
                sb.append("</table></html>");
                setStatus("🚛", sb.toString(), new Color(220, 240, 255), new Color(41, 128, 185));
                return;
            }
        }
        
        // Normalzustand
        setStatus("✔", "<html>System bereit.<br>Produktion läuft normal.</html>", 
                  new Color(230, 255, 230), new Color(39, 174, 96));
    }
    
    private void setStatus(String icon, String text, Color bg, Color fg) {
        statusPanel.setBackground(bg);
        lblStatusIcon.setText(icon);
        lblStatusIcon.setForeground(fg);
        lblStatusText.setText(text);
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
        
        for (int i = bestellungen.size() - 1; i >= 0; i--) {
            Bestellung b = bestellungen.get(i);
            
            int countStd = 0; int countPrem = 0;
            for(Produkt p : b.liefereBestellteProdukte()) {
                if(p instanceof Standardtuer) countStd++; else countPrem++;
            }

            long fertigCount = b.liefereBestellteProdukte().stream().filter(Produkt::istFertig).count();
            int total = b.liefereBestellteProdukte().size();
            String fortschritt = fertigCount + " / " + total;
            
            String status;
            if (b.istStorniert()) {
                status = "ABGEBROCHEN";
            } else if (total > 0 && fertigCount == total) {
                status = "FERTIG";
            } else if (fertigCount > 0) {
                status = "IN ARBEIT";
            } else {
                status = "WARTESCHLANGE";
            }

            orderTableModel.addRow(new Object[]{
                b.gibBestellungsNr(), countStd, countPrem, fortschritt, status
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