package io.hhplus.tdd

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.point.PointService
import io.hhplus.tdd.point.UserPoint
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock


class PointServiceUnitTest {
    private lateinit var userPointTable: UserPointTable
    private lateinit var pointHistoryTable: PointHistoryTable
    private lateinit var pointService: PointService

    @BeforeEach
    fun setUp() {
        userPointTable = mock()
        pointHistoryTable = mock()
        pointService = PointService(userPointTable, pointHistoryTable)
    }

    @Test //완료
    @DisplayName("충전시 최대잔고가 500000원을 넘게되면 확인 메시지가 발생하는 함수입니다.")
    fun maximumBalance(){
        val currentMillis = System.currentTimeMillis()

        val point = UserPoint(109, 300000, currentMillis)

        val exception = assertThrows<IllegalArgumentException> {
            point.charge(250000)
        }

        assertEquals("포인트의 합이 500,000점을 초과하여 충전할 수 없습니다.", exception.message)
    }

    @Test // 완료
    @DisplayName("0 포인트를 충전시 확인 메시지가 발생하는 함수입니다.")
    fun notRechargeZero() {
        val currentMillis = System.currentTimeMillis()

        val point = UserPoint(109, 1000, currentMillis)

        val exception = assertThrows<IllegalArgumentException> {
            point.charge(0)
        }

        assertEquals("충전할 금액을 다시 확인하고 입력해주세요.", exception.message)

    }


    @Test //완료
    @DisplayName("포인트 사용 시 잔고부족이면 메시지가 발생하는 함수입니다.")
    fun insufficientBalance() {

        val currentMillis = System.currentTimeMillis()

        val point = UserPoint(109, 50000, currentMillis)

        val exception = assertThrows<IllegalArgumentException> {
            point.use(55000)
        }

        assertEquals("잔고부족으로 포인트를 사용하실 수 없습니다.", exception.message)
    }

    @Test//완료
    @DisplayName("0포인트 사용 시 확인 메시지가 발생하는 함수입니다.")
    fun notUseZero() {
        val currentMillis = System.currentTimeMillis()

        val point = UserPoint(109, 1000, currentMillis)

        val exception = assertThrows<IllegalArgumentException> {
            point.use(0)
        }

        assertEquals("사용할 금액을 다시 확인하고 입력해주세요.", exception.message)
    }


}