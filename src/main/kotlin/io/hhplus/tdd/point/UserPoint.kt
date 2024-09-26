package io.hhplus.tdd.point

data class UserPoint(val id: Long, var point: Long, val updateMillis: Long){
    fun charge(chargePoint: Long){
        if(chargePoint + point > 500000) throw IllegalArgumentException("포인트의 합이 500,000점을 초과하여 충전할 수 없습니다.")
        if(chargePoint <= 0) throw IllegalArgumentException("충전할 금액을 다시 확인하고 입력해주세요.")

        this.point += chargePoint
    }

    fun use(usePoint: Long){
        if(usePoint > point) throw IllegalArgumentException("잔고부족으로 포인트를 사용하실 수 없습니다.")
        if(usePoint <= 0) throw IllegalArgumentException("사용할 금액을 다시 확인하고 입력해주세요.")

        this.point -= usePoint
    }
}
