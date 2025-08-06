package com.nxt64software.bacromana

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Acasă")
    object Sub1 : Screen("subiectul-1", "I")
    object Sub2 : Screen("subiectul-2", "II")
    object Sub3 : Screen("subiectul-3", "III")
    object Quiz : Screen("quizuri", "Quiz")
    object About : Screen("about", "Despre")
    object Privacy : Screen("privacy", "Politica de confidențialitate")
    object Terms : Screen("terms", "Termeni și condiții")
}
