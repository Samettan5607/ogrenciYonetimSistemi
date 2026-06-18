package öğrenci;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// --- OGRENCI SINIFI ---
class Ogrenci {
    private String ogrenciNo;
    private String ad;
    private String soyad;
    private String bolum;
    private double vize;
    private double finalNotu;
    private double ortalama;
    private String harfNotu;
    private String durum;

    public Ogrenci(String ogrenciNo, String ad, String soyad, String bolum, double vize, double finalNotu) {
        this.ogrenciNo = ogrenciNo;
        this.ad = ad;
        this.soyad = soyad;
        this.bolum = bolum;
        this.vize = vize;
        this.finalNotu = finalNotu;
        hesapla();
    }

    public void hesapla() {
        this.ortalama = this.vize * 0.4 + this.finalNotu * 0.6;
        
        if (this.ortalama >= 90) this.harfNotu = "AA";
        else if (this.ortalama >= 85) this.harfNotu = "BA";
        else if (this.ortalama >= 80) this.harfNotu = "BB";
        else if (this.ortalama >= 75) this.harfNotu = "CB";
        else if (this.ortalama >= 70) this.harfNotu = "CC";
        else if (this.ortalama >= 60) this.harfNotu = "DC";
        else if (this.ortalama >= 50) this.harfNotu = "DD";
        else if (this.ortalama >= 40) this.harfNotu = "FD";
        else this.harfNotu = "FF";

        if (this.ortalama >= 50 && this.finalNotu >= 50) {
            this.durum = "Geçti";
        } else {
            this.durum = "Kaldı";
        }
    }

    // Getters & Setters
    public String getOgrenciNo() { return ogrenciNo; }
    public void setOgrenciNo(String ogrenciNo) { this.ogrenciNo = ogrenciNo; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public String getBolum() { return bolum; }
    public void setBolum(String bolum) { this.bolum = bolum; }

    public double getVize() { return vize; }
    public void setVize(double vize) { this.vize = vize; hesapla(); }

    public double getFinalNotu() { return finalNotu; }
    public void setFinalNotu(double finalNotu) { this.finalNotu = finalNotu; hesapla(); }

    public double getOrtalama() { return ortalama; }
    public String getHarfNotu() { return harfNotu; }
    public String getDurum() { return durum; }
}

// --- VERITABANI SINIFI ---
class VeriTabani {
    private final List<Ogrenci> ogrenciler = new ArrayList<>();
    private final String dosyaYolu;

    public VeriTabani() {
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        this.dosyaYolu = desktopPath + File.separator + "ogrenciler.txt";
        verileriYukle();
    }

    public List<Ogrenci> getOgrenciler() {
        return ogrenciler;
    }

    public void ekle(Ogrenci ogrenci) {
        for (Ogrenci o : ogrenciler) {
            if (o.getOgrenciNo().equalsIgnoreCase(ogrenci.getOgrenciNo())) {
                throw new IllegalArgumentException("Bu öğrenci numarası zaten mevcut!");
            }
        }
        ogrenciler.add(ogrenci);
        verileriKaydet();
    }

    public void sil(String ogrenciNo) {
        ogrenciler.removeIf(o -> o.getOgrenciNo().equalsIgnoreCase(ogrenciNo));
        verileriKaydet();
    }

    public void guncelle(String eskiNo, Ogrenci yeniOgrenci) {
        for (int i = 0; i < ogrenciler.size(); i++) {
            if (ogrenciler.get(i).getOgrenciNo().equalsIgnoreCase(eskiNo)) {
                if (!eskiNo.equalsIgnoreCase(yeniOgrenci.getOgrenciNo())) {
                    for (Ogrenci o : ogrenciler) {
                        if (o.getOgrenciNo().equalsIgnoreCase(yeniOgrenci.getOgrenciNo())) {
                            throw new IllegalArgumentException("Yeni öğrenci numarası zaten başka bir öğrencide mevcut!");
                        }
                    }
                }
                ogrenciler.set(i, yeniOgrenci);
                verileriKaydet();
                return;
            }
        }
    }

