package com.github.ata.shared.extension

import org.slf4j.LoggerFactory
import org.slf4j.Logger

inline fun <reified T> T.getLogger(): Logger = LoggerFactory.getLogger(T::class.java)