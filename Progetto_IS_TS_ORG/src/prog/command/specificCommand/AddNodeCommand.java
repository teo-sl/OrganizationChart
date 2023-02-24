package prog.command.specificCommand;

import prog.command.Command;
import prog.model.unit.Unit;
import prog.view.TreeView;

public class AddNodeCommand implements Command {
    private final TreeView treeView;

    public AddNodeCommand(TreeView treeView) {
        this.treeView = treeView;
    }

    @Override
    public boolean doIt() {
        if(treeView==null) return false;
        Unit u = new Unit("*",-1,null,null,-1,false);
        treeView.addNode(u);
        return true;
    }
}
