package pwr.tiirt.energy.saver.model;

import com.google.common.collect.Lists;
import lombok.*;
import pwr.tiirt.energy.saver.Antenna;

import java.util.List;

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

    private int x;
    private int y;
    private int r;
    private boolean isActive;

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

    public static List<AntennaWithRadius> antennaToAntennaWithRadius(List<Antenna> antennas, int[] bestRanges){
        List<AntennaWithRadius> awr = Lists.newArrayList();
        for(int i=0; i<antennas.size(); i++){
            Antenna a = antennas.get(i);
            awr.add(new AntennaWithRadius(a.x, a.y, bestRanges[i], a.active));
        }
        return awr;
    }
}
