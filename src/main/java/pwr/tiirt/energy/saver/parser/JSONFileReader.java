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

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class JSONFileReader {

    private Gson instance;

    public JSONFileReader() {
        this.instance = new Gson();
    }

    public List<Antenna> getAntennaData(final String topologyDataFilepath, final Integer xRange, final Integer yRange, final Integer maxRange) throws IOException, AntennaOutOfBoundException {
        final JsonObject topologyData = readDataFromJson(topologyDataFilepath);
        final JsonArray antennaArray = topologyData.getAsJsonArray("antennas");
        final List<Antenna> antennas = parseAntennaData(antennaArray);
        if (validateAntennasCoord(antennas, xRange, yRange, maxRange)) {
            return antennas;
        } else {
            throw new AntennaOutOfBoundException(antennas.toString());
        }
    }

    public List<Integer> getBoardCoordinates(final String boardCoordFilePath) throws IOException {
        final JsonObject boardJsonData = readDataFromJson(boardCoordFilePath).getAsJsonObject("boardData");
        final JsonElement x = boardJsonData.get("x");
        final JsonElement y = boardJsonData.get("y");
        final JsonElement maxRange = boardJsonData.get("maxRange");
        return Lists.newArrayList(x.getAsInt(), y.getAsInt(), maxRange.getAsInt());
    }

    private JsonObject readDataFromJson(final String dataFilepath) throws IOException {

        try (FileReader fr = new FileReader(dataFilepath); JsonReader reader = new JsonReader(fr)) {
            return instance.fromJson(reader, JsonObject.class);
        }
    }

    private List<Antenna> parseAntennaData(final JsonArray antennaArray) {
        final List<Antenna> antennas = Lists.newArrayList();
        for (final JsonElement antenna : antennaArray) {
            antennas.add(instance.fromJson(antenna, Antenna.class));
        }
        return antennas;
    }

    private boolean validateAntennasCoord(final List<Antenna> antennas, final int xAxisRange, final int yAxisRange, final Integer maxRange) {
        return antennas.stream().allMatch(new AntennaValidCoordPredicate(xAxisRange, yAxisRange, maxRange));
    }
}
