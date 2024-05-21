import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;

import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import javax.imageio.ImageIO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Main extends JFrame {

    private JTextArea inputTextArea;
    private JButton processButton;

    public static HashMap<String, Integer> name = new HashMap<String, Integer>();

    public static HashMap<String, ArrayList<String>> bridge;

    public static HashMap<String, Integer> site;

    public static HashMap<String, ArrayList<String>> aid;

    public static String[] readFromString(String inputString) throws IOException {
        StringBuilder total = new StringBuilder();
        StringReader sr = new StringReader(inputString);

        BufferedReader br = new BufferedReader(sr);
        String line;
        while ((line = br.readLine()) != null) {
            line = line.replaceAll("[^a-zA-Z]", " ");
            total.append(line).append(" ");
        }
        String result = total.toString().trim();
        result = result.replaceAll("\\s\\s+", " ");
        return result.toLowerCase().split(" ");
    }

    public static HashMap<String, Integer> counting(String[] lists) {
        HashMap<String, Integer> Sites = new HashMap<String, Integer>();
        name.put(lists[0], 1);
        for (int i = 0; i < lists.length - 1; i++) {
            if (!name.containsKey(lists[i + 1])) name.put(lists[i + 1], 1);
            String cur = lists[i];
            String next = lists[i + 1];
            String combine = cur + "_" + next;
            if (Sites.containsKey(combine)) Sites.replace(combine, Sites.get(combine) + 1);
            else Sites.put(combine, 1);
        }
        return Sites;
    }

    public static HashMap<String, ArrayList<String>> preProcess(HashMap<String, Integer> site) {
        HashMap<String, ArrayList<String>> res = new HashMap<String, ArrayList<String>>();
        for (String s : site.keySet()) {
            String[] lists = s.split("_");
            if (res.containsKey(lists[0])) {
                ArrayList<String> n = res.get(lists[0]);
                n.add(lists[1]);
                res.replace(lists[0], n);
            } else res.put(lists[0], new ArrayList<>(Arrays.asList(lists[1])));
        }
        return res;
    }


    public static class MyEdge extends DefaultWeightedEdge {
        @Override
        public String toString() {
            return String.valueOf(getWeight());
        }
    }

    public static void showGraph(Graph<String, MyEdge> g) throws IOException {
        JFrame frame = new JFrame("DemoGraph");
        JGraphXAdapter<String, MyEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxIGraphLayout layout = new mxOrganicLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        frame.add(new mxGraphComponent(graphAdapter));
        frame.setSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("F:\\JetBrains\\projects\\lab1\\src\\main\\resources\\graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }


    public static void showDirectedGraph(HashMap<String, Integer> site) throws IOException {
        Graph<String, MyEdge> graph = new DefaultDirectedWeightedGraph<>(MyEdge.class);
        List<String> stringList = new ArrayList<>(name.keySet());
        for (String nodeName : stringList) {
            graph.addVertex(nodeName);
        }
        for (String src_dest : site.keySet()) {
            Double value = (double) site.get(src_dest);
            String[] parts = src_dest.split("_");
            assert (parts.length == 2);
            String src = parts[0];
            String dest = parts[1];
            MyEdge weightedEdge = graph.addEdge(src, dest);
            graph.setEdgeWeight(weightedEdge, value);
        }
        showGraph(graph);
    }


    public static ArrayList<String> queryBridgeWords(String word1, String word2) {
        ArrayList<String> res = new ArrayList<String>();
        if (!name.containsKey(word1) || !name.containsKey(word2)) {
            System.out.println("No word1 or word2 in the graph!");
            return res;
        }

        if (aid.containsKey(word1)) {
            ArrayList<String> mid = aid.get(word1);
            for (String s : mid) {
                if (aid.containsKey(s) && aid.get(s).contains(word2)) res.add(s);
            }
            return res;
        } else return res;
    }

    public static void printBridge(ArrayList<String> res) {
        if (res.isEmpty()) System.out.println("No bridge words from word1 to word2!");
        else {
            System.out.println("The bridge words from word1 to word2 are:");
            for (String s : res) System.out.println(s);
        }
    }

    public static void generateNewText(String inputText) {
        Random random = new Random();
        List<String> lists = new ArrayList<String>(List.of(inputText.toLowerCase().split(" ")));
        for (int i = 0; i < lists.size() - 1; i++) {
            String combine = lists.get(i) + "_" + lists.get(i + 1);
            if (!bridge.containsKey(combine)) continue;
            int ran = random.nextInt(100);
            ArrayList<String> st = bridge.get(combine);
            lists.add(i + 1, st.get(ran % st.size()));
            i++;
        }
        System.out.println(String.join(" ", lists));
    }

    public static HashMap<String, ArrayList<String>> buildBridge() {
        HashMap<String, ArrayList<String>> res = new HashMap<>();
        List<String> stringList = new ArrayList<>(name.keySet());
        for (int i = 0; i < name.keySet().size(); i++) {
            for (int j = 0; j < name.keySet().size(); j++) {
                ArrayList<String> bri = queryBridgeWords(stringList.get(i), stringList.get(j));
                if (bri.isEmpty()) continue;
                String conbine = stringList.get(i) + "_" + stringList.get(j);
                res.put(conbine, bri);
            }
        }
        return res;
    }

    public static String calcShortestPath(String word1, String word2) {
        // 采用单源dijkstra算法
        ArrayList<String> s = new ArrayList<String>(List.of(word1));
        // 到达word1的距离
        HashMap<String, Integer> distance = new HashMap<String, Integer>();
        HashMap<String, String> route = new HashMap<String, String>();
        String shortest_name = "";
        int shortest_int = 0xffff;
        for (String str : aid.get(word1)) {
            String combine = word1 + "_" + str;
            distance.put(str, site.get(combine));
            shortest_int = (shortest_int < site.get(combine)) ? shortest_int : site.get(combine);
            shortest_name = (shortest_int < site.get(combine)) ? shortest_name : str;
            route.put(str, word1 + "->" + str);
        }
        for (int i = 1; i < name.keySet().size() - 1; i++) {
            if (shortest_name.equals(word2)) break;
            s.add(shortest_name);
            String preShort_name = shortest_name;
            int preShort_int = shortest_int;
            shortest_name = "";
            shortest_int = 0xffff;
            if (!aid.keySet().contains(preShort_name)) {
                //重新选举最小距离，并继续
                for (String s_elect : distance.keySet()) {
                    if (s.contains(s_elect)) continue;
                    shortest_int = (shortest_int < distance.get(s_elect)) ? shortest_int : distance.get(s_elect);
                    shortest_name = (shortest_int < distance.get(s_elect)) ? shortest_name : s_elect;
                }
                continue;
            }
            for (String str : aid.get(preShort_name)) {
                String combine = preShort_name + "_" + str;
                try {
                    if (distance.get(str) > preShort_int + site.get(combine)) {
                        distance.replace(str, preShort_int + site.get(combine));
                        route.replace(str, route.get(preShort_name) + "->" + str);
                    }
                } catch (Exception e) {
                    distance.put(str, preShort_int + site.get(combine));
                    route.put(str, route.get(preShort_name) + "->" + str);
                }
            }
            for (String s_elect : distance.keySet()) {
                if (s.contains(s_elect)) continue;
                shortest_int = (shortest_int < distance.get(s_elect)) ? shortest_int : distance.get(s_elect);
                shortest_name = (shortest_int < distance.get(s_elect)) ? shortest_name : s_elect;
            }
        }
        return route.getOrDefault(word2, "unreachable");
    }

    public static String randomWalk() throws IOException {
        Random random = new Random();
        List<String> stringList = new ArrayList<>(aid.keySet());
        int ran = random.nextInt(100);
        String cursor = stringList.get(ran % stringList.size());
        String total = cursor;
        ArrayList<String> edge = new ArrayList<String>();
        boolean flag = true;
        while (flag) {
            ran = random.nextInt(100);
            List<String> next = aid.get(cursor);
            if (!aid.containsKey(cursor)) {
                flag = false;
                continue;
            }
            if (edge.contains(cursor + " " + next.get(ran % next.size()))) flag = false;
            edge.add(cursor + " " + next.get(ran % next.size()));
            cursor = next.get(ran % next.size());
            total += " " + cursor;
        }
        String fileName = "to.txt";
        FileWriter writer = new FileWriter(fileName);
        writer.write(total);
        writer.close();
        return total;
    }


    public Main() {
        name = new HashMap<>();
        bridge = new HashMap<>();
        site = new HashMap<>();
        aid = new HashMap<>();

        setTitle("Text Processing with GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputTextArea = new JTextArea();
        add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        processButton = new JButton("Process Text");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = inputTextArea.getText();
                    String[] lines = readFromString(text);
                    site = counting(lines);
                    // 读入文本，1
                    showDirectedGraph(site);
                    // 展示图，2
                    aid = preProcess(site);

                    ArrayList<String> bri = queryBridgeWords("new", "to");

                    printBridge(bri);
                    // 查询桥接词,3
                    bridge = buildBridge();
//sblab1
                    generateNewText("Seek to explore new and exciting synergies");
                    // 生成新文本，4
                    System.out.println(calcShortestPath("to", "and"));
                    // 计算最短路径，5
                    System.out.println(randomWalk());
                    // 随机游走， 6
                    System.out.println("Text processed.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(processButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}