package com.example.flightsearchapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flightsearchapp.ui.destination.FlightDestinationRoute
import com.example.flightsearchapp.ui.destination.FlightListDestination
import com.example.flightsearchapp.ui.home.HomeScreen
import com.example.flightsearchapp.ui.home.HomeScreenDestination

@Composable
fun FlightSearchNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeScreenDestination.route) {
            HomeScreen(
                navigateToSelectFlight = {
                    navController.navigate("${FlightDestinationRoute.route}/${it}")
                }
            )
        }

        composable(
            route = FlightDestinationRoute.routeWithArgs,
            arguments = listOf(navArgument(FlightDestinationRoute.AIRPORT_ID) {
                type = NavType.IntType
            })
        ) {
            FlightListDestination(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}