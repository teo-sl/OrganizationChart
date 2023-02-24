package prog.view;

import mapper.Mapper;
import prog.subject.Subject;
import prog.model.company.Company;
import prog.model.employee.Employee;
import prog.model.role.Role;
import prog.model.unit.Unit;
import prog.observer.Observer;
import prog.updateType.UpdateType;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TreeView extends JPanel implements TreeSelectionListener, Observer {
    private final DefaultTreeModel model;
    private final DefaultMutableTreeNode root;
    private final JTree tree;
    private final JTextArea text;
    private Map<DefaultMutableTreeNode,Unit> db;
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Company company;
    private final Set<Role> roles=new HashSet<>();
    private final Set<Employee> employees=new HashSet<>();
    private Role selectedRole;
    private Employee selectedEmployee;
    private final Mapper mapper=new Mapper();
    private String selectedCompany=null;

    private final JComboBox<String> companiesCombo;

    private final JComboBox<Role> roleCombo;
    private final JComboBox<Employee> employeeCombo;

    public TreeView() {
        Unit.setN(mapper.getMaxUnitCode()+1);
        company=new Company("WorkBench");
        db=new HashMap<>();

        Unit u=new Unit("Organizational chart's root",-1,company,null,-1,false);
        root = new DefaultMutableTreeNode(u.getName());
        db.put(root,u);
        u.registerObserver(this);
        company.addUnit(u);

        model= new DefaultTreeModel(root);
        tree=new JTree(model);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(this);
        JScrollPane treeview = new JScrollPane(tree);
        text=new JTextArea();
        text.setEditable(false);
        JScrollPane textAreaView=new JScrollPane(text);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeview);
        splitPane.setBottomComponent(textAreaView);
        splitPane.setResizeWeight(0.5);
        text.setText("Organizational charts' manager");

        this.setLayout(new BorderLayout());
        this.add(splitPane,BorderLayout.CENTER);

        JLabel roleLabel=new JLabel("Roles: "),employeeLable=new JLabel("Employeees: ");

        JPanel infPanel=new JPanel();

        roleCombo=new JComboBox<>();
        roleCombo.addActionListener((e)-> selectedRole=(Role) roleCombo.getSelectedItem());

        infPanel.add(roleLabel); infPanel.add(roleCombo);

        employeeCombo=new JComboBox<>();

        employeeCombo.addActionListener((e)-> selectedEmployee=(Employee)employeeCombo.getSelectedItem());

        infPanel.add(employeeLable); infPanel.add(employeeCombo);


        companiesCombo=new JComboBox<>();
        companiesCombo.addItem("");
        List<String> tmp=mapper.getCompanyString();
        for(String s : tmp)
            companiesCombo.addItem(s);
        companiesCombo.addActionListener((e)-> selectedCompany=(String)companiesCombo.getSelectedItem());
        infPanel.add(new JLabel("Companies"));infPanel.add(companiesCombo);

        this.add(infPanel,BorderLayout.SOUTH);
    }

    public void valueChanged(TreeSelectionEvent e) {
        if(e.getPath()==null) { text.setText("Organizational charts' manager"); return;}
        DefaultMutableTreeNode node=(DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        if(db.get(node)==null) { text.setText("Organizational charts' manager"); return;}
        text.setText(db.get(node).toString());
    }

    public void update(Subject s, UpdateType type) {
        if(tree.getSelectionPath()==null) return;
        DefaultMutableTreeNode current=(DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if(type==UpdateType.ROLE) {
            text.setText(db.get(current).toString());
        }else if(type==UpdateType.UNIT_NAME) {
            current.setUserObject(db.get(current).getName());
            text.setText(db.get(current).toString());
            model.nodeChanged(current);
        }
    }

    public Role getSelectedRole() { return selectedRole; }
    public Employee getSelectedEmployee() { return selectedEmployee; }

    public Unit getSelectedUnit() {
        if(tree.getSelectionPath()==null) return null;
        DefaultMutableTreeNode node= (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if(db.get(node)==null) return null;
        return db.get(node);
    }

    public void addNode(Unit u) {
        if(tree.getSelectionPath()==null) {toolkit.beep(); return; }
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        DefaultMutableTreeNode child=new DefaultMutableTreeNode(u.getName());

        u.setParentUnit(db.get(parent));
        u.setCompany(company);
        u.registerObserver(this);
        company.addUnit(u);
        db.put(child,u);

        model.insertNodeInto(child,parent,parent.getChildCount());
    }

    public void addRole(Role r) {
        if(roles.contains(r)) {JOptionPane.showMessageDialog(this, "Role already added", "Role already added", JOptionPane.ERROR_MESSAGE);return; }
        roles.add(r);
        roleCombo.addItem(r);
    }

    public void save2Db() {
        String companyName=company.getName();
        if(companyName.equals("WorkBench")) { JOptionPane.showMessageDialog(this,"Cannot save company called WorkBench","Error",JOptionPane.ERROR_MESSAGE); return; }
        if(mapper.saveToDb(company,roles,employees)) companiesCombo.addItem(companyName);
        JOptionPane.showMessageDialog(this,"Saving completed","Complete",JOptionPane.INFORMATION_MESSAGE);
    }


    public void loadFromDb() {
        if(selectedCompany==null||selectedCompany.equals("")) return;
        if(!mapper.loadFromDb(selectedCompany,company,roles,employees)) return;
        updateRAndE();
        db.clear();
        Map<Unit,DefaultMutableTreeNode> dbinv=new HashMap<>();
        List<Unit> tmp=company.getUnits();
        root.removeAllChildren();
        for(Unit u : tmp) {
            u.registerObserver(this);
            DefaultMutableTreeNode def;
            u.setCompany(company);
            if(u.getParentUnit()==null) def=root;
            else def=new DefaultMutableTreeNode();
            def.setUserObject(u.getName());
            db.put(def, u);
            dbinv.put(u,def);
        }
        for(Unit u : tmp) {
            if(u.getParentUnit()==null) continue;
            DefaultMutableTreeNode newChild=dbinv.get(u);
            DefaultMutableTreeNode parent=dbinv.get(u.getParentUnit());
            model.insertNodeInto(newChild,parent,parent.getChildCount());
        }
        text.setText("Organizational charts' manager");
        model.reload();
        JOptionPane.showMessageDialog(this,"Loading completed","Done!",JOptionPane.INFORMATION_MESSAGE);
    }
    private void updateRAndE() {
        roleCombo.removeAllItems(); employeeCombo.removeAllItems();
        for(Role r : roles)  roleCombo.addItem(r);
        for(Employee e : employees)  employeeCombo.addItem(e);

    }

    public void removeNode() {
        if(tree.getSelectionPath()==null) return;
        DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if(node==root) return;
        List<DefaultMutableTreeNode> nodes=new ArrayList<>(); nodes.add(node);
        DefaultMutableTreeNode tmp;
        while (!nodes.isEmpty()) {
            tmp=nodes.remove(0);
            company.removeUnit(db.get(tmp));
            db.remove(tmp);
            for(int i=0;i<tmp.getChildCount();++i)
                nodes.add((DefaultMutableTreeNode) tmp.getChildAt(i));
        }
        model.removeNodeFromParent(node);
    }

    public void addEmployee(Employee employee) {
        if(employees.contains(employee))
            JOptionPane.showMessageDialog(this,"Employee already added","Error",JOptionPane.ERROR_MESSAGE);
        else {
            employeeCombo.addItem(employee);
            employees.add(employee);
        }
    }

    public void saveEmpAndRole() {
        mapper.saveEmpAndRole2Db(roles,employees);
    }
    public void loadEmpAndRole() {
        int result = JOptionPane.showConfirmDialog (this, "If you load employees and role, the current organizational charts will be deleted.\n Do you want to continue?","WARNING", JOptionPane.YES_NO_OPTION);
        if(result==JOptionPane.NO_OPTION) return;
        mapper.loadEmpAndRoleFromDb(roles,employees);
        updateRAndE();
        newCompany();
        JOptionPane.showMessageDialog(this,"The loading of employees and roles is finished","Done!",JOptionPane.INFORMATION_MESSAGE);
    }
    private void loadCompanies() {
        List<String> companies=mapper.getCompanyString();
        companiesCombo.removeAllItems();
        companiesCombo.addItem("");
        for(String s : companies)
            companiesCombo.addItem(s);
    }

    public void fileAdded() {
        mapper.loadEmpAndRoleFromDb(roles,employees);
        updateRAndE();
        loadCompanies();
        Unit.setN(mapper.getMaxUnitCode()+1);
        newCompany();
    }
    public boolean newCompany() {
        return newCompany("WorkBench");
    }
    public boolean newCompany(String companyName) {
        if(mapper.getCompanyString().contains(companyName)) return false;
        company=new Company(companyName);
        if(db==null) db=new HashMap<>();
        else db.clear();
        Unit rootUnit=new Unit("Organizational charts' root",-1,company,null,-1,false);
        rootUnit.registerObserver(this);
        company.addUnit(rootUnit);
        root.removeAllChildren();
        root.setUserObject(rootUnit.getName());
        db.put(root,rootUnit);
        model.reload();
        return true;
    }



}