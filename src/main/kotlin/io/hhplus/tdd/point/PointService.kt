package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import org.springframework.stereotype.Service
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Service
class PointService(
    private val userPointTable: UserPointTable,
    private val pointHistoryTable: PointHistoryTable,
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointTable.selectById(userId)
    }

    fun getUserPointHistory(userId: Long): List<PointHistory> {
        return pointHistoryTable.selectAllByUserId(userId)
    }

    private val maxPoint = 500000L
    private val lock = ReentrantLock()

    // 검증 필요
    fun charge(userId: Long, chargePoint: Long): UserPoint {
        lock.lock()
        try {
            val userPoint = userPointTable.selectById(userId) ?: throw IllegalStateException("사용자가 존재하지않습니다.")

            userPoint.charge(chargePoint)

            val updateUserPoint = userPointTable.insertOrUpdate(userId, userPoint.point)?: throw IllegalStateException("충전되지 않았습니다.")

            pointHistoryTable.insert(
                id = userId,
                amount = userPoint.point,
                transactionType = TransactionType.CHARGE,
                updateMillis = System.currentTimeMillis()
            )

            return updateUserPoint

        }finally {
            lock.unlock()
        }

    }


    // 검증필요
    fun use(userId: Long, usePoint: Long): UserPoint {
        lock.lock()
        try {
            val userPoint = userPointTable.selectById(userId)?: throw IllegalStateException("사용자가 존재하지않습니다.")

            userPoint.use(usePoint)

            val updateUserPoint = userPointTable.insertOrUpdate(userId, userPoint.point)?: throw IllegalStateException("사용되지 않았습니다.")

            pointHistoryTable.insert(
                id = userId,
                amount = userPoint.point,
                transactionType = TransactionType.USE,
                updateMillis = System.currentTimeMillis()
            )

            return updateUserPoint

        }finally {
            lock.unlock()
        }

    }
}