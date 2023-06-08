package dev.rollczi.liteskinhistory.history

import dev.rollczi.liteskinhistory.support.SqiffyContainer
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import java.time.Instant.ofEpochSecond
import kotlin.test.assertEquals

@Testcontainers
internal class HistoryServiceIntegrationTest : HistoryServiceTest() {

    @Container
    private val sqiffyContainer = SqiffyContainer()

    override fun historyService(): HistoryService {
        return HistoryFactory.sqiffy(sqiffyContainer.sqiffyDatabase)
    }

}

abstract class HistoryServiceTest {

    abstract fun historyService(): HistoryService

    @Test
    fun `should create history records and get first, second and third page`() {
        val service = historyService()

        service.history("rollczi", "skin1", 1)
        service.history("rollczi", "skin2", 2)
        service.history("rollczi", "skin3", 3)
        service.history("rollczi", "skin1", 4)
        service.history("rollczi", "Crispi", 5)
        service.history("rollczi", "vLucky", 6)
        service.history("rollczi", "Mike", 7)
        service.history("rollczi", "Jeb", 8)
        service.history("rollczi", "vLucky", 9)
        service.history("rollczi", "hirobrine", 10)
        service.history("rollczi", "Alex", 11)

        val (firstPage, total) = service.findHistory("rollczi", 4, 0)
        assertEquals(9L, total)
        assertEquals(4, firstPage.size)

        assertEquals("Alex", firstPage[0].skin)
        assertEquals("hirobrine", firstPage[1].skin)
        assertEquals("vLucky", firstPage[2].skin)
        assertEquals("Jeb", firstPage[3].skin)
        assertEquals(ofEpochSecond(11L), firstPage[0].changedAt)
        assertEquals(ofEpochSecond(10L), firstPage[1].changedAt)
        assertEquals(ofEpochSecond(9L), firstPage[2].changedAt)
        assertEquals(ofEpochSecond(8L), firstPage[3].changedAt)
        assertEquals("Alex at 11", firstPage[0].skinValue)
        assertEquals("hirobrine at 10", firstPage[1].skinValue)
        assertEquals("vLucky at 9", firstPage[2].skinValue)
        assertEquals("Jeb at 8", firstPage[3].skinValue)

        val (secondPage, total2) = service.findHistory("rollczi", 4, 1)
        assertEquals(9L, total2)
        assertEquals(4, secondPage.size)

        assertEquals("Mike", secondPage[0].skin)
        assertEquals("Crispi", secondPage[1].skin)
        assertEquals("skin1", secondPage[2].skin)
        assertEquals("skin3", secondPage[3].skin)
        assertEquals(ofEpochSecond(7L), secondPage[0].changedAt)
        assertEquals(ofEpochSecond(5L), secondPage[1].changedAt)
        assertEquals(ofEpochSecond(4L), secondPage[2].changedAt)
        assertEquals(ofEpochSecond(3L), secondPage[3].changedAt)
        assertEquals("Mike at 7", secondPage[0].skinValue)
        assertEquals("Crispi at 5", secondPage[1].skinValue)
        assertEquals("skin1 at 4", secondPage[2].skinValue)
        assertEquals("skin3 at 3", secondPage[3].skinValue)

        val (thirdPage, total3) = service.findHistory("rollczi", 4, 2)
        assertEquals(9L, total3)
        assertEquals(1, thirdPage.size)

        assertEquals("skin2", thirdPage[0].skin)
        assertEquals(ofEpochSecond(2L), thirdPage[0].changedAt)
        assertEquals("skin2 at 2", thirdPage[0].skinValue)
    }

    private fun HistoryService.history(username: String, skin: String, seconds: Long) {
        this.createHistory(username, skin, "$skin at $seconds", ofEpochSecond(seconds))
    }

}