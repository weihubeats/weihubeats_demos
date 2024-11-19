## Iterable接口的作用

Iterable接口主要是通过`iterator()`返回一个Iterator对象，使对象可以在`foreach`中使用

实现自己自定义的迭代行为

## 不用迭代器的方法遍历

一般我们传统开发中，不会直接自己实现`Iterable`接口，都是使用实现了`Iterable`接口的集合进行遍历。


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


```java
public class TeacherByIterable implements Iterable<Student> {

    private final Student[] students;

    public TeacherByIterable(Student[] students) {
        this.students = students;
    }

    @Override
    public TeacherIterator iterator() {
        return new TeacherIterator();
    }

    private class TeacherIterator implements java.util.Iterator<Student> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < students.length;
        }

        @Override
        public Student next() {
            return students[index++];
        }
    }

    public static void main(String[] args) {
        Student[] students = {new Student("小奏", "18"), new Student("芙莉莲", "2000"), new Student("韩信", "30")};
        TeacherByIterable teacher = new TeacherByIterable(students);
        for (Student student : teacher) {
            System.out.println(student);
        }
    }
}
```

可以看到自己实现了`Iterable`接口后，我们可以直接使用`foreach`进行遍历了。

其次我们也可以自己对迭代遍历进行控制，比如我们可以在`TeacherIterator`中进行一些逻辑判断，一些数据校验或者处理等


## kafka中的Iterable

`kafka`如果我们进行消费的拉取，我们就可以看到返回的`ConsumerRecords`就是实现了`Iterable`接口，可以使用语法糖`foreach`进行遍历

```java
ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
for (ConsumerRecord<String, String> record : records) {
    System.out.println("接收到消息：key = " + record.key() + ", value = " + record.value() +
    ", partition = " + record.partition() + ", offset = " + record.offset());
    }
```

```java
public class ConsumerRecords<K, V> implements Iterable<ConsumerRecord<K, V>>
```


首先`ConsumerRecords`实现了`Iterable`接口, 不同于我们上面的`TeacherIterator`接口直接实现了`Iterator`接口

`kafka`的实现相对来说还要复杂一些



```java
    private static class ConcatenatedIterable<K, V> implements Iterable<ConsumerRecord<K, V>> {

        private final Iterable<? extends Iterable<ConsumerRecord<K, V>>> iterables;

        public ConcatenatedIterable(Iterable<? extends Iterable<ConsumerRecord<K, V>>> iterables) {
            this.iterables = iterables;
        }

        @Override
        public Iterator<ConsumerRecord<K, V>> iterator() {
            return new AbstractIterator<ConsumerRecord<K, V>>() {
                Iterator<? extends Iterable<ConsumerRecord<K, V>>> iters = iterables.iterator();
                Iterator<ConsumerRecord<K, V>> current;

                public ConsumerRecord<K, V> makeNext() {
                    while (current == null || !current.hasNext()) {
                        if (iters.hasNext())
                            current = iters.next().iterator();
                        else
                            return allDone();
                    }
                    return current.next();
                }
            };
        }
    }
```

`kafka`先是定义了一个`ConcatenatedIterable`类，`ConcatenatedIterable`类继续实现了`Iterable`，然后再这里返回了一个`Iterator`对象

不过这里需要注意的是`ConcatenatedIterable`并没有直接提供实现好的迭代器`Iterator`，而是通过构造方法传入了一个`Iterable`(一般是jdk中自己实现的)对象，然后将`Iterable`对象传入到`AbstractIterator`中，在`AbstractIterator`中进行迭代

`AbstractIterator`这个类实现了`Iterator`接口，提供了一些默认的实现，然后我们只需要实现`makeNext`方法就可以了

```java
public abstract class AbstractIterator<T> implements Iterator<T> {

    private enum State {
        READY, NOT_READY, DONE, FAILED
    }

    private State state = State.NOT_READY;
    private T next;

    @Override
    public boolean hasNext() {
        switch (state) {
            case FAILED:
                throw new IllegalStateException("Iterator is in failed state");
            case DONE:
                return false;
            case READY:
                return true;
            default:
                return maybeComputeNext();
        }
    }

    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        state = State.NOT_READY;
        if (next == null)
            throw new IllegalStateException("Expected item but none found.");
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Removal not supported");
    }

    public T peek() {
        if (!hasNext())
            throw new NoSuchElementException();
        return next;
    }

    protected T allDone() {
        state = State.DONE;
        return null;
    }

    protected abstract T makeNext();

    private Boolean maybeComputeNext() {
        state = State.FAILED;
        next = makeNext();
        if (state == State.DONE) {
            return false;
        } else {
            state = State.READY;
            return true;
        }
    }

}
```

`AbstractIterator`是`kafka`中通用的迭代器实现，主要是对jdk中的`Iterator`进行了一些扩展和优化

具体的优化点如下:
1. 扩展了State枚举来管理迭代器的内部状态（READY, NOT_READY, DONE, FAILED），让迭代器的行为更加可控和可预测
2. `maybeComputeNext()`方法实现了懒加载机制。只有在真正需要下一个元素时才会计算下一个元素，这样可以提高迭代器的性能。
3. 添加了一些异常处理和方法限制，比如不允许调用`remove()`方法，没有元数调用`next`方法时抛出异常自定义异常提示等
4. 提供了`peek()`方法，允许查看下一个元素而不消耗它
5. `allDone()`方法为子类提供了一种简单的方式来表示迭代结束
6. 明确不支持`remove()`操作，避免了不必要的实现复杂性

## 总结

在一些遍历复杂的业务场景中，我们可以自己实现`Iterable`接口，然后对整个遍历过程进行管理和监控。同时支持一些更复杂的便利逻辑处理

自定义`Iterable`接口可以实现延迟加载，处理大数据量的时候不需要一次性将所有数据加载到内存中

比如我们实现一个文件的读取，就可以基于`Iterable`接口实现，每次读取一行，这样可以避免一次性将所有数据加载到内存中，

```java
public class LazyDataProcessorWithCustomIterator implements Iterable<String> {
    
    private final String filename;

    public LazyDataProcessorWithCustomIterator(String filename) {
        this.filename = filename;
    }

    @Override
    public Iterator<String> iterator() {
        return new LazyFileIterator(filename);
    }

    private static class LazyFileIterator implements Iterator<String> {
        
        private BufferedReader reader;
        
        private String nextLine;

        public LazyFileIterator(String filename) {
            try {
                this.reader = new BufferedReader(new FileReader(filename));
                this.nextLine = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException("Error initializing file reader", e);
            }
        }

        @Override
        public boolean hasNext() {
            return nextLine != null;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String currentLine = nextLine;
            try {
                nextLine = reader.readLine();
                if (nextLine == null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading file", e);
            }
            return currentLine;
        }
    }

    public static void main(String[] args) {
        LazyDataProcessorWithCustomIterator processor = new LazyDataProcessorWithCustomIterator("large_file.txt");
        for (String item : processor) {
            System.out.println(item);
        }
    }
}
```

同时还可以简化代码，可以实现`foreach`语法糖，使代码更加简洁和优雅



