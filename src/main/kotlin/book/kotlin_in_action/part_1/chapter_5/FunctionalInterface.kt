package book.kotlin_in_action.part_1.chapter_5

/**
 * 추상 메서드가 하나만 있는 인터페이스는 함수형 인터페이스라고 한다.
 * 또는 SAM(Single Abstract Method, 단일 추상 메서드) 인터페이스라고 한다.
 *
 */

fun main() {
    //자바 메서드에 람다로 인자를 전달
    FP.postponeComputation(1000) { println(42) }
    FP.postponeComputation(1000, object: Runnable {
        override fun run() {
            TODO("Not yet implemented")
        }

    })
    val runnable = Runnable{ println(42) }
    FP.postponeComputation(10000, runnable)

    /**
     * 람다에 대해 무명 클래스를 만들고 그 클래스의 인스턴스를 만들어서 메서드에 넘긴다는 설명은 함수형 인터페이스를 받는 자바 메서드를 코틀린에서 호출할 때 쓰는 방식을 설명해주지만, 컬렉션을 확장한 메서드에 람다를 넘기는 경우 코틀린은 그런 방식을 사용하지 않는다.
     * 코틀린 inline으로 표시된 코틀린 함수에게 람다를 넘기면 아무런 무명 클래스도 만들어지지 않는다.
     * 대부분의 코틀린 확장 함수들은 inline 표시가 붙어 있다.
     * 코틀린에서 inline 키워드는 고차 함수(higher-order function)를 호출하는 과정에서 발생하는 오버헤드를 줄이기 위해 사용
     */


    /**
     * SAM 생성자는 람다를 함수형 인터페이스의 인스턴스로 변환할 수 있게 컴파일러가 자동으로 생성한 함수다.
     * 컴파일러가 자동으로 람다를 함수형 인터페이스 무명 클래스로 바꾸지 못하는 경우 SAM 생성자를 사용할 수 있다.
     *
     */
    createAllDoneRunnable().run()

}

fun createAllDoneRunnable(): Runnable {
    return Runnable { println("All Done!!!") }
}