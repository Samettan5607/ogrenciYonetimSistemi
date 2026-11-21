package öğrenci;

class Ogrenci {
    private final String ogrenciNo;
    private final String ad;
    private final String soyad;
    private final String bolum;
    private final double vize;
    private final double finalNotu;
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

    private void hesapla() {
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

    // Öğrenci bilgileri ve hesaplanan sonuçlar
    public String getOgrenciNo() { return ogrenciNo; }

    public String getAd() { return ad; }

    public String getSoyad() { return soyad; }

    public String getBolum() { return bolum; }

    public double getVize() { return vize; }

    public double getFinalNotu() { return finalNotu; }

    public double getOrtalama() { return ortalama; }
    public String getHarfNotu() { return harfNotu; }
    public String getDurum() { return durum; }
}
