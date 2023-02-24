package prog.command.specificCommand;

import prog.command.Command;
import prog.model.unit.Unit;

public class SetUnitNameCommand implements Command {

    private final Unit selectedUnit;
    private final String name;

    public SetUnitNameCommand(Unit selectedUnit,String name) {
        this.selectedUnit = selectedUnit;
        this.name=name;
    }
    @Override
    public boolean doIt() {
        if(selectedUnit==null || name==null || name.equals("")) return false;
        selectedUnit.setName(name);
        return true;
    }
}
