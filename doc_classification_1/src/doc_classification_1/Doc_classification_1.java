/*
 * 50 yi geçeb
 */
package doc_classification_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.normalization.TurkishSpellChecker;

/**
 *
 * @author murat acar 2
 */
public class Doc_classification_1 {

    private static ArrayList<File> dokumanlar = new ArrayList<File>();
    private static HashMap<String, Integer> gram2Ozellikler = new HashMap<>();
    private static HashMap<String, Integer> gram3Ozellikler = new HashMap<>();

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        HashMap<String, HashMap<String, Double>> varyans2GramDeger = new HashMap<>();
        HashMap<String, HashMap<String, Double>> varyans3GramDeger = new HashMap<>();

        HashMap<String, HashMap<String, Double>> ortalama2GramDeger = new HashMap<>();
        HashMap<String, HashMap<String, Double>> ortalama3GramDeger = new HashMap<>();

        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        TurkishSpellChecker spellChecker = new TurkishSpellChecker(morphology);

        File folder = new File("1150haber\\raw_texts\\");
        dokumanAl(folder);

        ArrayList<GramN> gramBelgeler = gramVeFrekansCikart(dokumanlar, spellChecker, morphology);

        gram2Ozellikler = ozellikSadelestir(gram2Ozellikler);
        gram3Ozellikler = ozellikSadelestir(gram3Ozellikler);

        ArrayList<GramN> egitimBelgeleri = egitimKumesi(gramBelgeler);

        ArrayList<GramN> testBelgeleri = testKumesi(gramBelgeler);
      

        /**
         * **************************** egitim kısmı ********************************************************
         */
        double varyans = 0, ortalama = 0;
        String kategoriAdi = "";

        HashMap<String, Double> geciciSozlukVar = new HashMap<>();
        HashMap<String, Double> geciciSozlukOrt = new HashMap<>();

        for (int i = 0; i < egitimBelgeleri.size(); ) {

            for (String key : gram2Ozellikler.keySet()) {

                kategoriAdi = egitimBelgeleri.get(i).kategoriAdı;

                varyans = varyans2GramFonk(egitimBelgeleri.subList(i, (i + egitimBelgeleri.size() / 5) ), key);
                ortalama = ortalama2GramFonk(egitimBelgeleri.subList(i, (i + egitimBelgeleri.size() / 5)), key);

                geciciSozlukVar.put(key, varyans);
                geciciSozlukOrt.put(key, ortalama);

            }

            varyans2GramDeger.put(kategoriAdi, geciciSozlukVar);
            ortalama2GramDeger.put(kategoriAdi, geciciSozlukOrt);

            i += (egitimBelgeleri.size() / 5) ;

            geciciSozlukVar = new HashMap<>();
            geciciSozlukOrt = new HashMap<>();
        }

        for (int i = 0; i < egitimBelgeleri.size(); i++) {

            for (String key : gram3Ozellikler.keySet()) {

                kategoriAdi = egitimBelgeleri.get(i).kategoriAdı;

                varyans = varyans3GramFonk(egitimBelgeleri.subList(i, (i + egitimBelgeleri.size() / 5) - 1), key);
                ortalama = ortalama3GramFonk(egitimBelgeleri.subList(i, (i + egitimBelgeleri.size() / 5) - 1), key);

                geciciSozlukVar.put(key, varyans);
                geciciSozlukOrt.put(key, ortalama);

            }

            varyans3GramDeger.put(kategoriAdi, geciciSozlukVar);
            ortalama3GramDeger.put(kategoriAdi, geciciSozlukOrt);

            i += (egitimBelgeleri.size() / 5) - 1;

            geciciSozlukVar = new HashMap<>();
            geciciSozlukOrt = new HashMap<>();
        }

        /**
         * ******************************************************************************************
         */
        // logaritması alinmiş kosullu olasilik formulu
        double ort = 0, var = 0;
        double kosulluOlasilik = Math.log10(Math.exp((-Math.pow((6 - ort), 2)) / (2 * Math.pow(var, 2))) / Math.sqrt(2 * Math.PI * Math.pow(var, 2)));

