package gerzen777gmail.com.locktask.service;

import gerzen777gmail.com.locktask.model.Account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MoneyTransferService {

    public void transfer(Account from, Account to, Long amount) {

        Lock first = new ReentrantLock();
        Lock second = new ReentrantLock();
        Map<Long, Lock> locksMap = new ConcurrentHashMap<>();

        if (from.getId() > to.getId()) {
            locksMap.put(from.getId(), first);
            locksMap.put(to.getId(), second);
        } else {
            locksMap.put(to.getId(), first);
            locksMap.put(from.getId(), second);
        }

        locksMap.putIfAbsent(from.getId(), first).lock();

        try {

            locksMap.putIfAbsent(to.getId(), second).lock();

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
            locksMap.get(from.getId()).unlock();
            locksMap.get(to.getId()).unlock();
        }
    }


}
