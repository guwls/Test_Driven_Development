import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.point.PointService
import io.hhplus.tdd.point.UserPoint
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future

class PointServiceConcurrencyTest {
    private val userPointTable = mock(UserPointTable::class.java)
    private val pointHistoryTable = mock(PointHistoryTable::class.java)
    private val pointService = PointService(userPointTable, pointHistoryTable)

    @BeforeEach
    fun setUp() {
        // Mocking: selectById가 항상 기본적인 UserPoint 객체를 반환하도록 설정
        val currentMillis = System.currentTimeMillis()
        val mockUserPoint = UserPoint(1L, 50000L, currentMillis)
        `when`(userPointTable.selectById(1L)).thenReturn(mockUserPoint)

        // insertOrUpdate 호출 시, 충전된 포인트 값을 반영한 UserPoint를 반환하도록 설정
        `when`(userPointTable.insertOrUpdate(anyLong(), anyLong())).thenAnswer { invocation ->
            val userId = invocation.getArgument<Long>(0)
            val newPoints = invocation.getArgument<Long>(1)
            UserPoint(userId, newPoints, currentMillis)
        }
    }

    @Test
    @DisplayName("특정 사용자에 대해 동시 충전에 대한 테스트입니다.")
    fun concurrencyControlCharging() {
        val userId = 1L
        val numberOfThreads = 10
        val executor = Executors.newFixedThreadPool(numberOfThreads)

        val startSignal = CountDownLatch(1)
        val doneSignal = CountDownLatch(numberOfThreads)
        val results = mutableListOf<Future<Boolean>>()

        repeat(numberOfThreads) {
            val result = executor.submit<Boolean> {
                try {
                    startSignal.await() // 모든 스레드가 동시에 시작되도록 대기
                    val updatedUserPoint = pointService.charge(userId, 1000) // 50,000 포인트 충전 시도
                    println("Thread $it 충전 성공: ${updatedUserPoint.point}")
                    true
                } catch (e: Exception) {
                    println("Thread $it 충전 실패: ${e.message}")
                    false
                } finally {
                    doneSignal.countDown()
                }
            }
            results.add(result)
        }

        startSignal.countDown()
        doneSignal.await()
        executor.shutdown()
    }

    @Test
    @DisplayName("특정 사용자에 대해 동시 사용에 대한 테스트입니다.")
    fun concurrencyControlUsing() {
        val userId = 1L
        val numberOfThreads = 10
        val executor = Executors.newFixedThreadPool(numberOfThreads)

        val startSignal = CountDownLatch(1)
        val doneSignal = CountDownLatch(numberOfThreads)
        val results = mutableListOf<Future<Boolean>>()

        repeat(numberOfThreads) {
            val result = executor.submit<Boolean> {
                try {
                    startSignal.await() // 모든 스레드가 동시에 시작되도록 대기
                    val updatedUserPoint = pointService.use(userId, 1000) // 50,000 포인트 충전 시도
                    println("Thread $it 사용 성공: ${updatedUserPoint.point}")
                    true
                } catch (e: Exception) {
                    println("Thread $it 사용 실패: ${e.message}")
                    false
                } finally {
                    doneSignal.countDown()
                }
            }
            results.add(result)
        }

        startSignal.countDown()
        doneSignal.await()
        executor.shutdown()
    }


}
