package book.kotlin_in_action.part_1.chapter_4.property

class LengthCounter{


    //이 클래스 밖에서 이 프로퍼티의 값을 바꿀 수 없다.
    var counter: Int = 0
    private set

    fun addWord(word: String) {
        counter += word.length
    }
}

fun main() {
    val lengthCounter = LengthCounter()
    lengthCounter.addWord("Hi!")
    print(lengthCounter.counter)
}