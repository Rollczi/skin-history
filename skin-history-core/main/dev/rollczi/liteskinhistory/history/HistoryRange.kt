package dev.rollczi.liteskinhistory.history

class HistoryRange(private val rangeSize: Int, private val rangeIndex: Int) {

    fun offset(): Int {
        return rangeSize * rangeIndex
    }

    fun limit(): Int {
        return rangeSize
    }

    fun nextRange(): HistoryRange {
        return HistoryRange(rangeSize, rangeIndex + 1)
    }

    fun previousRange(): HistoryRange {
        return HistoryRange(rangeSize, rangeIndex - 1)
    }

}