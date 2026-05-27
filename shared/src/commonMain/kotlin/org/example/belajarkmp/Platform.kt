package org.example.belajarkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform