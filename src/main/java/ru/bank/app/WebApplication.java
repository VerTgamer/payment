package ru.bank.app;

import lombok.Getter;
import ru.bank.app.exception.NumberAccountException;
import ru.bank.app.exception.NumberPhoneException;
import ru.bank.app.validation.NumberAccountValidation;
import ru.bank.app.validation.NumberPhoneValidation;
import ru.bank.command.CommandServerInput;
import ru.bank.command.GenerateIndenifier;
import ru.bank.server.BankServer;
import ru.bank.users.User;
import ru.bank.users.paymentAttributes.NumberAccount;

@Getter
public class WebApplication implements Application {
    GenerateIndenifier generateIndenifier = new GenerateIndenifier();

    public WebApplication() {
    }

    @Override
    public void makePaymentPhone(int transferAmount, String currencyMoney, User user, User client, BankServer bankServer) {
        NumberPhoneValidation numberPhoneValidation = new NumberPhoneValidation(client.getNumberPhone());
        NumberAccountValidation<NumberAccount> numberAccountValidation = new NumberAccountValidation<>(user.getNumberAccount());

        try {
            numberPhoneValidation.checkPrefix().checkLength().checkAllNumber();
            numberAccountValidation.checkLength().checkAllNumber();
            CommandServerInput commandServerInput = new CommandServerInput();
            commandServerInput.execute(transferAmount, currencyMoney, user, client, bankServer, generateIndenifier.generateIndenifier(user.getNumberPhone()));
        } catch (NumberPhoneException e) {
            System.out.println(e);
            System.out.println(e.getNumberPhone());
            throw e;
        } catch (NumberAccountException e) {
            System.out.println(e);
            System.out.println(e.getNumberAccount());
            throw e;
        }
    }
}