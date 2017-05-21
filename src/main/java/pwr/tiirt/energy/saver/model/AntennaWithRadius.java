package pwr.tiirt.energy.saver.model;

import com.google.common.collect.Lists;
import lombok.*;
import pwr.tiirt.energy.saver.Antenna;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kasia on 04.05.2017.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AntennaWithRadius {

    private int id;
    private int x;
    private int y;
    private int r;
    private boolean isActive;
    private List<Integer> neighborIds;


    public AntennaWithRadius(int x, int y, int r, boolean isActive) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isActive = isActive;
    }


    public int getSmallestX() {
        return getX() - getR();
    }

    public int getBiggestX() {
        return getX() + getR();
    }

    public int getSmallestY() {
        return getY() - getR();
    }

    public int getBiggestY() {
        return getY() + getR();
    }

    public static List<AntennaWithRadius> antennaToAntennaWithRadius(List<Antenna> antennas, int[] bestRanges) {

        return antennas
                .stream()
                .map(a -> new AntennaWithRadius(a.id, a.x, a.y, bestRanges[antennas.indexOf(a)], a.active, a.getNeighborIds()))
                .collect(Collectors.toList());
    }

    public List<AntennaWithRadius> getNeighbors(List<AntennaWithRadius> all) {
        return all.stream().filter(antenna -> this.neighborIds.contains(antenna.getId())).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Integer> getAdjacencyByAntennaId(int id, List<AntennaWithRadius> all) {
        List<AntennaWithRadius> antennasWithId = all.stream().filter(a -> id == a.id).collect(Collectors.toList());
        if (antennasWithId.size() != 1) {
            throw new IllegalArgumentException("Found antenna with duplicated id:" + id);
        } else
            return antennasWithId.get(0).neighborIds;
    }

    public static List<Antenna> antennaWithRadiusToAntenna(List<AntennaWithRadius> antennasWithRadius) {
        List<Antenna> antennas = antennasWithRadius.stream().map(a -> new Antenna(a.id, a.x, a.y, Lists.newArrayList(), a.isActive)).collect(Collectors.toList());
        return Antenna.setAdjacencies(antennas, antennasWithRadius);
    }
}
