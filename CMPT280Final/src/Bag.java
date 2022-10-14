/*
Sunny Ka Patel
SDK438
11267665
CMPT280.Final.Q1
 */

import lib280.exception.NoCurrentItem280Exception;

public class Bag<I extends Comparable<? super I>> extends AVLTreeModified<I> {

    /**
     * Constructor for creating a new Bag ADT
     */
    public Bag() {
        new AVLTreeModified<I>();
    }

    /**
     * Gets the number of copies of t in the Bag
     * @param t the item for which the number of copies is to be found
     * @return the number of copies of t in the Bag. If t does not exists in the bag then return 0
     * @timing O(log n)
     */
    public int numberIn(I t) {
        // searches for t in the Bag
        this.searchModified(t);

        // if t does not exists in the Bag then return 0
        if (this.cur == null) {
            return 0;
        }
        // else return the number of copies of t
        else {
            return this.cur.getCount();
        }
    }

    /**
     * Adds t to the Bag
     * NOTE: If a duplicate is found then it increases the number of copies by 1
     * @param t the item to be added
     * @timing O(log n)
     */
    public  void add(I t) {
        this.insertMain(t);
    }

    /**
     * Removes t from the Bag
     * NOTE: If more than 1 copies of t are found then it decreases the number
     * of copies of t by 1 until it reaches 1 after which it deletes t completely from the Bag
     * @param t the item to be removed
     * @timing O(log n)
     */
    public void remove(I t) {
        // move the cursor to t
        this.searchModified(t);

        // delete the item at the cursor
        this.deleteItemModified();
    }

    /**
     * Returns a string representation of the Bag ADT
     * @return a string representation of the Bag ADT
     */
    @Override
    public String toString() {
        return this.toStringByLevel(1);
    }

    /**
     * Helper method to create a string representation by level of the Bag ADT
     * in which each item is shown with their number of copies in parentheses
     * @param i level in the Bag ADT
     * @return a string representation of the Bag ADT
     */
    @Override
    protected String toStringByLevel(int i)
    {
        // Code is mostly similar to the one in AVLTreeModified
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("            ");

        String result = new String();
        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
            result += rootRightSubtreeModified().toStringByLevel(i+1);

        result += "\n" + blanks + i + "/";
        if( !isEmpty() ) result += this.rootNode().getLtHeight() + "/" + this.rootNode().getRtHeight();
        result += ": ";
        if (isEmpty())
            result += "-";
        else
        {
            result += rootNodeModified().item();

            // Get the number of copies of each item and put them in parentheses
            result += "(" + rootNodeModified().getCount() + ")";
            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                result += rootLeftSubtreeModified().toStringByLevel(i+1);
        }
        return result;
    }

    // Regression testing
    public static void main(String[] args) {
        // create a new Bag ADT
        Bag<Integer> B = new Bag<>();

        // counter for failed test cases
        int failed = 0;

        System.out.println("<-------------- Testing Constructor -------------->");

        // printing the initial bag
        System.out.println(B);

        System.out.println("\n<-------------- Testing add() -------------->\n");

        System.out.println("-> Adding 1 x1");
        B.add(1);

        System.out.println("-> Adding 2 x2");
        B.add(2);
        B.add(2);

        System.out.println("-> Adding 3 x3");
        B.add(3);
        B.add(3);
        B.add(3);

        System.out.println("-> Adding 4 x4");
        B.add(4);
        B.add(4);
        B.add(4);
        B.add(4);

        System.out.println("-> Adding 5 x5");
        B.add(5);
        B.add(5);
        B.add(5);
        B.add(5);
        B.add(5);

        System.out.println("-> Adding 6 x6");
        B.add(6);
        B.add(6);
        B.add(6);
        B.add(6);
        B.add(6);
        B.add(6);

        System.out.println("-> Adding 7 x7");
        B.add(7);
        B.add(7);
        B.add(7);
        B.add(7);
        B.add(7);
        B.add(7);
        B.add(7);

        // printing the bag after adding all the items
        if (!B.isEmpty()) {
            System.out.println(B);
        } else {
            failed++;
        }

        System.out.println("\n<-------------- Testing numberIn() -------------->\n");

        // testing with valid cases

        if (B.numberIn(5) != 5) {
            failed++;
            System.out.println("ERROR: numberIn() failed!");
        } else {
            System.out.println("Number of copies of 5 in the bag are: " + B.numberIn(5));
        }

        if (B.numberIn(2) != 2) {
            failed++;
            System.out.println("ERROR: numberIn() failed!");
        } else {
            System.out.println("Number of copies of 2 in the bag are: " + B.numberIn(2));
        }

        if (B.numberIn(7) != 7) {
            failed++;
            System.out.println("ERROR: numberIn() failed!");
        } else {
            System.out.println("Number of copies of 7 in the bag are: " + B.numberIn(7));
        }

        // testing with invalid cases

        if (B.numberIn(20) == 0) {
            System.out.println("Number of copies of 20 in the bag are: " + B.numberIn(20));
        } else {
            failed++;
            System.out.println("ERROR: numberIn() failed!");
        }

        if (B.numberIn(25) == 0) {
            System.out.println("Number of copies of 15 in the bag are: " + B.numberIn(15));
        } else {
            failed++;
            System.out.println("ERROR: numberIn() failed!");
        }

        System.out.println("\n<-------------- Testing remove() -------------->\n");

        // testing with valid cases

        System.out.println("\n-> Removing 4...");
        B.remove(4);
        if (B.numberIn(4) == 4) {
            failed++;
            System.out.println("ERROR: remove() failed!");
        } else {
            System.out.println("-> Number of copies of 4 left in the bag are: " + B.numberIn(4));
            System.out.println(B);
        }

        System.out.println("\n-> Removing 6...");
        B.remove(6);
        if (B.numberIn(6) == 6) {
            failed++;
            System.out.println("ERROR: remove() failed!");
        } else {
            System.out.println("-> Number of copies of 6 left in the bag are: " + B.numberIn(6));
            System.out.println(B);
        }

        System.out.println("\n-> Removing 1...");
        B.remove(1);
        if (B.numberIn(1) == 1) {
            failed++;
            System.out.println("ERROR: remove() failed!");
        } else {
            System.out.println("-> Number of copies of 1 left in the bag are: " + B.numberIn(1));
            System.out.println(B);
        }

        // testing with invalid cases
        System.out.println("\n-> Removing 12...");
        try {
            B.remove(12);
        } catch (NoCurrentItem280Exception e) {
            System.out.println("Exception thrown! remove() passed.");
        }

        System.out.println("\n-> Removing 10...");
        try {
            B.remove(10);
        } catch (NoCurrentItem280Exception e) {
            System.out.println("Exception thrown! remove() passed.");
        }

        if (failed == 0) {
            System.out.println("\nRegression testing complete!");
        }
    }
}