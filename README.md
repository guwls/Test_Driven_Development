# 동시성 제어 방식에 대한 분석 및 보고서 

## 1. 동시성 제어 방법

### 1.1 `Lock` 인터페이스
- `Lock`은 동시성 제어에서 가장 기본적인 방식 중 하나입니다.
- `Lock`을 통해 여러 스레드가 공유 자원에 접근할 때, 스레드 간의 경험을 제어할 수 있습니다. 
----------------------------------------------------------------------------
#### `Lock`을 활용한 코드 작성 예시
    fun charge(userId: Long, chargePoint: Long): UserPoint {
        lock.lock()
        try {
            val userPoint = userPointTable.selectById(userId)

            userPoint.charge(chargePoint)

            val updateUserPoint = userPointTable.insertOrUpdate(userId, userPoint.point)

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
---------------------------------------------------------------------------------
#### `Lock`의 장점
- synchronized 블록보다 유연하게 제어가 가능합니다.
- 공정성 설정을 통해 선점 전략을 조절할 수 있습니다.

#### `Lock`의 단점
- 락 해제를 명시적으로 관리해야 하므로, 코드가 길어지고 실수가 발생할 수 있습니다.
- 스레드가 락을 오랫동안 점유할 경우 교착상태가 발생할 가능성이 있습니다.



## 결론 
제가 `Lock`을 선택한 이유는 유연하게 제어가 가능하다 점과 락을 세밀하게 제어할 수 있기 때문에 선택하게 되었습니다.

