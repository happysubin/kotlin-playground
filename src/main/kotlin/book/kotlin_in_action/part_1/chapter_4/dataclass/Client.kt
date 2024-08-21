package book.kotlin_in_action.part_1.chapter_4.dataclass

class Client(val name: String, val postalCode: Int) {}

//toString, equals, hashcode를 정의하지 않아도 된다.
//데이터 클래스는 val 필드로 구성될 것을 권장한다.
data class ClientV2(val name: String, val postalCode: Int) {}

/***
 * 데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있게 코틀린 컴파일러는 한 가지 편의 메서드를 제공한다.
 * 그 메서드는 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 copy 메서드다.
 * 객체를 메모리상에서 직접 바꾸는 대신 복사본을 만드는 편이 더 낫다.
 * 복사본은 원본과 다른 생명주기를 가지며, 복사를 하면서 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도 프로그램에서 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않는다.
 */


fun main() {
    println(Client("ho", 3) == Client("ho", 3))
    println(Client("ho", 3))


    println(ClientV2("ho", 3) == ClientV2("ho", 3))
    println(ClientV2("ho", 3))

    val c1 = ClientV2("su", 13)
    println(c1 == c1.copy())
    println(c1.copy(postalCode = 19000))
}