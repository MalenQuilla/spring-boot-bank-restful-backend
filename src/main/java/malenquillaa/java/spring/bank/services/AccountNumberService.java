package malenquillaa.java.spring.bank.services;

import malenquillaa.java.spring.bank.models.User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountNumberService {

    public String generate(User user) {
        String var1 = user.getDOB().toString()
                .replaceAll("[^A-Za-z0-9]", "").substring(4, 8);
        Long var2 = user.getId();

        String core = var1 + var2;

        if (core.length() > 15) {
            throw new RuntimeException("Maximum number of users exceeded"); // over 99 billion users
        }

        Random rand = new Random();
        int randomLength = 15 - core.length();
        long max = (long) Math.pow(10, randomLength) - 1;
        long min = (long) Math.pow(10, randomLength - 1);
        long random = rand.nextLong(max - min) + min;

        return var1 + random + var2;
    }
}
