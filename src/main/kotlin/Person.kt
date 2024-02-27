class Person(
    val name: String,
    var age: Int) {
}



fun main() {
    val person = Person("subin", 111)
    println(person.name)
    person.age = 1123
    //person.name = "hh"
}