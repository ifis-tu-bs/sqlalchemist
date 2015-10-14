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


          ShopItemDAO.create(AvatarDAO.getByName("Mad Skipping Al"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Fresh D!"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Art Weaslby"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Ching Chang Cho"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Colin Creepy"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Crabbe and Sac"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Dolly"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Drag'o Femalfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Wilius Philwick"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Tour de la Flur"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("George and Michael"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("George and Michael Uniform"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Lockroy Gildehart"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Tonic Weaslby"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Hairy P."), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Hairy P. 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Hermene Mine Moe"), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Hermene Mine Moe 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Borace Jelzhorn"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Usain Shacklebolt"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Student 72"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Lucy Femalfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Lurra Loveburg"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Mimerma Mcmomamall"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("The Toilet Ghost"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Ms. Weaslby"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Mr. Beans"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Narziss Femalfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Neville Schlongbottom"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Nymphadora Tongues"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Padvatima"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Mongo Oma"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Wolfgang Wolf"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Jon Weaslby"), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Bon Weaslby"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Hoga"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Seamus Gallagher"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Serverus Snapy"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Sloop Dog"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Sybrille Trelawney"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Voldemotherfucker"), 20000);

          ShopItemDAO.create("Belt Slot 1", "url",  1000);
          ShopItemDAO.create("Belt Slot 2", "url",  2000);
          ShopItemDAO.create("Belt Slot 3", "url",  4000);
          ShopItemDAO.create("Belt Slot 4", "url",  8000);
          ShopItemDAO.create("Belt Slot 5", "url", 16000);

          Logger.info("Done initializing");
      }
}
