// Sunny Ka Patel
// SDK438
// 11267665
// CMPT 280.A5

import com.opencsv.CSVReader;
import lib280.base.Pair280;
import lib280.hashtable.KeyedChainedHashTable280;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;
import lib280.tree.OrderedSimpleTree280;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

// This project uses a JAR called opencsv which is a library for reading and
// writing CSV (comma-separated value) files.
// 
// You don't need to do this for this project, because it's already done, but
// if you want to use opencsv in other projects on your own, here's the process:
//
// 1. Download opencsv-3.1.jar from http://sourceforge.net/projects/opencsv/
// 2. Drag opencsv-3.1.jar into your project.
// 3. Right-click on the project in the package explorer, select "Properties" (at bottom of popup menu)
// 4. Choose the "Libraries" tab
// 5. Click "Add JARs"
// 6. Select the opencsv-3.1.jar from within your project from the list.
// 7. At the top of your .java file add the following imports:
//        import java.io.FileReader;
//        import com.opencsv.CSVReader;
//
// Reference documentation for opencsv is here:  
// http://opencsv.sourceforge.net/apidocs/overview-summary.html


public class QuestLog extends KeyedChainedHashTable280<String, QuestLogEntry> {

    public QuestLog() {
        super();
    }

    /**
     * Obtain an array of the keys (quest names) from the quest log.  There is
     * no expectation of any particular ordering of the keys.
     *
     * @return The array of keys (quest names) from the quest log.
     */
    public String[] keys() {
        // create a new String array for storing the keys
        String[] keysList = new String[this.count()];

        // move the cursor to the first element
        this.goFirst();

        int i = 0;
        // while item exists...
        while (this.itemExists()) {
            // get the current item's key and store it in the key list
            keysList[i] = this.itemKey();

            // move the cursor to the next element
            this.goForth();
            i++;
        }
        // sort the keys in the keysList
        Arrays.sort(keysList);
        return keysList;
    }

    /**
     * Format the quest log as a string which displays the quests in the log in
     * alphabetical order by name.
     *
     * @return A nicely formatted quest log.
     */
    public String toString() {
        // get the quest names in a string array
        String[] quests = this.keys();
        String result = "";

        // obtain the quest's details and concat it to the result
        for (String quest : quests) {
            result += this.obtain(quest).toString() + "\n";
        }
        return result;
    }

    /**
     * Obtain the quest with name k, while simultaneously returning the number of
     * items examined while searching for the quest.
     *
     * @param k Name of the quest to obtain.
     * @return A pair in which the first item is the QuestLogEntry for the quest named k, and the
     * second item is the number of items examined during the search for the quest named k.
     * Note: if no quest named k is found, then the first item of the pair should be null.
     */
    public Pair280<QuestLogEntry, Integer> obtainWithCount(String k) {
        // get the linked-list's index of the quest's name from the hashArray
        int hashIndex = this.hashPos(k);
        int numOfItemsExamined = 0;

        // get the linked-list stored in the hashArray at the given quest name's index
        LinkedList280<QuestLogEntry> hashList = this.hashArray[hashIndex];

        // examined 1 item so +1
        numOfItemsExamined++;

        // case1: if the found linked-list is null then return a pair of null and 0
        if (hashList == null) {
            return new Pair280<>(null, 0);
        }

        // case2: if not null then create an iterator and set it to the head
        LinkedIterator280<QuestLogEntry> iter = hashList.iterator();
        iter.goFirst();

        // search through the linked-list till not found and increment the number of items examined
        while (iter.itemExists() && iter.item().key().compareTo(k) != 0) {
            numOfItemsExamined++;
            iter.goForth();
        }
        // return the pair of the quest and the number of items examined when found
        return new Pair280<>(iter.item(), numOfItemsExamined);
    }


    public static void main(String args[]) {
        // Make a new Quest Log
        QuestLog hashQuestLog = new QuestLog();

        // Make a new ordered binary lib280.tree.
        OrderedSimpleTree280<QuestLogEntry> treeQuestLog =
                new OrderedSimpleTree280<QuestLogEntry>();


        // Read the quest data from a CSV (comma-separated value) file.
        // To change the file read in, edit the argument to the FileReader constructor.
        CSVReader inFile;
        try {
            //input filename on the next line - path must be relative to the working directory reported above.
            inFile = new CSVReader(new FileReader("QuestLog-Template/quests4.csv"));
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            return;
        }

        String[] nextQuest;
        try {
            // Read a row of data from the CSV file
            while ((nextQuest = inFile.readNext()) != null) {
                // If the read succeeded, nextQuest is an array of strings containing the data from
                // each field in a row of the CSV file.  The first field is the quest name,
                // the second field is the quest region, and the next two are the recommended
                // minimum and maximum level, which we convert to integers before passing them to the
                // constructor of a QuestLogEntry object.
                QuestLogEntry newEntry = new QuestLogEntry(nextQuest[0], nextQuest[1],
                        Integer.parseInt(nextQuest[2]), Integer.parseInt(nextQuest[3]));
                // Insert the new quest log entry into the quest log.
                hashQuestLog.insert(newEntry);
                treeQuestLog.insert(newEntry);
            }
        } catch (IOException e) {
            System.out.println("Something bad happened while reading the quest information.");
            e.printStackTrace();
        }

        // Print out the hashed quest log's quests in alphabetical order.
        // COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
        System.out.println(hashQuestLog);

        // Print out the lib280.tree quest log's quests in alphabetical order.
        // COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
        System.out.println(treeQuestLog.toStringInorder());


        // get all the keys of the hashQuestLog
        String[] hashQuestKeys = hashQuestLog.keys();

        // a variable to store the number of items examined
        double numOfItemsExamined = 0.0;
        // the total number of items in the hashQuestLog
        int numOfItems = hashQuestKeys.length;

        for (String x : hashQuestKeys) {
            System.out.println(hashQuestLog.obtainWithCount(x));
        }

        // run hashQuestLog.obtainWithCount on each item in the hashQuestLog and
        // add each item's number of item examined
        for (String key : hashQuestKeys) {
            numOfItemsExamined += hashQuestLog.obtainWithCount(key).secondItem();
        }
        // get the average number of items examined in the hashed quest log
        double hashAverage = numOfItemsExamined / numOfItems;
        System.out.println("Avg. # of items examined per query in the hashed quest log with " + hashQuestLog.count() + " entries: " + hashAverage);


        // run treeLogQuest.searchCount() on each item of the treeQuestLog and
        // add each item's number of items examined
        numOfItemsExamined = 0.0;
        for (String key : hashQuestKeys) {
            numOfItemsExamined += treeQuestLog.searchCount(hashQuestLog.obtain(key));
        }
        // get the average number of items examined in the tree quest log
        // NOTE: For treeQuestLog, I am assuming that the number of items in the tree is same as the number of items in the hashQuestLog
        double treeAverage = numOfItemsExamined / numOfItems;
        System.out.println("Avg. # of items examined per query in the tree quest log with " + hashQuestLog.count() + " entries: " + treeAverage);
    }


}
