package pwr.tiirt.energy.saver.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import pwr.tiirt.energy.saver.Antenna;

import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by Sandra on 07.05.2017.
 */
public class JSONFileReaderTest {

    private static final Integer BOARD_WIDTH = 1000;
    private static final Integer BOARD_HEIGHT = 1000;
    private static final Integer SAMPLE_MAX_RANGE = 100;
    public static final int EXPECTED_NO_OF_ANTENNAS = 5;
    private static JSONFileReader instance;
    private static final String TEST_TOPOLOGY_FILEPATH = JSONFileReaderTest.class.getResource("/test_topology.json").getFile();

    @BeforeClass
    public static void setUpClass() {
        instance = new JSONFileReader();
    }


    @Test
    public void shouldDeserializeDataCorrectly() throws Exception {
        List<Antenna> antennas = instance.getAntennaData(TEST_TOPOLOGY_FILEPATH, BOARD_WIDTH, BOARD_HEIGHT, SAMPLE_MAX_RANGE);
        assertEquals(EXPECTED_NO_OF_ANTENNAS, antennas.size());
        assertEquals(antennas.get(1), antennas.get(0).getNeighbours().get(0));
    }

    @Test
    public void getBoardCoordinates() throws Exception {
        List<Integer> coverageAreaData = instance.getBoardCoordinates(TEST_TOPOLOGY_FILEPATH);
        assertEquals("List should contain 3 entries - x dim, y dim and max range", 3, coverageAreaData.size());
        assertEquals(Integer.valueOf(100), coverageAreaData.get(0));
        assertEquals(Integer.valueOf(100), coverageAreaData.get(1));
        assertEquals(Integer.valueOf(150), coverageAreaData.get(2));

    }
}
