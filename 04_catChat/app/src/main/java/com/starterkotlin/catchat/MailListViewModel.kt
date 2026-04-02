package com.starterkotlin.catchat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MailListViewModel : ViewModel() {
    private val sampleMails = listOf(
        Mail("Hello", "1/1/2001", false),
        Mail("Hello1", "1/2/2001", false),
        Mail("Hello2", "1/3/2001", false),
        Mail("Hello3", "1/4/2001", false),
        Mail("Hello4", "1/5/2001", false),
    )
    var mailList by  mutableStateOf(sampleMails)
        private  set

    fun toggleReadStatus(mail: Mail) {
        mailList = mailList.map {
            if (it == mail) {
                it.copy(isRead = !it.isRead)
            } else {
                it
            }
        }
    }
}