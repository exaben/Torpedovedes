package hu.nye.progtech.torpedo2.service;



import org.springframework.stereotype.Repository;

@Repository
public class ControllerFacade {
    /**
     * Mezok
     */
    private Controller controller;


    /**
     * Construktor
     *
     * @param controller to control
     */
    public ControllerFacade(Controller controller) {
        this.controller = controller;
    }


    /**
     * A játék beállítása
     */
    public void setupGame() {
        controller.setupPlayers();
        controller.setupBoards();
    }

    /**
     * Játssz a játékkal
     */
    public void play() {
        //play stuff
        while (controller.isGameOnGoing()) {
            controller.play();
        }
    }
    public void database(){
        controller.createNewDatabase();
        controller.createNewTable();
    }


    /**
     * Gyoztes
     */
    public void kudosToWinner() {
        controller.kudosToWinner();
    }
}
