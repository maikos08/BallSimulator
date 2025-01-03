package software.ulpgc;

import java.util.List;

public interface  BallDisplay {
    void draw(List<Circle> circles);
    void of(Grabbed grabbed);
    void of(Released released);

    public record Circle(String id, int x, int y, int r) {
        public boolean isAt(int x, int y) {
            // Calculamos la distancia euclidiana desde el punto al centro del círculo
            double distance = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
            return distance <= r; // Si está dentro del radio, es válido
        }
    }

    interface Grabbed {
        void at(Circle circle);
    }

    interface  Released {
        void at(Circle circle);
    }
}
