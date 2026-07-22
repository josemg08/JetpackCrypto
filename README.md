# JetpackCrypto

A real-time cryptocurrency price tracker built as a **step-by-step teaching project** for Android Jetpack. The app fetches the top coins from the [CoinGecko API](https://www.coingecko.com/en/api) and displays them in a live, paginated list with a detail screen for each coin.

Each class maps to a branch. Checkout any branch to see the app at that exact stage of the course.

---

## What the App Does

- Fetches the top 100 cryptocurrencies from CoinGecko (no API key required)
- Displays a paginated list with coin name, symbol, logo, and current USD price
- Tapping a coin opens a detail screen with 24h high/low and price change
- Background price refresh via WorkManager every 15 minutes
- Two tabs via ViewPager2: **All Coins** and **Top Gainers**

---

## Course Structure

| Branch    | Topics                                                                               |
|-----------|--------------------------------------------------------------------------------------|
| `main`    | Empty Android project scaffold                                                       |
| `class-1` | MVVM Architecture, ViewModel, LiveData, Lifecycle-aware, Retrofit, Repository Pattern |
| `class-2` | Room Database, Entities, DAOs, Paging 3, RemoteMediator                              |
| `class-3` | View Binding, Data Binding, Parcelable                                               |
| `class-4` | WorkManager, ViewPager2, TabLayout                                                   |
| `class-5` | StateFlow, SharedFlow, Sealed UiState, Unidirectional Data Flow                      |
| `class-6` | Jetpack Compose, `collectAsStateWithLifecycle`                                       |

Each branch builds on the previous one. The domain and data layers are established in Class 1 and remain unchanged through Class 6 — only the UI layer evolves.

---

## Concepts Covered

**Class 1 — MVVM Foundation**
- **MVVM Architecture** — strict separation between Model, ViewModel, and View. Every layer communicates through interfaces, never concrete classes.
- **ViewModel** — survives configuration changes (rotation). Holds all UI state and business logic out of the Activity.
- **LiveData** — observable state holder. The Activity reacts to data changes without polling or callbacks.
- **Lifecycle-aware** — LiveData only delivers updates when the UI is active and automatically cleans up observers on destroy.

**Class 2 — Local Persistence & Paging**
- **Room** — SQLite abstraction. Define the schema as Kotlin data classes (`@Entity`), query through type-safe interfaces (`@Dao`).
- **Entities & DAOs** — `CoinEntity` maps to a database table; `CryptoDao` defines queries as suspend functions and `PagingSource` returns.
- **Paging 3 + RemoteMediator** — loads pages from the network, stores them in Room, and serves the UI from the local database. Room becomes the single source of truth.

**Class 3 — Binding**
- **View Binding** — eliminates `findViewById`. Every layout generates a typed binding class. No more `NullPointerException` from wrong IDs.
- **Data Binding** — the layout reads directly from the domain model (`@{coin.name}`). The Activity just sets `binding.coin = it`.
- **Parcelable** — pass rich domain objects across Activity boundaries. `@Parcelize` generates the implementation at compile time.

**Class 4 — Background Work & Navigation**
- **WorkManager** — schedules guaranteed background work. Constraints (network required), periodic intervals, and automatic retry are built in.
- **ViewPager2** — swipeable tabs backed by `FragmentStateAdapter`. Introduces Fragments as lightweight tab hosts inside an Activity.

**Class 5 — Reactive State**
- **StateFlow** — replaces LiveData in ViewModel contracts. Cold, stateful, coroutine-native.
- **SharedFlow** — one-shot navigation events that must not replay on rotation.
- **Sealed UiState classes** — `Loading`, `Success`, `Error` states modeled exhaustively. `when` expressions in the UI are exhaustive by the compiler.

**Class 6 — Jetpack Compose**
- **Compose** — declarative UI. Composables consume the same ViewModel interfaces defined in Class 1 — the ViewModel is untouched.
- **`collectAsStateWithLifecycle`** — lifecycle-safe StateFlow collection inside Composables.
- The Class 5 → Class 6 diff is the payoff: the entire UI migration is one line per Activity and a new Composable file. The architecture layer is zero changes.

---

## Architecture

```
domain/          Pure Kotlin. Models + Repository interface. No Android imports.
data/            Retrofit, Room, mappers, repository implementation.
ui/              Activities, ViewModels, Adapters, contracts.
```

The ViewModel always talks to a `CryptoRepository` interface. The Activity always talks to a `CryptoListContract` interface. Concrete classes are wired up in ViewModel factories. This is what makes every class-to-class migration surgical.

---

## Tech Stack

| Library               | Version    |
|-----------------------|------------|
| Kotlin                | 2.x        |
| Android Gradle Plugin | 9.x        |
| Retrofit + OkHttp     | 3.x / 5.x  |
| Coil                  | 2.x        |
| Room                  | 2.6        |
| Paging 3              | 3.3        |
| WorkManager           | 2.9        |
| Jetpack Compose BOM   | 2024.06    |

API: [CoinGecko Public API](https://www.coingecko.com/en/api) — free tier, no key required.
