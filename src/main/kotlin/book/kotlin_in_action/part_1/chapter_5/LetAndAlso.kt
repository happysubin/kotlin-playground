package book.kotlin_in_action.part_1.chapter_5


data class Data(var name: String, var num: Int)

fun main() {

    //let은 run, with과 동일한데 it 사용 가능
    val d = Data("Test", 23).let {
        it.num += 2
        it.name += "ho"
        100 //리턴 값
    }
    println(d)

    //apply와 같지만 it 사용 가능
    val data = Data("Test", 23).also {
        it.num += 2
        it.name += "ho"
    }

    println(data)
}