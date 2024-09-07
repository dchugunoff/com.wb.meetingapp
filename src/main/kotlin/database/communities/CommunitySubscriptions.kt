package database.communities

import database.users.Users
import org.jetbrains.exposed.sql.Table

object CommunitySubscriptions: Table("community_subscriptions") {
    val communityID = varchar("community_id", 100).references(Communities.id)
    val userID = varchar("user_id", 100).references(Users.id)
    override val primaryKey = PrimaryKey(communityID, userID)
}