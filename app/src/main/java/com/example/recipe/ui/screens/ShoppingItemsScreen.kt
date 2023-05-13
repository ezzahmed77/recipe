package com.example.recipe.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.recipe.AppViewModelProvider
import com.example.recipe.R
import com.example.recipe.data.local.model.ShoppingItemLocal
import com.example.recipe.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ShoppingListDestination : NavigationDestination {
    override val route = "shopping"
    override val titleRes = R.string.shopping_list
}

@Composable
fun ShoppingItemsScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingItemsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()

    val uiState = viewModel.uiState.collectAsState().value
    if(uiState is ShoppingItemsUiState.Success){
        LazyColumn {
            items(uiState.items) { shoppingItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${shoppingItem.ingredientName} (${shoppingItem.ingredientAmount} ${shoppingItem.ingredientUnit})",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { scope.launch{ viewModel.deleteShoppingItem(shoppingItem)}}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete_icon)
                        )
                    }
                }
            }
        }
    }

}
