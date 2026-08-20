package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.ui.contract.SearchContract
import com.josegonzalez.jetpackCrypto.ui.theme.JetpackCryptoTheme
import com.josegonzalez.jetpackCrypto.ui.viewmodels.SearchViewModel
import com.josegonzalez.jetpackCrypto.ui.viewmodels.factory.SearchViewModelFactory

class SearchBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: SearchViewModel by viewModels {
        val database = CryptoDatabase.getInstance(requireContext().applicationContext)
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())
        SearchViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                JetpackCryptoTheme {
                    SearchContent(
                        viewModel = viewModel,
                        onCoinClick = { coin ->
                            parentFragmentManager.setFragmentResult(
                                RESULT_KEY,
                                bundleOf(ARG_COIN to coin)
                            )
                            dismiss()
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val TAG = "SearchBottomSheetFragment"
        const val RESULT_KEY = "search_result"
        const val ARG_COIN = "coin"

        fun newInstance() = SearchBottomSheetFragment()
    }
}

@Composable
private fun SearchContent(
    viewModel: SearchContract,
    onCoinClick: (Coin) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = uiState.query,
            onValueChange = { viewModel.onQueryChanged(it) },
            label = { Text("Search coins") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onQueryChanged("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        LazyColumn {
            items(uiState.filteredCoins, key = { it.id }) { coin ->
                CoinItem(coin = coin, onClick = { onCoinClick(coin) })
            }
        }
    }
}
