package ua.com.bpgdev.customioc.reader.xml.testentity;

import javax.annotation.PostConstruct;
import java.io.Serializable;

public class MySubClass extends MyClass implements Serializable, Comparable {

    private MySubClass somePrivateMySubClass;
    private double somePrivateDouble = 19d;
    private Float somePrivateFloat = 111f;

    @PostConstruct
    public void init(){

    }

    public MySubClass getSomePrivateMySubClass() {
        return somePrivateMySubClass;
    }

    public double getSomePrivateDouble() {
        return somePrivateDouble;
    }

    public Float getSomePrivateFloat() {
        return somePrivateFloat;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
