package bootstrap;

import dao.AvatarDAO;
import dao.ShopItemDAO;

import play.Logger;

public class ShopItemBootstrap {


  //////////////////////////////////////////////////
  //  Init Method
  //////////////////////////////////////////////////

      /**
       *n Init method
       */
      public static void init() {
          Logger.info("Initialize 'ShopItem' data");


          ShopItemDAO.create(AvatarDAO.getByName("Alastor Moody"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Albus Dumbledore"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Arthur Weasley"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Cho Chang"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Colin Creevey"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Crabbe and Goyle"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Dolores Umbridge"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Draco Malfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Filius Flitwick"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Fleur Delacour"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Fred and George"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Fred and George Uniform"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Gilderoy Lockhart"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Ginny Weasley"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Harry Potter"), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Harry Potter 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Hermione Granger"), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Hermione Granger 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Horace Slughorn"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Kingsley Shacklebolt"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Lavender Brown"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Lucius Malfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Luna Lovegood"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Minerva McGonagall"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Moaning Myrtle"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Molly Weasley"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Mr. Binns"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Narcissa Malfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Neville Longbottom"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Nymphadora Tonks"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Padma and Parvati"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Pomona Sprout"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Remus Lupin"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Ron Weasley"), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Ron Weasley 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Rubeus Hagrid"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Seamus Finnigan"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Severus Snape"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Sirius Black"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Sybill Trelawney"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Voldemort"), 20000);

          ShopItemDAO.create("Belt Slot 1", "url",  1000);
          ShopItemDAO.create("Belt Slot 2", "url",  2000);
          ShopItemDAO.create("Belt Slot 3", "url",  4000);
          ShopItemDAO.create("Belt Slot 4", "url",  8000);
          ShopItemDAO.create("Belt Slot 5", "url", 16000);

          Logger.info("Done initializing");
      }
}
