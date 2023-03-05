package com.mclowicz.testfriends.infrastructure

class SystemClock : Clock {
    override fun now(): Long {
        return System.currentTimeMillis()
    }
}