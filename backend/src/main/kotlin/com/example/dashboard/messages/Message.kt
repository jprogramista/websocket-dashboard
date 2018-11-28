package com.example.dashboard.messages

data class Message(val from: String, val text: String)

data class OutputMessage(val from: String, val text: String, val time: String)