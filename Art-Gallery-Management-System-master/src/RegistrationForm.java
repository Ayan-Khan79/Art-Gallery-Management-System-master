import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JFrame {
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;
    public RegistrationForm(){
        setTitle("Create a new Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(1000,600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(200,50,400,600);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm obj = new LoginForm();
                registerUser();
                dispose();
            }
        });
        setVisible(true);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage obj=new HomePage();
                dispose();
            }
        });
    }

    private void registerUser() {
        String name= tfName.getText();
        String email= tfEmail.getText();
        String phone= tfPhone.getText();
        String address= tfAddress.getText();
        String password= String.valueOf(tfPassword.getPassword());
        String confirmPassword=String.valueOf(tfConfirmPassword.getPassword());

        if(name.isEmpty()|| email.isEmpty() || phone.isEmpty() || address.isEmpty()|| password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please Enter Your Details",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this, "Confirm Password does not match",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
       user= addUserToDatabase(name,email,phone,address,password);
        if(user!=null){
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,
                    "Failed to register", "try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public User user;
    private User addUserToDatabase(String name,String email, String phone, String address, String password){
        User user=null;
        final String DB_URL="jdbc:mysql://localhost:3306/Art_Gallery";
        final String USERNAME= "root";
        final String PASSWORD="Ayan7905@";
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement stnt= conn.createStatement();
            String sql="INSERT INTO persons(name,email,phone,address,password)"+
                    "VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,phone);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,password);

            int addedRows=preparedStatement.executeUpdate();
            if(addedRows>0){
                user=new User();
                user.name=name;
                user.email=email;
                user.phone=phone;
                user.address=address;
                user.password=password;
            }

            stnt.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
}

    public static void main(String[] args) {
        RegistrationForm myform=new RegistrationForm();
        User user=myform.user;
        if(user!=null){
            System.out.println("Successful Registration of:"+user.name);

        }
        else{
            System.out.println("Registration Canceled");
        }
    }
}
