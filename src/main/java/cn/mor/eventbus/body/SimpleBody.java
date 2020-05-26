package cn.mor.eventbus.body;

public class SimpleBody {

    private String name ;

    private int age;


    public static  SimpleBody valueOf(String name, int age) {
        SimpleBody result = new SimpleBody();
        result.name = name;
        result.age = age;
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
