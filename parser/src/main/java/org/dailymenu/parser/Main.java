package org.dailymenu.parser;

public class Main {


    public static void main(String[] args) {
        ClassProviderFactory factory = new ClassProviderFactory();

        factory.init();

        if (factory.canProvide("ChIJ3VMlLwWUEkcRiEOim7E_5ks")) {
            System.out.println(factory.create("ChIJ3VMlLwWUEkcRiEOim7E_5ks").parse());
        }
    }
}
