package book.kotlin_in_action.part1.chapter_4.constructor

import book.kotlin_in_action.part1.chapter_4.nested.View

/**
 * 클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 주 생성자라고 부른다.
 * constructor 키워드는 주 생서아나 부 생성자 정의를 시작할 때 사용
 * init 키워드는 초기화 블록을 시작
 */
class User constructor(_nickname: String) {
    val nickname: String

    init {
        nickname = _nickname
    }
}

class UserV2 (_nickname: String) {
    val nickname = _nickname
}

class UserV3 (val nickname: String)

class UserV4 (val nickname: String,
              val isSubscribed: Boolean = true)


open class OpenUser(val nickname: String) {}

class UserV5 (nickname: String): OpenUser(nickname)



//아래는 디폴트 생성자가 만들어짐.
open class Open

class OpenV2


//어떤 클래스를 클래스 외부에서 인스턴스화하지 못하게 막고 싶다면 모든 생성자를 Private으로 만들면 된다.

class SecretType private constructor()


class Context
class AttributeSet

open class OpenView {
    constructor(ctx: Context){}

    constructor(ctx: Context, attr: AttributeSet){}

}

class MyButton: OpenView {

    //체이닝도 가능
    constructor(ctx: Context, values: Object) : this(ctx,  AttributeSet())

    constructor(ctx: Context) : super(ctx) {

    }

    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {

    }


}