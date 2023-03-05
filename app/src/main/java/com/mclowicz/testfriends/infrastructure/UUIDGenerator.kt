package com.mclowicz.testfriends.infrastructure

import java.util.*

class UUIDGenerator : IdGenerator {
    override fun next(): String {
        return UUID.randomUUID().toString()
    }
}