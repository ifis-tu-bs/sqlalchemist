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


          ShopItemDAO.create(AvatarDAO.getByName("Skipping Al"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Fresh D!"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Art Weasleby"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Ching Chang Cho"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Colin Creepy"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Crabbe and Crabber"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Dolly P."), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Draco Femalfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Wilius Philwick"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Tour de la Flur"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("James and Oliver"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("James and Oliver Uniform"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Capt. Cockhard"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Tonic Weasleby"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Hairy P."), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Hairy P. 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Hermene Mine Moe"), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Hermene Mine Moe 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Borace Jelzhorn"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Usain Shacklebolt"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Student 72"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Lucy Femalfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Lurra Ladybug"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Ms. McKittycat"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Grieving Grace"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Mother Weasleby"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Mr. Beans"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Narziss Femalfoy"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Neville Strongbottom"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Nymphadora Tongues"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Party Patil"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Brussela Kraut"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Wolfgang Wolf"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Jon Weasleby"), 0); //Bereits im Besitz des Benutzers
          ShopItemDAO.create(AvatarDAO.getByName("Bon Weasleby 2"), 8000);
          ShopItemDAO.create(AvatarDAO.getByName("Hoga"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Seamus Gallagher"), 500);
          ShopItemDAO.create(AvatarDAO.getByName("Serverus Snoopy"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Sloop Dog"), 20000);
          ShopItemDAO.create(AvatarDAO.getByName("Sybrille Truehorny"), 2000);
          ShopItemDAO.create(AvatarDAO.getByName("Vollderlord"), 20000);

          ShopItemDAO.create("Belt Slot 1", "url",  1000);
          ShopItemDAO.create("Belt Slot 2", "url",  2000);
          ShopItemDAO.create("Belt Slot 3", "url",  4000);
          ShopItemDAO.create("Belt Slot 4", "url",  8000);
          ShopItemDAO.create("Belt Slot 5", "url", 16000);

          Logger.info("Done initializing");
      }
}
