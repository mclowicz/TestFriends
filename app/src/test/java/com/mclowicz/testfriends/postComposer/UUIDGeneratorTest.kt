package com.mclowicz.testfriends.postComposer

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.infrastructure.UUIDGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.regex.Pattern

@ExtendWith(InstantTaskExecutorExtension::class)
class UUIDGeneratorTest {

    companion object {
        private const val UUID_REGEX = """^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}${'$'}"""
    }

    @Test
    fun generatesCorrectUUID() {
        val pattern = Pattern.compile(UUID_REGEX)

        val uuid = UUIDGenerator().next()

        Assertions.assertTrue(
            pattern.matcher(uuid).matches()
        )
    }
}