package com.example.dashboard.services

import com.example.dashboard.messages.AlertMessage
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class AlertService(val template : SimpMessagingTemplate) {

    fun sendAlert(alertMessage: AlertMessage) {
        template.convertAndSend("/topic/alerts", alertMessage)
    }
}