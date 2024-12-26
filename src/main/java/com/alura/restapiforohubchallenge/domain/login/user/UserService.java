package com.alura.restapiforohubchallenge.domain.login.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findById(Long idUser) {
        if (!userRepository.existsById(idUser)) {
            throw new ValidationException("This user does not exists.");
        }
        return userRepository.getReferenceById(idUser);
    }

    public void createNewUser(UserCreateDTO userCreateDTO) {
        if (!isValidEmail(userCreateDTO.email())) {
            throw new ValidationException("The email is invalid.");
        }
        // Encriptar la contrasena con el algoritmo HMAC256.
        String passwordEncrypt = bcrypt(userCreateDTO.password());

        UserEntity userEntity = new UserEntity(
                null,
                userCreateDTO.username(),
                userCreateDTO.email(),
                passwordEncrypt
        );
        userRepository.save(userEntity);
    }

    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        UserEntity userEntity = userRepository.getReferenceByEmail(userUpdateDTO.actualEmail());

        if (userUpdateDTO.newUserName() != null) {
            userEntity.setUserName(userUpdateDTO.newUserName());
        }
        if (userUpdateDTO.newEmail() != null) {
            if(!isValidEmail(userUpdateDTO.newEmail())) {
                throw new ValidationException("The email is invalid.");
            }
            userEntity.setEmail(userUpdateDTO.newEmail());
        }
        if (userUpdateDTO.newPassword() != null) {
            userEntity.setUserPassword(bcrypt(userUpdateDTO.newPassword()));
        }
    }







    /* Metodo para verificar si un email tiene el formato correcto.
     *
     * ^: Indica el inicio de la cadena.
     * [A-Za-z0-9+_.-]+: Permite una o más ocurrencias de letras, números, signos más,
     *                   guiones bajos, puntos y guiones.
     *
     * @: El símbolo arroba, que separa el nombre de usuario del dominio.
     *
     * (.+): Cualquier carácter(uno o más) hasta el final de la cadena (el dominio).
    */
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Metodo para encriptar contrasenas con el algoritmo BCrypt.
    public static String bcrypt(String password) {
        // Generar una sal aleatoria con un trabajo adecuado.
        String salt = BCrypt.gensalt();

        // Generar el hash de la contraseña.
        return BCrypt.hashpw(password, salt);
    }

    // Testear el metodo de encriptacion bcrypt();
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
