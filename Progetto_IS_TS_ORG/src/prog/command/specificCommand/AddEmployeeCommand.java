package prog.command.specificCommand;

import prog.command.Command;
import prog.communicator.Communicator;
import prog.model.employee.Employee;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeCommand implements Command {
    private final Communicator c;

    public AddEmployeeCommand(Communicator c) {
        this.c =c;
    }


    public boolean doIt() {
        int N=10;
        JFrame frame=new JFrame("Add employee"); frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel nameLabel=new JLabel("Name:"),surnameLabel=new JLabel("Surname:"),cfLabel=new JLabel("sn:"),emailLabel=new JLabel("email:");

        JTextField nameField=new JTextField(N),surnameField=new JTextField(N),cfField=new JTextField(N),emailField=new JTextField(N);

        JPanel panel=new JPanel(new BorderLayout());
        JPanel gridPanel=new JPanel(new GridLayout(4,2));

        gridPanel.add(nameLabel);gridPanel.add(nameField);
        gridPanel.add(surnameLabel); gridPanel.add(surnameField);
        gridPanel.add(cfLabel); gridPanel.add(cfField);
        gridPanel.add(emailLabel); gridPanel.add(emailField);

        JPanel buttonPanel=new JPanel();
        JButton submitButton=new JButton("Submit");
        JButton cancelButton=new JButton("Cancel");

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        panel.add(gridPanel,BorderLayout.CENTER);
        panel.add(buttonPanel,BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        cancelButton.addActionListener((e)->frame.dispose());

        submitButton.addActionListener((e)->{
            String name=nameField.getText().trim(),surname=surnameField.getText().trim(),email=emailField.getText().trim();
            if(name.equals("") || surname.equals("") || email.equals("")) {
                showError(frame); return; }
            int sn;
            try {
                 sn= Integer.parseInt(cfField.getText().trim());
            }catch (NumberFormatException nfe) {
                showError(frame); return;}
            if(sn<0) return;
            Employee employee=new Employee(sn,name,surname,email);
            c.addEmployee(employee);
            frame.dispose();
        });


        return true;
    }


    public void showError(JFrame mainFrame) {
        JOptionPane.showMessageDialog(mainFrame,"Bad format error","Error",JOptionPane.ERROR_MESSAGE);
    }

}
