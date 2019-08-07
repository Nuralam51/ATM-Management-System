package com.example.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.example.Menu.AddCustomer;
import com.example.Menu.EditCustomer;
import com.example.Menu.PassowordReset;

public class AdminPanel extends JPanel{

	JPanel panelEast, panelNorth, panelCenter, panelCenterCenter, panelCenterNorth, panelAdmin;
	JPanel panelNorthWest, panelNorthCenter;
	JPanel panelEastNorth, panelEastCenter;
	JPanel panelEastNorthNorth, panelEastNorthCenter;

	SuggestText cmbSearch = new SuggestText();

	JLabel lblAdmin, lblAdminInfo, lbl, lblAdminId, lblAdminName;
	JTextField txtAdminName, txtAdminId;

	JButton btnAdd = new JButton("Add Customer");
	JButton btnEdit = new JButton("Edit Customer");
	JButton btnLogOut = new JButton("Log Out");
	JButton btnDelete = new JButton("Delete Customer");
	JButton btnPasswordReset = new JButton("Password Reset");
	JButton btnRefresh = new JButton("Refresh");
	JButton btnSearch = new JButton(new ImageIcon("image/search.png"));

	String col[]= {"Id","Account No", "Name", "Address","Email", "Phone", "Balance"};
	Object row[][]= {};
	DefaultTableModel modelAdmin=new DefaultTableModel(row,col);
	JTable tableAdmin = new JTable(modelAdmin);
	JScrollPane scrollAdmin=new JScrollPane(tableAdmin, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JFileChooser fileChooser;
	File imageFile=null;
	JFrame frame;
	SessionBean sessionBean;

	public AdminPanel(JFrame frm, SessionBean bean) {
		this.frame=frm;
		this.sessionBean=bean;

		cmp();
		btnAction();
		btnBackgroundwork();
		tableDataLoadNewCustomer();
		cmbDataLoad();
		SessionBeanAdminName();
	}
	private void btnAction() {
		btnAdd.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				AddCustomer addCustomer = new AddCustomer(sessionBean);
				addCustomer.setVisible(true);
			}
		});
		btnEdit.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				
				/*int row1=tableAdmin.getSelectedRow();
				if(row1==0) {
					int row=tableAdmin.getSelectedRow();
					int column=0;
					String id=tableAdmin.getValueAt(row, column).toString();
					findDataLoadEditCustomer(id);
				}
				else {
					JOptionPane.showMessageDialog(null, "Select Customer Data from table for Edit!");

				}*/
				
				int row=tableAdmin.getSelectedRow();
				int column=0;
				String id=tableAdmin.getValueAt(row, column).toString();
				findDataLoadEditCustomer(id);
			}
		});
		btnDelete.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				int row=tableAdmin.getSelectedRow();
				if(row==0) {
					if(checkConfirmation("Sure to Delete?")) {
						if(deteteCustomer()) {
							tableDataLoadNewCustomer();
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Select Customer Data from table for delete!");

				}
			}
		});
		btnPasswordReset.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				/*PassowordReset passwordreset = new PassowordReset();
				passwordreset.setVisible(true);*/
				int row=tableAdmin.getSelectedRow();
				int column=0;
				String id=tableAdmin.getValueAt(row, column).toString();
				findDataLoadPasswordReset(id);
			}
		});
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Sure to Log Out?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if(a == JOptionPane.YES_OPTION) {
					frame.setVisible(false);
					LogIn login = new LogIn();
					login.setVisible(true);
				}
			}
		});
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkValidation()) {
					searchCustomer();
				}
			}
		});
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tableDataLoadNewCustomer();
				cmbSearch.txtSuggest.setText("");
			}
		});
		Timer timer = new Timer(0, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tableDataLoadNewCustomer();
			}
		});
		timer.setDelay(10000);
		timer.start();
	}
	private boolean checkValidation()
	{
		if(!cmbSearch.txtSuggest.getText().trim().isEmpty()) {
			return true;
		}
		else {
			//JOptionPane.showMessageDialog(null, "");
		}
		return false;
	}
	public void SessionBeanAdminName()
	{
		try {
			String query="select name, upload from tbadmininfo where accountno='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while(rs.next()) {
				txtAdminName.setText(rs.getString("name"));
				if(rs.getString("upload").isEmpty()) {
					String imagePath =rs.getString("upload").replace("#", "\\");
					Image img=Toolkit.getDefaultToolkit().getImage(imagePath);
					Image resizeImg = img.getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_DEFAULT);
					lbl.setIcon(new ImageIcon(resizeImg));
				}
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
	}
	public void cmbDataLoad()
	{
		try {
			cmbSearch.v.clear();
			cmbSearch.v.add("");
			String query="select customerid, accountno from tbcustomerinfo order by name";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while(rs.next()) {
				cmbSearch.v.add(rs.getString("customerid") + " # " + rs.getString("accountno"));
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
	}
	private void searchCustomer() {
		try {
			for(int a=modelAdmin.getRowCount()-1;a>=0;a--){
				modelAdmin.removeRow(a);
			}
			String query="select * from tbcustomerinfo where accountno='"+cmbSearch.txtSuggest.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while(rs.next()){
				modelAdmin.addRow(new java.lang.Object[]{rs.getString("customerid"),rs.getString("accountno"),
						rs.getString("name"),rs.getString("address"),rs.getString("email"),
						rs.getString("phone"),rs.getString("balance")});
			}
			dbConnection.con.close();
		}
		catch(Exception exp){
			JOptionPane.showMessageDialog(null, exp);
		}
	}
	private boolean deteteCustomer() {
		int index=tableAdmin.getSelectedRow();
		TableModel model = tableAdmin.getModel();

		String CustomerId=tableAdmin.getValueAt(index, 0).toString();
		try
		{
			String query="delete from tbcustomerinfo where customerid='"+CustomerId.trim()+"'";
			dbConnection.connect();
			dbConnection.sta.executeUpdate(query);
			dbConnection.con.close();
			return true;
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
		return false;
	}
	public void tableDataLoadNewCustomer(){
		try{
			for(int a=modelAdmin.getRowCount()-1;a>=0;a--){
				modelAdmin.removeRow(a);
			}
			String query="select customerid,accountno,name,address,email,phone,balance from tbcustomerinfo order by name";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while(rs.next()){
				modelAdmin.addRow(new java.lang.Object[]{rs.getString("customerid"),rs.getString("accountno"),
						rs.getString("name"),rs.getString("address"),rs.getString("email"),
						rs.getString("phone"),rs.getString("balance")});
			}
			dbConnection.con.close();
		}
		catch(Exception exp){
			JOptionPane.showMessageDialog(null, exp);
		}
	}
	private void findDataLoadEditCustomer(String id) 
	{	
		EditCustomer editCustomer = new EditCustomer(sessionBean);
		try
		{
			String query="select customerid,accountno,name,password,designation,address,email,phone,balance,ifnull(upload,'')upload "
					+ "from tbcustomerinfo where customerid='"+id+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next())
			{
				editCustomer.txtCustomerId.setText(rs.getString("customerid"));
				editCustomer.txtAccountNo.setText(rs.getString("accountno"));
				editCustomer.txtName.setText(rs.getString("name"));
				editCustomer.txtPassword.setText(rs.getString("password"));
				editCustomer.txtDesignation.setText(rs.getString("designation"));
				editCustomer.txtAddress.setText(rs.getString("address"));
				editCustomer.txtEmail.setText(rs.getString("email"));
				editCustomer.txtPhone.setText(rs.getString("phone"));
				editCustomer.txtBalance.setText(rs.getString("balance"));
				if(rs.getString("upload").isEmpty()) {
					String imagePath =rs.getString("upload").replace("#", "\\");
					Image img=Toolkit.getDefaultToolkit().getImage(imagePath);
					Image resizeImg = img.getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_DEFAULT);
					editCustomer.lbl.setIcon(new ImageIcon(resizeImg));
				}
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
	}
	private void findDataLoadPasswordReset(String id) 
	{	
		PassowordReset passreset = new PassowordReset();
		try
		{
			String query="select accountno from tbcustomerinfo where customerid='"+id+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next())
			{
				passreset.txtAccountNo.setText(rs.getString("accountno"));
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
	}
	private boolean checkConfirmation(String caption)
	{ 
		int a=JOptionPane.showConfirmDialog(null, caption, "Confirmation......" ,JOptionPane.YES_NO_OPTION);
		if(a==JOptionPane.YES_OPTION)
		{
			return true;
		}
		return false;
	}
	private void cmp() {
		panelAdmin = new JPanel();
		add(panelAdmin);
		panelAdmin.setLayout(new BorderLayout());
		panelNorth = new JPanel();
		panelAdmin.add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		panelCenter = new JPanel();
		panelAdmin.add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		panelEast = new JPanel();
		panelAdmin.add(panelEast, BorderLayout.EAST);
		paneleastwork();
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 90));
		panelNorth.setLayout(new GridLayout());
		panelNorthWest = new JPanel();
		panelNorth.add(panelNorthWest);
		panelNorthWestwork();
		panelNorthCenter = new JPanel();
		panelNorth.add(panelNorthCenter);
		panelNorthCenterwork();
	}
	private void panelNorthWestwork() {
		FlowLayout flow = new FlowLayout();
		panelNorthWest.setLayout(flow);
		flow.setVgap(30);
		lblAdmin = new JLabel("Administration Mode");
		panelNorthWest.add(lblAdmin);
		lblAdmin.setFont(new Font("Monaco", Font.PLAIN, 20));
	}
	private void panelNorthCenterwork() {

	}
	private void panelcenterwork() {
		panelCenter.setLayout(new BorderLayout());
		panelCenterNorth = new JPanel();
		panelCenter.add(panelCenterNorth, BorderLayout.NORTH);
		panelCenterNorthwork();
		panelCenterCenter = new JPanel();
		panelCenter.add(panelCenterCenter, BorderLayout.CENTER);
		panelCenterCenterwork();
	}
	private void panelCenterNorthwork() {
		//panelCenterNorth.setBackground(Color.decode("#00bcd4"));
		//panelCenterNorth.setBackground(Color.white);
		panelCenterNorth.setPreferredSize(new Dimension(0, 50));
		FlowLayout flow = new FlowLayout();
		flow.setVgap(10);
		panelCenterNorth.setLayout(flow);
		panelCenterNorth.add(cmbSearch.cmbSuggest);
		cmbSearch.cmbSuggest.setFont(new Font("Monaco", Font.PLAIN, 13));
		cmbSearch.cmbSuggest.setPreferredSize(new Dimension(340, 30));
		panelCenterNorth.add(btnSearch);
		btnSearch.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnSearch.setPreferredSize(new Dimension(120, 35));
		btnSearch.setFocusable(false);
	}
	private void panelCenterCenterwork() {
		//panelCenterCenter.setBackground(Color.decode("#00bcd4"));
		//panelCenterCenter.setBackground(Color.white);
		panelCenterCenter.add(scrollAdmin);
		FlowLayout flow=new FlowLayout();
		panelCenterCenter.setLayout(flow);
		flow.setVgap(0);
		scrollAdmin.setPreferredSize(new Dimension(835, 550));
		tableAdmin.getTableHeader().setReorderingAllowed(false);
		tableAdmin.setFont(new Font("Monaco", Font.PLAIN, 12));
	}
	private void paneleastwork() {
		//panelEast.setBackground(Color.decode("#00bcd4"));
		//panelEast.setBackground(Color.white);
		panelEast.setPreferredSize(new Dimension(305, 0));
		panelEast.setLayout(new BorderLayout());
		panelEastNorth = new JPanel();
		panelEast.add(panelEastNorth, BorderLayout.NORTH);
		panelEastNorthwork();
		panelEastCenter = new JPanel();
		panelEast.add(panelEastCenter, BorderLayout.CENTER);
		panelEastCenterwork();
	}
	private void panelEastNorthwork() {
		panelEastNorth.setLayout(new BorderLayout());
		panelEastNorthNorth = new JPanel();
		panelEastNorth.add(panelEastNorthNorth, BorderLayout.NORTH);
		panelEastNorthNorthwork();
		panelEastNorthCenter = new JPanel();
		panelEastNorth.add(panelEastNorthCenter, BorderLayout.CENTER);
		panelEastNorthCenterwork();
	}
	private void panelEastNorthNorthwork() {
		panelEastNorthNorth.setPreferredSize(new Dimension(0, 260));
		FlowLayout flow = new FlowLayout();
		panelEastNorthNorth.setLayout(flow);
		flow.setVgap(25);
		lblAdminInfo = new JLabel("Admin Information");
		panelEastNorthNorth.add(lblAdminInfo);
		lblAdminInfo.setFont(new Font("Monaco", Font.PLAIN, 16));
		lbl = new JLabel(new ImageIcon("image/imageuser2.png"));
		panelEastNorthNorth.add(lbl);
		lbl.setPreferredSize(new Dimension(170, 170));
		//lbl.setIcon(new ImageIcon(sessionBean.getAccountImagePath()));
	}
	private void panelEastNorthCenterwork() {
		panelEastNorthCenter.setLayout(new GridBagLayout());
		GridBagConstraints grid = new GridBagConstraints();
		grid.fill = GridBagConstraints.BOTH;
		grid.insets = new Insets(5, 5, 5, 5);

		lblAdminId = new JLabel("Admin Id");
		grid.gridx = 0;
		grid.gridy = 2;
		panelEastNorthCenter.add(lblAdminId, grid);
		lblAdminId.setFont(new Font("Monaco", Font.PLAIN, 13));

		txtAdminId = new JTextField(10);
		grid.gridx = 1;
		grid.gridy = 2;
		panelEastNorthCenter.add(txtAdminId, grid);
		txtAdminId.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAdminId.setPreferredSize(new Dimension(50, 25));
		txtAdminId.setText(sessionBean.getUserId());
		txtAdminId.setEditable(false);
		txtAdminId.setHorizontalAlignment(JTextField.CENTER);
		txtAdminId.setBorder(null);

		lblAdminName = new JLabel("Admin Name");
		grid.gridx = 0;
		grid.gridy = 3;
		panelEastNorthCenter.add(lblAdminName, grid);
		lblAdminName.setFont(new Font("Monaco", Font.PLAIN, 13));

		txtAdminName = new JTextField(15);
		grid.gridx = 1;
		grid.gridy = 3;
		panelEastNorthCenter.add(txtAdminName, grid);
		txtAdminName.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAdminName.setPreferredSize(new Dimension(50, 25));
		txtAdminName.setEditable(false);
		txtAdminName.setHorizontalAlignment(JTextField.CENTER);
		txtAdminName.setBorder(null);
	}
	private void panelEastCenterwork() {
		FlowLayout flow = new FlowLayout();
		panelEastCenter.setLayout(flow);
		flow.setVgap(25);
		panelEastCenter.add(btnAdd);
		btnAdd.setPreferredSize(new Dimension(145, 55));
		panelEastCenter.add(btnEdit);
		btnEdit.setPreferredSize(new Dimension(145, 55));
		panelEastCenter.add(btnDelete);
		btnDelete.setPreferredSize(new Dimension(145, 55));
		panelEastCenter.add(btnPasswordReset);
		btnPasswordReset.setPreferredSize(new Dimension(145, 55));
		panelEastCenter.add(btnRefresh);
		btnRefresh.setPreferredSize(new Dimension(145, 55));
		panelEastCenter.add(btnLogOut);
		btnLogOut.setPreferredSize(new Dimension(145, 55));
	}
	private void btnBackgroundwork() {
		btnAdd.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnAdd.setForeground(Color.white);
		btnAdd.setFocusable(false);
		btnAdd.setBackground(Color.decode("#4c4177"));
		btnEdit.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnEdit.setBackground(Color.decode("#4c4177"));
		btnEdit.setForeground(Color.white);
		btnEdit.setFocusable(false);
		btnDelete.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnDelete.setBackground(Color.decode("#4c4177"));
		btnDelete.setForeground(Color.white);
		btnDelete.setFocusable(false);
		btnPasswordReset.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnPasswordReset.setBackground(Color.decode("#4c4177"));
		btnPasswordReset.setForeground(Color.white);
		btnPasswordReset.setFocusable(false);
		btnLogOut.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnLogOut.setBackground(Color.decode("#4c4177"));
		btnLogOut.setForeground(Color.white);
		btnLogOut.setFocusable(false);
		btnRefresh.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnRefresh.setBackground(Color.decode("#4c4177"));
		btnRefresh.setForeground(Color.white);
		btnRefresh.setFocusable(false);
	}
}
