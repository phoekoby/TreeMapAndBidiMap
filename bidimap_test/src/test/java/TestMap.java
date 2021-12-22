import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.vsu.map.bidimap.BidiMap;
import ru.vsu.map.bidimap.SimpleDualBidiMap;
import ru.vsu.map.treemap.SimpleTreeMap;
import org.junit.jupiter.api.*;
import java.util.Map;

public class TestMap {
    private Map<Integer, String> map;
    private BidiMap<Integer, String> bidiMap;

    @BeforeEach
    public void testCreatingMaps(){
        map = new SimpleTreeMap<>();
        bidiMap = new SimpleDualBidiMap<>();
    }
    @Test
    public void testNewMapsMustHaveZeroSize(){
        Assertions.assertEquals(map.size(),0);
        Assertions.assertEquals(bidiMap.size(),0);
    }
    @Test
    public void testEmpty(){
        Assertions.assertTrue(map.isEmpty());
        Assertions.assertTrue(bidiMap.isEmpty());
    }
    @Test
    public void testAdd(){
        map.put(2,"13");
        map.put(4,"13");
        map.put(2,"12");

        bidiMap.put(2,"13");
        bidiMap.put(4,"13");
        bidiMap.put(2,"12");
        Assertions.assertFalse(map.isEmpty());
        Assertions.assertFalse(bidiMap.isEmpty());
        Assertions.assertEquals(map.size(),2);
        Assertions.assertEquals(bidiMap.size(), 2);
    }

    @Test
    public void testGetting(){
        map.put(2,"13");
        map.put(4,"13");
        map.put(2,"12");

        bidiMap.put(2,"13");
        bidiMap.put(4,"13");
        bidiMap.put(2,"12");
        Assertions.assertEquals(map.get(2),"12");
        Assertions.assertEquals(bidiMap.getKey("12"),2);
        Assertions.assertEquals(bidiMap.getValue(4),"13");
    }
    @Test
    public void testRemove(){
        map.put(2,"13");
        map.put(4,"13");
        map.put(2,"12");

        bidiMap.put(2,"13");
        bidiMap.put(4,"13");
        bidiMap.put(2,"12");

        map.remove(4);
        bidiMap.removeValue("12");
        Assertions.assertEquals(map.size(),1);
        Assertions.assertEquals(bidiMap.size(),1);
    }

}
