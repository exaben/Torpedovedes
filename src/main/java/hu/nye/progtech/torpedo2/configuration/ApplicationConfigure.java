package hu.nye.progtech.torpedo2.configuration;

import hu.nye.progtech.torpedo2.service.Controller;
import hu.nye.progtech.torpedo2.service.ControllerFacade;
import org.springframework.context.annotation.Bean;

public class ApplicationConfigure {
  @Bean
  ControllerFacade controllerFacade(Controller controller)
  {
    return new ControllerFacade(new Controller());
  }
}
