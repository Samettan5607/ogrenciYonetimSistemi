# 🎓 Gelişmiş Öğrenci Yönetim Sistemi (ÖYS)

Java Swing kullanılarak geliştirilmiş, modern tasarıma, veri kalıcılığına ve akıllı not hesaplama özelliklerine sahip gelişmiş bir **Öğrenci Yönetim Sistemi** (Student Management System) uygulamasıdır.

---

## ✨ Özellikler

- **Modern Yönetici Paneli (Dashboard)**: Sekmeli yapı yerine sol menü (Sidebar) tasarımına sahip modern masaüstü uygulaması görünümü.
- **İstatistiksel Analiz (KPI Cards)**: Toplam öğrenci sayısı, sınıf ortalaması, geçen ve kalan öğrenci sayısını anlık olarak gösteren renk kodlu KPI kartları.
- **Dinamik Başarı Grafiği (`BasariBar`)**: Sınıfın geçme/kalma oranını zümrüt yeşili ve mercan kırmızısı tonlarında gösteren, Java 2D Graphics ile özel çizilmiş başarı çubuğu.
- **Gelişmiş Arama ve Filtreleme**: Numaraya, isme veya bölüme göre kelime bazlı filtreleme.
- **Dosya Tabanlı Kalıcı Depolama (Data Persistence)**: Masaüstünde otomatik oluşturulan `ogrenciler.txt` dosyası yardımıyla veriler CSV formatında (UTF-8 kodlamasında) saklanır; program kapansa dahi verileriniz kaybolmaz.
- **Özel Arayüz Bileşenleri**:
  - `RoundedButton`: Fare ile üzerine gelindiğinde aydınlanan yumuşak köşeli modern butonlar.
  - `RoundedTextField`: Odaklanıldığında turkuaz renkli çerçeve kalınlığı artan modern girdi alanları.
  - Zebra desenli (`alternate row colors`) ve göz yormayan açık turkuaz seçilim rengine sahip modern `JTable` listesi.

---

## 🛠️ Gereksinimler

- **Java Development Kit (JDK)**: JDK 11 veya üzeri sürüm (JDK 25 ile test edilmiştir).
- **İşletim Sistemi**: Windows (Windows Look and Feel entegrasyonu mevcuttur).

---

## 🚀 Çalıştırma Talimatları

Projeyi yerel bilgisayarınızda derlemek ve çalıştırmak için aşağıdaki adımları takip edebilirsiniz:

1. **Projeyi Derleyin**:
   ```bash
   # build klasörünü oluşturun
   mkdir build

   # Kodları UTF-8 formatında derleyin
   javac -encoding UTF-8 -d build src/öğrenci/OgrenciYonetimSistemi.java
   ```

2. **Uygulamayı Çalıştırın**:
   ```bash
   java -cp build öğrenci.OgrenciYonetimSistemi
   ```

---

## 📂 Veri Saklama Yapısı

Uygulama, verilerini masaüstünüzde (`~/Desktop/ogrenciler.txt`) şu yapıda saklar:
```csv
OgrenciNo;Ad;Soyad;Bolum;VizeNotu;FinalNotu
```
*Not: Not ortalamaları, harf notları ve geçme durumları vize (%40) ve final (%60) notlarına göre çalışma zamanında dinamik olarak hesaplanır.*
