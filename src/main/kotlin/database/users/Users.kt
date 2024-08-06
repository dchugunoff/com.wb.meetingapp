package database.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val id = Users.varchar("id", 100)
    val hasForm = Users.bool("hasForm")
    val phoneNumber = Users.varchar("phoneNumber", 20)
    val pinCode = Users.varchar("pinCode", 4)
    val firstName = Users.varchar("firstName", 15)
    val secondName = Users.varchar("secondName", 15)
    val avatar = Users.varchar("avatar", 20)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[id] = userDTO.id
                it[hasForm] = userDTO.hasForm
                it[phoneNumber] = userDTO.phoneNumber
                it[pinCode] = userDTO.pinCode
                it[firstName] = userDTO.firstName.orEmpty()
                it[secondName] = userDTO.secondName.orEmpty()
                it[avatar] = userDTO.avatar.orEmpty()
            }
        }
    }

    fun fetchUser(userId: String): UserDTO? {
        return transaction {
            Users.selectAll().where { Users.id eq userId }
                .mapNotNull {
                    UserDTO(
                        id = it[Users.id],
                        hasForm = it[hasForm],
                        phoneNumber = it[phoneNumber],
                        pinCode = it[pinCode],
                        firstName = it[firstName],
                        secondName = it[secondName],
                        avatar = it[avatar]
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
                            id = it[Users.id],
                            hasForm = it[hasForm],
                            phoneNumber = it[Users.phoneNumber],
                            pinCode = it[pinCode],
                            firstName = it[firstName],
                            secondName = it[secondName],
                            avatar = it[avatar]
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
            Users.update({ Users.id eq userId }) {
                it[firstName] = updatedUser.firstName.orEmpty()
                it[secondName] = updatedUser.secondName.orEmpty()
                it[hasForm] = updatedUser.hasForm
            }
        }
    }
}