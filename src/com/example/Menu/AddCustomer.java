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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
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

public class AddCustomer extends JFrame {

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
	JLabel lblPassword = new JLabel("Password");
	JLabel lblname = new JLabel("Name");
	JLabel lblAddress = new JLabel("Address");
	JLabel lblEmail = new JLabel("Email");
	JLabel lblPhone = new JLabel("Phone");
	JLabel lblBalance = new JLabel("Balance");
	JLabel lblAccountNo = new JLabel("Account No");
	JLabel lblDesignation = new JLabel("Designation");
	JLabel lbl = new JLabel(new ImageIcon("image/imageuser.png"));

	JTextField txtCustomerId = new JTextField(30);
	JPasswordField txtPassword = new JPasswordField();
	JTextField txtName = new JTextField();
	JTextArea txtAddress = new JTextArea(3,1);
	JTextField txtEmail = new JTextField();
	JTextField txtPhone = new JTextField();
	JTextField txtBalance = new JTextField();
	JTextField txtAccountNo = new JTextField();
	public static JTextField txtDesignation = new JTextField();
	JScrollPane scrollAddress=new JScrollPane(txtAddress,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	JButton btnAdd = new JButton("Add");
	JButton btnBrowse = new JButton("Browse");
	JButton btnGenarate = new JButton("Generate");
	JButton btnPassGenarate = new JButton("Generate");
	JButton btnRefresh = new JButton("Refresh");

	boolean isUpdate = false;
	JFileChooser fileChooser;
	File imageFile=null;
	SessionBean sessionBean;

	public AddCustomer(SessionBean bean) {
		this.sessionBean=bean;
		sessionBean.getAccountImagePath();
		frame();
		cmp();
		btnBackgroundwork();
		btnAction();

		autoIdNewCustomer();
	}
	private void btnAction() {
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UploadAction();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(checkValidationNewCustomer()) {
					if(isExistNewCustomer()) {
						if(isExistAccountNo()) {
							if(checkConfirmation("Sure to Save?")) {
								if(insertDataNewCustomer()) {
									sendEmail();
									JOptionPane.showMessageDialog(null, "All information Save successfully");
									txtClearNewCustomer();
									autoIdNewCustomer();
								}
							}
						}
					}
				}
			}
		});
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtClearNewCustomer();
				autoIdNewCustomer();
			}
		});
		btnGenarate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Random rand = new Random(); 
				int rand_int1 = rand.nextInt(100000); 
				String s1 = Integer.toString(rand_int1);
				txtAccountNo.setText("100000"+s1);
			}
		});
		btnPassGenarate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Random random = new Random(); 
				String s1 = String.format("%05d", random.nextInt(10000)); 
				//String s1 = Integer.toString(rand_int1);
					txtPassword.setText("2"+s1);
					System.out.println(s1);
			}
		});
		txtPhone.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char input = e.getKeyChar();
				if((input < '0' || input > '9') && input != '\b') {
					e.consume();
					JOptionPane.showMessageDialog(null, "Enter Only Number!");
				}
			}
		});
		txtPhone.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(txtPhone.getText().trim().length()<4) {
					txtPhone.setText("+880");
				}
			}
		});
	}
	protected boolean sendEmail() {
		String email="";
		try 
		{
			String query="select email from tbcustomerinfo where accountno='"+txtAccountNo.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next()) {
				email = rs.getString("email");
			}
			dbConnection.con.close();

			String host ="smtp.gmail.com";
			String user = "thinkingwholeworld2@gmail.com";
			String pass = "nuralambabu";
			String to = email;
			String from = "thinkingwholeworld2@gmail.com";
			String subject = "Your ATM Account Information";
			String messageText = "Account No is: "+txtAccountNo.getText().trim() 
					+"\n"+"Password is: "+txtPassword.getText().trim();
			boolean sessionDebug = false;

			Properties props = System.getProperties();

			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");

			//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(subject); msg.setSentDate(new Date());
			msg.setText(messageText);

			Transport transport=mailSession.getTransport("smtp");
			transport.connect(host, user, pass);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			//txtShowPassword.setText("Your Password Reset Successfull.");
			//System.out.println(s1 + email);
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
		return false;
	}
	public void autoIdNewCustomer(){
		try{
			String query="select ifnull(max(cast(subString(customerid,locate('-',customerid)+"
					+ "1,length(customerid)-locate('-',customerid)) as UNSIGNED)),0)+1 "
					+ "as id from tbcustomerinfo order by AutoId";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);

			if(rs.next()){
				String userId=rs.getString("id");
				txtCustomerId.setText("Customer-"+userId);
			}
			dbConnection.con.close();
		}
		catch(Exception exp){
			JOptionPane.showMessageDialog(null, exp + "autoid");
		}
	}
	private boolean checkValidationNewCustomer()
	{
		if(!txtCustomerId.getText().trim().isEmpty())
		{
			if(!txtAccountNo.getText().trim().isEmpty())
			{
				if(!txtName.getText().trim().isEmpty())
				{
					if(!txtPassword.getText().trim().isEmpty())
					{
						if(!txtAddress.getText().trim().isEmpty())
						{
							if(!txtEmail.getText().trim().isEmpty())
							{
								if(!txtPhone.getText().trim().isEmpty())
								{
									if(!txtBalance.getText().trim().isEmpty())
									{
										return true;
										/*if(!lbl.getText().trim().isEmpty())
										{
											return true;
										}
										else 
										{
											JOptionPane.showMessageDialog(null, "please insert your image");
										}*/
									}
									else 
									{
										JOptionPane.showMessageDialog(null, "please deposit some money greater then 500");
									}
								}
								else 
								{
									JOptionPane.showMessageDialog(null, "please insert your phone number");
								}
							}
							else 
							{
								JOptionPane.showMessageDialog(null, "please insert email");
							}
						}
						else 
						{
							JOptionPane.showMessageDialog(null, "please insert address");
						}
					}
					else 
					{
						JOptionPane.showMessageDialog(null, "Please Generate Password Using Generate Button!!");
					}
				}
				else 
				{
					JOptionPane.showMessageDialog(null, "please insert Name");
				}
			}
			else 
			{
				JOptionPane.showMessageDialog(null, "Please Generate Account Number Using Generate Button!!");
			}
		}
		else 
		{
			JOptionPane.showMessageDialog(null, "Please insert CustomerId");
		}
		return false;
	}
	private boolean insertDataNewCustomer()
	{
		String path="";
		if(imageFile!=null) {
			ImageSave();
			path=sessionBean.getAccountImagePath()+"\\"+txtCustomerId.getText().trim()+".jpg";
			path=path.replace("\\", "#");
		}
		try
		{
			String query="insert into tbcustomerinfo (customerid,accountno,password,name,designation,address,"
					+ "email,phone,balance,upload,userIp,entryTime)"
					+ "values('"+txtCustomerId.getText().trim()+"','"+txtAccountNo.getText().trim()+"',"
					+ "'"+txtPassword.getText().trim()+"','"+txtName.getText().trim()+"',"
					+ "'"+txtDesignation.getText().trim()+"','"+txtAddress.getText().trim()+"',"
					+ "'"+txtEmail.getText().trim()+"','"+txtPhone.getText().trim()+"',"
					+ "'"+txtBalance.getText().trim()+"','"+path+"','',now())";
			dbConnection.connect();
			dbConnection.sta.executeUpdate(query);
			dbConnection.con.close();
			return true;
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp+ "insert");
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
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Please Insert Image");
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
	private boolean isExistNewCustomer()
	{
		try 
		{
			String query="select * from tbcustomerinfo where Name='"+txtName.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next())
			{
				JOptionPane.showMessageDialog(null, "sorry this user name already exists!");
				return false;
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
		return true;
	}
	private boolean isExistAccountNo()
	{
		try 
		{
			String query="select * from tbcustomerinfo where accountno='"+txtAccountNo.getText().trim()+"'";
			dbConnection.connect();
			ResultSet rs=dbConnection.sta.executeQuery(query);
			if(rs.next())
			{
				JOptionPane.showMessageDialog(null, "sorry this account no already exists! Again Account Generate.");
				return false;
			}
			dbConnection.con.close();
		}
		catch(Exception exp)
		{
			JOptionPane.showMessageDialog(null, exp);
		}
		return true;
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
	private void txtClearNewCustomer() {
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
	private void frame() {
		setSize(920, 630);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Add Customer");
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
		panelNorth.setBackground(Color.decode("#ff5252"));
	}
	private void panelsouthwork() {
		panelSouth.setPreferredSize(new Dimension(0, 50));
		panelSouth.setBackground(Color.decode("#ff5252"));
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
		panelCenterWestCenter.setPreferredSize(new Dimension(500, 0));
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
		txtCustomerId.setEditable(false);

		grid.gridx=0;
		grid.gridy=1;
		panelCenterWestCenter.add(lblAccountNo, grid);
		lblAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=1;
		panelCenterWestCenter.add(txtAccountNo, grid);
		txtAccountNo.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtAccountNo.setPreferredSize(new Dimension(140, 30));
		txtAccountNo.setEditable(false);

		grid.gridx=2;
		grid.gridy=1;
		panelCenterWestCenter.add(btnGenarate, grid);
		btnGenarate.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnGenarate.setFocusable(false);	

		grid.gridx=0;
		grid.gridy=2;
		panelCenterWestCenter.add(lblname, grid);
		lblname.setFont(new Font("Monaco", Font.PLAIN, 13));

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
		txtDesignation.setText("User");
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
		txtPassword.setEditable(false);

		grid.gridx=2;
		grid.gridy=4;
		panelCenterWestCenter.add(btnPassGenarate, grid);
		btnPassGenarate.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnPassGenarate.setFocusable(false);

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
		txtPhone.setText("+880");

		grid.gridx=0;
		grid.gridy=8;
		panelCenterWestCenter.add(lblBalance, grid);
		lblBalance.setFont(new Font("Monaco", Font.PLAIN, 13));

		grid.gridx=1;
		grid.gridy=8;
		panelCenterWestCenter.add(txtBalance, grid);
		txtBalance.setFont(new Font("Monaco", Font.PLAIN, 13));
		txtBalance.setPreferredSize(new Dimension(140, 30));
	}
	private void panelcenterwestsouthwork() {
		panelCenterWestSouth.setPreferredSize(new Dimension(0, 70));
		panelCenterWestSouth.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		panelCenterWestSouth.setLayout(flow);
		flow.setVgap(20);
		flow.setAlignment(2);
		panelCenterWestSouth.add(btnAdd);
		btnAdd.setPreferredSize(new Dimension(130, 35));
		panelCenterWestSouth.add(btnRefresh);
		btnRefresh.setPreferredSize(new Dimension(130, 35));

	}
	private void paneleastwork() {
		panelEast.setPreferredSize(new Dimension(55, 0));
		panelEast.setBackground(Color.decode("#ff5252"));
	}
	private void panelwestwork() {
		panelWest.setPreferredSize(new Dimension(55, 0));
		panelWest.setBackground(Color.decode("#ff5252"));
	}
	private void btnBackgroundwork() {
		btnAdd.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnAdd.setBackground(Color.decode("#ff5252"));
		btnAdd.setForeground(Color.white);
		btnAdd.setFocusable(false);
		btnBrowse.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnBrowse.setBackground(Color.decode("#ff5252"));
		btnBrowse.setForeground(Color.white);
		btnBrowse.setFocusable(false);	
		btnRefresh.setFont(new Font("Monaco", Font.PLAIN, 13));
		btnRefresh.setBackground(Color.decode("#ff5252"));
		btnRefresh.setForeground(Color.white);
		btnRefresh.setFocusable(false);	
	}
}
