# Finance App - Android + Spring Boot + SQLite (JDK 25)

Frontend dan backend dipisahkan:

```text
FinanceApp_Frontend_Backend_SQLite_JDK25/
├── frontend/   # Android Studio + Kotlin + Jetpack Compose + Retrofit
└── backend/    # Spring Boot + Kotlin + REST API + SQLite
```

## Versi utama
- JDK: **25.0.4.1**
- Gradle: **9.3.1**
- Android Gradle Plugin: **9.1.1**
- Kotlin: **2.2.10**
- Spring Boot: **3.5.16**
- Database: **SQLite**
- Android compile/target SDK: 35

## Database
Tidak membutuhkan MySQL, XAMPP, Homebrew MySQL, atau Anaconda MySQL.

SQLite otomatis menggunakan `backend/data/finance_app.db`.

## Backend
Buka folder `backend` pada VS Code atau Android Studio.

Pastikan JDK 25:
```bash
java -version
```

Jalankan menggunakan Gradle 9.3.1:
```bash
gradle bootRun
```

Server: `http://localhost:8080`

Endpoint:
- `/api/transactions`
- `/api/targets`
- `/api/dashboard`

## Frontend
Buka folder `frontend` di Android Studio. Atur **Gradle JDK = JDK/JBR 25**, lakukan Gradle Sync, lalu jalankan emulator.

Backend URL untuk Android Emulator:
`http://10.0.2.2:8080/` (sudah menjadi default di `ApiService.kt`)

Untuk HP fisik, ubah `BASE_URL` di `frontend/app/src/main/java/com/example/financeapp/data/ApiService.kt` ke IP LAN komputer, misalnya `http://192.168.1.10:8080/`.

## Menghindari Kotlin Compile Daemon
Project menggunakan:
```properties
kotlin.compiler.execution.strategy=in-process
```
untuk menghindari error `Could not connect to Kotlin compile daemon`.

## Gradle wrapper
Konfigurasi wrapper menunjuk ke Gradle 9.3.1. Jika Android Studio meminta regenerasi wrapper, gunakan Gradle 9.3.1.

## Alur
```text
Android App → Retrofit → Spring Boot REST API → SQLite
```
