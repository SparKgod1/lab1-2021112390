package graph;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static java.util.stream.Collectors.counting;
import static org.junit.Assert.*;

public class MainTest {


    @Test
    public void queryBridgeWords() throws IOException {
        String[] lines = Main.readFile("F:\\JetBrains\\projects\\lab1-2021112390\\src\\main\\resources\\test.txt");
        Main.site = Main.counting(lines);
        Main.aid = Main.preProcess(Main.site);

        ArrayList<String> bri = Main.queryBridgeWords("new", "to");
        ArrayList<String> expect = new ArrayList<String>();
        expect.add("worlds");
        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);

        bri = Main.queryBridgeWords("new", "");
        expect.clear();
        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);

        bri = Main.queryBridgeWords("n@ew", "to");
        expect.clear();
        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);

        bri = Main.queryBridgeWords("to", "new");
        expect.clear();
        for (int i = 0; i < bri.size(); i++) {
            Assert.assertEquals(expect.get(i), bri.get(i));
        }
        Main.printBridge(bri);
    }

}