    public void verileriKaydet() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dosyaYolu), "UTF-8"))) {
            for (Ogrenci o : ogrenciler) {
                bw.write(String.format("%s;%s;%s;%s;%.2f;%.2f\n",
                        o.getOgrenciNo(), o.getAd(), o.getSoyad(), o.getBolum(), o.getVize(), o.getFinalNotu()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verileriYukle() {
        File file = new File(dosyaYolu);
        if (!file.exists()) return;
        ogrenciler.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    try {
                        String no = parts[0];
                        String ad = parts[1];
                        String soyad = parts[2];
                        String bolum = parts[3];
                        double vize = Double.parseDouble(parts[4].replace(",", "."));
                        double fnal = Double.parseDouble(parts[5].replace(",", "."));
                        ogrenciler.add(new Ogrenci(no, ad, soyad, bolum, vize, fnal));
                    } catch (NumberFormatException e) {
                        // geçersiz satırları atla
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// --- MODERNIZE EDILMIS YUMUSAK KENARLI BUTON ---
class RoundedButton extends JButton {
    private final Color normalColor;
    private final Color hoverColor;

    public RoundedButton(String text, Color bg, Color fg) {
        super(text);
        this.normalColor = bg;
        this.hoverColor = bg.brighter();
        setForeground(fg);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }
        });
        setBackground(bg);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g2);
        g2.dispose();
    }
}

// --- MODERNIZE EDILMIS YUMUSAK KENARLI METIN ALANI (TEXTFIELD) ---
class RoundedTextField extends JTextField {
    private final Color focusColor = new Color(26, 188, 156);  // Turkuaz
    private final Color normalColor = new Color(220, 221, 225); // Açık gri
    private boolean isFocused = false;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }
            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Arka plan rengini çiz
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        
        // Çerçeveyi çiz
        g2.setColor(isFocused ? focusColor : normalColor);
        g2.setStroke(new BasicStroke(isFocused ? 1.5f : 1.0f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        
        g2.dispose();
        super.paintComponent(g);
    }
}

// --- SOL MENÜ (SIDEBAR) BUTONU ---
class SidebarButton extends JButton {
    private boolean active = false;
    private final Color activeBg = new Color(52, 73, 94);       // Seçili koyu gri
    private final Color hoverBg = new Color(52, 73, 94).brighter();
    private final Color normalBg = new Color(44, 62, 80);        // Varsayılan
    private final Color accentColor = new Color(26, 188, 156);   // Sol şerit turkuazı

    public SidebarButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(14, 25, 14, 25));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) setBackground(hoverBg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) setBackground(normalBg);
            }
        });
        setBackground(normalBg);
    }

    public void setActive(boolean active) {
        this.active = active;
        setBackground(active ? activeBg : normalBg);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Arka planı boya
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        // Seçiliyse sol tarafa turkuaz gösterge çizgisi çiz
        if (active) {
            g2.setColor(accentColor);
            g2.fillRect(0, 0, 5, getHeight());
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
}

// --- TASARIMSAL DÜZGÜN GÖRÜNEN KPI KARTI ---
class KpiCard extends JPanel {
    public KpiCard(String title, JLabel valLabel, Color color) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setOpaque(false);

        // İç kart paneli (Kenarları yuvarlatılmış ve sol tarafında renk bandı bulunan)
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Beyaz kart gövdesi
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                
                // İnce dış çerçeve
                g2.setColor(new Color(230, 230, 230));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                
                // Sol taraftaki kalın renkli şerit
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 6, getHeight() - 1, 14, 14);
                g2.fillRect(3, 0, 3, getHeight() - 1); // İç sağ köşenin düz olmasını sağlar
                
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel content = new JPanel(new GridLayout(2, 1, 4, 4));
        content.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(new Color(149, 165, 166));
        content.add(lblTitle);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLabel.setForeground(color);
        content.add(valLabel);

        mainPanel.add(content, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
}

// --- BASARI ORANI GRAFIK BILESENI ---
class BasariBar extends JPanel {
    private double gecmeOrani = 0.0;

    public void setOran(double oran) {
        this.gecmeOrani = oran;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        int barHeight = 28;
        int barY = (h - barHeight) / 2;
        int barWidth = w - 40;
        int barX = 20;

        // Arka plan kutusu (Kaldı kısmı - Mercan Kırmızısı)
        g2.setColor(new Color(231, 76, 60));
        g2.fillRoundRect(barX, barY, barWidth, barHeight, 14, 14);

        // Ön plan kutusu (Geçti kısmı - Zümrüt Yeşili)
        int passedWidth = (int) (barWidth * gecmeOrani);
        if (passedWidth > 0) {
            g2.setColor(new Color(46, 204, 113));
            g2.fillRoundRect(barX, barY, passedWidth, barHeight, 14, 14);
        }

        // Metin çizimi
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String text = String.format("Geçti: %.1f%%  |  Kaldı: %.1f%%", gecmeOrani * 100, (1 - gecmeOrani) * 100);
        FontMetrics fm = g2.getFontMetrics();
        int textX = barX + (barWidth - fm.stringWidth(text)) / 2;
        int textY = barY + (barHeight - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, textX, textY);
    }
}

// --- ANA UYGULAMA SINIFI ---
public class OgrenciYonetimSistemi extends JFrame {
    private final VeriTabani veriTabani;
    
    // Panel ve Layout Yönetimi
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private SidebarButton btnDash;
    private SidebarButton btnList;
    private SidebarButton btnForm;

    // View Başlık Etiketleri (Form Başlığı güncellemesi için saklanmıştır)
    private JLabel lblFormHeaderTitle;
    private JLabel lblFormHeaderSub;
    
    // UI Bileşenleri
    private DefaultTableModel tableModel;
    private JTable table;
    
    // Arama alanları
    private RoundedTextField txtSearch;
    private JComboBox<String> cmbSearchCriteria;

    // Dashboard Etiketleri
    private JLabel lblTotalStudents;
    private JLabel lblClassAverage;
    private JLabel lblPassedCount;
    private JLabel lblFailedCount;
    private BasariBar basariBar;

    // Form Alanları
    private RoundedTextField txtNo;
    private RoundedTextField txtAd;
    private RoundedTextField txtSoyad;
    private RoundedTextField txtBolum;
    private RoundedTextField txtVize;
    private RoundedTextField txtFinal;
    private RoundedButton btnSave;
    private RoundedButton btnClear;

    // Düzenleme Durumu
    private boolean isEditMode = false;
    private String editingOgrenciNo = "";

    // Renk Teması
    private final Color primaryColor = new Color(44, 62, 80);    // Lacivert
    private final Color accentColor = new Color(26, 188, 156);   // Turkuaz
    private final Color dangerColor = new Color(231, 76, 60);    // Kırmızı
    private final Color infoColor = new Color(52, 152, 219);     // Mavi
    private final Color bgColor = new Color(245, 246, 250);       // Açık Gri
    
    public OgrenciYonetimSistemi() {
        veriTabani = new VeriTabani();

        // Pencere ayarları
        setTitle("Gelişmiş Öğrenci Yönetim Sistemi - Yönetici Paneli");
        setSize(1020, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // System Look & Feel ayarla
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // --- SOL MENÜ (SIDEBAR) ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(primaryColor);
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Logo Alanı
        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setBackground(new Color(34, 49, 63));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        JLabel lblBrand = new JLabel("ÖĞRENCİ SİSTEMİ");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setHorizontalAlignment(SwingConstants.CENTER);
        brandPanel.add(lblBrand, BorderLayout.CENTER);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Menü Butonları
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 0, 2));
        menuPanel.setBackground(primaryColor);
        
        btnDash = new SidebarButton(" Gösterge Paneli");
        btnList = new SidebarButton(" Öğrenci Listesi");
        btnForm = new SidebarButton(" Yeni Öğrenci Ekle");

        menuPanel.add(btnDash);
        menuPanel.add(btnList);
        menuPanel.add(btnForm);

        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(primaryColor);
        menuWrapper.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(menuWrapper, BorderLayout.CENTER);

        // Sidebar Alt Bilgi (Footer)
        JLabel lblFooter = new JLabel("v1.2.0 | ÖYS");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(127, 140, 141));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        sidebar.add(lblFooter, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // --- SAĞ TARAF (CARDPANEL) ---
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Sekme (View) panellerini oluştur
        JPanel dashView = createDashboardView();
        JPanel listView = createListView();
        JPanel formView = createFormView();

        cardPanel.add(dashView, "DASHBOARD");
        cardPanel.add(listView, "LIST");
        cardPanel.add(formView, "FORM");

        add(cardPanel, BorderLayout.CENTER);

        // Buton Eylemleri - Kartlar arası geçiş
        btnDash.addActionListener(e -> {
            cardLayout.show(cardPanel, "DASHBOARD");
            setActiveButton(btnDash);
        });
        btnList.addActionListener(e -> {
            cardLayout.show(cardPanel, "LIST");
            setActiveButton(btnList);
        });
        btnForm.addActionListener(e -> {
            cardLayout.show(cardPanel, "FORM");
            setActiveButton(btnForm);
        });

        // Varsayılan sekmeyi aktif yap
        setActiveButton(btnDash);

        // Verileri ilk defa yükle
        refreshData();

        // Göster
        setVisible(true);
    }

    private void setActiveButton(SidebarButton activeBtn) {
        btnDash.setActive(btnDash == activeBtn);
        btnList.setActive(btnList == activeBtn);
        btnForm.setActive(btnForm == activeBtn);
    }

    // --- GÖRÜNÜMLERİN (VIEWS) OLUŞTURULMASI ---

    private JPanel createDashboardView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        // View Header
        panel.add(createViewHeader("Gösterge Paneli", "Sistemdeki öğrencilerin genel başarı durumunu ve not dağılım grafiklerini buradan inceleyin."), BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new BorderLayout(0, 20));
        body.setBackground(bgColor);
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 4'lü KPI Kartları Çubuğu (Yatay)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setBackground(bgColor);

        lblTotalStudents = new JLabel("0");
        lblClassAverage = new JLabel("0.00");
        lblPassedCount = new JLabel("0");
        lblFailedCount = new JLabel("0");

        cardsPanel.add(new KpiCard("TOPLAM ÖĞRENCİ", lblTotalStudents, infoColor));
        cardsPanel.add(new KpiCard("SINIF ORTALAMASI", lblClassAverage, new Color(155, 89, 182))); // Mor
        cardsPanel.add(new KpiCard("GEÇEN ÖĞRENCİ", lblPassedCount, accentColor));
        cardsPanel.add(new KpiCard("KALAN ÖĞRENCİ", lblFailedCount, dangerColor));

        body.add(cardsPanel, BorderLayout.NORTH);

        // Başarı Oranı Kartı
        JPanel chartCard = new JPanel(new BorderLayout());
        chartCard.setBackground(Color.WHITE);
        chartCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblChartTitle = new JLabel("Genel Sınıf Başarı Oranı Grafiği");
        lblChartTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblChartTitle.setForeground(primaryColor);
        lblChartTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        chartCard.add(lblChartTitle, BorderLayout.NORTH);

        basariBar = new BasariBar();
        basariBar.setPreferredSize(new Dimension(0, 60));
        basariBar.setBackground(Color.WHITE);
        chartCard.add(basariBar, BorderLayout.CENTER);

        body.add(chartCard, BorderLayout.CENTER);

        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        // View Header
        panel.add(createViewHeader("Öğrenci Listesi", "Kayıtlı öğrencilerin listesini görüntüleyin, arama yapın, notlarını güncelleyin veya kayıtları silin."), BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(bgColor);
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Arama ve Filtreleme Üst Paneli (Modern ve Yumuşak Kenarlı)
        JPanel searchCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        searchCard.setBackground(Color.WHITE);
        searchCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        searchCard.add(new JLabel("Arama Kriteri:"));
        cmbSearchCriteria = new JComboBox<>(new String[]{"Tümü", "Öğrenci No", "Ad / Soyad", "Bölüm"});
        cmbSearchCriteria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchCard.add(cmbSearchCriteria);

        searchCard.add(new JLabel("Arama Kelimesi:"));
        txtSearch = new RoundedTextField(20);
        searchCard.add(txtSearch);

        RoundedButton btnSearch = new RoundedButton("Ara", primaryColor, Color.WHITE);
        RoundedButton btnClearSearch = new RoundedButton("Temizle", new Color(127, 140, 141), Color.WHITE);
        searchCard.add(btnSearch);
        searchCard.add(btnClearSearch);

        body.add(searchCard, BorderLayout.NORTH);

        // Tablo Oluşturma
        String[] columns = {"Öğrenci No", "Ad", "Soyad", "Bölüm", "Vize", "Final", "Ortalama", "Harf Notu", "Durum"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Satır renklendirmesini destekleyen özelleştirilmiş JTable
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                return comp;
            }
        };
        
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(209, 242, 235)); // Açık turkuaz seçili rengi
        table.setSelectionForeground(primaryColor);

        // Tablo başlığını özelleştir
        JTableHeader header = table.getTableHeader();
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(100, 36));

        // Hücre hizalamalarını ortalama
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1 && i != 2) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Durum sütunu için özel renklendirme
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, isSel, hasFocus, r, c);
                setHorizontalAlignment(JLabel.CENTER);
                if (val != null) {
                    if (val.toString().equals("Geçti")) {
                        comp.setForeground(new Color(46, 204, 113));
                        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
                    } else {
                        comp.setForeground(dangerColor);
                        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
                    }
                }
                if (isSel) {
                    comp.setBackground(t.getSelectionBackground());
                } else {
                    comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                return comp;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        body.add(scrollPane, BorderLayout.CENTER);

        // Alt Eylem Butonları
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(bgColor);

        RoundedButton btnEdit = new RoundedButton("Seçileni Düzenle", infoColor, Color.WHITE);
        RoundedButton btnDelete = new RoundedButton("Seçileni Sil", dangerColor, Color.WHITE);

        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        body.add(actionPanel, BorderLayout.SOUTH);

        panel.add(body, BorderLayout.CENTER);

        // Buton Eylemleri
        btnSearch.addActionListener(e -> performSearch());
        txtSearch.addActionListener(e -> performSearch());
        
        btnClearSearch.addActionListener(e -> {
            txtSearch.setText("");
            cmbSearchCriteria.setSelectedIndex(0);
            refreshData();
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz öğrenciyi tablodan seçin.", "Seçim Yok", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String no = table.getValueAt(selectedRow, 0).toString();
            String ad = table.getValueAt(selectedRow, 1).toString();
            String soyad = table.getValueAt(selectedRow, 2).toString();

            int confirm = JOptionPane.showConfirmDialog(this, 
                String.format("%s %s (%s) isimli öğrenciyi silmek istediğinize emin misiniz?", ad, soyad, no),
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                veriTabani.sil(no);
                refreshData();
                JOptionPane.showMessageDialog(this, "Öğrenci kaydı başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Lütfen düzenlemek istediğiniz öğrenciyi tablodan seçin.", "Seçim Yok", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String no = table.getValueAt(selectedRow, 0).toString();
            
            Ogrenci ogrenci = veriTabani.getOgrenciler().stream()
                .filter(o -> o.getOgrenciNo().equals(no))
                .findFirst().orElse(null);

            if (ogrenci != null) {
                // Form alanlarını doldur
                txtNo.setText(ogrenci.getOgrenciNo());
                txtNo.setEditable(false);
                txtAd.setText(ogrenci.getAd());
                txtSoyad.setText(ogrenci.getSoyad());
                txtBolum.setText(ogrenci.getBolum());
                txtVize.setText(String.valueOf(ogrenci.getVize()));
                txtFinal.setText(String.valueOf(ogrenci.getFinalNotu()));

                // Modu değiştir
                isEditMode = true;
                editingOgrenciNo = no;
                
                lblFormHeaderTitle.setText("Öğrenci Kaydı Düzenle");
                lblFormHeaderSub.setText("Mevcut öğrencinin bilgilerini ve dönem notlarını güncelleyin.");
                btnSave.setText("Güncelle");
                btnClear.setText("Düzenlemeyi İptal Et");

                // Form kartını göster
                cardLayout.show(cardPanel, "FORM");
                setActiveButton(btnForm);
            }
        });

        return panel;
    }

    private JPanel createFormView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        // Form Başlığı Konteyneri (Başlık ve Açıklama)
        lblFormHeaderTitle = new JLabel("Yeni Öğrenci Ekle");
        lblFormHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormHeaderTitle.setForeground(primaryColor);

        lblFormHeaderSub = new JLabel("Sisteme yeni bir öğrenci kaydetmek için aşağıdaki formu doldurun.");
        lblFormHeaderSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFormHeaderSub.setForeground(new Color(127, 140, 141));

        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(Color.WHITE);
        formHeaderPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 221, 225)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        formHeaderPanel.add(lblFormHeaderTitle, BorderLayout.NORTH);
        formHeaderPanel.add(lblFormHeaderSub, BorderLayout.SOUTH);
        panel.add(formHeaderPanel, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        body.setBackground(bgColor);

        // Modern Form Kartı
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        // Form alanlarının oluşturulması
        txtNo = new RoundedTextField(22);
        txtAd = new RoundedTextField(22);
        txtSoyad = new RoundedTextField(22);
        txtBolum = new RoundedTextField(22);
        txtVize = new RoundedTextField(22);
        txtFinal = new RoundedTextField(22);

        // Form Satırlarını Doldur
        addFormRow(formCard, "Öğrenci Numarası (*)", txtNo, gbc, 0);
        addFormRow(formCard, "Ad (*)", txtAd, gbc, 2);
        addFormRow(formCard, "Soyad (*)", txtSoyad, gbc, 4);
        addFormRow(formCard, "Bölüm (*)", txtBolum, gbc, 6);
        addFormRow(formCard, "Vize Notu (0-100)", txtVize, gbc, 8);
        addFormRow(formCard, "Final Notu (0-100)", txtFinal, gbc, 10);

        // Kaydet ve Temizle Butonları
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setBackground(Color.WHITE);
        
        btnSave = new RoundedButton("Kaydet", accentColor, Color.WHITE);
        btnClear = new RoundedButton("Temizle", new Color(127, 140, 141), Color.WHITE);

        btnPanel.add(btnClear);
        btnPanel.add(btnSave);

        gbc.gridy = 12;
        gbc.insets = new Insets(20, 0, 0, 0);
        formCard.add(btnPanel, gbc);

        body.add(formCard);
        panel.add(body, BorderLayout.CENTER);

        // Eylemler
        btnSave.addActionListener(e -> saveForm());
        btnClear.addActionListener(e -> clearForm());

        return panel;
    }

    // --- YARDIMCI VE DESTEKLEYİCİ METOTLAR ---

    private JPanel createViewHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 221, 225)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        header.add(lblTitle, BorderLayout.NORTH);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(127, 140, 141));
        header.add(lblSub, BorderLayout.SOUTH);

        return header;
    }

    private void addFormRow(JPanel container, String labelText, JTextField textField, GridBagConstraints gbc, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.insets = new Insets(6, 0, 3, 0);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(primaryColor);
        container.add(label, gbc);

        gbc.gridy = gridy + 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        container.add(textField, gbc);
    }

    private void refreshData() {
        veriTabani.verileriYukle();
        populateTable(veriTabani.getOgrenciler());
        updateDashboard();
    }

    private void populateTable(List<Ogrenci> list) {
        tableModel.setRowCount(0);
        for (Ogrenci o : list) {
            tableModel.addRow(new Object[]{
                o.getOgrenciNo(), o.getAd(), o.getSoyad(), o.getBolum(),
                o.getVize(), o.getFinalNotu(), String.format("%.2f", o.getOrtalama()),
                o.getHarfNotu(), o.getDurum()
            });
        }
    }

    private void updateDashboard() {
        List<Ogrenci> list = veriTabani.getOgrenciler();
        int total = list.size();
        double totalAvg = 0;
        int passed = 0;
        int failed = 0;

        for (Ogrenci o : list) {
            totalAvg += o.getOrtalama();
            if (o.getDurum().equals("Geçti")) {
                passed++;
            } else {
                failed++;
            }
        }

        double average = total > 0 ? totalAvg / total : 0.0;
        double passRate = total > 0 ? (double) passed / total : 0.0;

        lblTotalStudents.setText(String.valueOf(total));
        lblClassAverage.setText(String.format("%.2f", average));
        lblPassedCount.setText(String.valueOf(passed));
        lblFailedCount.setText(String.valueOf(failed));

        basariBar.setOran(passRate);
    }

    private void performSearch() {
        String query = txtSearch.getText().trim().toLowerCase();
        int criteria = cmbSearchCriteria.getSelectedIndex();

        if (query.isEmpty()) {
            refreshData();
            return;
        }

        List<Ogrenci> filtered = veriTabani.getOgrenciler().stream()
            .filter(o -> {
                switch (criteria) {
                    case 1:
                        return o.getOgrenciNo().toLowerCase().contains(query);
                    case 2:
                        String tamAd = (o.getAd() + " " + o.getSoyad()).toLowerCase();
                        return tamAd.contains(query);
                    case 3:
                        return o.getBolum().toLowerCase().contains(query);
                    default:
                        return o.getOgrenciNo().toLowerCase().contains(query) ||
                               (o.getAd() + " " + o.getSoyad()).toLowerCase().contains(query) ||
                               o.getBolum().toLowerCase().contains(query);
                }
            })
            .collect(Collectors.toList());

        populateTable(filtered);
    }

    private void saveForm() {
        String no = txtNo.getText().trim();
        String ad = txtAd.getText().trim();
        String soyad = txtSoyad.getText().trim();
        String bolum = txtBolum.getText().trim();
        String vizeStr = txtVize.getText().trim();
        String finalStr = txtFinal.getText().trim();

        if (no.isEmpty() || ad.isEmpty() || soyad.isEmpty() || bolum.isEmpty() || vizeStr.isEmpty() || finalStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm zorunlu (*) alanları doldurun.", "Eksik Bilgi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double vize, fnal;
        try {
            vize = Double.parseDouble(vizeStr.replace(",", "."));
            fnal = Double.parseDouble(finalStr.replace(",", "."));

            if (vize < 0 || vize > 100 || fnal < 0 || fnal > 100) {
                JOptionPane.showMessageDialog(this, "Notlar 0 ile 100 arasında olmalıdır.", "Hatalı Not", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lütfen vize ve final notları için geçerli sayısal değerler girin.", "Geçersiz Not Formatı", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Ogrenci yeniOgrenci = new Ogrenci(no, ad, soyad, bolum, vize, fnal);

            if (isEditMode) {
                veriTabani.guncelle(editingOgrenciNo, yeniOgrenci);
                JOptionPane.showMessageDialog(this, "Öğrenci kaydı başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {
                veriTabani.ekle(yeniOgrenci);
                JOptionPane.showMessageDialog(this, "Yeni öğrenci başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }

            clearForm();
            refreshData();
            
            // Öğrenci listesi görünümüne geç
            cardLayout.show(cardPanel, "LIST");
            setActiveButton(btnList);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtNo.setText("");
        txtNo.setEditable(true);
        txtAd.setText("");
        txtSoyad.setText("");
        txtBolum.setText("");
        txtVize.setText("");
        txtFinal.setText("");

        if (isEditMode) {
            isEditMode = false;
            editingOgrenciNo = "";
            lblFormHeaderTitle.setText("Yeni Öğrenci Ekle");
            lblFormHeaderSub.setText("Sisteme yeni bir öğrenci kaydetmek için aşağıdaki formu doldurun.");
            btnSave.setText("Kaydet");
            btnClear.setText("Temizle");
            
            cardLayout.show(cardPanel, "LIST");
            setActiveButton(btnList);
        }
    }

    // --- MAIN ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OgrenciYonetimSistemi());
    }
}
