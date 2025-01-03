package software.ulpgc;

import java.util.*;


public class BallPresenter {
    public static final Timer timer = new Timer();
    private final BallDisplay ballDisplay;
    private final BallSimulator simulator;
    private  List<Ball> balls;
    private final static double dt = 0.001;
    private final static int period = (int) (1000 * dt);
    private Ball grabbedBall;

    public BallPresenter(BallDisplay ballDisplay, BallSimulator simulator) {
        this.ballDisplay = ballDisplay;
        this.simulator = simulator;
        this.ballDisplay.of(ballGrabbed());
        this.ballDisplay.of(ballReleased());
        this.balls = new ArrayList<>();
    }

    private BallDisplay.Released ballReleased() {
        return _ -> grabbedBall = null;
    }

    private BallDisplay.Grabbed ballGrabbed() {
        return c -> grabbedBall = toBall(c);
    }

    private Ball toBall(BallDisplay.Circle c) {
        Ball ball = findBall(c.id());
        return new Ball(
                ball.id(),
                ball.r(),
                toMeters(c.x()),
                toMeters(c.y()),
                0,
                ball.g(),
                ball.cr());
    }

    private Ball findBall(String id) {
        return balls.stream()
                .filter(b -> b.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public BallPresenter add(Ball ball) {
        this.balls.add(ball);
        return this;
    }

    public  void execute() {
        timer.schedule(task(), 0, period);
    }

    public  void stop() { timer.cancel(); }

    private TimerTask task() {
        return  new TimerTask() {
            @Override
            public void run() {
                simulate();
                draw();
            }

            private void draw() {
                ballDisplay.draw(toCircles(balls));
            }
        };
    }

    private List<BallDisplay.Circle> toCircles(List<Ball> balls) {
        return balls.stream()
                .map(this::map)
                .toList();
    }

    private final static double PixelsPerMeter = 5 / 0.2;
    private BallDisplay.Circle map(Ball b) {
        return new BallDisplay.Circle(b.id(), toPixels(b.x()), toPixels(b.h()), toPixels(b.r())) ;
    }

    private static int toPixels(double b) {
        return (int) (b * PixelsPerMeter);
    }

    public static double toMeters(double b) {
        return (b / PixelsPerMeter);
    }

    private void draw() {
        balls.forEach(_ ->draw());
    }

    private void simulate() {
        balls = balls.stream()
                .map(this::simulateBall)
                .toList();
    }

    private Ball simulateBall(Ball ball) {
        if ( grabbedBall != null && ball.id().equals(grabbedBall.id())) return grabbedBall;
        return simulator.simulate(ball);
    }
}
