# JetpackCrypto — Testing Plan

## Goal

Add JUnit tests to **Class1** that demonstrate the value of the contract/interface architecture.
The tests must stay green as the project evolves through future branches — or, where they
unavoidably break, the break itself becomes the teaching moment.

---

## Architecture Review

### Why Class1 is testable

| Layer | Class | Android-free? | Testable in JUnit? |
|---|---|---|---|
| Domain model | `Coin` | ✅ | ✅ |
| Repository interface | `CryptoRepository` | ✅ | ✅ (fake it) |
| Navigation interface | `CryptoNavigation` | ✅ | ✅ (fake it) |
| ViewModel contracts | `CryptoListContract` / `CryptoDetailContract` | ⚠️ uses `LiveData` | ✅ with `InstantTaskExecutorRule` |
| ViewModels (concrete) | `CryptoListViewModel` / `CryptoDetailViewModel` | ⚠️ uses `LiveData` | ✅ with `InstantTaskExecutorRule` |
| Activities | `CryptoListActivity` / `CryptoDetailActivity` | ❌ | ❌ (instrumented only) |

**Key design decision:** Activities hold the ViewModel as the *contract type*
(`private lateinit var viewModel: CryptoListContract`), not the concrete class.
This means any fake implementation of the contract can be injected without touching Activity code.

### Known bug (unrelated to tests)
`CoinDetailDto.toDomain()` hardcodes all numeric fields to `0.0` — the detail screen always
shows zero prices. Fix before shipping Class1 tests so mapper tests pass.

---

## Test Dependencies to Add

In `app/build.gradle`, add to the `dependencies` block:

```groovy
testImplementation "androidx.arch.core:core-testing:2.2.0"       // InstantTaskExecutorRule
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0" // UnconfinedTestDispatcher
```

No Mockito or MockK — fakes are written by hand. This keeps the teaching focus on the
architecture, not on a mocking library.

---

## Fake Implementations

### `FakeCryptoRepository`

Implements `CryptoRepository`. Configurable per test:
- `shouldThrow: Boolean` — makes both methods throw an `Exception`
- `coins: List<Coin>` — the list returned by `getCoins()`
- `coinDetail: Coin` — the object returned by `getCoinDetail()`

```kotlin
class FakeCryptoRepository(
    private val coins: List<Coin> = emptyList(),
    private val coinDetail: Coin = Coin(/* sensible defaults */),
    private val shouldThrow: Boolean = false
) : CryptoRepository {

    override suspend fun getCoins(page: Int, perPage: Int): List<Coin> {
        if (shouldThrow) throw Exception("Network error")
        return coins
    }

    override suspend fun getCoinDetail(coinId: String): Coin {
        if (shouldThrow) throw Exception("Network error")
        return coinDetail
    }
}
```

### `FakeCryptoNavigation`

Implements `CryptoNavigation`. Records calls so tests can assert on them:

```kotlin
class FakeCryptoNavigation : CryptoNavigation {
    var navigateToDetailCalls: MutableList<Coin> = mutableListOf()
    var navigateBackCount = 0

    override fun navigateToDetail(coin: Coin) {
        navigateToDetailCalls.add(coin)
    }

    override fun navigateBack() {
        navigateBackCount++
    }
}
```

---

## Test Cases

### `CryptoListViewModelTest`

| # | Test name | Setup | Assert |
|---|---|---|---|
| 1 | `loadCoins_success_populatesCoinsLiveData` | `FakeCryptoRepository` returns 3 coins | `coins.value` has 3 items |
| 2 | `loadCoins_success_setsIsLoadingFalse` | Same | `isLoading.value == false` after load |
| 3 | `loadCoins_success_errorMessageIsNull` | Same | `errorMessage.value == null` |
| 4 | `loadCoins_error_setsErrorMessage` | `FakeCryptoRepository(shouldThrow = true)` | `errorMessage.value != null` |
| 5 | `loadCoins_error_setsIsLoadingFalse` | Same | `isLoading.value == false` |
| 6 | `onRefresh_reloadsCoins` | Fake returns 3 coins; call `onRefresh()` | `coins.value` has 3 items |
| 7 | `onCoinSelected_delegatesToNavigation` | `FakeCryptoNavigation` | `navigation.navigateToDetailCalls` contains the coin |

### `CryptoDetailViewModelTest`

