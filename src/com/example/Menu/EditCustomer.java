package com.example.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.example.Admin.SessionBean;
import com.example.Admin.dbConnection;

public class EditCustomer extends JFrame{

	JPanel panelNorth = new JPanel();
	JPanel panelCenter = new JPanel();
	JPanel panelCenterWest = new JPanel();
	JPanel panelCenterWestCenter = new JPanel();
	JPanel panelCenterWestSouth = new JPanel();
	JPanel panelCenterCenter = new JPanel();
	JPanel panelSouth = new JPanel();
	JPanel panelEast = new JPanel();
	JPanel panelWest = new JPanel();

	JLabel lblCustomerId = new JLabel("Customer Id");
	JLabel lblAccountNo = new JLabel("Account No");
	JLabel lblPassword = new JLabel("Password");
	JLabel lblName = new JLabel("Name");
	JLabel lblDesignation = new JLabel("Designation");
	JLabel lblAddress = new JLabel("Address");
	JLabel lblEmail = new JLabel("Email");
	JLabel lblPhone = new JLabel("Phone");
	JLabel lblBalance = new JLabel("Balance");
	public JLabel lbl = new JLabel(new ImageIcon("image/imageuser.png"));

	public JTextField txtCustomerId = new JTextField(30);
	public JTextField txtAccountNo = new JTextField();
	public JPasswordField txtPassword = new JPasswordField();
	public JTextField txtName = new JTextField();
	public JTextField txtDesignation = new JTextField();
	public JTextArea txtAddress = new JTextArea(3,1);
	public JTextField txtEmail = new JTextField();
	public JTextField txtPhone = new JTextField();
	public JTextField txtBalance = new JTextField();
	JScrollPane scrollAddress=new JScrollPane(txtAddress,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JButton btnAdd = new JButton("Add");
	JButton btnBrowse = new JButton("Browse");

	JFileChooser fileChooser;
	File imageFile=null;
	SessionBean sessionBean;
	
	public EditCustomer(SessionBean bean) {
		this.sessionBean=bean;
		sessionBean.getAccountImagePath();
		frame();
		cmp();
		btnBackgroundwork();
		btnAction();
	}
	private void btnAction() {
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UploadAction();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkConfirmation("Sure to Edit?")) {
					if(deteteCustomer()) {
						if(insertEditCustomer()) {
							JOptionPane.showMessageDialog(null, "All information Update successfully");
							txtClearEditCustomer();
						}
					}
				}
			}
		});
	}
	private void txtClearEditCustomer() {
		txtCustomerId.setText("");
		txtName.setText("");
		txtPassword.setText("");
		txtEmail.setText("");
		txtAddress.setText("");
		txtPhone.setText("");
		txtBalance.setText("");
		lbl.setIcon(new ImageIcon("image/imageuser.png"));
		imageFile=null;
		txtAccountNo.setText("");
	}
	private boolean checkConfirmation(String caption)
	{
		int a=JOptionPane.showConfirmDialog(null, caption,"confirmation.....",JOptionPane.YES_NO_OPTION);
		if(a==JOptionPane.YES_OPTION)
		{
			return true;
		}
		return false;
	}
	private boolean deteteCustomer() {
		try
		{
			String query="delete from tbcustomerinfo where customerid='"+txtCustomerId.getText().trim()+"'";
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
	private boolean insertEditCustomer()
	{
		String path="";
		if(imageFile!=null) {
			ImageSave();
			path=sessionBean.getAccountImagePath()+"\\"+txtCustomerId.getText().trim()+".jpg";
			path=path.replace("\\", "#");
		}
		try
		{
			String query="insert into tbcustomerinfo (customerid,accountno,password,name,designation,address,email,phone,balance,upload,userIp,entryTime)"
					+ "values('"+txtCustomerId.getText().trim()+"','"+txtAccountNo.getText().trim()+"','"+txtPassword.getText().trim()+"',"
					+ "'"+txtName.getText().trim()+"','"+txtDesignation.getText().trim()+"','"+txtAddress.getText().trim()+"',"
					+ "'"+txtEmail.getText().trim()+"','"+txtPhone.getText().trim()+"',"
					+ "'"+txtBalance.getText().trim()+"','"+path+"',"
					+ "'',now())";
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
	private void ImageSave() {
		if(imageFile!=null) {
			File file = new File(sessionBean.getAccountImagePath());
			if(!file.isDirectory()) {
				file.mkdir();
			}
			File imagePath = new File(sessionBean.getAccountImagePath()+"\\"+txtCustomerId.getText().trim()+".jpg");
			if(imagePath.exists()) {
				imagePath.delete();
			}
			try {
				BufferedImage buffer = ImageIO.read(imageFile);
				ImageIO.write(buffer, "jpg", imagePath);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
			else {
				JOptionPane.showMessageDialog(null, "Please insert Image File");
			}
	}
	private void UploadAction() {
		fileChooser = new JFileChooser();
		FileNameExtensionFilter nameExtensionFilter = new FileNameExtensionFilter("image", "jpeg", "png", "jpg", "gif");
		fileChooser.setFileFilter(nameExtensionFilter);
		fileChooser.setDialogTitle("Choose Image");
		fileChooser.showOpenDialog(this);
		imageFile = fileChooser.getSelectedFile();
		if(imageFile!=null) {
			Image image = Toolkit.getDefaultToolkit().getImage(imageFile.getPath());
			Image size = image.getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_DEFAULT);
			ImageIcon imageicon = new ImageIcon(size);
			lbl.setIcon(imageicon);
		}
	}
	private void frame() {
		setSize(900, 600);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Edit Customer");
	}
	private void cmp() {
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		panelnorthwork();
		add(panelCenter, BorderLayout.CENTER);
		panelcenterwork();
		add(panelSouth, BorderLayout.SOUTH);
		panelsouthwork();
		add(panelEast, BorderLayout.EAST);
		paneleastwork();
		add(panelWest, BorderLayout.WEST);
		panelwestwork();
	}
	private void panelnorthwork() {
		panelNorth.setPreferredSize(new Dimension(0, 50));
		panelNorth.setBackground(Color.decode("#39b5ff"));
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 50));
		panelSouth.setBackground(Color.decode("#39b5ff"));
	}
	private void panelcenterwork() {
		panelCenter.setBackground(Color.white);
		panelCenter.setLayout(new BorderLayout());
		panelCenter.add(panelCenterWest, BorderLayout.WEST);
		panelcenterwestwork();
		panelCenter.add(panelCenterCenter, BorderLayout.CENTER);
		panelcentercenterwork();
	}
	private void panelcentercenterwork() {
		panelCenterCenter.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		panelCenterCenter.setLayout(flow);
		flow.setVgap(25);
		panelCenterCenter.add(lbl);
		lbl.setPreferredSize(new Dimension(256, 256));
		panelCenterCenter.add(btnBrowse);
		btnBrowse.setPreferredSize(new Dimension(130, 35));
	}
	private void panelcenterwestwork() {
		panelCenterWest.setLayout(new BorderLayout());
		panelCenterWest.add(panelCenterWestCenter, BorderLayout.CENTER);
		panelcenterwestcenterwork();
		panelCenterWest.add(panelCenterWestSouth, BorderLayout.SOUTH);
		panelcenterwestsouthwork();
	}
	private void panelcenterwestcenterwork() {
		panelCenterWestCenter.setPreferredSize(new Dimension(460, 0));
		panelCenterWestCenter.setBackground(Color.white);
		panelCenterWestCenter.setLayout(new GridBagLayout());
		GridBagConstraints grid = new GridBagConstraints();
		grid.fill = GridBagConstraints.BOTH;
		grid.insets = new Insets(5, 5, 5, 5);

		grid.gridx=0;
		grid.gridy=0;
		panelCenterWestCenter.add(lblCustomerId, grid);
		lblCustomerId.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=0;
		panelCenterWestCenter.add(txtCustomerId, grid);
		txtCustomerId.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtCustomerId.setPreferredSize(new Dimension(140, 30));
		txtCustomerId.setEnabled(false);

		grid.gridx=0;
		grid.gridy=1;
		panelCenterWestCenter.add(lblAccountNo, grid);
		lblAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=1;
		panelCenterWestCenter.add(txtAccountNo, grid);
		txtAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAccountNo.setPreferredSize(new Dimension(140, 30));
		txtAccountNo.setEnabled(false);

		grid.gridx=0;
		grid.gridy=2;
		panelCenterWestCenter.add(lblName, grid);
		lblName.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=2;
		panelCenterWestCenter.add(txtName, grid);
		txtName.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtName.setPreferredSize(new Dimension(140, 30));
		
		grid.gridx=0;
		grid.gridy=3;
		panelCenterWestCenter.add(lblDesignation, grid);
		lblDesignation.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=3;
		panelCenterWestCenter.add(txtDesignation, grid);
		txtDesignation.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtDesignation.setPreferredSize(new Dimension(140, 30));
		txtDesignation.setEditable(false);

		grid.gridx=0;
		grid.gridy=4;
		panelCenterWestCenter.add(lblPassword, grid);
		lblPassword.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=4;
		panelCenterWestCenter.add(txtPassword, grid);
		txtPassword.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtPassword.setPreferredSize(new Dimension(140, 30));
		txtPassword.setEnabled(false);
		
		grid.gridx=0;
		grid.gridy=5;
		panelCenterWestCenter.add(lblAddress, grid);
		lblAddress.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=5;
		panelCenterWestCenter.add(scrollAddress, grid);
		txtAddress.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAddress.setPreferredSize(new Dimension(140, 30));

		grid.gridx=0;
		grid.gridy=6;
		panelCenterWestCenter.add(lblEmail, grid);
		lblEmail.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=6;
		panelCenterWestCenter.add(txtEmail, grid);
		txtEmail.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtEmail.setPreferredSize(new Dimension(140, 30));
		
		grid.gridx=0;
		grid.gridy=7;
		panelCenterWestCenter.add(lblPhone, grid);
		lblPhone.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=7;
		panelCenterWestCenter.add(txtPhone, grid);
		txtPhone.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtPhone.setPreferredSize(new Dimension(140, 30));
		
		grid.gridx=0;
		grid.gridy=8;
		panelCenterWestCenter.add(lblBalance, grid);
		lblBalance.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=8;
		panelCenterWestCenter.add(txtBalance, grid);
		txtBalance.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtBalance.setPreferredSize(new Dimension(140, 30));
		txtBalance.setEnabled(false);
	}
	private void panelcenterwestsouthwork() {
		panelCenterWestSouth.setPreferredSize(new Dimension(0, 80));
		panelCenterWestSouth.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		panelCenterWestSouth.setLayout(flow);
		flow.setVgap(20);
		flow.setAlignment(2);
		panelCenterWestSouth.add(btnAdd);
		btnAdd.setPreferredSize(new Dimension(130, 35));
	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(55, 0));
		panelEast.setBackground(Color.decode("#39b5ff"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(55, 0));
		panelWest.setBackground(Color.decode("#39b5ff"));
	}
	private void btnBackgroundwork() {
		btnAdd.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnAdd.setBackground(Color.decode("#39b5ff"));
		btnAdd.setForeground(Color.white);
		btnAdd.setFocusable(false);
		btnBrowse.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnBrowse.setBackground(Color.decode("#39b5ff"));
		btnBrowse.setForeground(Color.white);
		btnBrowse.setFocusable(false);	
	}
}
