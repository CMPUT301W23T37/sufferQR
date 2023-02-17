package com.example.sufferqr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateUniqueNameTest {

//    @Test
//    public void createWord() {
//        CreateUniqueName cu = new CreateUniqueName();
//        String newname = cu.getRandomUniqueString();
//        System.out.println(newname);
//        // check if sucessfully found unique
//        //assertEquals(true,cu.CheckUnique(newname));
//    }

    public void UniqueTest(){
        CreateUniqueName cu = new CreateUniqueName();
//        assertEquals(false,cu.CheckUnique("example"));
//        assertEquals(true,cu.CheckUnique("awekljsrhfgjksfdgklsfdjhgioredmjghusfydrtghjd"));
    }

//    @Test
//    public void AddInWordAbility()  {
//        CreateUniqueName cu = new CreateUniqueName();
//        boolean fail = false;
//        try {
//            cu.UpdateRandomWord("/data/data/com.example.sufferqr/cache/wordlist.txt",50);
//        } catch (IOException e) {
//            fail = true;
//        }
//        assertEquals(false,fail);
//    }
}
