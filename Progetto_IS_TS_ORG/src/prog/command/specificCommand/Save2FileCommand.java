package prog.command.specificCommand;

import prog.command.Command;
import fileManager.ConverterMediator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Save2FileCommand implements Command {
    private final ConverterMediator mediator;
    public Save2FileCommand(ConverterMediator mediator) {
        this.mediator=mediator;
    }

    @Override
    public boolean doIt() {
        JFrame frame=new JFrame("Insert file name");

        JFileChooser fileChooser=new JFileChooser();


        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel=new JPanel(new BorderLayout());
        JTextField nameField=new JTextField();
        JButton okButton=new JButton("Submit"),cancelButton=new JButton("Cancel");
        JPanel buttonPanel=new JPanel(); buttonPanel.add(okButton); buttonPanel.add(cancelButton);
        panel.add(nameField,BorderLayout.CENTER);
        panel.add(buttonPanel,BorderLayout.SOUTH);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        cancelButton.addActionListener((e)->frame.dispose());
        okButton.addActionListener((e)->{
            String fileName=nameField.getText().trim();
            String regex=".*\\.json$";
            if(fileName.equals("") || fileName.contains(" ") ) return;
            if(!fileName.matches(regex))
                if(fileName.charAt(fileName.length()-1)=='.') fileName=fileName+"json";
                else fileName=fileName+".json";

            int result=fileChooser.showOpenDialog(panel);
            if(result==JFileChooser.APPROVE_OPTION) {
                File directory=fileChooser.getSelectedFile();
                boolean res = mediator.save2File(directory.getAbsolutePath()+"\\"+fileName);
                if(res) JOptionPane.showMessageDialog(null,"Saving completed","Completed",JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(null,"Saving error","Error",JOptionPane.ERROR_MESSAGE);
                frame.dispose();

            }

        });
        return true;

    }

}
