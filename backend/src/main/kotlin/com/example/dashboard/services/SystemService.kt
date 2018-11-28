package com.example.dashboard.services

import com.example.dashboard.messages.SystemInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger
import java.util.regex.Pattern
import java.util.stream.Collectors

enum class MeterSystem {
    macosx, linux
}

@Service
class SystemService(
        @Value("\${meters.system}") val meterSystem: MeterSystem,
        @Value("\${meters.cpu.command}") val cpuMeter: String,
        @Value("\${meters.mem.command}") val memMeter: String,
        @Value("\${meters.disk.command}") val diskMeter: String,
        @Value("\${meters.swap.command}") val swapMeter: String,
        val template : SimpMessagingTemplate) {

    val logger = LoggerFactory.getLogger(SystemService::class.java)

    var counter: BigInteger = BigInteger.ZERO

    @Throws(Exception::class)
    @Scheduled(fixedDelay = 5000)
    fun meters() {
        if (logger.isTraceEnabled) {
            logger.trace("cpuMeter: $cpuMeter")
        }

        template.convertAndSend("/topic/messages", SystemInfo("cpu", terminalOutput(cpuMeter)))
        template.convertAndSend("/topic/messages", SystemInfo("mem", terminalOutput(memMeter)))
        template.convertAndSend("/topic/messages", SystemInfo("diskUsed", terminalOutput(diskMeter)))
        terminalOutput(swapMeter)?.also {
            when(meterSystem) {
                MeterSystem.macosx -> macOsxSwapMeter(it)
                MeterSystem.linux -> linuxSwapMeter(it)
            }

        }
        template.convertAndSend("/topic/messages", SystemInfo("wsRequests", counter++))
    }

    private fun linuxSwapMeter(commandOutput: String) {
        val pattern = Pattern.compile("^Swap:[ ]+([0-9]+)[ ]+([0-9]+).*")
        val matcher = pattern.matcher(commandOutput)
        if (matcher.find()) {
            template.convertAndSend("/topic/messages", SystemInfo("swap", matcher.group(1)))
            template.convertAndSend("/topic/messages", SystemInfo("swapUsed", matcher.group(2)))
        } else {
            logger.warn("no swap info accessible")
        }

    }

    private fun macOsxSwapMeter(commandOutput: String) {
        val pattern = Pattern.compile(".*total = ([0-9.,]+[A-Z]).*used = ([0-9.,]+[A-Z]).*")
        val matcher = pattern.matcher(commandOutput)
        if (matcher.find()) {
            template.convertAndSend("/topic/messages", SystemInfo("swap", matcher.group(1)))
            template.convertAndSend("/topic/messages", SystemInfo("swapUsed", matcher.group(2)))
        } else {
            logger.warn("no swap info accessible")
        }
    }

    private fun terminalOutput(command: String): String? {
        val process = Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", command))
        process.waitFor()
        val result = BufferedReader(InputStreamReader(process.inputStream))
                .lines().collect(Collectors.joining(""))
        if (logger.isDebugEnabled) {
            logger.debug("Meter: $result")
        }
        return result
    }


}