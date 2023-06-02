package com.example.trainserve.entiy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QaRandom {

    public static String achieveCode() {
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"};
        List list = Arrays.asList(beforeShuffle);//将数组转换为集合
        Collections.shuffle(list);//打乱顺序
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<list.size();i++){
            String s=(String) list.get(i);
            //进行追加
            stringBuilder.append(s);
        }
        //对字符串进行截取
        return stringBuilder.toString().substring(3,8);
    }

}
