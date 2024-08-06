package database.tokens

import database.users.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Tokens : Table("tokens") {
//    val id = Tokens.integer("id").autoIncrement()
    val userId = Tokens.varchar("user_id", 100).references(Users.id)
    val token = Tokens.varchar("token", 100)

    fun upsert(userId: String, token: String) {
        transaction {
            val existingToken = Tokens.select { Tokens.userId eq userId }.singleOrNull()
            if (existingToken != null) {
                // Если запись существует, обновляем токен
                Tokens.update({ Tokens.userId eq userId }) {
                    it[Tokens.token] = token
                }
            } else {
                // Если запись не существует, вставляем новую
                Tokens.insert {
                    it[Tokens.userId] = userId
                    it[Tokens.token] = token
                }
            }
        }
    }

    fun fetchToken(userId: String): String? {
        return transaction {
            Tokens.select { Tokens.userId eq userId }
                .mapNotNull { it[Tokens.token] }
                .singleOrNull()
        }
    }

    fun fetchUserIdByToken(token: String): String? {
        return transaction {
            Tokens.select { Tokens.token eq token }
                .mapNotNull { it[userId] }
                .singleOrNull()
        }
    }
}