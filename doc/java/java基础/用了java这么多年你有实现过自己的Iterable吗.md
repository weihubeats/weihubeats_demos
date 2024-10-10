## Iterable接口的作用

Iterable接口主要是通过`iterator()`返回一个Iterator对象，使对象可以在`foreach`中使用

运行自己自定义类的迭代行为

## 不用迭代器的方法遍历

一般我们传统开发中，不会直接使用`Iterable`接口，都是使用实现了`Iterable`接口的集合进行遍历。


比我们我们有一个老师对象，老师下面有很多学生，那么一般的做法就是

1. 首先定义一个学生对象

```java
public class Student {
    
    String name;
    
    String age;
    
}
```

2. 然后定义一个老师对象，然老师去持有学生

```java
public class Teacher {

    private List<Student> students;

}

```


如果我们想要获取所有学生对象然后进行一些业务逻辑操作就是如下方式

```java
public class Main {
    public static void main(String[] args) {
        Teacher teacher = new Teacher();
        
        List<Student> students = teacher.getStudents();
        
        for (Student student : students) {
            System.out.println(student.name);
        }
    }
}
```

可以看到我们并不能直接操作`Teacher`对象，而是通过`Teacher`对象获取到`List<Student>`对象，然后通`List`对象进行遍历。因为`List`接口继承了`Iterable`接口，所以我们可以直接使用`foreach`进行遍历


如果上面的`Teacher`对象实现了`Iterable`接口，那么我们就可以直接使用`foreach`进行遍历了

## 自己实现Iterable接口


