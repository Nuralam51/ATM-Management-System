package com.example.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.mail.Session;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.omg.CORBA.Object;

import com.example.Admin.SessionBean;
import com.example.Admin.dbConnection;

public class Transacion extends JFrame{

	JPanel panelChangePassword = new JPanel();
	JPanel panelNorth = new JPanel();
	JPanel panelCenter = new JPanel();
	JPanel panelCenterSouth = new JPanel();
	JPanel panelCenterCenter = new JPanel();
	JPanel panelCenterNorth = new JPanel();
	JPanel panelSouth = new JPanel();
	JPanel panelEast = new JPanel();
	JPanel panelWest = new JPanel();
	JPanel panelWestNorth = new JPanel();
	JPanel panelWestSouth = new JPanel();

	JLabel lblQuotestopIcon = new JLabel(new ImageIcon("image/Quotestop3.png"));
	
	String col[] = {"Account No", "Receiver Account No", "Balance", "EntryTime", "Status"};
	Object row[][] = {};
	DefaultTableModel modelTransaction = new DefaultTableModel(row,col);
	JTable tableTransaction = new JTable(modelTransaction);
	JScrollPane scrollTransaction = new JScrollPane(tableTransaction, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	SessionBean sessionBean;
	
	public Transacion(SessionBean bean) {
		this.sessionBean=bean;
		frame();
		cmp();
		tableDataLoadTransaction();
		btnAcion();
	}
	private void btnAcion() {
		Timer timer = new Timer(0, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tableDataLoadTransaction();
			}
		});
		timer.setDelay(5000);
		timer.start();
	}
	public void tableDataLoadTransaction(){
		try{
			for(int a=modelTransaction.getRowCount()-1;a>=0;a--){
				modelTransaction.removeRow(a);
			}
			String query="select accountno, receiveraccountno, balance, entrytime, "
					+ "status from tbtransactioninfo where accountno='"+sessionBean.getUserId()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			while(rs.next()){
				modelTransaction.addRow(new java.lang.Object[]{rs.getString("accountno"),
						rs.getString("receiveraccountno"),rs.getString("balance"),rs.getString("entrytime"),
						rs.getString("status")});
			}
			dbConnection.con.close();
		}
		catch(Exception exp){
			JOptionPane.showMessageDialog(null, exp);
		}
	}
	private void frame() {
		setSize(950, 530);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Transaction");
	}
	private void cmp() {
		add(scrollTransaction);
		FlowLayout flow=new FlowLayout();
		setLayout(flow);
		flow.setVgap(0);
		scrollTransaction.setPreferredSize(new Dimension(940, 500));
		tableTransaction.getTableHeader().setReorderingAllowed(false);
		tableTransaction.setFont(new Font("Monaco", Font.PLAIN, 12));
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)tableTransaction.getTableHeader().getDefaultRenderer();
		renderer.setHorizontalAlignment(0);
		tableTransaction.setFont(new Font("Monaco", Font.PLAIN, 12));
		tableTransaction.setDefaultEditor(Object.class, null);
		tableTransaction.getTableHeader().setFont(new Font("Monaco", Font.PLAIN, 12));
	}
}