        naiveBayesTest(varyans2GramDeger, ortalama2GramDeger, testBelgeleri);

//        System.out.println("egitim belgeleri boyutu :  " + egitimBelgeleri.size());
//        System.out.println("test belgeleri boyutu : " + testBelgeleri.size());
//
//        for (int i = 0; i < (egitimBelgeleri.size() / 5) + 1; i++) {
//            System.out.println(egitimBelgeleri.get(i).kategoriAdı);
//
//        }
//        System.out.println("----->>>> "+ testBelgeleri.size());
//        System.out.println(" ----->>>>>" +egitimBelgeleri.size());
//        System.out.println(gramBelgeler.get(1).dosyaYolu + " "+gramBelgeler.get(1).dosyaAdı+" "+ gramBelgeler.get(1).kategoriAdı);
//        
//        gramBelgeler.get(1).showAllGrams2();
//        System.out.println("\n\n\n ");
//        gramBelgeler.get(1).showAllGrams3();
//
//        System.out.print("|    KEYS    |  VALUES  |\n");
//        gram3Ozellikler.keySet().forEach((key) -> {
//            System.out.println("|     " + key + "     |   " + gram3Ozellikler.get(key) + "   |");
//        });
//       System.out.println(icerik("C:\\Users\\murat acar\\Documents\\Ders Notları\\3.Sınıf Dersleri\\Yazılım Laboratuarı\\Döküman Sınıflandırma(2_3)\\1150haber\\raw_texts\\ekonomi\\2.txt"));
//
//        String key = "ekonomi";
//            System.out.println("|     " + key + "     |   " );
//            
//                   varyans2GramDeger.get(key).keySet().forEach((keyIn) -> {
//            System.out.println( "part : "+ keyIn+"------------"+ varyans2GramDeger.get(key).get(keyIn) + "   |");
//              });
//            
//    
//                   
//            System.out.println("|     " + key + "     |   " );
//            
//                   ortalama2GramDeger.get(key).keySet().forEach((keyIn) -> {
//            System.out.println( ortalama2GramDeger.get(key).get(keyIn) + "   |");
//              });
    }

    public static void naiveBayesTest(
            HashMap<String, HashMap<String, Double>> varyans2GramDeger,
            HashMap<String, HashMap<String, Double>> ortalama2GramDeger,
            ArrayList<GramN> testBelgeleri) {

        double ort = 0, var = 0, kosulluSonuc = 0;
        double kosulluOlasilik = 0;
   
        for (int i = 0; i < testBelgeleri.size(); i++) {

            for (String key : gram2Ozellikler.keySet()) {
                
                
                
                ort = ortalama2GramDeger.get("spor").get(key);
                var = varyans2GramDeger.get("spor").get(key);
                
               
               
                if (ort != 0 || var!=0) {
                    if (testBelgeleri.get(i).checkKey2(key)) {
                         System.out.println( testBelgeleri.get(i).dosyaAdı+"--"+key +"----"+
                                testBelgeleri.get(i).getFrequency2(key) +"-"+ort +"-"+
                                 var);
                        
                        kosulluOlasilik = Math.log10(Math.exp(
                                (-Math.pow((testBelgeleri.get(i).getFrequency2(key) - ort), 2)) 
                                / (2 * var)) / 
                                Math.sqrt(Math.PI * var * 2));

                   } else {
                         kosulluOlasilik = Math.log10(Math.exp(
                                (-Math.pow((0 - ort), 2)) 
                                / (2 * var)) / 
                                Math.sqrt(Math.PI * var * 2));
                    }
                    
                }
                else{
                    kosulluOlasilik=0;
                }
                   


                kosulluSonuc += kosulluOlasilik;

            }
            
            
        //  System.out.println("------>  " + testBelgeleri.get(i).dosyaAdı +"   "+testBelgeleri.get(i).kategoriAdı+ " -- " + kosulluSonuc);

            kosulluSonuc = 0;
        }

        //return testBelgeleri;
    }

    public static double varyans2GramFonk(List<GramN> egitimKategori, String gram) {

        double varNumerator = 0, varyans = 0;

        for (int i = 0; i < egitimKategori.size(); i++) {

            if (egitimKategori.get(i).checkKey2(gram)) {
                varNumerator += Math.pow((double) (egitimKategori.get(i).getFrequency2(gram) - ortalama2GramFonk(egitimKategori, gram)), 2);
            }

        }

        varyans = varNumerator / egitimKategori.size();

        return varyans;
    }

    public static double varyans3GramFonk(List<GramN> egitimKategori, String gram) {

        double varNumerator = 0, varyans = 0;

        for (int i = 0; i < egitimKategori.size(); i++) {

            if (egitimKategori.get(i).checkKey3(gram)) {
                varNumerator += Math.pow((double) (egitimKategori.get(i).getFrequency3(gram) - ortalama3GramFonk(egitimKategori, gram)), 2);
            }

        }

        varyans = varNumerator / egitimKategori.size();

        return varyans;
    }

    /**
     * o 2gramın egitimden verilen kategori içerisinde ne kadar geçtiğini bulup
     * kategorideki eleman sayısına bölerek ortalamasını veren fonksiyondur
     *
     * @param kategori --> egitimden parçalanmış gelen kategori (gramN) listesi
     * @param gram aranacak gram değeri
     * @return ortalaması
     */
    public static double ortalama2GramFonk(List<GramN> kategori, String gram) {

        double meanValue = 0;
        double sum = 0;

        for (int i = 0; i < kategori.size(); i++) {

            if (kategori.get(i).checkKey2(gram)) {
                sum += kategori.get(i).getFrequency2(gram);
            }
        }

        meanValue = sum / kategori.size();

        return meanValue;
    }

    /**
     * o 3gramın egitimden verilen kategori içerisinde ne kadar geçtiğini bulup
     * kategorideki eleman sayısına bölerek ortalamasını veren fonksiyondur
     *
     * @param kategori --> egitimden parçalanmış gelen kategori (gramN) listesi
     * @param gram aranacak gram değeri
     * @return ortalaması
     */
    public static double ortalama3GramFonk(List<GramN> kategori, String gram) {

        double meanValue = 0;
        double sum = 0;

        for (int i = 0; i < kategori.size(); i++) {

            if (kategori.get(i).checkKey3(gram)) {
                sum += kategori.get(i).getFrequency3(gram);
            }

        }

        return meanValue;
    }

    /**
     * egitim yapılacak gramN nesnelerinden oluşan egitim kümesini bir liste
     * olarak verir.
     *
     * @param gramBelgeler tum belgelere ait liste
     * @return egitim listesi
     */
    public static ArrayList<GramN> egitimKumesi(ArrayList<GramN> gramBelgeler) {

        ArrayList<GramN> egitimSiniflari = new ArrayList<>();
        //  egitimSiniflari.ensureCapacity(gramBelgeler.size()*4/5);

        int skip = gramBelgeler.size() * 1 / 20;

        int trainingSize = (int) Math.ceil((double) gramBelgeler.size() * 3 / 20);

        int gramSize = gramBelgeler.size();

        for (int i = 0; i < gramSize; i++) {
            egitimSiniflari.addAll(gramBelgeler.subList(i, i + trainingSize));
            i += trainingSize + skip;
        }
        return egitimSiniflari;
    }

    /**
     * test yapılacak gramN nesnelerinden olusan kümeyi liste olarak geri verir
     *
     * @param gramBelgeler tüm belgerlertest listesi
     * @return test listesi
     */
    public static ArrayList<GramN> testKumesi(ArrayList<GramN> gramBelgeler) {

        ArrayList<GramN> testSiniflari = new ArrayList<>();

        int testSize = gramBelgeler.size() * 1 / 20;
        int skip = (int) Math.ceil((double) gramBelgeler.size() * 3 / 20);
        int gramSize = gramBelgeler.size();
        int count = 0;

        for (int i = testSize; i < gramSize; i++) {

            testSiniflari.addAll(gramBelgeler.subList(i, i + testSize));
            i += testSize + skip;

        }

        return testSiniflari;
    }

    /**
     * 50 den küçük frekans değerlerini silen fonk.
     *
     * @param ozellikler
     * @return
     */
    public static HashMap<String, Integer> ozellikSadelestir(HashMap<String, Integer> ozellikler) {

        ArrayList<String> silinecekler = new ArrayList<>();

        for (String key : ozellikler.keySet()) {

            if (ozellikler.get(key) < 50) {
                silinecekler.add((String) key);
            }

        }

        for (String temp : silinecekler) {
            ozellikler.remove(temp);
        }

//        for (Map.Entry<String, Integer> entry : ozellikler.entrySet()) {
//            if (entry.getValue()< 50) {
//                ozellikler.remove(entry.getKey());
//            }
//        }
//        
//        Iterator<Map.Entry<String, Integer>> entries = ozellikler.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry<String, Integer> entry = entries.next();
//            if (entry.getValue() < 50) {
//                ozellikler.remove(entry.getKey());
//            }
//        }
//        
        return ozellikler;
    }

    /**
     * dökümanları global file listesine alır . çalıştı
     *
     * @param folder
     */
    public static void dokumanAl(final File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                dokumanAl(fileEntry);
            } else {
                //  System.out.println(fileEntry.getName());

                dokumanlar.add(fileEntry);

            }
        }
    }

    /**
     * alınan file listesindeki dosyaları Gram nesnelerine çevirerek bir liste
     * şeklinde geri döndürür.
     *
     * @param dokumanlar file listesi
     * @return GramN tipinde bir liste döndürür.
     */
    public static ArrayList<GramN> gramVeFrekansCikart(ArrayList<File> dokumanlar, TurkishSpellChecker spellChecker, TurkishMorphology morphology) {

        ArrayList<GramN> geciciGramList = new ArrayList<>();

        for (int i = 0; i < dokumanlar.size(); i++) {

            geciciGramList.add(parcala(dokumanlar.get(i), spellChecker, morphology));
        }

        return geciciGramList;
    }

    /**
     * alınan file tipindeki dosyayı alır ve içeriğini parçalayıp bir GramN
     * nesneine çevirir
     *
     * @param belge alınan file tipindeki text dosyası
     * @return GramN tipinde bir text belgesine ait nesne
     */
    public static GramN parcala(File belge, TurkishSpellChecker spellChecker, TurkishMorphology morphology) {

        GramN geciciGram = new GramN();
        String islenmisVeri = "";

        try {
            islenmisVeri = imla(icerik(belge.getPath()), spellChecker, morphology);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Doc_classification_1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Doc_classification_1.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!"".equals(islenmisVeri)) {
            String gramPart;
            for (int i = 0; i < islenmisVeri.length() - 2; i++) {

                gramPart = islenmisVeri.substring(i, i + 2);
                if (geciciGram.checkKey2(gramPart)) {
                    geciciGram.setFrequency2(gramPart);
                } else {
                    geciciGram.add2(gramPart, 1);
                }
                if (gram2Ozellikler.containsKey(gramPart)) {

                    gram2Ozellikler.replace(gramPart, gram2Ozellikler.get(gramPart) + 1);
                } else {
                    gram2Ozellikler.put(gramPart, 1);

                }
            }

            for (int i = 0; i < islenmisVeri.length() - 3; i++) {

                gramPart = islenmisVeri.substring(i, i + 3);

                if (geciciGram.checkKey3(gramPart)) {
                    geciciGram.setFrequency3(gramPart);
                } else {
                    geciciGram.add3(gramPart, 1);
                }

                if (gram3Ozellikler.containsKey(gramPart)) {

                    gram3Ozellikler.replace(gramPart, gram3Ozellikler.get(gramPart) + 1);
                } else {
                    gram3Ozellikler.put(gramPart, 1);

                }

            }

        }

        geciciGram.dosyaAdı = belge.getName();
        geciciGram.dosyaYolu = belge.getPath();
        geciciGram.kategoriAdı = belge.getParentFile().getName();

        return geciciGram;
    }

    /**
     * Dosya yolu verilen text dosyasını okur ve geriye içeriğini döndürür
     * çalıştı
     *
     * @param dosyaYolu text dosyasının yolu
     * @return dosya içeriği
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String icerik(String dosyaYolu) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String line;
        String icerik = new String();

        try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(dosyaYolu), "ISO-8859-9"))) {
            while ((line = br1.readLine()) != null) {

                icerik += line;

            }
        }

        return icerik;
    }

    /**
     * ham metini önişleme tabi tutar. çalıştı
     *
     * @param prg ham metin
     * @param spellChecker
     * @param morphology
     * @return ön işlem yapılmış string
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String imla(String prg, TurkishSpellChecker spellChecker, TurkishMorphology morphology) throws UnsupportedEncodingException, IOException {

        prg = prg.toLowerCase();
        ArrayList<String> stopwords = stopWordsRead();
        String[] words = prg.split("[.,:;!/\"'-_]| ");

        //       System.out.println(words[1]);
        ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));
        wordList.removeAll(Arrays.asList(null, ""));

//        System.out.println(">>>>>>>>>>>>>>>>" + " " + wordList.get(0));
        //wordList.add("badraklığımın");
        for (int i = 0; i < stopwords.size(); i++) {
            wordList.removeAll(Arrays.asList(null, stopwords.get(i)));

        }
        ArrayList<String> oneri;
        for (int i = 0; i < wordList.size(); i++) {

            if (!spellChecker.check(wordList.get(i))) {

                oneri = (ArrayList) spellChecker.suggestForWord(wordList.get(i));
                if (!oneri.isEmpty()) {
                    wordList.set(i, oneri.get(0));
                }

                wordList.get(i);

            }

            WordAnalysis result = morphology.analyze(wordList.get(i));

            for (SingleAnalysis analysis : result) {

                //    System.out.println("\tLemmas = " + analysis.getLemmas());
                wordList.set(i, analysis.getLemmas().get(analysis.getLemmas().size() - 1));
            }
        }

        String temp = "";

        for (int i = 0; i < wordList.size(); i++) {

            //System.out.println("|>><>" + islenmisVeri.get(i) + "|");
            temp += wordList.get(i) + "_";
            //System.out.println(">>>> "+temp+"<<<<");
        }

        return temp;

    }

    /**
     * stop wordslerin listesini verir
     *
     * @return @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static ArrayList<String> stopWordsRead() throws FileNotFoundException, UnsupportedEncodingException, IOException {

        ArrayList<String> lSepWords = new ArrayList<>();
        String dosyaYolu = "stop_words.txt";

        String line;
        String paragraph = new String();

        try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(dosyaYolu), "ISO-8859-9"))) {
            while ((line = br1.readLine()) != null) {

                lSepWords.add(line.trim());

            }
        }

        return lSepWords;
    }
}
