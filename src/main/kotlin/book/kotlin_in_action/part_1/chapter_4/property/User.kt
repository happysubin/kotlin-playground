package book.kotlin_in_action.part_1.chapter_4.property

interface User {
    val nickName: String
}

class PrivateUser(override val nickName: String): User

/**
 * 커스텀 객체로 프로퍼티를 설정.
 */
class SubscribingUser(val email: String): User {
    override val nickName: String
        get() = email.substringBefore("@")
}

class FaceBookUser(val accountId: Int): User {
    override val nickName = getFaceBookName(accountId)

    private fun getFaceBookName(accountId: Int): String {
        TODO("Not yet implemented")
    }
}

/**
 * field라는 특별한 접근자를 사용해 뒷받침하는 필드에 접근할 수 있다.
 * 게터에서는 field 값을 읽을 수만 있고, 세터에서는 field 값을 읽거나 쓸 수 있다.
 */

class UserEntity(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("Address was changed for $name: $field -> ${value.trimIndent()}  ")
            //address = value X. 스택오버 플로. 무한 루프?
            field = value
        }
}

fun main() {
    val u = UserEntity("siu")
    u.address = "ho"

}