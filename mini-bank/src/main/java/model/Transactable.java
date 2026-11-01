package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

/** Practical 6: capabilities an account offers. */
public interface Transactable {
    void deposit(long amount) throws InvalidAmountException;

    boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException;
}
