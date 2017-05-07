package pwr.tiirt.energy.saver.parser;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.*;
import pwr.tiirt.energy.saver.model.AntennaOutOfBoundException;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JSONFileReader {

    private Gson instance;
    public static final String DEFAULT_TOPOLOGY_FILE_PATH = "D:\\workspace\\repos\\energy-saver\\src\\main\\resources\\sample_topology.json";

    public List<AntennaWithRadius> getAntennaData(String topologyDataFilepath, int xRange, int yRange) throws IOException, AntennaOutOfBoundException {
        JsonObject topologyData = readDataFromJson(topologyDataFilepath);
        JsonArray antennaArray = topologyData.getAsJsonArray("antennas");
        List<AntennaWithRadius> antennas = parseAntennaData(antennaArray);
        if (validateAntennasCoord(antennas, xRange, yRange)){
            return antennas;
        }
        else{
            throw new AntennaOutOfBoundException();
        }
    }

    private JsonObject readDataFromJson(String topologyDataFilepath) throws IOException {

        try (FileReader fr = new FileReader(topologyDataFilepath); JsonReader reader = new JsonReader(fr)) {
            return instance.fromJson(reader, JsonObject.class);
        }
    }

    private List<AntennaWithRadius> parseAntennaData(JsonArray antennaArray) {
        List<AntennaWithRadius> antennas = Lists.newArrayList();
        for (JsonElement antenna : antennaArray) {
            antennas.add(instance.fromJson(antenna, AntennaWithRadius.class));
        }
        return antennas;
    }

    private boolean validateAntennasCoord(List<AntennaWithRadius> antennas, int xAxisRange, int yAxisRange) {
        return antennas.stream().allMatch(new AntennaValidCoordPredicate(xAxisRange, yAxisRange));
    }

}
