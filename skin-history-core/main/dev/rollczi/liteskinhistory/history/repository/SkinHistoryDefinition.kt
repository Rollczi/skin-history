package dev.rollczi.liteskinhistory.history.repository

import com.dzikoysk.sqiffy.definition.*
import com.dzikoysk.sqiffy.definition.ConstraintType.*
import com.dzikoysk.sqiffy.definition.DataType.*
import dev.rollczi.liteskinhistory.LiteSkinHistoryVersion

@Definition([
    DefinitionVersion(
        version = LiteSkinHistoryVersion.V_0_1_0,
        name = "skin_history",
        properties = [
            Property(name = "id", type = SERIAL),
            Property(name = "skinId", type = INT),
            Property(name = "userId", type = INT),
            Property(name = "changedAt", type = TIMESTAMP),
        ],
        constraints = [
            Constraint(type = PRIMARY_KEY, name = "pk_skin_history_id", on = ["id"]),
            Constraint(type = FOREIGN_KEY,
                name = "fk_skin_history_skinId",
                on = ["skinId"],
                referenced = SkinDefinition::class,
                references = "id"
            ),
            Constraint(type = FOREIGN_KEY,
                name = "fk_skin_history_userId",
                on = ["userId"],
                referenced = UserDefinition::class,
                references = "id"
            ),
        ]
    )
])
internal object SkinHistoryDefinition