package com.leos.smsmessenger.models

data class SearchResult(val messageId: Long, val title: String, val phoneNumber: String?, val snippet: String, val date: String, val threadId: Long, var photoUri: String)
