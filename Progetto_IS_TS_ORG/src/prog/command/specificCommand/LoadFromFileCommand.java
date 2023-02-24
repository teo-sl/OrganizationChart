package prog.command.specificCommand;

import prog.command.Command;
import fileManager.ConverterMediator;
import prog.view.TreeView;


import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LoadFromFileCommand implements Command {
    private final ConverterMediator mediator;
    private final TreeView treeView;

    public LoadFromFileCommand(ConverterMediator mediator,TreeView treeView) {
        this.mediator = mediator;
        this.treeView=treeView;
    }

    @Override
    public boolean doIt() {
        JFrame frame = new JFrame("Select a json file");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea log = new JTextArea(5, 20);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(panel);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            boolean res= mediator.loadFromFile(selectedFile.getAbsolutePath());
            if(res) JOptionPane.showMessageDialog(null,"Loading completed","Completed",JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(null,"Loading error","Error",JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            treeView.fileAdded();
            return true;
        }
        else return false;
    }
}
