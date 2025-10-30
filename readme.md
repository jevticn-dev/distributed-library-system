# PDS Projekat: Biblioteka (Reader & Loan Microservices)

## 1. Uvod i Pregled Arhitekture

Ovaj projekat implementira distribuirani sistem za upravljanje čitaocima i pozajmicama (knjiga) koristeći **Spring Boot Microservices** i Spring Cloud alate.

* **Tema:** Biblioteka (Reader & Loan)
* **Implementirane komponente:**
    * `discovery-service` (Eureka Server)
    * `api-gateway` (Spring Cloud Gateway)
    * `reader-service` (CRUD nad Reader entitetom)
    * `loan-service` (CRUD nad Loan entitetom + poslovna logika)

## 2. Tehnički Zahtevi i Implementacija

| Zahtev | Implementacija | Detalji |
| :--- | :--- | :--- |
| **Service Discovery** | Eureka Server | `discovery-service` je podignut na portu **8761**. |
| **API Gateway** | Spring Cloud Gateway | Centralna ulazna tačka na portu **8080**. |
| **Komunikacija (S-S)** | OpenFeign | `loan-service` koristi Feign klijent za pozivanje `reader-service`-a (npr. provera postojanja čitaoca). |
| **Otpornost** | Resilience4j | Primena **Circuit Breaker** i **Retry** mehanizama na Feign pozivu unutar `loan-service`-a. |
| **Agregacioni Endpoint** | Feign poziv | GET `Loan` poziva `Reader` za spajanje podataka. |
| **Persistencija** | H2 Database | Oba servisa koriste **in-memory H2** bazu podataka. |
| **Autentikacija (Opciono)** | Basic Auth | Konfigurisan na Gateway-u sa kredencijalima: `admin`/`password123`. |

## 3. Pokretanje Aplikacije

### 3.1. Lokalno Pokretanje

1.  **Pokrenuti servise (redosledom):**
    * `discovery-service` (port: 8761)
    * `reader-service` (port: 8081)
    * `loan-service` (port: 8082)
    * `api-gateway` (port: 8080)

2.  **API Pristup:** Svi pozivi idu preko Gateway-a na `http://localhost:8080`.

## 4. API Endpoints (Primeri Testova - Postman)

Ova sekcija sadrži ključne rute (endpoint-e) koji se koriste za testiranje funkcionalnosti servisa, uključujući osnovni CRUD, servis-servis komunikaciju i Basic Auth proveru.

Svi pozivi se rutiraju preko **API Gateway-a** na `http://localhost:8080` koji je podešen kao varijabla na Postman-u.

### 4.1. Loans API (`loan-service`)

| Metoda | Putanja (Primer) | Opis |
| :--- | :--- | :--- |
| **POST** | `/api/loans` | Kreiranje pozajmice bez autentifikacije (**Basic Auth** test) |
| **POST** | `/api/loans` | Kreiranje pozajmice (Sa nepostojećim **čitaocem**) |
| **POST** | `/api/loans` | Uspešno kreiranje pozajmice (`Add loan`) |
| **GET** | `/api/loans` | Dohvatanje svih pozajmica (`Get loans`) |
| **PUT** | `/api/loans/{id}` | Ažuriranje pozajmice (Sa nepostojećim čitaocem) |
| **PUT** | `/api/loans/{id}` | Uspešno ažuriranje pozajmice (`Update loan`) |
| **GET** | `/api/loans/{id}` | Dohvatanje pozajmice po ID-u (`Get loan by id`) |
| **GET** | `/api/loans/{id}/reader` | **Agregacioni Endpoint:** Dohvatanje pozajmice sa detaljima čitaoca (Preko Feign-a) |
| **DEL** | `/api/loans/{id}` | Brisanje pozajmice (`Delete loan`) |


### 4.2. Readers API (`reader-service`)

| Metoda | Putanja (Primer) | Opis |
| :--- | :--- | :--- |
| **POST** | `/api/readers` | Kreiranje čitaoca (**Basic Auth** test) |
| **POST** | `/api/readers` | Uspešno kreiranje čitaoca (`Add reader`) |
| **GET** | `/api/readers` | Dohvatanje svih čitaoca (`Get readers`) |
| **PUT** | `/api/readers/{id}` | Ažuriranje čitaoca (`Update reader`) |
| **GET** | `/api/readers/{id}` | Dohvatanje čitaoca po ID-u (`Get reader by id`) |
| **DEL** | `/api/readers/{id}` | Brisanje čitaoca (`Delete reader`) |endpoint. |

**Napomena:**
Pri gašenju `reader-service`-a u demonstraciji poziva nakon ponovnog uključivanja prvih **6-7** poziva će biti neuspešno dok se servis u potpunosti ne oporavi

## 5. API Dokumentacija (Swagger/OpenAPI)

Kompletna Swagger UI dokumentacija za oba mikroservisa je dostupna preko direktnih portova.

* **Reader Service:**
    `http://localhost:8081/swagger-ui/index.html`
* **Loan Service:**
    `http://localhost:8082/swagger-ui/index.html`

**Napomene:**
* Svi **stvarni** API pozivi (CRUD, agregacija) moraju se rutirati preko **API Gateway-a** (`http://localhost:8080`).
* Gateway zahteva **Basic Auth** (Korisnik: `admin`, Lozinka: `password123`).
* Eureka Registry za praćenje servisa je dostupna na: `http://localhost:8761/`.