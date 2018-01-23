import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

public class ToDoList extends JFrame {
  private JFrame frame;
  private JTable table;
  private DefaultTableModel model;
  private JSplitPane splitPane;
  private JScrollPane scrollPane;
  private JPanel panel;
  private JButton add;
  private JButton edit;
  private JButton del;
  private JButton help;

  //ToDoList class
  public ToDoList() {
    JFrame frame = new JFrame("To Do List");
    JTable table = new JTable();
    Object[] columns = {"Title", "Description", "Date", "Priority"};
    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(columns);
    table.setModel(model);
    table.setAutoCreateRowSorter(true); //Järjestää listan valitun sarakkeen perusteella nousevasti tai laskevasti.

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    //Lisää -nappi johon on liitetty kuuntelija, joka kutsuu lisää -funktiota kun nappia painetaan.
    JButton add = new JButton("Add");
    add.setAlignmentX(Component.CENTER_ALIGNMENT);
    add.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addTask(frame, model);
      }
    });

    //Muokkaa -nappi, johon on liitetty kuuntelija, joka tarkistaa onko listasta valittu tehtävää ja kutsuu muokkaa -funktiota jos on.
    JButton edit = new JButton("Edit");
    edit.setAlignmentX(Component.CENTER_ALIGNMENT);
    edit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int i = table.getSelectedRow();
        if(i >= 0) {
          editTask(frame, model, i);
        } else {
          JOptionPane.showMessageDialog(panel, "You haven't chosen an entry to modify!", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    //Poista -nappi, johon on liitetty kuuntelija, joka tarkistaa onko listasta valittu tehtävää. Jos on niin se avaa varmistaa, että käyttäjä haluaa poistaa
    //tehtävän ja poistaa sen listasta.
    JButton del = new JButton("Delete");
    del.setAlignmentX(Component.CENTER_ALIGNMENT);
    del.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int i = table.getSelectedRow();
        if(i >= 0) {
          int delConfirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete this entry?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
          if(delConfirm == 0)
          {
            model.removeRow(i);
          }
        } else {
          JOptionPane.showMessageDialog(panel, "You haven't chosen an entry to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
       }
    });

    //Ohje -nappi, johon on liitetty kuuntelija, joka avaa käyttöohjeen.
    JButton help = new JButton("Help");
    help.setAlignmentX(Component.CENTER_ALIGNMENT);
    help.addActionListener(new ActionListener() {
    @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(panel, "-Add new a new entry with 'Add' -button\n-Edit an existing entry by clicking on it in the list and pressing the 'Edit' -button\n-Delete an entry from the list by clicking on it and pressing the 'Delete' -button\n-Reorganize the list by column by clicking on it in the list", "Help", JOptionPane.PLAIN_MESSAGE);
      }
    });

    //Napit lisätään oikella olevaan paneeliin ja ne järjestetään pystysuorassa keskelle paneelia ja erossa toisistaan.
    panel.add(Box.createVerticalGlue());
    panel.add(add);
    panel.add(Box.createVerticalGlue());
    panel.add(edit);
    panel.add(Box.createVerticalGlue());
    panel.add(del);
    panel.add(Box.createVerticalGlue());
    panel.add(help);
    panel.add(Box.createVerticalGlue());

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBounds(0, 0, 880, 200);

    //Pääikkuna jaetaan splitpaneeliin, jossa vasemmalla scrollpaneeliin on liitetty taulukko ja oikealle paneeliin on liitetty napit.
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, panel);

    frame.add(splitPane);
    frame.setSize(600,400);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

//Alustaa tehtävälistan
  public static void main(String[] args) {
    ToDoList todolist = new ToDoList();
  }

//Funktio, jonka avulla voidaan lisätä uusi tehtävä listaan. Funktio avaa uuden ikkunan. Tekstikenttien vieressä ovat niitä kuvaavat nimikkeet.
//Lisäyksen voi perua ja se heittää virhedialogin jos otsikko on jätetty tyhjäksi lisäystä yrittäessä.
  private void addTask(JFrame frame, DefaultTableModel model) {
	  JPanel addPanel = new JPanel(new BorderLayout(5,5));
	  JPanel addLabel = new JPanel(new GridLayout(0,1,2,2));
    addLabel.add(new JLabel("Title*", SwingConstants.RIGHT));
    addLabel.add(new JLabel("Description", SwingConstants.RIGHT));
	  addLabel.add(new JLabel("Date", SwingConstants.RIGHT));
	  addLabel.add(new JLabel("Priority", SwingConstants.RIGHT));
    addPanel.add(addLabel, BorderLayout.WEST);

    JPanel addControls = new JPanel(new GridLayout(0,1,2,2));
    JTextField title = new JTextField();
    addControls.add(title);
    JTextField description = new JTextField();
    addControls.add(description);
	  JTextField date = new JTextField();
	  addControls.add(date);
	  JTextField priority = new JTextField();
	  addControls.add(priority);
    addPanel.add(addControls, BorderLayout.CENTER);

    int addConfirm = JOptionPane.showConfirmDialog(frame, addPanel, "Add", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if(addConfirm == JOptionPane.OK_OPTION) {
      if(!title.getText().isEmpty()) {
        Object[] row = new Object[4];
    	  row[0] = title.getText();
        row[1] = description.getText();
        row[2] = date.getText();
        row[3] = priority.getText();
        model.addRow(row);
      } else {
        JOptionPane.showMessageDialog(panel, "Title is empty!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

//Funktio, jota kutsumalla voidaan muokata hiirellä valittua tehtävää. Funktio avaa uuden ikkunan. Tekstikenttien vieressä ovat niitä kuvaavat nimikkeet.
//Valitun tehtävän tiedot lisätään automaattisesti tyhjiin tekstikenttiin. Muokkauksen voi perua ja se heittää virhedialogin jos otsikko on jätetty tyhjäksi.
  private void editTask(JFrame frame, DefaultTableModel model, int i) {
    JPanel editPanel = new JPanel(new BorderLayout(5,5));
    JPanel editLabel = new JPanel(new GridLayout(0,1,2,2));
    editLabel.add(new JLabel("Title*", SwingConstants.RIGHT));
    editLabel.add(new JLabel("Description", SwingConstants.RIGHT));
    editLabel.add(new JLabel("Date", SwingConstants.RIGHT));
    editLabel.add(new JLabel("Priority", SwingConstants.RIGHT));
    editPanel.add(editLabel, BorderLayout.WEST);

    JPanel editControls = new JPanel(new GridLayout(0,1,2,2));
    JTextField title = new JTextField();
    title.setText(model.getValueAt(i, 0).toString());
    editControls.add(title);
    JTextField description = new JTextField();
    description.setText(model.getValueAt(i, 1).toString());
    editControls.add(description);
    JTextField date = new JTextField();
    date.setText(model.getValueAt(i, 2).toString());
    editControls.add(date);
    JTextField priority = new JTextField();
    priority.setText(model.getValueAt(i, 3).toString());
    editControls.add(priority);
    editPanel.add(editControls, BorderLayout.CENTER);

    int editConfirm = JOptionPane.showConfirmDialog(frame, editPanel, "Edit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if(editConfirm == JOptionPane.OK_OPTION) {
      if(!title.getText().isEmpty()) {
        model.setValueAt(title.getText(), i, 0);
        model.setValueAt(description.getText(), i, 1);
        model.setValueAt(date.getText(), i, 2);
        model.setValueAt(priority.getText(), i, 3);
      } else {
        JOptionPane.showMessageDialog(editPanel, "Title is empty!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
