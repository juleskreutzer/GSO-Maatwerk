package controllers;

import domain.StockApp;
import domain.User;
import exceptions.*;
import interfaces.IStockReceive;
import interfaces.IUserHandling;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import util.Helper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * | Created by juleskreutzer
 * | Date: 31-10-16
 * |
 * | Project Info:
 * | Project Name: StockAppClient
 * | Project Package Name: controllers
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
public class LoginController implements Initializable {

    @FXML
    TextField txtLoginUsername, txtRegisterUsername, txtRegisterEmail;

    @FXML
    PasswordField txtLoginPassword, txtRegisterPassword, txtRegisterPasswordConfirm;

    @FXML
    Button btnLogin, btnRegister;

    private static Stage stage;

    private IUserHandling iUserHandling;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(StockApp.getInstance().getIUserHandlingInterface() != null) {
            this.iUserHandling = StockApp.getInstance().getIUserHandlingInterface();
        } else {
            NullPointerException e = new NullPointerException("Didn't receive user handling interface.");
            AlertMessage.showException("At this time, we're unable to contact the server.\nPlease try again later.", e);
        }

    }

    public void login()
    {
        String username = txtLoginUsername.getText();
        String password = txtLoginPassword.getText();

        if(username.equals("") || username.isEmpty()) {
            AlertMessage.show("Please fill in your username.", "", Alert.AlertType.ERROR);
        } else if(password.equals("") || password.isEmpty()) {
            AlertMessage.show("Please fill in your password.", "", Alert.AlertType.ERROR);
        } else {
            // Username and password are set.
            User user = User.createUser(username, password, "");

            IStockReceive instance = StockApp.getInstance();
            user.setReceiveInterface(instance);

            if(user != null) {
                try {
                    boolean loginSuccess = iUserHandling.loginUser(user);

                    if(loginSuccess) {
                        // Login is correct. Show main UI
                        Helper.getInstance().setUser(user);

                        // Check if file with all stock symbols exist
                        File file = new File("tickersymbols.txt");
                        if(file.exists() && !file.isDirectory()) {
                            // File exists, show main screen
                            MainScreenController.showMenu();
                        } else {
                            // File doesn't exists, fetch them!
                            SetupController.showMenu();
                        }

                    } else {
                        // Login failed, but a UserNotFound or InvalidCredentials Exception should've been thrown instead...
                        AlertMessage.show("The provided username and password are incorrect.\nPlease try again.", "", Alert.AlertType.ERROR);
                    }
                } catch (RemoteException | UserIsNullException e) {
                    AlertMessage.showException("Something went wrong. Please try again later.", e);
                } catch (UserNotFoundException | InvalidCredentialsException e) {
                    AlertMessage.show("Login failed.", e.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                AlertMessage.show("Unable to perform login. Please try again later.", "Couldn't create an user object.", Alert.AlertType.ERROR);
            }
        }

    }

    public void register() {
        String username = txtRegisterUsername.getText();
        String email = txtRegisterEmail.getText();
        String password = txtRegisterPassword.getText();
        String passwordConfirm = txtRegisterPasswordConfirm.getText();

        if(username.equals("") || username.isEmpty()) {
            AlertMessage.show("Please fill in an username.", "An username is required to register a new account.", Alert.AlertType.WARNING);
        } else if (email.equals("") || email.isEmpty()) {
            AlertMessage.show("Please fill in an email address.", "An email is required to register a new account.", Alert.AlertType.WARNING);
        } else if(password.equals("") || password.isEmpty()) {
            AlertMessage.show("Please fill in a password.", "A password is required to register a new account.", Alert.AlertType.WARNING);
        } else if(!password.equals(passwordConfirm)) {
            AlertMessage.show("Both passwords don't match.", "Please re-enter both passwords.", Alert.AlertType.ERROR);
        } else {
            // Username, email and password are set

            User user = User.createUser(username, password, email);

            if(user != null) {
                try {
                    boolean registrationSuccess = iUserHandling.registerUser(user);

                    if(registrationSuccess) {
                        // User is registered and can login now.
                        AlertMessage.show("Congratulations, your new account has been registered.", "You can now login using your username and password.", Alert.AlertType.INFORMATION);
                    } else {
                        // Something went wrong, but an error should've been thrown
                        AlertMessage.show("Couldn't finish your registration. Please try again later.", "", Alert.AlertType.ERROR);
                    }
                } catch (MultipleFoundException | UserIsRegisteredException e) {
                    AlertMessage.show("Registration failed.", e.getMessage(), Alert.AlertType.WARNING);
                } catch (RemoteException | UserIsNullException | NotFoundException e) {
                    AlertMessage.showException("Something went wrong. please try again later.", e);
                }

            } else {
                AlertMessage.show("Unable to perform registration. Please try again later.", "Couldn't create an user object.", Alert.AlertType.ERROR);
            }
        }

    }

    public static void showMenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(LoginController.class.getResource("/Login.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setTitle("StockApp");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setScene(scene);
        stage.show();

    }
}
