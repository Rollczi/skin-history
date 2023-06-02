package dev.rollczi.liteskinhistory.history

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HistoryRangeTest {

    @Test
    fun shouldReturnOffsetAndLimit() {
        val historyRange = HistoryRange(10, 0)

        Assertions.assertEquals(0, historyRange.offset())
        Assertions.assertEquals(10, historyRange.limit())
    }

    @Test
    fun shouldReturnOffsetAndLimit2() {
        val historyRange = HistoryRange(10, 1)

        Assertions.assertEquals(10, historyRange.offset())
        Assertions.assertEquals(10, historyRange.limit())
   }

}