package com.xfl.boot;

/**
 * Created by XFL
 * time on 2018/8/24 20:54
 * description:
 */
public class TestMaxNum {
    static int[] arr = new int[]{1, 4, -8, 9, -12, 8, 13, 19};

    public static void main(String[] args) {
        System.out.println(maxSubSequent(1, arr.length - 1));
    }

    public static int maxSubSequent(int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return arr[startIndex] > 0 ? arr[endIndex] : 0;
        }
        int center = (startIndex + endIndex) / 2;
        //一直向左拆分，然后得到左子序列的最大值
        int maxLeftValue = maxSubSequent(startIndex, center);
        //一直向右拆分，然后得到右子序列的最大值
        int maxRightValue = maxSubSequent(center + 1, endIndex);

        //临时变量，存储序列相加的值
        int tValue;

        //从中间向左扫描求最大序列和
        int centerToLeftMaxValue = 0;
        tValue = 0;
        for (int i = center; i >= startIndex; i--) {
            tValue += arr[i];
            //如果没有大于0的数，那么，就当左边的是0了
            if (tValue > centerToLeftMaxValue) {
                centerToLeftMaxValue = tValue;
            }
        }

        //从中间向右扫面最大序列和
        int centerToRightMaxValue = 0;
        tValue = 0;
        for (int i = center + 1; i <= endIndex; i++) {
            tValue += arr[i];
            if (tValue >= centerToRightMaxValue) {
                centerToRightMaxValue = tValue;
            }
        }

        /**
         *  经过上面的操作，已经知道了三个值
         *  maxLeftValue左序列的最大和
         *  maxRightValue右序列的最大和
         *  左右合并的之后序列的最大和
         *  再在这三个最大和之间取最大值即可
         */
        return Math.max(Math.max(maxLeftValue, maxRightValue), centerToLeftMaxValue + centerToRightMaxValue);
    }
}
