package io.hhplus.tdd.database

import io.hhplus.tdd.point.PointHistory
import io.hhplus.tdd.point.TransactionType
import org.springframework.stereotype.Component

/**
 * 해당 Table 클래스는 변경하지 않고 공개된 API 만을 사용해 데이터를 제어합니다.
 */
@Component
class PointHistoryTable {
    private val table = mutableListOf<PointHistory>() // 포인트 내역을 저장하는 가변 리스트
    private var cursor: Long = 1L // PointHistory 객체들을 리스트 형태로 관리하여 유저의 트랜잭션 이력을 저장한다.

    // 특정 유저의 포인트 트랜잭션을 추가. 충전, 사용 등과 같은 트랜잭션 기록이 발생할 때, 이 정보를 리스트에 저장한다.
    fun insert(
        id: Long,
        amount: Long,
        transactionType: TransactionType,
        updateMillis: Long,
    ): PointHistory {
        Thread.sleep(Math.random().toLong() * 300L)
        // 새로운 PointHistory 객체를 생성
        val history = PointHistory(
            id = cursor++, // 커서 값이 포인트 이력의 고유 ID로 설정되며, 이후 커서 값이 1 증가
            userId = id, // 해당 유저의 ID
            amount = amount, // 트랜잭션 금액
            type = transactionType, // 트랜잭션 종류
            timeMillis = updateMillis, // 트랜잭션이 발생한 시간
        )
        table.add(history) // 생성한 PointHistory 객체를 리스트에 추가
        return history
    }

    // 특정 유저의 포인트 트랜잭션 이력을 조회
    fun selectAllByUserId(userId: Long): List<PointHistory> {
        // 저장된 PointHistory 리스트에서 userId가 주어진 값과 일치하는 모든 내역을 필터링하여 반환
        return table.filter { it.userId == userId }
    }
}