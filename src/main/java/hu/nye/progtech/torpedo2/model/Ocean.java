package hu.nye.progtech.torpedo2.model;

import hu.nye.progtech.torpedo2.ui.Ui;

public class Ocean implements Field{
    /**
     * Mezok
     */
    private FieldState fieldState = new IntactFIeldState();

    @Override
    public String hit() {
        if (!fieldState.isBombed()) {
            setFieldState(new NukedFieldState());
            return Ui.ANSI_CYAN + "Melle" + Ui.ANSI_RESET;
        }
        return Ui.ANSI_CYAN + "Talald el ujra! :D" + Ui.ANSI_RESET;
    }

    @Override
    public void setFieldState(FieldState fieldState) {

        this.fieldState = fieldState;
    }

    @Override
    public FieldState getFieldState() {

        return fieldState;
    }
}
