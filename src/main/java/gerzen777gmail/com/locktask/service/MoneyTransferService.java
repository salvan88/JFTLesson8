package gerzen777gmail.com.locktask.service;

import gerzen777gmail.com.locktask.model.Account;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MoneyTransferService {

    Lock locker = new ReentrantLock();

    public void transfer(Account from, Account to, Long amount) {


        locker.lock();
        try {
            if (from.getBalance() >= amount) {
                from.setBalance(from.getBalance() - amount);
                to.setBalance(to.getBalance() + amount);
            } else {
                throw new IllegalArgumentException("Нет денег!");
            }
        } finally {
            locker.unlock();
        }
    }


}
