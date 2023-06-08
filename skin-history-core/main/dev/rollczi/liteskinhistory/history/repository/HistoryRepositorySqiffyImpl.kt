package dev.rollczi.liteskinhistory.history.repository

import com.dzikoysk.sqiffy.SqiffyDatabase
import com.dzikoysk.sqiffy.changelog.Migrator
import com.dzikoysk.sqiffy.dsl.*
import com.dzikoysk.sqiffy.dsl.statements.JoinType
import com.dzikoysk.sqiffy.dsl.statements.Order
import dev.rollczi.liteskinhistory.history.HistoryRange
import dev.rollczi.liteskinhistory.history.api.SkinHistoryRecord
import java.time.Instant

internal class HistoryRepositorySqiffyImpl(private val sqiffyDatabase: SqiffyDatabase) : HistoryRepository {

    init {
        val changeLog = sqiffyDatabase.generateChangeLog(
            UserDefinition::class,
            SkinDefinition::class,
            SkinHistoryDefinition::class,
        )

        sqiffyDatabase.runMigrations(changeLog)
    }

    override fun createHistory(username: String, skinName: String, skinValue: String, changedAt: Instant): SkinHistory {
        val skin = sqiffyDatabase.insertOrUpdate(
            SkinTable,
            where = { SkinTable.name eq skinName },
            serialize = {
                it[SkinTable.name] = skinName
                it[SkinTable.value] = skinValue
            },
            deserialize = {
                Skin(
                    it[SkinTable.id],
                    skinName,
                    skinValue
                )
            }
        )

        val user = sqiffyDatabase.insertIfNotExist(
            UserTable,
            where = { UserTable.name eq username },
            serialize = { it[UserTable.name] = username },
            deserialize = { User(it[UserTable.id], username) }
        )

        sqiffyDatabase.delete(SkinHistoryTable)
            .where {
                and(
                    SkinHistoryTable.skinId eq skin.id,
                    SkinHistoryTable.userId eq user.id
                )
            }
            .execute()

        return sqiffyDatabase.insert(SkinHistoryTable) {
            it[SkinHistoryTable.userId] = user.id
            it[SkinHistoryTable.skinId] = skin.id
            it[SkinHistoryTable.changedAt] = changedAt
        }.map {
            SkinHistory(
                id = it[SkinHistoryTable.id],
                userId = user.id,
                skinId = skin.id,
                changedAt = changedAt
            )
        }.first()
    }

    override fun findHistory(username: String, historyRange: HistoryRange): List<SkinHistoryRecord> {
        return sqiffyDatabase.select(SkinHistoryTable)
            .slice(SkinTable.name, SkinHistoryTable.changedAt)
            .join(JoinType.INNER, SkinHistoryTable.userId, UserTable.id)
            .join(JoinType.INNER, SkinHistoryTable.skinId, SkinTable.id)
            .where { UserTable.name eq username }
            .orderBy(Pair(SkinHistoryTable.changedAt, Order.DESC))
            .limit(limit = historyRange.limit(), offset = historyRange.offset())
            .map {
                SkinHistoryRecord(
                    skin = it[SkinTable.name],
                    skinValue = it[SkinTable.value],
                    changedAt = it[SkinHistoryTable.changedAt]
                )
            }
            .toList()
    }

    override fun countHistory(username: String): Long {
        return sqiffyDatabase.select(SkinHistoryTable)
            .slice(SkinHistoryTable.id.count())
            .join(JoinType.INNER, SkinHistoryTable.userId, UserTable.id)
            .where { UserTable.name eq username }
            .map {
                it[SkinHistoryTable.id.count()]
            }
            .first()
    }

    private fun <TABLE : Table, ENTITY> SqiffyDatabase.insertIfNotExist(
        table: TABLE,
        where: () -> Expression<out Column<*>, Boolean>,
        serialize: (Values) -> Unit,
        deserialize: (Row) -> ENTITY,
    ): ENTITY {
        val entity: ENTITY? = select(table)
            .where(where)
            .map(deserialize)
            .firstOrNull()

        if (entity != null) {
            return entity
        }

        return insert(table, serialize)
            .map(deserialize)
            .first()
    }

    private fun <TABLE : Table, ENTITY> SqiffyDatabase.insertOrUpdate(
        table: TABLE,
        where: () -> Expression<out Column<*>, Boolean>,
        serialize: (Values) -> Unit,
        deserialize: (Row) -> ENTITY,
    ): ENTITY {
        val entity: ENTITY? = select(table)
            .where(where)
            .map(deserialize)
            .firstOrNull()

        if (entity != null) {
            update(table, serialize)
                .where(where)
                .execute()

            return entity
        }

        return insert(table, serialize)
            .map(deserialize)
            .first()
    }

}