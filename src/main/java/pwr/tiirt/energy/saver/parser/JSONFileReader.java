package pwr.tiirt.energy.saver.parser;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pwr.tiirt.energy.saver.Antenna;
import pwr.tiirt.energy.saver.model.AntennaOutOfBoundException;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class JSONFileReader {

    public static final String ANTENNAS_JSON_KEY = "antennas";
    public static final String COVERAGE_COORD_JSON_KEY = "boardData";
    public static final String X_AXIS_COORD_JSON_KEY = "x";
    public static final String Y_AXIS_COORD_JSON_KEY = "y";
    public static final String MAX_ANTENNA_RANGE_JSON_KEY = "maxRange";
    private Gson instance;

    public JSONFileReader() {
        this.instance = new Gson();
    }

    public List<Antenna> getAntennaData(final String topologyDataFilepath, final Integer xRange, final Integer yRange, final Integer maxRange) throws IOException, AntennaOutOfBoundException {
        final JsonObject topologyData = readDataFromJson(topologyDataFilepath);
        final JsonArray antennaArray = topologyData.getAsJsonArray(ANTENNAS_JSON_KEY);
        final List<AntennaWithRadius> antennasWithRadius = parseAntennaData(antennaArray);
        return AntennaWithRadius.antennaWithRadiusToAntenna(antennasWithRadius);
//        if (validateAntennasCoord(antennasWithRadius, xRange, yRange, maxRange)) {
//            return AntennaWithRadius.antennaWithRadiusToAntenna(antennasWithRadius);
//        } else {
//            throw new AntennaOutOfBoundException(antennasWithRadius.toString());
//        }
    }

    public List<Integer> getBoardCoordinates(final String boardCoordFilePath) throws IOException {
        final JsonObject boardJsonData = readDataFromJson(boardCoordFilePath).getAsJsonObject(COVERAGE_COORD_JSON_KEY);
        final JsonElement x = boardJsonData.get(X_AXIS_COORD_JSON_KEY);
        final JsonElement y = boardJsonData.get(Y_AXIS_COORD_JSON_KEY);
        final JsonElement maxRange = boardJsonData.get(MAX_ANTENNA_RANGE_JSON_KEY);
        return Lists.newArrayList(x.getAsInt(), y.getAsInt(), maxRange.getAsInt());
    }

    private JsonObject readDataFromJson(final String dataFilepath) throws IOException {

        try (FileReader fr = new FileReader(dataFilepath); JsonReader reader = new JsonReader(fr)) {
            return instance.fromJson(reader, JsonObject.class);
        }
    }

    private List<AntennaWithRadius> parseAntennaData(final JsonArray antennaArray) {
        final List<AntennaWithRadius> antennas = Lists.newArrayList();
        for (final JsonElement antenna : antennaArray) {
            antennas.add(instance.fromJson(antenna, AntennaWithRadius.class));
        }
        return antennas;
    }

    private boolean validateAntennasCoord(final List<AntennaWithRadius> antennas, final int xAxisRange, final int yAxisRange, final Integer maxRange) {
        return antennas.stream().allMatch(new AntennaValidCoordPredicate(xAxisRange, yAxisRange, maxRange));
    }
}
