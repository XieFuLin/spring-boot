package com.xfl.boot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by XFL
 * time on 2018/8/24 13:35
 * description:
 */
public class TestRandom {

    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int randNum;
        //产生10000个数据测试
        for (int i = 0; i < 10000; i++) {
            randNum = generateRandom13();
            if (map.containsKey(randNum)) {
                map.put(randNum, map.get(randNum) + 1);
            } else {
                map.put(randNum, 1);
            }
        }
        System.out.println(map);

    }

    /**
     * 产生0-5的随机数
     *
     * @return
     */
    public static int generateRandom5() {
        Random r = new Random();
        return r.nextInt(6);
    }

    /**
     * 产生0-1的随机数
     *
     * @return
     */
    public static int generateRandom1() {
        return generateRandom5() % 2;
    }

    /**
     * 产生0-15之间的随机数
     *
     * @return
     */
    public static int generateRandom13() {
        StringBuilder sb = new StringBuilder();
        sb.append(generateRandom1());
        sb.append(generateRandom1());
        sb.append(generateRandom1());
        sb.append(generateRandom1());
        //将二进制转换为十进制
        int randNumber = Integer.valueOf(sb.toString(), 2);
        //随机数大于13时舍去，重新生成，暂时不考虑极端情况产生的数据都是大于13的
        if (randNumber > 13) {
            randNumber = generateRandom13();
        }
        return randNumber;
    }
}
