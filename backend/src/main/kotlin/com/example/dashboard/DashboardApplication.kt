package com.example.dashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class DashboardApplication

fun main(args: Array<String>) {
    runApplication<DashboardApplication>(*args)
}
