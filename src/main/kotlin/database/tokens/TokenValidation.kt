package database.tokens

object TokenValidation {
    fun isValidToken(token: String, userId: String): Boolean {
        val storedToken = Tokens.fetchToken(userId)
        return token == storedToken
    }
}