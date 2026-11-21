# Öğrenci Yönetim Sistemi

Öğrenci kayıtlarını, sınav notlarını ve sınıf istatistiklerini tek pencerede yöneten bir Java Swing masaüstü uygulaması. Harici kütüphane veya veritabanı kurulumu gerektirmez.

## Özellikler

- Öğrenci ekleme, düzenleme ve onaylı silme
- Öğrenci numarası, ad soyad ve bölüme göre arama
- Toplam öğrenci sayısı, sınıf ortalaması ve geçme oranını gösteren panel
- Öğrenci kayıtlarını UTF-8 dosyasında saklama

## Derleme ve çalıştırma

JDK 11 veya üzeri ve grafik masaüstü ortamı gerekir. Aşağıdaki komutları deponun kök klasöründe çalıştırın. Aynı komutlar PowerShell ve Bash üzerinde kullanılabilir.

```sh
javac -encoding UTF-8 -d build "src/öğrenci/Ogrenci.java" "src/öğrenci/VeriTabani.java" "src/öğrenci/OgrenciYonetimSistemi.java"
java -cp build öğrenci.OgrenciYonetimSistemi
```

## Uygulamayı kullanma

1. **Yeni Öğrenci Ekle** ekranında öğrenci numarasını, adını, soyadını, bölümünü ve notlarını girin. Öğrenci numarası benzersiz olmalı ve notlar 0–100 aralığında olmalıdır. Ondalık notlarda virgül veya nokta kullanabilirsiniz.
2. **Öğrenci Listesi** ekranında arama yapabilir, bir satırı seçip düzenleyebilir veya silebilirsiniz.
3. **Gösterge Paneli** üzerinden sınıf istatistiklerini izleyebilirsiniz. Ağırlıklı ortalama, vizenin %40'ı ile finalin %60'ının toplamıdır. Geçmek için hem ortalamanın hem final notunun en az 50 olması gerekir.

## Veri dosyası

Kayıtlar, kullanıcı klasörünün altındaki `Desktop/ogrenciler.txt` dosyasında saklanır. İlk başarılı kayıtta dosya ve gerekirse `Desktop` klasörü oluşturulur. UTF-8 dosyasında başlık satırı yoktur. Her satır bir öğrenci kaydıdır.

```text
1001;Deniz;Yılmaz;Bilgisayar Mühendisliği;75.0;85.5
```

Alan sırası öğrenci numarası, ad, soyad, bölüm, vize ve finaldir. Metin alanlarında noktalı virgül ve satır sonu kullanılamaz. Ortalama, harf notu ve geçme durumu uygulama tarafından hesaplanır.

Önceki sürümün noktalı veya virgüllü not içeren dosyaları okunabilir. Uygulama geçersiz bir kayıt bulursa açılışta dosya yolunu ve satır numarasını gösterir. Kayıt hatasında mevcut dosya korunur ve hata ekranda gösterilir. Dosyayı yedeklemek için uygulama kapalıyken bir kopyasını alın.

Uygulama tek kullanıcı ve tek açık pencere ile kullanım için tasarlanmıştır. Uygulama içi kullanıcı hesabı ve rol kontrolü yoktur. Öğrenci bilgileri düz metin olarak saklanır ve dosyaya erişimi işletim sistemi izinleri belirler.

## Kaynak kod

| Dosya | Sorumluluk |
|---|---|
| `OgrenciYonetimSistemi.java` | Swing ekranları, arama ve kullanıcı işlemleri |
| `Ogrenci.java` | Öğrenci bilgileri, ağırlıklı ortalama, harf notu ve geçme durumu |
| `VeriTabani.java` | Kayıt doğrulama, yerel dosyayı okuma ve güvenli biçimde değiştirme |
