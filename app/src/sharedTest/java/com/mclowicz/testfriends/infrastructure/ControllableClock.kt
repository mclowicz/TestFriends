package com.mclowicz.testfriends.infrastructure

class ControllableClock(private val timeStamp: Long) : Clock {

    override fun now(): Long {
        return timeStamp
    }
}