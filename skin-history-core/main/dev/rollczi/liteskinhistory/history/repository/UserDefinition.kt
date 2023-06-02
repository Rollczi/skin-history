package dev.rollczi.liteskinhistory.history.repository

import com.dzikoysk.sqiffy.definition.*
import dev.rollczi.liteskinhistory.LiteSkinHistoryVersion

@Definition([
    DefinitionVersion(
        version = LiteSkinHistoryVersion.V_0_1_0,
        name = "users",
        properties = [
            Property(name = "id", type = DataType.SERIAL),
            Property(name = "name", type = DataType.VARCHAR, details = "16"),
        ],
        constraints = [
            Constraint(type = ConstraintType.PRIMARY_KEY, name = "pk_users_id", on = ["id"]),
        ],
        indices = [
            Index(type = IndexType.UNIQUE_INDEX, name = "unique_index_users_name", columns = ["name"])
        ],
    )
])
internal object UserDefinition

