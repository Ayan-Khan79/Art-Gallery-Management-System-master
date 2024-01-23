import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame{
    private JPanel panel1;
    private JTextField tfEmail;
    private JPasswordField pfpassword;
    private JButton btnCancel;
    private JButton btnOK;
    private JPanel loginPanel;
    public LoginForm(){
        //super(parent);
        setTitle("LOGIN");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(500,500));
        //setModal(true);
        //setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(350,100,500,400);
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email= tfEmail.getText();
                String password=String.valueOf(pfpassword.getPassword());

               user= getAuthenticatedUser(email,password);
               if(user!= null){
                   ArtGalleryManagementSystem obj = new ArtGalleryManagementSystem();
                   dispose();
               }
               else{
                   JOptionPane.showMessageDialog(LoginForm.this,
                           "Email or Password Invalid",
                           "Try Again",JOptionPane.ERROR_MESSAGE);
               }
            }
        });
        setVisible(true);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage page=new HomePage();
                dispose();
            }
        });
    }

    public User user;
    private User getAuthenticatedUser(String email,String password){
        User user=null;

        final String DB_URL="jdbc:mysql://localhost:3306/Art_Gallery";
        final String USERNAME= "root";
        final String PASSWORD="Ayan7905@";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stnt=conn.createStatement();
            String sql="SELECT * FROM persons where email=? and password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                user=new User();
                user.name=resultSet.getString("name");
                user.email=resultSet.getString("email");
                user.phone=resultSet.getString("phone");
                user.address=resultSet.getString("address");
                user.password=resultSet.getString("password");
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm=new LoginForm();
        User user=loginForm.user;
        if(user!= null){
            System.out.println("Successful Authentication of:"+user.name);
            System.out.println("              Email:"+user.email);
            System.out.println("              Phone:"+user.phone);
            System.out.println("              Address:"+user.address);
        }
        else{
            System.out.println("Authentication Cancelled");
        }
    }
}
