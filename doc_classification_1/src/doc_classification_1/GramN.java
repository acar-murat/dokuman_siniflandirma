/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doc_classification_1;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author murat acar 2
 */
public class GramN {

    public String kategoriAdı;
    public String dosyaAdı;
    public String dosyaYolu;
   
    public String tahmin;
   
    private Map<String, Integer> gram2 = new HashMap<>();
    private Map<String, Integer> gram3 = new HashMap<>();
    
    public void showAllGrams2() {

        System.out.print("|    KEYS    |  VALUES  |\n");
        gram2.keySet().forEach((key) -> {
            System.out.println("|     " + key + "     |   " + gram2.get(key) + "   |");
        });

    }

    public int getFrequency2(String key) {

            return gram2.get(key);

    }

    public void setFrequency2(String key) {

        int value = gram2.get(key);
        value++;

        gram2.put(key, value);

    }

    public void add2(String key, int frequency) {
        gram2.put(key, frequency);

    }

    public boolean checkKey2(String key) {
        return gram2.get(key) != null;
    }

    public void showAllGrams3() {

        System.out.print("|    KEYS    |  VALUES  |\n");
        gram3.keySet().forEach((key) -> {
            System.out.println("|     " + key + "     |   " + gram3.get(key) + "   |");
        });

    }

    public int getFrequency3(String key) {

        return gram3.get(key);

    }

    public void setFrequency3(String key) {

        int value = gram3.get(key);
        value++;

        gram3.put(key, value);

    }

    public void add3(String key, int frequency) {
        gram3.put(key, frequency);

    }

    public boolean checkKey3(String key) {
        return gram3.get(key) != null;
    }

}
