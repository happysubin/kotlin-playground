package book.kotlin_in_action.part1.chapter_4.delegate

/**
 * 종종 상속을 허용하지 않는 클래스에 새로운 동작을 추가해야하는데, 이럴 때 사용하는 일반적인 방법이 데코레이터패턴이다.
 * 이 패턴은 상속을 허용하지 않는 클래스 대신 사용할 수 있는 클래스를 만들되 기존 클래스와 같은 인터페이스를 데코레이터가 제공하게 만들고, 기존 클래스를 데코레이터 내부에 필드로 유지하는 것이다.
 * 이때 새로 정의해야 하는 기능은 데코레티어의 메서드에 새로 정의하고 기존  기능이 그대로 필요한 부분은 데코레이터의 메서드가 기존클래스의 메서드에게 요청을 전달한다.
 *
 * 이건 코드가 너무 많이 작성되는 단점이 있다.
 */

class DelegatingCollection<T>: Collection<T> {
    private val innerList= arrayListOf<T>()
    override val size: Int get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()

    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)
}


// 인터페이스를 구현할 때 by 키워드를 이용해 그 인터페이스에 대한 구현을 다른 객체에 위임 중이라는 사실을 병시할 수 있다.

class DelegatingCollectionV2<T> (
    innerList: Collection<T> = ArrayList<T>()
): Collection<T> by innerList {

}

class CountingSet<T> (
    val innerSet: MutableCollection<T> = HashSet<T>()
): MutableCollection<T> by innerSet {
    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(c: Collection<T>): Boolean {
        objectsAdded += c.size
        return innerSet.addAll(c)
    }
}

/**
 * add All을 오버라이드해서 카운터를 증가시키고, MutableCollection 인터페이스의 나머지 메서드는 내부 컨테이너 (innerSet)에게 위임한다.
 * 예를 들어 내부 컨테이너가 addAll을 처리할 때 루프를 돌면서 add를 호출할 수 있지만, 최적화를 위해 다른 방식을 택할 수도 있다.
 */
