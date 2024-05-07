package malenquillaa.java.spring.bank.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import malenquillaa.java.spring.bank.models.User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Setter
@Getter
@AllArgsConstructor
@Service
public class AccountNumberService {

    public String generate(User user) {
        String var1 = user.getDOB().toString()
                .replaceAll("[^A-Za-z0-9]", "").substring(4, 8);
        Long var2 = user.getId();

        String core = var1 + var2;

        if (core.length() > 15) {
            throw new RuntimeException("Maximum number of users exceeded");
        }

        Random rand = new Random();
        int randomLength = 15 - core.length();
        long max = (long) Math.pow(10, randomLength) - 1;
        long min = (long) Math.pow(10, randomLength - 1);
        long random = rand.nextLong(max - min) + min;

        return var1 + random + var2;
    }
}
