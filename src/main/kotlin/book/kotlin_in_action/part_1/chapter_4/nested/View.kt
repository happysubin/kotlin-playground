package book.kotlin_in_action.part_1.chapter_4.nested

import java.io.Serializable

interface State: Serializable

interface View {
    fun getCurrentState(): State
    fun restoreState(state: State)
}