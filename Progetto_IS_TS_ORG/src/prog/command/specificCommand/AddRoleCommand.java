package prog.command.specificCommand;

import prog.command.Command;
import prog.model.role.Role;
import prog.view.TreeView;

public class AddRoleCommand implements Command {
    private final TreeView treeView;
    private final String roleName;
    public AddRoleCommand(TreeView treeView,String roleName) {
        this.treeView=treeView;
        this.roleName=roleName;
    }

    @Override
    public boolean doIt() {
        if(treeView==null || roleName==null || roleName.equals("")) return false;
        treeView.addRole(new Role(roleName));
        return true;
    }
}
