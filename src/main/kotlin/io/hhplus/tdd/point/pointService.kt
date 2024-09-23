package io.hhplus.tdd.point

import io.hhplus.tdd.database.UserPointTable
import org.springframework.stereotype.Service

@Service
class pointService(
     private val userPointTable: UserPointTable
) {
    fun getUserPoint(id: Long): UserPoint {
        val userPoint = userPointTable.selectById(id)
            ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        return userPoint
    }
}