| # | Test name | Setup | Assert |
|---|---|---|---|
| 1 | `loadCoin_success_populatesCoinLiveData` | `FakeCryptoRepository` returns a coin | `coin.value` matches expected |
| 2 | `loadCoin_success_setsIsLoadingFalse` | Same | `isLoading.value == false` |
| 3 | `loadCoin_success_errorMessageIsNull` | Same | `errorMessage.value == null` |
| 4 | `loadCoin_error_setsErrorMessage` | `FakeCryptoRepository(shouldThrow = true)` | `errorMessage.value != null` |
| 5 | `loadCoin_error_setsIsLoadingFalse` | Same | `isLoading.value == false` |

### `CoinDtoMapperTest` (bonus — pure Kotlin, zero setup)

| # | Test name | Assert |
|---|---|---|
| 1 | `coinDto_toDomain_mapsAllFieldsCorrectly` | Every field on resulting `Coin` matches the DTO |
| 2 | `coinDetailDto_toDomain_mapsAllFieldsCorrectly` | Same for detail DTO (also validates the zero-price bug is fixed) |

---

## Test Infrastructure Pattern

Every ViewModel test class needs this boilerplate:

```kotlin
@get:Rule
val instantTaskExecutorRule = InstantTaskExecutorRule() // makes LiveData synchronous

private val testDispatcher = UnconfinedTestDispatcher()

@Before
fun setUp() {
    Dispatchers.setMain(testDispatcher)
}

@After
fun tearDown() {
    Dispatchers.resetMain()
}
```

---

## Survival Analysis — Future Branches

Tests are grouped into three tiers based on how far they survive.

---

### Tier 1 — Always green (survive all 6 classes)

**`CoinDtoMapperTest`**

Pure Kotlin. No Android classes, no coroutines, no LiveData. Mapping logic (`CoinDto → Coin`,
`CoinDetailDto → Coin`) only changes if the API response structure changes, which is independent
of architecture evolution. These tests prove the domain layer is truly isolated and are the
clearest example of why the data mapping pattern pays off.

---

### Tier 2 — Survive Class2–Class4, break at Class5

**`CryptoDetailViewModelTest`** (all 5 tests)

`getCoinDetail(coinId: String): Coin` is a single-item fetch. It is not affected by Paging3
(Class2), ViewBinding/Navigation (Class3), or WorkManager (Class4). It will compile and pass
through those branches unchanged.

It breaks at **Class5** when the contracts switch from LiveData to StateFlow + sealed `UiState`:
- `InstantTaskExecutorRule` no longer applies
- `.value` assertions on LiveData don't compile
- The test structure must be rewritten using `runTest` + `turbine` (or manual `collect`)

**This is a Tier 2 teaching moment at Class5:** the *scenarios* (success, error, loading) are
identical — only the assertion mechanism changes. `FakeCryptoRepository` stays untouched.

---

### Tier 3 — Break at Class2 (earliest teaching moment)

**`CryptoListViewModelTest`** (all 7 tests) + **`FakeCryptoRepository.getCoins()`**

Class2 introduces Paging3 with RemoteMediator and a Room-based Single Source of Truth.
The standard implementation for this pattern requires changing the `CryptoRepository` interface:

```kotlin
// Class1
suspend fun getCoins(page: Int, perPage: Int): List<Coin>

// Class2 — Paging3 + Room
fun getCoins(): Flow<PagingData<Coin>>
```

`CryptoListContract.coins` changes from `LiveData<List<Coin>>` to a paged flow.
`FakeCryptoRepository` no longer compiles. All 7 list ViewModel tests fail at compile time.

`CryptoDetailViewModelTest` is **not affected** because `getCoinDetail()` is a single-item call.
`CoinDtoMapperTest` is **not affected** — pure Kotlin.

**Design decision for Class2:**

| Option | Description | Effect on Class1 tests |
|---|---|---|
| A | Keep `CryptoRepository` returning `List<Coin>`; wire Paging3 via a separate `PagingSource` that calls the API directly | ✅ All tests survive |
| B | Change `CryptoRepository` to return `Flow<PagingData<Coin>>` | ❌ List tests break — use as teaching moment |

Option B is the more honest Paging3 architecture and the better teaching moment:
> *"Our list tests broke because the contract evolved. The compiler told us exactly what to
> update. Without the contract and the fakes, we'd have to run the app to discover this."*

---

### Class3 — ViewBinding, DataBinding, Parcelable, Jetpack Navigation ⚠️ One test at risk

