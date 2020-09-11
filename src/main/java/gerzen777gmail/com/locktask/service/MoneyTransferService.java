package gerzen777gmail.com.locktask.service;

import gerzen777gmail.com.locktask.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MoneyTransferService {


    public void transfer(Account from, Account to, Long amount) {

        Lock first = new ReentrantLock();
        Lock second = new ReentrantLock();
        Map<Lock, Long> locksMap = new HashMap<>();

        if (from.getId() > to.getId()) {
            locksMap.put(first, from.getId());
            locksMap.put(second, to.getId());
        } else {
            locksMap.put(first, to.getId());
            locksMap.put(second, from.getId());
        }

        first.lock();
        from.setId(locksMap.get(first));
        try {
        second.lock();
            to.setId(locksMap.get(second));
            try {
                if (from.getBalance() >= amount) {
                    from.setBalance(from.getBalance() - amount);
                    to.setBalance(to.getBalance() + amount);
                } else {
                    throw new IllegalArgumentException("Нет денег!");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            first.unlock();
            second.unlock();
        }
    }


}
