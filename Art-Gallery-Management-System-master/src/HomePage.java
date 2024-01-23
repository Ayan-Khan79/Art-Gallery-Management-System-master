import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame{
    private JPanel panel1;
    private JButton btnLogin;
    private JButton btnRegister;
    private JPanel HomePage;
    private JComboBox comboBox1;

    public HomePage() {
        //super(parent);
        setTitle("Home Page");
        setContentPane(panel1);
        setMinimumSize(new Dimension(600, 400));
        //setModal(true);
        //setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setBounds(300,100,500,400);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm login=new LoginForm();
                HomePage.setVisible(false);
                dispose();
            }

        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm register=new RegistrationForm();
                HomePage.setVisible(false);
                dispose();
            }
        });
    }


    public static void main(String[] args) {

        HomePage homePage = new HomePage();
    }
}


