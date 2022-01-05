package hu.nye.progtech.torpedo2.service.ships;

import hu.nye.progtech.torpedo2.model.FieldState;
import hu.nye.progtech.torpedo2.service.Ship;
import hu.nye.progtech.torpedo2.ui.Ui;

public class BattleShip extends Ship {

    private final String NAME = "Csatahajo";
    private final int LENGTH = 4;
    private int health = LENGTH;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void update(FieldState fieldState) {
        if (!fieldState.isBombed() && health > 0) {
            health--;
            Ui.printf(Ui.ANSI_RED + "%s BOOM %n" + Ui.ANSI_RESET, NAME);
        }
    }
}
