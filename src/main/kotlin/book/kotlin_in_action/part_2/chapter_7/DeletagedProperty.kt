package book.kotlin_in_action.part_2.chapter_7

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


/**
 * 지연 초기화는 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요할 경우 초기화할 때 흔히 쓰이는 패턴이다.
 * 초기화 과정에 자원을 많이 사용하거나 객체를 사용할 때마다 꼭 초기화하지 않아도 되는 프로퍼티에 대해 지연 초기화 패턴을 사용할 수 있다.
 */

class Email

fun loadEmails(person: Person): List<Email> {
    println("$person")// 이메일을 가져옴
    return listOf() //그리고 넘긴다고 가정
}

class Person(val name: String) {
    private var _emails: List<Email>? = null
    val emails: List<Email>
        get() {
            if(_emails == null) {
                //레이지 로딩..!!
                _emails = loadEmails(this)
            }
            return _emails!!
        }
}

fun loadEmails(person: PersonV2): List<Email> {
    println("$person")// 이메일을 가져옴
    return listOf() //그리고 넘긴다고 가정
}

//위 클래스와 동일함.
class PersonV2(val name: String) {
    val emails by lazy { loadEmails(this) }
}

/**
 * PropertyChangeSupport와 PropertyChangeEvent 클래스를 사용해객체가 바뀌면 통지를 처리한다.
 */
open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

class PersonV3(val name: String, age: Int, salary: Int): PropertyChangeAware() {
    var age: Int = age
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("age", oldValue, newValue)
        }
    var salary:Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("salary", oldValue, newValue)
        }

}

/***
 *
 * 리팩토링
 */

class ObservableProperty(
    val propName: String,
    var propValue: Int,
    val changeSupport: PropertyChangeSupport
) {

    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(propName, oldValue, newValue)
    }
}




class PersonV4(
    val name: String, age: Int, salary: Int
): PropertyChangeAware() {
    val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(value) {
            _age.setValue(value)
        }
    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(value) {
            _salary.setValue(value)
        }
}

/**
 * 리팩토링
 */

class ObservablePropertyV2(
    var propValue: Int = 10, val changeSupport: PropertyChangeSupport
) {
    operator fun getValue(p: PersonV5, prop: KProperty<*>): Int = propValue
    operator fun setValue(P: PersonV5, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}

class PersonV5(
    val name: String, age: Int, salary: Int
): PropertyChangeAware() {
    var age: Int by ObservablePropertyV2(age, changeSupport)
    var salary: Int by ObservablePropertyV2(salary, changeSupport)
}

class PersonV6(
    val name: String, age: Int, salary: Int
): PropertyChangeAware() {
    private val observer = {
        prop: KProperty<*>, oldValue: Int, newValue: Int ->
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
    var age: Int by Delegates.observable(age, observer)
    var salsary: Int by Delegates.observable(salary, observer)
}

fun main() {
    val p = PersonV3("Dmirty", 34, 300)
    p.addPropertyChangeListener { event ->
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    }

    p.age = 35
    p.salary = 10

    println()
    val p2 = PersonV4("su", 35, 300)
    p2.addPropertyChangeListener { event ->
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    }
    p2.age = 300
    p2.salary = 100

    println()
    val p3 = PersonV5("bin", 33, 333)
    p3.addPropertyChangeListener { event ->
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    }
    p3.age = 200
    p3.salary = 22


    println()

    val p4 = PersonV6("bin", 33, 333)
    p4.addPropertyChangeListener { event ->
        println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
    }

    p4.salsary = 300
    p4.age = 12312

}

//표준 라이브러리에는 ObservableProperty와 비슷한 클래스가 많다.

