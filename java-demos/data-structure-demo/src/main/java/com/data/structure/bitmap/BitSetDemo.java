package com.data.structure.bitmap;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * @author : wh
 * @date : 2024/2/28 16:10
 * @description:
 */
public class BitSetDemo {

    public static void main(String[] args) {
        int range = 10000000;
        Random random = new Random();
        List<Integer> list = new ArrayList<>(range);
        for (int i = 0; i < range; i++) {
            list.add(random.nextInt(range));
        }

        BitSet bitSet = new BitSet(range);
        for (Integer i : list) {
            bitSet.set(i);
        }
        for (int i = 0; i < 100000000; i++) {
            if (!bitSet.get(i)) {
                System.out.println(i);
            }
        }

    }

    public void testNextSetBit() {
        BitSet bitSet = new BitSet();
        bitSet.set(2);
        bitSet.set(5);
        bitSet.set(9);

        int index = bitSet.nextSetBit(0); // index = 2
        index = bitSet.nextSetBit(3); // index = 5
        index = bitSet.nextSetBit(6); // index = 9
        index = bitSet.nextSetBit(10); // index = -1
    }
}
