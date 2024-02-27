fun main(){
    println("Hello World");
    val max = max(3, 4)
    println(max)
    val answer : String = "The world ${max} !!!!"
    println(answer)
}

fun max(a: Int, b: Int): Int = if(a > b) a else b