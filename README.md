# Kurumsal Başvuru Sistemi

Şirket içi personel başvurularının (izin, eğitim, avans, malzeme, görev talebi vb.) oluşturulup takip edildiği, rol tabanlı yetkilendirmeye sahip tam kapsamlı bir web uygulaması. Backend **Spring Boot (Java 21)**, frontend **React** ile geliştirilmiştir.

## İçindekiler

- [Özellikler](#özellikler)
- [Teknoloji Yığını](#teknoloji-yığını)
- [Proje Yapısı](#proje-yapısı)
- [Gereksinimler](#gereksinimler)
- [Kurulum](#kurulum)
  - [1. Veritabanı](#1-veritabanı)
  - [2. Backend](#2-backend)
  - [3. Frontend](#3-frontend)
- [Varsayılan Kullanıcı](#varsayılan-kullanıcı)
- [API Uç Noktaları](#api-uç-noktaları)
- [Roller ve Yetkilendirme](#roller-ve-yetkilendirme)
- [Ortam Değişkenleri / Yapılandırma](#ortam-değişkenleri--yapılandırma)
- [Swagger / API Dokümantasyonu](#swagger--api-dokümantasyonu)
- [Derleme ve Dağıtım](#derleme-ve-dağıtım)
- [Katkıda Bulunma](#katkıda-bulunma)

## Özellikler

- **Kimlik doğrulama:** JWT tabanlı giriş/kayıt işlemleri
- **Rol tabanlı yetkilendirme:** `PERSONEL` ve `ADMIN` rolleri
- **Başvuru formları:** Oluşturma, listeleme, detay görüntüleme, güncelleme, silme
- **Başvuru iş akışı:** Onaylama, reddetme, durum güncelleme, iptal etme (`NEW`, `IN_REVIEW`, `APPROVED`, `REJECTED`, `CANCELLED`)
- **Dosya/ek yönetimi:** Başvurulara dosya yükleme, indirme ve silme
- **Form tipleri:** Yönetilebilir başvuru kategorileri (İzin, Eğitim, Avans, Malzeme, Görev)
- **Dashboard:** Başvuru istatistiklerinin özet görünümü
- **Kullanıcı yönetimi:** Profil görüntüleme/güncelleme, admin tarafından rol atama ve kullanıcı silme
- **API dokümantasyonu:** springdoc-openapi ile otomatik Swagger UI

## Teknoloji Yığını

**Backend**
- Java 21, Spring Boot 3.3.4
- Spring Web, Spring Data JPA, Spring Security
- PostgreSQL
- JJWT (JSON Web Token)
- MapStruct + Lombok
- springdoc-openapi (Swagger UI)
- Maven

**Frontend**
- React 19
- React Router 7
- MUI (Material UI) 9
- Axios

## Proje Yapısı
basvuru-sistemi/
├── backend/
│ ├── src/main/java/com/sirket/basvuru/
│ │ ├── config/ # Güvenlik, OpenAPI, başlangıç verisi (DataInitializer)
│ │ ├── controller/ # REST controller'lar
│ │ ├── dto/ # İstek/yanıt DTO'ları
│ │ ├── entity/ # JPA entity'leri
│ │ ├── enums/ # Role, ApplicationStatus
│ │ ├── exception/ # Global hata yönetimi
│ │ ├── mapper/ # MapStruct mapper'ları
│ │ ├── repository/ # Spring Data repository'leri
│ │ ├── security/ # JWT filtre ve servisleri
│ │ ├── service/ # Servis arayüzleri ve implementasyonları
│ │ └── specification/ # Dinamik sorgu (JPA Specification) sınıfları
│ └── src/main/resources/
│ ├── application.yml.example # Yapılandırma şablonu (gerçek dosya git'e dahil değil)
│ └── application.yml # Yerel yapılandırmanız (.gitignore içinde, siz oluşturursunuz)
└── frontend/
└── src/
├── api/ # Axios tabanlı API istemcileri
├── components/ # Ortak bileşenler (Layout, PrivateRoute, StatusChip)
├── constants/ # Sabitler (durum kodları vb.)
├── context/ # AuthContext (kimlik doğrulama durumu)
└── pages/ # Sayfa bileşenleri (Login, Dashboard, Form, Profil, Kullanıcılar)

basvuru-sistemi/
├── backend/
│ ├── src/main/java/com/sirket/basvuru/
│ │ ├── config/ # Güvenlik, OpenAPI, başlangıç verisi (DataInitializer)
│ │ ├── controller/ # REST controller'lar
│ │ ├── dto/ # İstek/yanıt DTO'ları
│ │ ├── entity/ # JPA entity'leri
│ │ ├── enums/ # Role, ApplicationStatus
│ │ ├── exception/ # Global hata yönetimi
│ │ ├── mapper/ # MapStruct mapper'ları
│ │ ├── repository/ # Spring Data repository'leri
│ │ ├── security/ # JWT filtre ve servisleri
│ │ ├── service/ # Servis arayüzleri ve implementasyonları
│ │ └── specification/ # Dinamik sorgu (JPA Specification) sınıfları
│ └── src/main/resources/
│ ├── application.yml.example # Yapılandırma şablonu (gerçek dosya git'e dahil değil)
│ └── application.yml # Yerel yapılandırmanız (.gitignore içinde, siz oluşturursunuz)
└── frontend/
└── src/
├── api/ # Axios tabanlı API istemcileri
├── components/ # Ortak bileşenler (Layout, PrivateRoute, StatusChip)
├── constants/ # Sabitler (durum kodları vb.)
├── context/ # AuthContext (kimlik doğrulama durumu)
└── pages/ # Sayfa bileşenleri (Login, Dashboard, Form, Profil, Kullanıcılar)


## Gereksinimler

- JDK 21+
- Maven 3.9+ (ya da projede yer alan `mvnw` wrapper)
- PostgreSQL 14+
- Node.js 18+ ve npm

## Kurulum

### 1. Veritabanı

PostgreSQL üzerinde boş bir veritabanı oluşturun:

```sql
CREATE DATABASE basvuru_db;
```

Tablolar uygulama ilk çalıştığında Hibernate (`ddl-auto: update`) tarafından otomatik oluşturulur, elle şema oluşturmanıza gerek yoktur.

### 2. Backend

Depoda güvenlik nedeniyle gerçek `application.yml` dosyası **yer almaz** (`.gitignore` ile hariç tutulmuştur). Bunun yerine `application.yml.example` şablonu bulunur.

```bash
cd backend/src/main/resources
cp application.yml.example application.yml
```

Ardından `application.yml` dosyasını açıp kendi ortamınıza göre doldurun:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/basvuru_db
    username: <postgresql-kullanici-adiniz>
    password: <postgresql-sifreniz>
```

> `username` ve `password` alanları şablonda bilerek **boş** bırakılmıştır; kendi PostgreSQL kimlik bilgilerinizi girmeden uygulama veritabanına bağlanamaz.

Ayrıca `app.jwt.secret` alanını üretim ortamında rastgele/uzun bir değerle değiştirmeniz önerilir.

Backend'i çalıştırın:

```bash
cd backend
./mvnw spring-boot:run
```

veya IDE üzerinden `BasvuruSistemiApplication` sınıfını çalıştırabilirsiniz. Uygulama varsayılan olarak `http://localhost:8080` üzerinde ayağa kalkar.

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

Uygulama varsayılan olarak `http://localhost:3000` adresinde açılır ve backend'e `http://localhost:8080` üzerinden istek atar (bkz. `src/api/axiosInstance.js`).

## Varsayılan Kullanıcı

Uygulama ilk çalıştırıldığında `DataInitializer` sınıfı aşağıdaki admin kullanıcısını ve varsayılan form tiplerini otomatik olarak oluşturur:

| E-posta | Şifre | Rol |
|---|---|---|
| `admin@sirket.com` | `admin1234` | `ADMIN` |

Varsayılan form tipleri: `Izin`, `Egitim`, `Avans`, `Malzeme`, `Gorev`.

> Üretim ortamına almadan önce bu varsayılan admin şifresini mutlaka değiştirin.

## API Uç Noktaları

Tüm uç noktalar `/api` öneki ile başlar.

| Yöntem | Yol | Açıklama |
|---|---|---|
| POST | `/api/auth/register` | Yeni kullanıcı kaydı |
| POST | `/api/auth/login` | Giriş yapma, JWT token alma |
| GET | `/api/forms` | Başvuru formlarını listele |
| POST | `/api/forms` | Yeni başvuru formu oluştur |
| GET | `/api/forms/{id}` | Başvuru detayını getir |
| PUT | `/api/forms/{id}` | Başvuruyu güncelle |
| DELETE | `/api/forms/{id}` | Başvuruyu sil |
| PUT | `/api/forms/{id}/approve` | Başvuruyu onayla |
| PUT | `/api/forms/{id}/reject` | Başvuruyu reddet |
| PUT | `/api/forms/{id}/status` | Başvuru durumunu güncelle |
| PUT | `/api/forms/{id}/cancel` | Başvuruyu iptal et |
| POST | `/api/attachments/upload/{applicationFormId}` | Başvuruya dosya yükle |
| GET | `/api/attachments/download/{id}` | Ekli dosyayı indir |
| DELETE | `/api/attachments/{id}` | Ekli dosyayı sil |
| GET | `/api/form-types` | Form tiplerini listele |
| POST | `/api/form-types` | Yeni form tipi oluştur |
| GET | `/api/form-types/{id}` | Form tipi detayı |
| PUT | `/api/form-types/{id}` | Form tipini güncelle |
| DELETE | `/api/form-types/{id}` | Form tipini sil |
| GET | `/api/dashboard` | Dashboard özet verileri |
| GET | `/api/users/me` | Giriş yapan kullanıcının profili |
| PUT | `/api/users/me` | Profil güncelleme |
| GET | `/api/users` | Kullanıcıları listele (admin) |
| PUT | `/api/users/{id}/role` | Kullanıcı rolünü güncelle (admin) |
| DELETE | `/api/users/{id}` | Kullanıcı sil (admin) |

Ayrıntılı istek/yanıt gövdeleri için Swagger UI'ı kullanabilirsiniz.

## Roller ve Yetkilendirme

- **PERSONEL:** Kendi başvurularını oluşturabilir, listeleyebilir, düzenleyebilir ve iptal edebilir.
- **ADMIN:** Tüm başvuruları görüntüleyebilir, onaylayabilir/reddedebilir, form tiplerini yönetebilir, kullanıcı rollerini değiştirebilir ve kullanıcı silebilir.

Yetkilendirme, giriş sırasında alınan JWT içindeki role bilgisine göre Spring Security tarafından uygulanır.

## Ortam Değişkenleri / Yapılandırma

Backend yapılandırması `backend/src/main/resources/application.yml` dosyasında tutulur (bkz. [Kurulum](#2-backend)). Önemli alanlar:

| Alan | Açıklama |
|---|---|
| `spring.datasource.url` | PostgreSQL bağlantı adresi |
| `spring.datasource.username` / `password` | Veritabanı kimlik bilgileri (repoda boş bırakılmıştır, siz doldurun) |
| `app.jwt.secret` | JWT imzalama anahtarı |
| `app.jwt.expiration-ms` | Token geçerlilik süresi (ms) |
| `app.file.upload-dir` | Yüklenen dosyaların saklanacağı klasör |
| `server.port` | Backend'in çalışacağı port (varsayılan `8080`) |

## Swagger / API Dokümantasyonu

Backend ayaktayken:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Derleme ve Dağıtım

**Backend için çalıştırılabilir JAR:**

```bash
cd backend
./mvnw clean package
java -jar target/basvuru-sistemi-1.0-SNAPSHOT.jar
```

**Frontend için production build:**

```bash
cd frontend
npm run build
```

Oluşan `build/` klasörü herhangi bir statik dosya sunucusu (Nginx vb.) üzerinden servis edilebilir.

## Katkıda Bulunma

1. Depoyu fork'layın / bir dal (branch) oluşturun: `git checkout -b ozellik/yeni-ozellik`
2. Değişikliklerinizi yapın ve commit edin
3. Dalınızı push'layın ve bir Pull Request açın

