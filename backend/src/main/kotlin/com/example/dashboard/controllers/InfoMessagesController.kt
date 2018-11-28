package com.example.dashboard.controllers

import com.example.dashboard.messages.Message
import com.example.dashboard.messages.OutputMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import java.text.SimpleDateFormat
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import java.util.*


@Controller
class InfoMessagesController {

    private val logger: Logger = LoggerFactory.getLogger(InfoMessagesController::class.java)

    @MessageMapping("/info")
    @SendTo("/topic/messages")
    @Throws(Exception::class)
    fun send(message: Message): OutputMessage {
        logger.info("Received message: $message")
        val time = SimpleDateFormat("HH:mm").format(Date())
        return OutputMessage(message.from, message.text, time)
    }


}