package com.example.ekologapp

data class User(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
) {
    constructor() : this("", "", "", "")
}
