package prog.command.specificCommand;

import prog.command.Command;
import prog.view.TreeView;

import javax.swing.*;
import java.awt.*;

public class NewCompanyCommand implements Command {
    private final TreeView treeView;

    public NewCompanyCommand(TreeView treeView) {
        this.treeView=treeView;
    }

    public boolean doIt() {
        JFrame frame=new JFrame("New Company");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(treeView);
        JPanel mainPanel=new JPanel(new BorderLayout());
        JTextField textField=new JTextField();
        JButton okButton=new JButton("Submit");
        mainPanel.add(textField,BorderLayout.PAGE_START);
        mainPanel.add(okButton,BorderLayout.CENTER);

        okButton.addActionListener((e)->{
            if(!treeView.newCompany(textField.getText().trim())) JOptionPane.showMessageDialog(frame,
                    "Company name already in use in the db",
                    "Company error",
                    JOptionPane.ERROR_MESSAGE);
            else frame.dispose();
        });

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        return true;
    }

}
