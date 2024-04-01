package org.d3if0072.assessment1mobpro.navigation

sealed class Screen (val route: String){
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutscreen")
}