ViewBinding and DataBinding are pure UI changes. `@Parcelize` adds an annotation to `Coin`
but keeps all fields. These do not affect any test.

**Jetpack Navigation + Safe Args** introduces a risk for one test:
`onCoinSelected_delegatesToNavigation` asserts that `CryptoNavigation.navigateToDetail(coin)`
is called with the right `Coin`. If Safe Args changes the navigation contract to pass only
a `coinId: String` (the common pattern), the interface becomes:

```kotlin
// Possible Class3 change
fun navigateToDetail(coinId: String)   // was: navigateToDetail(coin: Coin)
```

`FakeCryptoNavigation` and the test would need a one-line update. All other tests are safe.

**Recommendation:** keep `navigateToDetail(coin: Coin)` in Class3 since `@Parcelize` makes
`Coin` parcelable anyway and Safe Args supports passing parcelable objects. This keeps the
test green and avoids an unrelated break.

---

### Class4 — WorkManager, ViewPager2 ✅ All tests survive

WorkManager is initialized at the `Application` level with network constraints. It does not
appear in any ViewModel contract. ViewPager2 + TabLayoutMediator and parent-child fragment
communication are UI-layer concerns only. No contract changes.

---

### Class5 — StateFlow, SharedFlow, Sealed UiState ❌ All ViewModel tests break

This is the intended evolution of the contract architecture and the primary Class5
teaching moment.

The contracts change from:
```kotlin
// Class1 contracts
val coins: LiveData<List<Coin>>
val isLoading: LiveData<Boolean>
val errorMessage: LiveData<String?>
fun loadCoins()
fun onRefresh()
fun onCoinSelected(coin: Coin)
```
to UDF-style:
```kotlin
// Class5 contracts
val uiState: StateFlow<CryptoListUiState>   // sealed class: Loading, Success, Error
val navigationEvent: SharedFlow<NavigationEvent>
fun onRefresh()
fun onSearchQueryChanged(query: String)     // new — flatMapLatest + debounce
fun onCoinSelected(coin: Coin)
```

What breaks:
- `InstantTaskExecutorRule` no longer relevant
- All `.value` assertions on `LiveData` don't compile
- `onRefresh_reloadsCoins` needs to assert on the `uiState` flow, not a `coins` LiveData
- Navigation events move to `SharedFlow` — `FakeCryptoNavigation` may be replaced

What **stays the same**:
- `FakeCryptoRepository` — both `getCoins()` and `getCoinDetail()` signatures are unchanged
  if Class2 used Option A. Even with Option B they only changed at Class2, not Class5.
- The test *scenarios* are identical (success, error, loading, navigation)
- The fake-based test structure is the same — only the assertion API changes

**Recommended Class5 approach:** keep the Class1 test files, let them fail (they won't compile),
and add new `CryptoListViewModelFlowTest` and `CryptoDetailViewModelFlowTest` files that
demonstrate the same scenarios with `runTest` + `turbine`. Show both files side by side.

> *"The architecture held. The fakes survived. Only the observable mechanism changed —
> and the tests told us exactly what to update."*

---

### Class6 — Jetpack Compose ✅ All tests survive (relative to Class5)

Compose replaces the XML View layer entirely. The ViewModels still expose `StateFlow`
(introduced in Class5). `collectAsStateWithLifecycle()` is Compose's collection mechanism —
it lives in the UI layer, not in the ViewModel or its contracts. All Class5 tests carry
forward unchanged.

---

## File Layout (after implementation)

```
app/src/test/java/com/josegonzalez/jetpackCrypto/
├── fake/
│   ├── FakeCryptoRepository.kt
│   └── FakeCryptoNavigation.kt
├── viewmodels/
│   ├── CryptoListViewModelTest.kt
│   └── CryptoDetailViewModelTest.kt
└── data/
    └── mapper/
        └── CoinDtoMapperTest.kt
```

---

## Implementation Order

1. Fix the `CoinDetailDto.toDomain()` zero-price bug
2. Add test dependencies to `app/build.gradle`
3. Write `FakeCryptoRepository` and `FakeCryptoNavigation`
4. Write `CoinDtoMapperTest` (no fakes needed, easiest warmup)
5. Write `CryptoListViewModelTest`
6. Write `CryptoDetailViewModelTest`
7. Run `./gradlew test` and confirm all pass
