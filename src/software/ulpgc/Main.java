package software.ulpgc;

/// Model view presenter
// Facilita el principio de responsabilidad unica -> una razón para el cambio se hace el cambio en ese solo sitio
// Compatible con el fundamento de modularidad -> toda la logica del presenter no esta intóxicada con ninguna
// independencia de librerias, como podría tener la vista

public class Main {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        BallPresenter presenter = new BallPresenter( mainFrame.getBallDisplay(), new BallSimulator( 0.001))
                .add(new Ball("1",0.2,0, 10, 0, -9.8, 0.3))
                .add(new Ball("2",0.4,-10, 0.5, 0, -1.8, 10))
                .add(new Ball("3",0.8,10, 20, 0, -5.8, 1));
        presenter.execute();
        mainFrame.setVisible(true);
    }
}
