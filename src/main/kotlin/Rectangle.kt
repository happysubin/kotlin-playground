class Rectangle(
    val height: Int,
    val width: Int
) {
    val isSquare: Boolean
        get() {
            return height == width
        }
}

fun main(){
    val rectangle = Rectangle(3, 3)
    println(rectangle.isSquare)
}