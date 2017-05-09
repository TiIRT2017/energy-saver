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

    public List<Antenna> getAntennaData(String topologyDataFilepath, Integer xRange, Integer yRange) throws IOException, AntennaOutOfBoundException {
        JsonObject topologyData = readDataFromJson(topologyDataFilepath);
        JsonArray antennaArray = topologyData.getAsJsonArray("antennas");
        List<Antenna> antennas = parseAntennaData(antennaArray);
        if (validateAntennasCoord(antennas, xRange, yRange)) {
            return antennas;
        } else {
            throw new AntennaOutOfBoundException(antennas.toString());
        }
    }

    public List<Integer> getBoardCoordinates(String boardCoordFilePath) throws IOException {
        JsonObject boardJsonData = readDataFromJson(boardCoordFilePath).getAsJsonObject("boardDim");
        JsonElement x = boardJsonData.get("x");
        JsonElement y = boardJsonData.get("y");
        return Lists.newArrayList(x.getAsInt(), y.getAsInt());
    }

    private JsonObject readDataFromJson(String dataFilepath) throws IOException {

        try (FileReader fr = new FileReader(dataFilepath); JsonReader reader = new JsonReader(fr)) {
            return instance.fromJson(reader, JsonObject.class);
        }
    }

    private List<Antenna> parseAntennaData(JsonArray antennaArray) {
        List<Antenna> antennas = Lists.newArrayList();
        for (JsonElement antenna : antennaArray) {
            antennas.add(instance.fromJson(antenna, Antenna.class));
        }
        return antennas;
    }

    private boolean validateAntennasCoord(List<Antenna> antennas, int xAxisRange, int yAxisRange) {
        return antennas.stream().allMatch(new AntennaValidCoordPredicate(xAxisRange, yAxisRange));
    }
}
