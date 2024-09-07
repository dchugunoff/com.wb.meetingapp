package database.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val id = Users.varchar("id", 100)
    val phoneNumber = Users.varchar("phone_number", 20)
    val name = Users.varchar("name", 15).nullable()
    val avatar = Users.varchar("avatar", 100).nullable()
    val city = Users.varchar("city", 50).nullable()
    val aboutUser = Users.varchar("about_user", 150).nullable()
    val tags = Users.text("tags").nullable()
    val telegramNickname = Users.varchar("telegram_nickname", 50).nullable()
    val habrNickname = Users.varchar("habr_nickname", 50).nullable()

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[id] = userDTO.id
                it[phoneNumber] = userDTO.phoneNumber
                it[name] = userDTO.name
                it[avatar] = userDTO.avatar
                it[city] = userDTO.city
                it[aboutUser] = userDTO.aboutUser
                it[tags] = userDTO.tagsInterests?.joinToString(separator = ",")
                it[telegramNickname] = userDTO.telegramNickname
                it[habrNickname] = userDTO.habrNickname
            }
        }
    }

    fun fetchUser(userId: String): UserDTO? {
        return transaction {
            Users.selectAll().where { Users.id eq userId }
                .mapNotNull {
                    UserDTO(
                        it[Users.id],
                        it[phoneNumber],
                        it[name],
                        it[avatar],
                        it[city],
                        it[aboutUser],
                        it[tags]?.split(","),
                        it[telegramNickname],
                        it[habrNickname]
                    )
                }.singleOrNull()
        }
    }

    fun fetchUserByPhoneNumber(phoneNumber: String): UserDTO? {
        return try {
            transaction {
                Users.selectAll().where { Users.phoneNumber eq phoneNumber }
                    .mapNotNull {
                        UserDTO(
                            it[Users.id],
                            it[Users.phoneNumber],
                            it[name],
                            it[avatar],
                            it[city],
                            it[aboutUser],
                            it[tags]?.split(","),
                            it[telegramNickname],
                            it[habrNickname]
                        )
                    }.singleOrNull()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun update(userId: String, updatedUser: UserDTO) {
        transaction {
            Users.update({ Users.id eq userId }) { statement ->
                updatedUser.name?.let { statement[name] = it }
                updatedUser.avatar?.let { statement[avatar] = it }
                updatedUser.city?.let { statement[city] = it }
                updatedUser.aboutUser?.let { statement[aboutUser] = it }
                updatedUser.tagsInterests?.let { statement[tags] = it.joinToString(separator = ",") }
                updatedUser.telegramNickname?.let { statement[telegramNickname] = it }
                updatedUser.habrNickname?.let { statement[habrNickname] = it }
            }
        }
    }
}