package org.d3if0072.assessment1mobpro.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("MainScreen")
    data object About : Screen("AboutScreen")
}