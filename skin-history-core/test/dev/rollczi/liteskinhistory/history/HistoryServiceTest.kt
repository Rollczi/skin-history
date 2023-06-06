package dev.rollczi.liteskinhistory.history

import dev.rollczi.liteskinhistory.support.SqiffyContainer
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
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
    fun `should create history records and get first and second page`() {
        val service = historyService()

        service.createHistory("rollczi", "skin1")
        service.createHistory("rollczi", "skin2")
        service.createHistory("rollczi", "skin3")
        service.createHistory("rollczi", "skin1")
        service.createHistory("rollczi", "Crispi")
        service.createHistory("rollczi", "vLucky")
        service.createHistory("rollczi", "Mike")
        service.createHistory("rollczi", "Jeb")
        service.createHistory("rollczi", "vLucky")
        service.createHistory("rollczi", "hirobrine")
        service.createHistory("rollczi", "Alex")

        val (firstPage, total) = service.findHistory("rollczi", 4, 0)

        assert(total == 11L)
        assertEquals(4, firstPage.size)
        assertEquals("Alex", firstPage[0].skin)
        assertEquals("hirobrine", firstPage[1].skin)
        assertEquals("vLucky", firstPage[2].skin)
        assertEquals("Jeb", firstPage[3].skin)

        val (secondPage, total2) = service.findHistory("rollczi", 4, 1)

        assertEquals(11L, total2)
        assertEquals(4, secondPage.size)
        assertEquals("Mike", secondPage[0].skin)
        assertEquals("Crispi", secondPage[1].skin)
        assertEquals("skin1", secondPage[2].skin)
        assertEquals("skin3", secondPage[3].skin)

        val (thirdPage, total3) = service.findHistory("rollczi", 4, 2)

        assertEquals(11L, total3)
        assertEquals(1, thirdPage.size)
        assertEquals("skin2", thirdPage[0].skin)
    }

}