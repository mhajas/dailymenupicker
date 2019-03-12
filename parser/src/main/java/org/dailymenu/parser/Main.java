package org.dailymenu.parser;

public class Main {


    public static void main(String[] args) {
        ClassProviderFactory factory = new ClassProviderFactory();

        factory.init();

        if (factory.canProvide("ChIJD55B_ASUEkcRgoFJeL_VJjE")) {
            System.out.println(factory.create("ChIJD55B_ASUEkcRgoFJeL_VJjE").parse());
        }
    }
}
