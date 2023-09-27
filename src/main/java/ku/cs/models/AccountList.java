package ku.cs.models;

import java.util.ArrayList;
import java.util.Comparator;


public class AccountList {
    private ArrayList<Account> accountList;
    public AccountList(){

        accountList = new ArrayList<>();
    }
    public void addAccount(Account user){
        accountList.add(user);
    }
    public boolean loginSuccess(String username,String password){
        Account account = searchAccountByUsername(username);
        if (account != null  )
            return account.loginSuccess(username,password);
        return false;
    }
    public boolean usernameIsUsed(String username){
        Account account = searchAccountByUsername(username);
        if (account != null )
            return true;
        return false;
    }
    public Account searchAccountByUsername(String username){
        for (Account account: accountList)
            if (account.checkAccount(username))
                return account;
        return null;
    }
    public ArrayList<Account> getAllAccount(){return accountList;}
    public boolean changePasswordByUsername(String username, String newPassword){
        Account account = searchAccountByUsername(username);
        account.changePassword(newPassword);
        return true;
    }
    public ArrayList<Account> sortByTime(){
        ArrayList<Account> temp = accountList;
        temp.sort(new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {

                if (o1.getTime().isBefore(o2.getTime())) return 1;
                if (o1.getTime().isAfter(o2.getTime())) return -1;
                return 0;
            }
        });
        return temp;
    }
}

