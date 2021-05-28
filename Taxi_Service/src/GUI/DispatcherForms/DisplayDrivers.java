package GUI.DispatcherForms;

import AllUsers.Driver;
import Main.TaxiService;
import Main.TaxiServiceMain;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayDrivers extends JFrame {

    private JToolBar mainToolbar = new JToolBar();
    private JButton btnAdd = new JButton();
    private JButton btnEdit = new JButton();
    private JButton btnDelete = new JButton();

    private DefaultTableModel tableModel;
    private JTable DriversDisplay;

    private TaxiService taxiService;

    public DisplayDrivers(TaxiService taxiService) {
        this.taxiService = taxiService;
        setTitle("Drivers");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initGUI();
        initActions();
    }

    private void initGUI() {
        ImageIcon addIcon = new ImageIcon(getClass().getResource("/images/add.gif"));
        btnAdd.setIcon(addIcon);
        ImageIcon editIcon = new ImageIcon(getClass().getResource("/images/edit.gif"));
        btnEdit.setIcon(editIcon);
        ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/images/remove.gif"));
        btnDelete.setIcon(deleteIcon);

        mainToolbar.add(btnAdd);
        mainToolbar.add(btnEdit);
        mainToolbar.add(btnDelete);
        add(mainToolbar, BorderLayout.NORTH);

        String[] headings = new String[] {"", "Prezime", "Pol", "Korisnicko ime", "Sifra"};
        Object[][] content = new Object[taxiService.allNotDeletedDrivers().size()][headings.length];

        for(int i=0; i<taxiService.allNotDeletedDrivers().size(); i++) {
            Driver driver = taxiService.allNotDeletedDrivers().get(i);
            content[i][0] = driver.getUsername();
            content[i][1] = driver.getPassword();
            content[i][2] = driver.getName();
            content[i][3] = driver.getLastName();
            content[i][4] = driver.getJmbg();
            content[i][5] = driver.getAddress();
            content[i][6] = driver.getPhoneNumber();
            content[i][7] = driver.getGender();
            content[i][8] = driver.getId();
            content[i][9] = driver.getRoles();
            content[i][10] = driver.getDriverPay();
            content[i][11] = driver.getMembershipCard();

        }

        tableModel = new DefaultTableModel(content, headings);
        DriversDisplay = new JTable(tableModel);

        DriversDisplay.setRowSelectionAllowed(true);
        DriversDisplay.setColumnSelectionAllowed(false);
        DriversDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DriversDisplay.setDefaultEditor(Object.class, null);
        DriversDisplay.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(DriversDisplay);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initActions() {
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = DriversDisplay.getSelectedRow();
                if(row == -1) {
                    JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
                }else {
                    String username = tableModel.getValueAt(row, 3).toString();
                    Driver driver = taxiService.findDriver(username);

                    int choice = JOptionPane.showConfirmDialog(null,
                            "Da li ste sigurni da zelite da obrisete prodavca?",
                            username + " - Portvrda brisanja", JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        driver.setDeleted(true);
                        tableModel.removeRow(row);
                        taxiService.saveDrivers(TaxiServiceMain.Drivers_File);
                    }
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProdavciForma pf = new ProdavciForma(prodavnica, null);
                pf.setVisible(true);
            }
        });

        btnEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int red = prodavciTabela.getSelectedRow();
                if(red == -1) {
                    JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
                }else {
                    String korisnickoIme = tableModel.getValueAt(red, 3).toString();
                    Prodavac prodavac = prodavnica.nadjiProdavca(korisnickoIme);
                    if(prodavac == null) {
                        JOptionPane.showMessageDialog(null, "Greska prilikom pronalazenja prodavca sa tim korisnickim imenom", "Greska", JOptionPane.WARNING_MESSAGE);
                    }else {
                        ProdavciForma pf = new ProdavciForma(prodavnica, prodavac);
                        pf.setVisible(true);
                    }
                }
            }
        });
    }


}
