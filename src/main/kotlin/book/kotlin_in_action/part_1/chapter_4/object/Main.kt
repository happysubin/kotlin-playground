package book.kotlin_in_action.part_1.chapter_4.`object`

import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File

/**
 *
 * object 키워드를 다양한 상황에서 사용하지만 모든 경우 클래스를 정의하면서 동시에 인스턴스를 생성한다는 공통점이 있다.
 *
 * 객체 선언:(싱글턴 정의)
 * 동반 객체: 인스턴스 메서드는 아니지만 어떤 클래스와 관련 있는 메서드와 팩토리 메서드를 담을 때 쓰인다.
 * 동반 객체 메서드에 접근할 때는 동반 객체가 포함된 클래스의 이름을 사요할 수 있다.
 *
 * 객체 식은 자바의 무명 내부 클래스 대신 쓰인다.
 */

//싱글톤

class Person

object PayRoll {
    val allEmployees = arrayOf<Person>()

    fun calculateSalary() {
        for (allEmployee in allEmployees) {
            //TODO
        }
    }
}

/**
 * 객체 선언은 object로 시작. 클래스를 정의하고 그 클래스의 인스턴스를 만들어서 변수에 저장하는 모든 작업을 단 한문장으로 처리
 * 클래스와 마찬가지로 객체 선언 안에도 프로퍼티, 메서드, 초기화 블록등이 들어갈 수 있다.
 * 하지만 생성자는 객체 선언에 사용 불가
 *
 * 상속도 가능
 */

object CaseInsensitiveFileComparator: Comparator<File> {
    override fun compare(o1: File?, o2: File?): Int {
        TODO("Not yet implemented")
    }

}

//클래스 안에서 객체 선언도 가능. 그 객체도 인스턴스는 단 하나


data class PersonV2(val name:String) {
    object NameComparator: Comparator<PersonV2> {
        override fun compare(o1: PersonV2, o2: PersonV2): Int {
            return o1.name.compareTo(o2.name)
        }

    }
}

fun main() {
    println(PayRoll.allEmployees)
    println(PersonV2.NameComparator)

    //아래 둘은 똑같음
    println(PersonV3.Loader.fromJson("json.."))
    println(PersonV3.fromJson("json2.."))

    println(Objectt.of("name"))
    println(PersonV5.Companion.fromJson("123"))
    println(PersonV5.fromJson("123"))

}

class Objectt(val value: String) {
    companion object {
        fun of(name: String): Objectt {
            return Objectt(name)
        }
    }
}

class PersonV3(val name:String) {
    companion object Loader {
        fun fromJson(jsonText: String) : PersonV3 {
            return PersonV3(jsonText)
        }
    }
}

interface JSONFactory<T> {
    fun fromJson(jsonText: String): T
}

class PersonV4(val name: String) {

    //동반 객체에서 인터페이스 구현
    companion object: JSONFactory<PersonV4> {
        override fun fromJson(jsonText: String): PersonV4 {
            TODO("Not yet implemented")
        }

    }
}


class PersonV5(val firstName: String, val lastName: String) {
    companion object {

    }
}


//동반 객체도 확장 함수가 가능하다.
fun PersonV5.Companion.fromJson(json: String) : PersonV5 {
    return PersonV5("", "")
}

fun countClicks(window: Window) {
    var clickCount = 0

    //무명 객체를 활용햣 
    window.addMouseListener(object: MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            clickCount++
        }
    })
}

//무영 객체 대입 가능
var listener = object: MouseAdapter() {
    override fun mouseClicked(e: MouseEvent?) {
        super.mouseClicked(e)
    }

    override fun mousePressed(e: MouseEvent?) {
        super.mousePressed(e)
    }

    override fun mouseReleased(e: MouseEvent?) {
        super.mouseReleased(e)
    }

    override fun mouseEntered(e: MouseEvent?) {
        super.mouseEntered(e)
    }
}