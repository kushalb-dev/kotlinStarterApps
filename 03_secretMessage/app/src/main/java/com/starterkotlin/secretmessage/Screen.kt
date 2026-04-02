package com.starterkotlin.secretmessage

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object EnterMessage : Screen("enter_message")
    object ShowEncrypted : Screen("show_encrypted/{msg}") {
        fun createRoute(msg: String) = "show_encrypted/$msg"
    }
}