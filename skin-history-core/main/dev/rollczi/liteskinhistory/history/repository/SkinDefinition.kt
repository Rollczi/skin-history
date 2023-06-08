package dev.rollczi.liteskinhistory.history.repository

import com.dzikoysk.sqiffy.definition.*
import dev.rollczi.liteskinhistory.LiteSkinHistoryVersion

@Definition([
    DefinitionVersion(
        version = LiteSkinHistoryVersion.V_0_1_0,
        name = "skin",
        properties = [
            Property(name = "id", type = DataType.SERIAL),
            Property(name = "name", type = DataType.VARCHAR, details = "16"),
            Property(name = "value", type = DataType.TEXT),
        ],
        constraints = [
            Constraint(type = ConstraintType.PRIMARY_KEY, name = "pk_skin_id", on = ["id"]),
        ],
    )
])
internal object SkinDefinition