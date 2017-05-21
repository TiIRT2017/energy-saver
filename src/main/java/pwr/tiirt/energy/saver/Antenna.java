package pwr.tiirt.energy.saver;

import com.google.common.collect.Lists;
import lombok.Getter;
import pwr.tiirt.energy.saver.model.AntennaWithRadius;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Antenna {

    public final int id;
    public final int x;
    public final int y;
    public final List<Antenna> neighbours;
    public boolean active;

    public Antenna(int id, int x, int y, List<Antenna> neighbours, boolean active) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.neighbours = neighbours;
        this.active = active;
    }

    private List<Antenna> getAdjacency(List<Antenna> all, List<AntennaWithRadius> neighborIdsSourceList) {
        List<Integer> ids = AntennaWithRadius.getAdjacencyByAntennaId(this.id, neighborIdsSourceList);
        if (ids.contains(this.id)) {
            throw new IllegalArgumentException("Circular reference detected - antenna with id: " + this.id + " contains itself in own neiborhood.");
        }
        List<Antenna> adjacency = all.stream().filter(a -> ids.contains(a.id)).collect(Collectors.toList());
        if(ids.size() != adjacency.size()){
            throw new IllegalArgumentException("Some neighbors object are missing - expected neighbors: "+ids.size()+" but found only: "+adjacency.size());
        }
        return adjacency;
    }

    public List<Integer> getNeighborIds(){
        return this.neighbours.stream().map(ante -> ante.id).collect(Collectors.toList());
    }

    public static List<Antenna> setAdjacencies(List<Antenna> all, List<AntennaWithRadius> neighborIdsSourceList) {
        List<Antenna> copyOf = Lists.newArrayList(all);
        copyOf.stream().forEach(ante -> ante.neighbours.addAll(ante.getAdjacency(copyOf, neighborIdsSourceList)));
        return copyOf;
    }

    @Override
    public String toString() {
        return "Antenna{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", neighbours=" + neighbours.stream().map(a -> a.getId()).collect(Collectors.toList()) +
                ", active=" + active +
                '}';
    }
}
