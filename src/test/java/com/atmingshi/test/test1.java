package com.atmingshi.test;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @create 2023-07-15 16:57
 */
@SpringBootTest
public class test1 {
    public static void main(String[] args) {
        List<animal> animals = new ArrayList<>();
        animals.add(new animal("1"));
        animals.add(new animal("2"));
        animals.add(new animal("3"));
        List<pet> pets = new ArrayList<>();
        BeanUtils.copyProperties(animals,pets);
        System.out.println(pets);
    }
}

class animal{

    public animal(String name) {
        this.name = name;
    }

    String name;


}

class pet extends animal{
    public pet(String name, int age) {
        super(name);
        this.age = age;
    }

    int age;
}



