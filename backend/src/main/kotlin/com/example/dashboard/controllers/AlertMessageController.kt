package com.example.dashboard.controllers

import com.example.dashboard.messages.AlertMessage
import com.example.dashboard.services.AlertService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class AlertMessageController(val alertService: AlertService) {

    @CrossOrigin(origins = ["*"])
    @PostMapping("/alert")
    @ResponseStatus(HttpStatus.OK)
    fun receiveAlertMessage(@RequestBody alertMessage: AlertMessage) {
        alertService.sendAlert(alertMessage)
    }
}