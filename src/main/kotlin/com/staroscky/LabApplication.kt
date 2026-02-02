package com.staroscky

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients(basePackages = ["com.staroscky"])
class LabApplication

fun main(args: Array<String>) {
	runApplication<LabApplication>(*args)
}
