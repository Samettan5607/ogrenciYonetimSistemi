package öğrenci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Öğrenci kayıtlarını yerel UTF-8 dosyasında saklar. */
class VeriTabani {
    private final Path dosyaYolu;
    private List<Ogrenci> ogrenciler;

    VeriTabani() throws IOException {
        this(Path.of(System.getProperty("user.home"), "Desktop", "ogrenciler.txt"));
    }

    VeriTabani(Path dosyaYolu) throws IOException {
        this.dosyaYolu = dosyaYolu.toAbsolutePath();
        this.ogrenciler = verileriYukle();
    }

    List<Ogrenci> getOgrenciler() {
        return Collections.unmodifiableList(ogrenciler);
    }

    void ekle(Ogrenci ogrenci) throws IOException {
        kaydiDogrula(ogrenci);
        if (kayitIndeksi(ogrenci.getOgrenciNo()) >= 0) {
            throw new IllegalArgumentException("Bu öğrenci numarası zaten mevcut.");
        }
        List<Ogrenci> yeniListe = new ArrayList<>(ogrenciler);
        yeniListe.add(ogrenci);
        verileriKaydet(yeniListe);
    }

    void sil(String ogrenciNo) throws IOException {
        int indeks = kayitIndeksi(ogrenciNo);
        if (indeks >= 0) {
            List<Ogrenci> yeniListe = new ArrayList<>(ogrenciler);
            yeniListe.remove(indeks);
            verileriKaydet(yeniListe);
        }
    }

    void guncelle(String eskiNo, Ogrenci yeniOgrenci) throws IOException {
        kaydiDogrula(yeniOgrenci);
        int indeks = kayitIndeksi(eskiNo);
        if (indeks < 0) {
            throw new IllegalArgumentException("Güncellenecek öğrenci bulunamadı.");
        }
        int yeniIndeks = kayitIndeksi(yeniOgrenci.getOgrenciNo());
        if (yeniIndeks >= 0 && yeniIndeks != indeks) {
            throw new IllegalArgumentException("Bu öğrenci numarası başka bir kayıtta kullanılıyor.");
        }
        List<Ogrenci> yeniListe = new ArrayList<>(ogrenciler);
        yeniListe.set(indeks, yeniOgrenci);
        verileriKaydet(yeniListe);
    }

    private int kayitIndeksi(String ogrenciNo) {
        for (int i = 0; i < ogrenciler.size(); i++) {
            if (ogrenciler.get(i).getOgrenciNo().equalsIgnoreCase(ogrenciNo)) {
                return i;
            }
        }
        return -1;
    }

    static boolean gecerliNot(double not) {
        return not >= 0 && not <= 100;
    }

    private static void kaydiDogrula(Ogrenci ogrenci) {
        String[] alanlar = {ogrenci.getOgrenciNo(), ogrenci.getAd(), ogrenci.getSoyad(), ogrenci.getBolum()};
        for (String alan : alanlar) {
            if (alan.isBlank() || alan.indexOf(';') >= 0 || alan.indexOf('\n') >= 0 || alan.indexOf('\r') >= 0) {
                throw new IllegalArgumentException("Öğrenci bilgileri boş olamaz ve noktalı virgül veya satır sonu içeremez.");
            }
        }
        if (!gecerliNot(ogrenci.getVize()) || !gecerliNot(ogrenci.getFinalNotu())) {
            throw new IllegalArgumentException("Notlar 0 ile 100 arasında olmalıdır.");
        }
    }

    private void verileriKaydet(List<Ogrenci> yeniListe) throws IOException {
        Path klasor = dosyaYolu.getParent();
        try {
            Files.createDirectories(klasor);
            Path geciciDosya = Files.createTempFile(klasor, "ogrenciler-", ".tmp");
            try {
                try (BufferedWriter yazici = Files.newBufferedWriter(geciciDosya)) {
                    for (Ogrenci ogrenci : yeniListe) {
                        yazici.write(String.join(";", ogrenci.getOgrenciNo(), ogrenci.getAd(),
                                ogrenci.getSoyad(), ogrenci.getBolum(), Double.toString(ogrenci.getVize()),
                                Double.toString(ogrenci.getFinalNotu())));
                        yazici.newLine();
                    }
                }
                // Bellekteki listeyi ancak dosya başarıyla değiştirildikten sonra güncelle.
                Files.move(geciciDosya, dosyaYolu, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                ogrenciler = yeniListe;
            } catch (IOException ex) {
                try {
                    Files.deleteIfExists(geciciDosya);
                } catch (IOException temizlemeHatasi) {
                    ex.addSuppressed(temizlemeHatasi);
                }
                throw ex;
            }
        } catch (IOException ex) {
            throw new IOException("Öğrenci dosyası kaydedilemedi.\n" + dosyaYolu + "\n" + ex.getMessage(), ex);
        }
    }

    private List<Ogrenci> verileriYukle() throws IOException {
        List<Ogrenci> kayitlar = new ArrayList<>();
        try (BufferedReader okuyucu = Files.newBufferedReader(dosyaYolu)) {
            String satir;
            int satirNo = 0;
            while ((satir = okuyucu.readLine()) != null) {
                satirNo++;
                if (satir.isBlank()) {
                    continue;
                }
                try {
                    String[] alanlar = satir.split(";", -1);
                    if (alanlar.length != 6) {
                        throw new IllegalArgumentException("Altı alan bekleniyor.");
                    }
                    Ogrenci ogrenci = new Ogrenci(alanlar[0], alanlar[1], alanlar[2], alanlar[3],
                            Double.parseDouble(alanlar[4].replace(',', '.')),
                            Double.parseDouble(alanlar[5].replace(',', '.')));
                    kaydiDogrula(ogrenci);
                    kayitlar.add(ogrenci);
                } catch (IllegalArgumentException ex) {
                    throw new IOException(satirNo + ". satır geçersiz. " + ex.getMessage(), ex);
                }
            }
        } catch (NoSuchFileException ex) {
            return kayitlar;
        } catch (IOException ex) {
            throw new IOException("Öğrenci dosyası okunamadı.\n" + dosyaYolu + "\n" + ex.getMessage(), ex);
        }
        return kayitlar;
    }
}
