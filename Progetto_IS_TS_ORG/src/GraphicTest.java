import prog.command.CommandHandler;
import prog.command.CommandHandlerImpl;
import prog.command.specificCommand.*;
import prog.controller.Controller;
import prog.communicator.Communicator;
import prog.view.TreeView;
import fileManager.ConverterMediator;

import javax.swing.*;
import java.awt.*;

public class GraphicTest {

    public static void main(String[] args) {
        JFrame f = new JFrame("Organizational charts' manager");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TreeView treeView=new TreeView();
        CommandHandler cmdH=new CommandHandlerImpl();
        Controller controller=new Controller(cmdH);
        Communicator c=new Communicator(treeView);
        ConverterMediator converterMediator =new ConverterMediator();

        controller.setCommunicator(c);

        treeView.setPreferredSize(new Dimension(400, 400));

        JToolBar toolbar = new JToolBar();

        JMenuBar menuBar=new JMenuBar();
        f.setJMenuBar(menuBar);

        JMenu menu=new JMenu("Import/Export");
        JMenuItem load,save,save2File,loadFromFile,loadEmpAndRole,saveEmpAndRole;

        load=new JMenuItem("Load company from db");
        save=new JMenuItem("Save company to db");
        save2File=new JMenuItem("Save db content to file");
        loadFromFile=new JMenuItem("Load db content from file");
        loadEmpAndRole=new JMenuItem("Load employees and roles from db");
        saveEmpAndRole=new JMenuItem("Save employees and roles to db");

        menu.add(load);menu.add(save);menu.add(save2File);menu.add(loadFromFile);
        menu.add(loadEmpAndRole); menu.add(saveEmpAndRole);
        menuBar.add(menu);


        JButton newCompany=new JButton("New company");
        newCompany.addActionListener((e)->cmdH.handle(new NewCompanyCommand(treeView)));
        toolbar.add(newCompany);

        JButton addNode=new JButton("Add child");
        addNode.addActionListener((e)->cmdH.handle(new AddNodeCommand(treeView)));
        toolbar.add(addNode);

        JButton removeNode=new JButton("Remove node");
        removeNode.addActionListener((e)-> treeView.removeNode());
        toolbar.add(removeNode);


        JButton addRole=new JButton("Add role");
        JTextField roleText=new JTextField();

        addRole.addActionListener((e)-> {
            cmdH.handle(new AddRoleCommand(treeView, roleText.getText().trim()));
            roleText.setText("");
        });

        save2File.addActionListener((e)-> cmdH.handle(new Save2FileCommand(converterMediator)));
        loadFromFile.addActionListener((e)-> cmdH.handle(new LoadFromFileCommand(converterMediator,treeView)));

        toolbar.add(addRole);toolbar.add(roleText);


        load.addActionListener((e)-> treeView.loadFromDb());
        save.addActionListener((e)-> treeView.save2Db());

        loadEmpAndRole.addActionListener((e)->treeView.loadEmpAndRole());
        saveEmpAndRole.addActionListener((e)->treeView.saveEmpAndRole());
        f.add(toolbar,BorderLayout.NORTH);
        f.add(treeView,BorderLayout.CENTER);
        f.add(controller,BorderLayout.SOUTH);
        f.pack();
        f.setVisible(true);
    }
}
