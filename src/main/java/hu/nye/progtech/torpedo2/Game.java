package hu.nye.progtech.torpedo2;

import hu.nye.progtech.torpedo2.service.Controller;
import hu.nye.progtech.torpedo2.service.ControllerFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Game {
    public static void main(String[] args) {
            //ControllerFacade controller = new ControllerFacade(new Controller());
            ApplicationContext context = new AnnotationConfigApplicationContext("hu.nye.progtech.torpedo2");
            ControllerFacade controller = context.getBean(ControllerFacade.class);
            controller.database();
            //Controller muxik
            controller.setupGame();
            controller.play();
            controller.kudosToWinner();
    }
}
