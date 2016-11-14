package controllers;

import app.Main;
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

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
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

    private static IUserHandling iUserHandling;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
                //TODO: Connect to server and check if login is correct.
                try {
                    boolean loginSuccess = iUserHandling.loginUser(user);

                    if(loginSuccess) {
                        // Login is correct. Show main UI

                        MainScreenController.showMenu();
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
                //TODO: Connect to server and register a new account.

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

        try {
            iUserHandling = Main.getIUserHandling();
        } catch (RemoteException e) {
            AlertMessage.showException("Unable to connect to our server.", e);
        } catch (NotBoundException e) {
            AlertMessage.showException("No server has been found with the name \"StockApp\" on the remote host.\nPlease try again later", e);
        }
        stage.show();

    }
}
