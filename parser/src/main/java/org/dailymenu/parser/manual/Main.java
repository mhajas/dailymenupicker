package org.dailymenu.parser.manual;

public class Main {


    public static void main(String[] args) {
        ClassProviderFactory factory = new ClassProviderFactory();

        factory.init();

        if (factory.canProvide("ChIJQ7t5NgeUEkcRfF9rW4VCrjA")) {
            System.out.println(factory.create("ChIJQ7t5NgeUEkcRfF9rW4VCrjA").parse());
        }
    }
}
