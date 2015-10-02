package bootstrap;

import dao.TaskFileDAO;

import Exception.SQLAlchemistException;

import play.Logger;

public class TaskFileBootstrap {
  //////////////////////////////////////////////////
  //  init method
  //////////////////////////////////////////////////

      /**
       * This method fill the database with initial data
       */
      public static void init()
      {
          Logger.info("TaskFile.init - Initialize data");


          String xml =
                  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                          "\n" +
                          "<tasks schemaversion=\"1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"recipe.xsd\">\n" +
                          "\n" +
                          "  <task taskid=\"exercises-wise13\" querylanguage=\"SQL\">\n" +
                          "    \n" +
                          "    <titles>\n" +
                          "      <title language=\"EN\">Super Heros</title>\n" +
                          "    </titles>\n" +
                          "    \n" +
                          "    <flufftexts>\n" +
                          "      <flufftext language=\"EN\">Super Heros und Super Villains.</flufftext>\n" +
                          "    </flufftexts>\n" +
                          "    \n" +
                          "    <schema>\n" +
                          "      <relation>\n" +
                          "        <intension>\n" +
                          "          CREATE TABLE Comic(\n" +
                          "            no INTEGER NOT NULL PRIMARY KEY,\n" +
                          "            title VARCHAR(255) NOT NULL,\n" +
                          "            pages INTEGER,\n" +
                          "            publisher VARCHAR(255)\n" +
                          "          );\n" +
                          "        </intension>\n" +
                          "        <extension>\n" +
                          "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (12, 'Wonderboy''s Comeback', 'Marvel');</tuple>\n" +
                          "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (13, 'Superman Returns', 'DC');</tuple>\n" +
                          "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (15, 'The Revenge of Young Nastyman', 'Marvel');</tuple>\n" +
                          "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (18, 'Wonderboy Rocks', 'Marvel');</tuple>\n" +
                          "          <tuple>INSERT INTO Comic(no, title, publisher) VALUES (21, 'Young Nastyman''s Revenge', 'Marvel');</tuple>\n" +
                          "        </extension>\n" +
                          "      </relation>\n" +
                          "      <relation>\n" +
                          "        <intension>\n" +
                          "          CREATE TABLE PoweredPerson(\n" +
                          "            alias VARCHAR(255) NOT NULL PRIMARY KEY,\n" +
                          "            firstname VARCHAR(100) NOT NULL,\n" +
                          "            lastname VARCHAR(100),\n" +
                          "            evilness_level INTEGER\n" +
                          "          );\n" +
                          "        </intension>\n" +
                          "        <extension>\n" +
                          "          <tuple>INSERT INTO PoweredPerson VALUES ('Wonderboy', 'KG', NULL, -25);</tuple>\n" +
                          "          <tuple>INSERT INTO PoweredPerson VALUES ('Magneto', 'Erik Magnus', 'Lensherr', 46);</tuple>\n" +
                          "          <tuple>INSERT INTO PoweredPerson VALUES ('Superman', 'Clark', 'Kent', -63);</tuple>\n" +
                          "          <tuple>INSERT INTO PoweredPerson VALUES ('Professor X', 'Charles', 'Xavier', -41);</tuple>\n" +
                          "          <tuple>INSERT INTO PoweredPerson VALUES ('Young Nastyman', 'JB', NULL, 39);</tuple>\n" +
                          "          <tuple>INSERT INTO PoweredPerson VALUES ('Mystique', 'Raven', 'Darkholme', 43);</tuple>\n" +
                          "        </extension>\n" +
                          "      </relation>\n" +
                          "      <relation>\n" +
                          "        <intension>\n" +
                          "          CREATE TABLE HasPowerSince (\n" +
                          "            comic INTEGER NOT NULL REFERENCES Comic(no),\n" +
                          "            alias VARCHAR(255) NOT NULL REFERENCES PoweredPerson (alias),\n" +
                          "            power VARCHAR(100) NOT NULL,\n" +
                          "            PRIMARY KEY (comic, alias, power)\n" +
                          "          );\n" +
                          "        </intension>\n" +
                          "        <extension>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (13, 'Young Nastyman', 'Flight');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (12, 'Professor X', 'Telepathy');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (15, 'Mystique', 'Shapeshifting');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (13, 'Superman', 'Telepathy');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (18, 'Wonderboy', 'Telekinesis');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (12, 'Magneto', 'Magnetism');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (21, 'Young Nastyman', 'Mind Bullets');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (21, 'Superman', 'Flight');</tuple>\n" +
                          "          <tuple>INSERT INTO HasPowerSince VALUES (12, 'Wonderboy', 'Levitation');</tuple>\n" +
                          "        </extension>\n" +
                          "      </relation>\n" +
                          "      <relation>\n" +
                          "        <intension>\n" +
                          "          CREATE TABLE SuperHero(\n" +
                          "            alias VARCHAR(255) NOT NULL REFERENCES PoweredPerson(alias),\n" +
                          "            cape BOOLEAN,\n" +
                          "            PRIMARY KEY(alias)\n" +
                          "          );\n" +
                          "        </intension>\n" +
                          "        <extension>\n" +
                          "          <tuple>INSERT INTO SuperHero VALUES ('Wonderboy', NULL);</tuple>\n" +
                          "          <tuple>INSERT INTO SuperHero VALUES ('Superman', TRUE);</tuple>\n" +
                          "          <tuple>INSERT INTO SuperHero VALUES ('Professor X', FALSE);</tuple>\n" +
                          "        </extension>\n" +
                          "      </relation>\n" +
                          "      <relation>\n" +
                          "        <intension>\n" +
                          "          CREATE TABLE SuperVillain(\n" +
                          "            alias VARCHAR(255) NOT NULL REFERENCES PoweredPerson(alias),\n" +
                          "            PRIMARY KEY(alias)\n" +
                          "          );\n" +
                          "        </intension>\n" +
                          "        <extension>\n" +
                          "          <tuple>INSERT INTO SuperVillain VALUES ('Magneto');</tuple>\n" +
                          "          <tuple>INSERT INTO SuperVillain VALUES ('Mystique');</tuple>\n" +
                          "          <tuple>INSERT INTO SuperVillain VALUES ('Young Nastyman');</tuple>\n" +
                          "        </extension>\n" +
                          "      </relation>\n" +
                          "    </schema>\n" +
                          "    \n" +
                          "    <subtasks>\n" +
                          "      \n" +
                          "      <subtask>\n" +
                          "      <subtaskid>1</subtaskid>" +
                          "        <tasktexts>\n" +
                          "          <tasktext language=\"EN\">The super villain table is an excerpt of the powered person table. Provide a query on the powered person table that reproduces the super villain table.</tasktext>\n" +
                          "        </tasktexts>\n" +
                          "        <solution>\n" +
                          "          <referencestatement>\n" +
                          "            SELECT alias\n" +
                          "            FROM PoweredPerson\n" +
                          "            WHERE evilness_level > 0;\n" +
                          "          </referencestatement>\n" +
                          "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                          "        </solution>\n" +
                          "        <points>1</points>\n" +
                          "      </subtask>\n" +
                          "      \n" +
                          "      <subtask>\n" +
                          "      <subtaskid>2</subtaskid>" +
                          "        <tasktexts>\n" +
                          "          <tasktext language=\"EN\">A query for all super heroes having no cape.</tasktext>\n" +
                          "        </tasktexts>\n" +
                          "        <solution>\n" +
                          "          <referencestatement>\n" +
                          "            SELECT *\n" +
                          "            FROM SuperHero\n" +
                          "            WHERE cape = 'FALSE';\n" +
                          "          </referencestatement>\n" +
                          "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                          "        </solution>\n" +
                          "        <points>1</points>\n" +
                          "      </subtask>\n" +
                          "      \n" +
                          "      <subtask>\n" +
                          "      <subtaskid>3</subtaskid>" +
                          "        <tasktexts>\n" +
                          "          <tasktext language=\"EN\">A query for the names of all powers of Young Nastyman and the number of the comic they first appeared in, ordered by their appearance (newest first). If two powers appeared at the same time, sort them alphabetically by their name.</tasktext>\n" +
                          "        </tasktexts>\n" +
                          "        <solution>\n" +
                          "          <referencestatement>\n" +
                          "            SELECT power, comic\n" +
                          "            FROM HasPowerSince\n" +
                          "            WHERE alias = 'Young Nastyman'\n" +
                          "            ORDER BY comic DESC, power ASC;\n" +
                          "          </referencestatement>\n" +
                          "          <evaluationstrategy>LIST</evaluationstrategy>\n" +
                          "        </solution>\n" +
                          "        <requiredterms>\n" +
                          "          <term>ORDER BY</term>\n" +
                          "        </requiredterms>\n" +
                          "        <points>2</points>\n" +
                          "      </subtask>\n" +
                          "      \n" +
                          "      <subtask>\n" +
                          "      <subtaskid>4</subtaskid>" +
                          "        <tasktexts>\n" +
                          "          <tasktext language=\"EN\">A query for the number of pages I would have to read if I wanted to read all Marvel comics in the database.</tasktext>\n" +
                          "        </tasktexts>\n" +
                          "        <solution>\n" +
                          "          <referencestatement>\n" +
                          "            SELECT SUM(pages) AS sum\n" +
                          "            FROM comic\n" +
                          "            WHERE publisher = 'Marvel';\n" +
                          "          </referencestatement>\n" +
                          "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                          "        </solution>\n" +
                          "        <requiredterms>\n" +
                          "          <term>SUM</term>\n" +
                          "        </requiredterms>\n" +
                          "        <points>2</points>\n" +
                          "      </subtask>\n" +
                          "      \n" +
                          "      <subtask>\n" +
                          "      <subtaskid>5</subtaskid>" +
                          "        <tasktexts>\n" +
                          "          <tasktext language=\"EN\">A query that states if the average powered person is evil (evilness > 0) or good (evilness = 0), depending on all powered persons evilness levels.</tasktext>\n" +
                          "        </tasktexts>\n" +
                          "        <solution>\n" +
                          "          <referencestatement>\n" +
                          "            SELECT AVG(evilness_level)\n" +
                          "            FROM PoweredPerson;\n" +
                          "          </referencestatement>\n" +
                          "          <evaluationstrategy>SET</evaluationstrategy>\n" +
                          "        </solution>\n" +
                          "        <points>2</points>\n" +
                          "      </subtask>\n" +
                          "      \n" +
                          "      \n" +
                          "      \n" +
                          "    </subtasks>\n" +
                          "    \n" +
                          "  </task>\n" +
                          "  \n" +
                          "</tasks>";

          try {
              TaskFileDAO.create(null, xml, false);
          } catch (SQLAlchemistException e) {
              Logger.info(e.getMessage());
          }

          Logger.info("TaskFile.init - Done initializing");
      }
}
