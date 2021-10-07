package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class DbOperations {

    EntityManagerFactory emf= Persistence.createEntityManagerFactory("KrishnanSDB");
    EntityManager em= emf.createEntityManager();

    public Account createAccount(String fname,String lname,String ifsc, String pan,String city,long accno,long aadhar,long pno){

        Account error = new Account();
        Account f = em.find(Account.class, accno);
        if (f != null) {
            System.out.println("Account already exists");
            error.setFirst_name("Account already exists");
            return error;
        }
        long balance=0;
        Account a = new Account();
        a.setAccount_number(accno);
        a.setFirst_name(fname);
        a.setLast_name(lname);
        a.setCity(city);
        a.setAadharnumber(aadhar);
        a.setPan_number(pan);
        a.setPhone_number(pno);
        a.setIfsc(ifsc);
        a.setBalance(balance);

        em.getTransaction().begin();
        em.persist(a);
        em.getTransaction().commit();

        return a;
    }

    public Transaction Transact(String dorw,long account_number,long amount){

        Transaction error= new Transaction();
        long accbal=0;
        Account f = em.find(Account.class, account_number);
        if (f == null) {
            System.out.println("Account number doesn't exists");
            error.setTransaction_type("Account number doesn't exists");
            return error;
        }
        accbal= f.getBalance();
        if(dorw.equals("deposit")){
            accbal+=amount;
        }
        else{
            if(accbal-amount < 0){
                System.out.println("Not enough amount");
                error.setTransaction_type("Not enough amount");
                return error;
            }
            accbal-=amount;
        }
        f.setBalance(accbal);
        Transaction t = new Transaction();
        t.setAccount_number(account_number);
        t.setAmount(amount);
        t.setTransaction_type(dorw);
        t.setTransaction_status("success");
        t.setTransaction_date(LocalDate.now());

        em.getTransaction().begin();
        em.persist(t);
        em.persist(f);
        em.getTransaction().commit();
        return t;


    }

    public Account createjsn(Account a){
        em.getTransaction().begin();
        em.persist(a);
        em.getTransaction().commit();
        return a;

    }

    public List<Transaction> passbook(long account_number){
        return em.createQuery("Select t from Transaction t where t.account_number="+Long.toString(account_number)+"and t.transaction_date between 2021-10-04 and 2021-10-07").getResultList();
    }


}
