package com.weihubeats.netty.demo.recycler;

import io.netty.util.Recycler;

/**
 * @author : wh
 * @date : 2023/6/1 09:48
 * @description:
 */
public class RecyclerTest {
    
    
    
    public void test() {

        // 1、从对象池获取 User 对象
        User user1 = userRecycler.get();
        // 2、设置 User 对象的属性
        user1.setName("hello");
        // 3、回收对象到对象池
        user1.recycle();
        // 4、从对象池获取对象
        User user2 = userRecycler.get(); 

        System.out.println(user2.getName());

        System.out.println(user1 == user2);
        
    }


    private static final Recycler<User> userRecycler = new Recycler<User>() {

        @Override

        protected User newObject(Handle<User> handle) {

            return new User(handle);

        }

    };

    static final class User {

        private String name;

        private Recycler.Handle<User> handle;

        public void setName(String name) {
            this.name = name;

        }

        public String getName() {
            return name;
        }

        public User(Recycler.Handle<User> handle) {
            this.handle = handle;
        }

        public void recycle() {
            handle.recycle(this);
        }

    }

}
