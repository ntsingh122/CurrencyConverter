# 💱 Currency Converter

A production-ready Android application that fetches live exchange rates and supports
real-time currency conversion. Built to demonstrate modern Android development 
practices with a focus on clean, scalable, and maintainable code.


---

## 🏗️ Architecture

This project follows **Clean Architecture** combined with **MVVM** — the same 
pattern recommended by Google and used in production Android apps.
```
UI Layer          →   Compose Screens + ViewModels
Domain Layer      →   Models, Repository Interface, Mapper  
Data Layer        →   Retrofit API, DTOs, Repository Implementation
```

### Why This Architecture

- **Separation of concerns** — each layer has one job and one reason to change
- **Testability** — every layer is independently unit testable
- **Scalability** — adding new features doesn't require touching existing code
- **Maintainability** — a new developer can understand the codebase immediately

---

## 🛠️ Tech Stack

| Category | Technology | Reason |
|---|---|---|
| Language | Kotlin | Concise, null-safe, coroutine support |
| UI | Jetpack Compose | Declarative, less boilerplate than XML |
| Architecture | MVVM + Clean Architecture | Separation of concerns, testability |
| DI | Hilt | Compile-time safe, lifecycle-aware |
| Networking | Retrofit + OkHttp | Declarative API, coroutine support |
| Async | Coroutines + StateFlow | Structured concurrency, reactive UI |
| Navigation | Jetpack Navigation Compose | Type-safe, single activity |
| Image Loading | Coil | Kotlin-first, Compose-compatible |
| JSON Parsing | Gson | Automatic serialization/deserialization |

---

## 📁 Project Structure
```
app/src/main/java/com/example/currencyconverter/
│
├── data/                          # Data Layer
│   ├── remote/
│   │   ├── api/
│   │   │   └── CurrencyApiService.kt     # Retrofit interface
│   │   └── dto/
│   │       └── CurrencyResponseDto.kt    # Raw API response model
│   └── repository/
│       └── CurrencyRepositoryImpl.kt     # Repository implementation
│
├── domain/                        # Domain Layer (pure Kotlin, zero Android deps)
│   ├── model/
│   │   └── Currency.kt                   # Clean domain model
│   ├── mapper/
│   │   └── CurrencyMapper.kt             # DTO → Domain model conversion
│   └── repository/
│       └── CurrencyRepository.kt         # Repository abstraction
│
├── ui/                            # UI Layer
│   ├── list/
│   │   ├── CurrencyListScreen.kt         # List composable
│   │   └── CurrencyListViewModel.kt      # List state holder
│   └── converter/
│       ├── ConverterScreen.kt            # Converter composable
│       └── ConverterViewModel.kt         # Converter state holder
│
├── di/                            # Dependency Injection
│   ├── NetworkModule.kt                  # Provides Retrofit, OkHttp, ApiService
│   └── RepositoryModule.kt              # Binds repository interface to impl
│
├── navigation/
│   ├── AppNavHost.kt                     # Navigation graph
│   └── Routes.kt                         # Type-safe route definitions
│
├── utils/
│   └── UiState.kt                        # Sealed class for UI states
│
├── App.kt                                # HiltAndroidApp
└── MainActivity.kt                       # Single activity entry point
```

---

## ✨ Features

- 📡 **Live exchange rates** fetched from FreeCurrencyAPI
- 💱 **Real-time conversion** between any two currencies
- 🔍 **Search and filter** currencies by code
- ⚡ **Loading, error, and success states** handled gracefully
- 🔄 **Automatic retry** on network failure
- 📱 **Responsive UI** built entirely in Jetpack Compose

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android SDK 26+
- A free API key from [freecurrencyapi.com](https://freecurrencyapi.com)

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/ntsingh122/CurrencyConverter.git
cd CurrencyConverter
```

**2. Configure API keys**
```bash
cp local.properties.example local.properties
```

Open `local.properties` and fill in your values:
```
CURRENCY_API_KEY=your_api_key_here
BASE_URL=https://api.freecurrencyapi.com/
```

**3. Sync and run**

Open in Android Studio → Sync Gradle → Run on emulator or device

---

## 🔑 API

This app uses the [FreeCurrencyAPI](https://freecurrencyapi.com/docs) which is 
free to use with no credit card required.

| Endpoint | Description |
|---|---|
| `GET /v1/latest` | Fetch latest exchange rates relative to a base currency |

**Response shape:**
```json
{
    "data": {
        "USD": 1.0,
        "EUR": 0.92,
        "GBP": 0.79,
        "AED": 3.67
    }
}
```

---

## 🧠 Key Implementation Decisions

### Why DTOs and Domain Models are separate
The API response shape and the UI model are deliberately kept separate. If the 
API changes its response structure, only the DTO and mapper need to change — 
the rest of the app is unaffected.

### Why Hilt over manual DI
Manual dependency injection becomes unmaintainable as the app grows. Hilt 
provides compile-time safety, lifecycle awareness, and eliminates boilerplate 
while making every class independently testable.

### Why StateFlow over LiveData
StateFlow is Kotlin-native, works seamlessly with Compose, supports multiple 
collectors, and has no observer lifecycle concerns. LiveData is a legacy API 
that predates Kotlin coroutines.

### Why a single Activity
Following Google's recommended single-activity architecture, `MainActivity` 
is purely a container. All navigation is handled by Jetpack Navigation Compose 
with type-safe routes — no fragment transactions, no intent extras.

---

## 📄 License
```
MIT License — feel free to use this project as a reference or starting point
```

---

<p align="center">Built with ❤️ to demonstrate modern Android development</p>
