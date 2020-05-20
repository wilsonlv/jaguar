package org.jaguar.test;

import junit.framework.TestCase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lvws on 2019/11/1.
 */
public class TestRun extends TestCase {



    public void testThree() {

        String[] str = {"19","3","3","7","5","1","6","10"};
        int resLength = (int) Math.floor(str.length/2);
        String[] res = new String[resLength];
        for (int i = 0; i < res.length; i++) {
            res[i] = str[(i+1)*2-1];
        }
        System.out.println(Arrays.toString(res));

    }

    public void testTwo() {
        List<Map> list = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("kk", "1");
        list.add(map);
        map = new HashMap();
        map.put("ss", "2");
        list.add(map);
        map = new HashMap();
        map.put("kk", "3");
        list.add(map);
        map = new HashMap();
        map.put("dd", "4 ");
        list.add(map);
        map = new HashMap();
        map.put("kk", "3");
        list.add(map);
        map = new HashMap();
        map.put("k", "1");
        list.add(map);
        System.out.println(list);

        List< String> keyList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Iterator<Object> it = list.get(i).keySet().iterator();
            while(it.hasNext()){
                keyList.add(it.next().toString());
            }
            if (i>0) {
                for (int j = 0; j < i; j++) {
                    if (keyList.get(i).equals(keyList.get(j))) {
                        if (list.get(i).get(keyList.get(i)).equals(list.get(j).get(keyList.get(j)))) {
                            list.remove(i);
                        }
                    }
                }
            }
        }
        System.out.println(list);


    }
}
