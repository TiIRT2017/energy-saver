package pwr.tiirt.energy.saver.model;

import lombok.*;

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
public class Rectangle {

    // zał: ordered jak trzeba
    private List<Integer> x;
    private List<Integer> y;

    private Point A;
    private Point B;
    private Point C;
    private Point D;

    public Rectangle(List<Integer> x, List<Integer> y) {
        this.x = x;
        this.y = y;
        this.A = new Point(x.get(0), y.get(0));
        this.B = new Point(x.get(1), y.get(1));
        this.C = new Point(x.get(2), y.get(2));
        this.D = new Point(x.get(3), y.get(3));
    }

}
