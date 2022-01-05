package hu.nye.progtech.torpedo2.service;

import java.util.ArrayList;
import java.util.List;
import hu.nye.progtech.torpedo2.model.*;
import hu.nye.progtech.torpedo2.ui.Ui;

public class ShipComponent implements Field {
  /**
   * Mezok
   */
  private List<Observer> observers = new ArrayList<>();
  private FieldState fieldState = new IntactFIeldState();

  @Override
  public String hit() {
    notifyAllObservers();
    if (!fieldState.isBombed()) {
      setFieldState(new NukedFieldState());
      return Ui.ANSI_YELLOW + "%s BOOM %n" + Ui.ANSI_RESET;
    }
    return Ui.ANSI_YELLOW + "%s BOOM X2 %n" + Ui.ANSI_RESET;
  }

  @Override
  public void setFieldState(FieldState fieldState) {
    this.fieldState = fieldState;
  }

  @Override
  public FieldState getFieldState() {
    return fieldState;
  }

  /**
   * Kapcsolat megfigyelovel
   *
   * @param observer
   */
  public void attach(Observer observer) {
    observers.add(observer);
  }

  /**
   * Ertesites minden figyelonek
   */
  public void notifyAllObservers() {
    for (Observer observer : observers) {
      observer.update(fieldState);
    }
  }
}
