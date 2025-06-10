package com.study.kotlin_in_action.part_1.chapter_4.nested;

public class ButtonJava implements View{
    @Override
    public State getCurrentState() {
        return new ButtonState();
    }

    @Override
    public void restoreState(State state) {

    }


    public static class ButtonState implements State {}

    public static void main(String[] args) {
        ButtonJava button = new ButtonJava();
        button.getCurrentState();
    }
}
