package graph;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RandomWalkTest {

    @Test
    public void testRandomWalkEmptyAid() throws IOException {
        Main.aid = new HashMap<>(); // Empty aid
        String result = Main.randomWalk();
        assertEquals("", result); // Should be empty because no nodes to walk
    }

    @Test
    public void testRandomWalkSingleNode() throws IOException {
        Main.aid = new HashMap<>();
        Main.aid.put("a", new ArrayList<>()); // Single node with no edges
        String result = Main.randomWalk();
        assertEquals("a", result); // Should return the single node
    }

    @Test
    public void testRandomWalkTwoNodes() throws IOException {
        Main.aid = new HashMap<>();
        Main.aid.put("a", new ArrayList<>(Arrays.asList("b"))); // Two nodes with one edge
        String result = Main.randomWalk();
        assertEquals("a b", result); // Should return the path "a b"
    }

    @Test
    public void testRandomWalkCyclic() throws IOException {
        Main.aid = new HashMap<>();
        Main.aid.put("a", new ArrayList<>(Arrays.asList("b")));
        Main.aid.put("b", new ArrayList<>(Arrays.asList("a"))); // Cycle a -> b -> a

        // Random walk should stop to avoid infinite loop
        String result = Main.randomWalk();
        assertTrue(result.equals("a b") || result.equals("b a"));
    }

    @Test
    public void testRandomWalkMultipleEdges() throws IOException {
        Main.aid = new HashMap<>();
        Main.aid.put("a", new ArrayList<>(Arrays.asList("b", "c"))); // Multiple edges

        // Possible paths: "a b", "a c"
        String result = Main.randomWalk();
        assertTrue(result.equals("a b") || result.equals("a c"));
    }

    @Test
    public void testRandomWalkComplexGraph() throws IOException {
        Main.aid = new HashMap<>();
        Main.aid.put("a", new ArrayList<>(Arrays.asList("b", "c")));
        Main.aid.put("b", new ArrayList<>(Arrays.asList("c")));
        Main.aid.put("c", new ArrayList<>(Arrays.asList("d")));
        Main.aid.put("d", new ArrayList<>());

        // Possible paths: "a b c d", "a c d"
        String result = Main.randomWalk();
        assertTrue(result.equals("a b c d") || result.equals("a c d") || result.equals("b c d") || result.equals("c d") || result.equals("d"));
    }

    @Test
    public void testRandomWalkNoCycles() throws IOException {
        Main.aid = new HashMap<>();
        Main.aid.put("a", new ArrayList<>(Arrays.asList("b")));
        Main.aid.put("b", new ArrayList<>(Arrays.asList("c")));
        Main.aid.put("c", new ArrayList<>(Arrays.asList("d")));
        Main.aid.put("d", new ArrayList<>(Arrays.asList("a"))); // Adding a cycle a -> b -> c -> d -> a

        // Random walk should detect cycle and stop to avoid infinite loop
        String result = Main.randomWalk();
        assertTrue(result.equals("a b c d") || result.equals("b c d a") || result.equals("c d a b") || result.equals("d a b c"));
    }
}
