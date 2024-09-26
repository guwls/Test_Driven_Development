package io.hhplus.tdd.database

import io.hhplus.tdd.point.UserPoint
import org.springframework.stereotype.Component

/**
 * 해당 Table 클래스는 변경하지 않고 공개된 API 만을 사용해 데이터를 제어합니다.
 */
@Component // Spring 컴포넌트 스캔에 자동으로 빈으로 등록, 이클래스는 다른 클래스에서 의존성 주입을 통해 사용할 수 있다.
class UserPointTable {
    private val table = HashMap<Long, UserPoint>()

    // id로 해당 유저의 포인트 정보 조회
    fun selectById(id: Long): UserPoint {
        Thread.sleep(Math.random().toLong() * 200L) // 조회 작업에 약간의 지연을 추가
        // 해시맵에서 유저의 포인트 정보를 조회 -> 해당 ID의 유저가 없으면 UserPoint를 반환해 기본 값으로 포인트 0을 가진 유저 정보를 만든다.
        return table[id] ?: UserPoint(id = id, point = 0, updateMillis = System.currentTimeMillis())
    }

    // 유저의 포인트를 업데이트하거나, 새로운 유저에 대해 포인트 정보를 삽입한다.
    fun insertOrUpdate(id: Long, amount: Long): UserPoint {
        Thread.sleep(Math.random().toLong() * 300L) // 삽입/업데이트 작업에 약간의 지연을 추가
        // 주어진 id와 amount로 새로운 UserPoint 객체를 생성하고, updateMillis는 현재 시간을 기록한다.
        val userPoint = UserPoint(id = id, point = amount, updateMillis = System.currentTimeMillis())
        table[id] = userPoint // 해시맵에 id를 키로, 새로운 또는 업데이트된 UserPoint 객체를 값으로 저장한다.
        return userPoint
    }
}