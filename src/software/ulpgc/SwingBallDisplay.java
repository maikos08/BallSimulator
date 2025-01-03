package software.ulpgc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static software.ulpgc.BallPresenter.toMeters;

public class SwingBallDisplay extends JPanel implements BallDisplay {
    private final List<Circle> circles;
    private Grabbed grabbed = null;
    private Released released = null;
    private Optional<Circle> circle = Optional.empty();
    private int width;
    private int height;

    public SwingBallDisplay() {
        this.circles = new ArrayList<>();
        this.addMouseListener(createMouseListener());
        this.addMouseMotionListener(createMouseMotionListener());
    }

    private MouseListener createMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if (grabbed == null) return;
                circle = findCircle(toReferenceX(e.getX()), toReferenceY(e.getY()));
                circle.ifPresent(c -> grabbed.at(c));
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                if (released == null) return;
                circle.ifPresent(c -> released.at(new Circle(c.id(),
                        toReferenceX(e.getX()),
                        toReferenceY(e.getY()),
                        c.r())));
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }

    private MouseMotionListener createMouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (grabbed == null || circle.isEmpty()) return;
               circle.ifPresent(c -> grabbed.at(
                       new Circle(c.id(),
                       toReferenceX(e.getX()),
                       toReferenceY(e.getY()),
                       c.r())));
            }


            @Override
            public void mouseMoved(MouseEvent e) {}
        };
    }

    private Optional<Circle> findCircle(int x, int y) {
        synchronized (circles) {
            return circles.stream().filter(c -> c.isAt(x, y)).findFirst();
        }
    }

    private int toReferenceY(int y) {
        return height - y;
    }

    private int toReferenceX(int x) {
        return x - width / 2;
    }

    @Override
    public void draw(List<Circle> circles) {
        synchronized (this.circles) {
            this.circles.clear();
            this.circles.addAll(circles);
            repaint();
        }
    }

    @Override
    public void of(Grabbed grabbed) {
        this.grabbed = grabbed;
    }

    @Override
    public void of(Released released) {
        this.released = released;
    }


    @Override
    protected void paintComponent(Graphics graphics) {

        // Ejemplo de dibujo con Graphics

        synchronized (this.circles) {
            super.paintComponent(graphics);
            width = getWidth();
            height = getHeight() - 5; // La base {línea}
            clearCanvas(graphics, width, height);
            circles.forEach(c -> draw(graphics, c, width, height));
        }
    }

    private Circle toCircle(String id, int x, int y, int r) {
        return new Circle(id, x, y, r);
    }

    private void draw(Graphics graphics, Circle c, int width, int height) {
        graphics.setColor(Color.GREEN );
        graphics.fillOval(width / 2 + c.x() - c.r(), height - c.y() - c.r(), c.r() * 2, c.r() * 2);
    }

    private static void clearCanvas(Graphics graphics, int width, int height) {
        graphics.setColor(Color.black);
        graphics.fillRect(0,0, width, height);
        graphics.setColor(Color.white);
        graphics.fillRect(0, height, width, height + 5);
    }
}
