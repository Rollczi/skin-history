package dev.rollczi.liteskinhistory.history

import dev.rollczi.liteskinhistory.support.SqiffyContainer
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

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

        val (firstPage, total) = service.findHistory("rollczi", 0, 4)

        assert(total == 11L)
        assert(firstPage.size == 4)
        assert(firstPage[0].skin == "Alex")
        assert(firstPage[1].skin == "hirobrine")
        assert(firstPage[2].skin == "vLucky")
        assert(firstPage[3].skin == "Jeb")

        val (secondPage, total2) = service.findHistory("rollczi", 1, 4)

        assert(total2 == 11L)
        assert(secondPage.size == 4)
        assert(secondPage[0].skin == "Mike")
        assert(secondPage[1].skin == "Crispi")
        assert(secondPage[2].skin == "skin1")
        assert(secondPage[3].skin == "skin3")

        val (thirdPage, total3) = service.findHistory("rollczi", 2, 4)

        assert(total3 == 11L)
        assert(thirdPage.size == 1)
        assert(thirdPage[0].skin == "skin2")
    }

}