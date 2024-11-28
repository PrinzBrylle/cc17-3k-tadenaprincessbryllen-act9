@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.flightsearchapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.FlightSearchTopAppBar
import com.example.flightsearchapp.R
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.model.FlightType
import com.example.flightsearchapp.model.toAirportType
import com.example.flightsearchapp.model.toFavorite
import com.example.flightsearchapp.ui.AppViewModelProvider
import com.example.flightsearchapp.ui.components.FlightDestinationDetails
import com.example.flightsearchapp.ui.components.FlightSearchDisplay
import com.example.flightsearchapp.ui.navigation.NavigationDestination
import com.example.flightsearchapp.ui.theme.CustomKorma
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme

object HomeScreenDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToSelectFlight: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FlightSearchTopAppBar(
                title = R.string.top_bar_app_name,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        HomeBody(
            viewModel = viewModel,
            navigateToSelectFlight = navigateToSelectFlight,
            onAirportValueChange = viewModel::onAirportValueChange,
            contentPaddingValues = innerPadding
        )
    }
}

@Composable
private fun HomeBody(
    viewModel: HomeScreenViewModel,
    navigateToSelectFlight: (Int) -> Unit,
    onAirportValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val searchPreferences by viewModel.loadPreferenceSearchText().collectAsStateWithLifecycle("")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        SearchBar(
            modifier = Modifier
                .padding(contentPaddingValues)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                        onAirportValueChange(it)
                    },
                    onSearch = {
                        expanded = false
                        viewModel.updateSearchText(it)
                        onAirportValueChange(it)
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = searchPreferences.isNotEmpty() },
                    placeholder = { Text(stringResource(R.string.textfield_placeholder)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                ListItem(
                    headlineContent = { Text(searchPreferences) },
                    supportingContent = { Text(stringResource(R.string.searchbar_preference)) },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier =
                    Modifier
                        .clickable {
                            searchText = searchPreferences
                            expanded = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        FlightList(
            viewModel.uiState.airport,
            viewModel,
            navigateToSelectFlight,
        )
    }
}

@Composable
private fun FlightList(
    airportList: List<Airport>,
    viewModel: HomeScreenViewModel,
    navigateToSelectFlight: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (viewModel.uiState.searchText.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(airportList, key = { item -> item.id }) { airport ->
                FlightSearchDisplay(airport, navigateToSelectFlight)
            }
        }
    } else {
        viewModel.loadFavoriteList()

        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = viewModel.displayItemTitleList(title = stringResource(R.string.subtitle_favorite_routes)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            }

            items(
                viewModel.favoriteUiState.airportsFavorite,
                key = { item -> item.id }) { airportFlights ->
                FlightDestinationDetails(
                    airportFrom = airportFlights.toAirportType(FlightType.Departure),
                    airportTo = airportFlights.toAirportType(FlightType.Destination),
                    onSaveFavorite = {
                        viewModel.removeFavoriteList(favorite = airportFlights.toFavorite())
                    }, buttonColor = CustomKorma
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    FlightSearchAppTheme {
        val viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)

        HomeBody(viewModel = viewModel,
            onAirportValueChange = {},
            navigateToSelectFlight = {})
    }
}