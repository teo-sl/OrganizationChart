package prog.controller;
import prog.command.CommandHandler;
import prog.command.specificCommand.*;
import prog.communicator.Communicator;


import javax.swing.*;
import java.awt.*;

public class Controller extends JPanel {
    private final CommandHandler cmdH;
    private final JTextField unitName=new JTextField();
    private Communicator communicator;

    public void setCommunicator(Communicator communicator) { this.communicator =communicator;}
    public Controller(CommandHandler cmdH) {
        this.cmdH=cmdH;

        JButton addRoleToUser=new JButton("Add role to user");
        JButton removeRoleFromUser=new JButton("Remove role from user");
        JButton setUnitTitle=new JButton("Set title to unit");
        JButton addEmployee = new JButton("Add Employee");
        JButton addRole2Unit=new JButton("Add role to unit");
        JButton removeRoleFromUnit=new JButton("Remove role from unit");
        JPanel buttonPane=new JPanel();

        addRoleToUser.addActionListener((e)-> cmdH.handle(new AddRoleToUserCommand(communicator.getSelectedRole(), communicator.getSelectedUnit(), communicator.getSelectedEmployee())));
        removeRoleFromUser.addActionListener((e)-> cmdH.handle(new RemoveRoleFromUserCommand(communicator.getSelectedRole(), communicator.getSelectedUnit(), communicator.getSelectedEmployee())));
        setUnitTitle.addActionListener((e)-> { cmdH.handle(new SetUnitNameCommand(communicator.getSelectedUnit(),unitName.getText().trim())); unitName.setText(""); });
        addEmployee.addActionListener((e)->cmdH.handle(new AddEmployeeCommand(communicator)));
        addRole2Unit.addActionListener((e)->cmdH.handle(new AddRoleToUnitCommand(communicator.getSelectedUnit(), communicator.getSelectedRole())));
        removeRoleFromUnit.addActionListener((e)->cmdH.handle(new RemoveRoleFromUnitCommand(communicator.getSelectedUnit(), communicator.getSelectedRole())));


        JPanel inferiorPanel=new JPanel(new GridLayout(1,2));
        inferiorPanel.add(setUnitTitle);
        inferiorPanel.add(unitName);

        buttonPane.add(addRoleToUser); buttonPane.add(removeRoleFromUser); buttonPane.add(addEmployee);
        buttonPane.add(addRole2Unit); buttonPane.add(removeRoleFromUnit);
        this.setLayout(new BorderLayout());
        add(buttonPane,BorderLayout.CENTER);
        add(inferiorPanel,BorderLayout.SOUTH);

    }


}