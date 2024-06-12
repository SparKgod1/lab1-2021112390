package graph;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;



public class MainTest {


    @Test
    public void queryBridgeWords_1() throws IOException {
        String[] lines = Main.readFile("F:\\JetBrains\\projects\\lab1-2021112390\\src\\main\\resources\\test.txt");
        Main.site = Main.counting(lines);
        Main.aid = Main.preProcess(Main.site);

        System.out.println("测试用例编号：1");

        ArrayList<String> bri = Main.queryBridgeWords("new", "to");
        ArrayList<String> expect = new ArrayList<>();
        expect.add("worlds");
        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);

    }

    @Test
    public void queryBridgeWords_2() throws IOException {
        String[] lines = Main.readFile("F:\\JetBrains\\projects\\lab1-2021112390\\src\\main\\resources\\test.txt");
        Main.site = Main.counting(lines);
        Main.aid = Main.preProcess(Main.site);

        System.out.println("测试用例编号：2");
        ArrayList<String> bri = Main.queryBridgeWords("new", "");
        ArrayList<String> expect = new ArrayList<>();
        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);

    }

    @Test
    public void queryBridgeWords_3() throws IOException {
        String[] lines = Main.readFile("F:\\JetBrains\\projects\\lab1-2021112390\\src\\main\\resources\\test.txt");
        Main.site = Main.counting(lines);
        Main.aid = Main.preProcess(Main.site);
        System.out.println("测试用例编号：3");
        ArrayList<String> bri = Main.queryBridgeWords("n@ew", "to");
        ArrayList<String> expect = new ArrayList<>();

        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);

    }

    @Test
    public void queryBridgeWords_4() throws IOException {
        String[] lines = Main.readFile("F:\\JetBrains\\projects\\lab1-2021112390\\src\\main\\resources\\test.txt");
        Main.site = Main.counting(lines);
        Main.aid = Main.preProcess(Main.site);
        System.out.println("测试用例编号：4");
        ArrayList<String> bri = Main.queryBridgeWords("to", "new");
        ArrayList<String> expect = new ArrayList<>();
        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);
    }

}