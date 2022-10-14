// Sunny Ka Patel
// SDK438
// 11267665
// CMPT280.A3

import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;

public class SkillTree extends BasicMAryTree280<Skill> {

	/**	
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.  
	 * @timing O(1) 
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes 
	 */
	public SkillTree(Skill x, int m)
	{
		super(x,m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 * 
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTree rootSubTree(int i) {
		return (SkillTree)super.rootSubtree(i);
	}

	/**
	 * gets a list of all the skills needed to unlock the given skill
	 * @param skillName name of the skill for which need to find the dependencies
	 * @return a list of all the skill required to unlock given skill
	 * NOTE skillName included
	 */
	public LinkedList280<Skill> skillDependencies(String skillName) {
		LinkedList280<Skill> list = new LinkedList280<>();
		if (depthSearch(this, skillName, list)) {
			return list;
		} else {
			throw new RuntimeException("Given skill not found!");
		}
	}

	/**
	 * a helper method to do a depth-first search on the given tree to find the given skill
	 * @param tree tree to be traversed on
	 * @param skillName name of the skill to find
	 * @param list a list of all the skill required to unlock given skill
	 * @return true if the given skill is found in the given tree; false otherwise
	 */
	private boolean depthSearch(BasicMAryTree280<Skill> tree, String skillName, LinkedList280<Skill> list) {
		if (tree.isEmpty()) {
			return false;
		}

		if (tree.rootItem().getSkillName().compareTo(skillName) == 0) {
			list.insert(tree.rootItem());
			return true;
		}

		for (int i = 1; i <= this.rootLastNonEmptyChild(); i++) {
			if (depthSearch(tree.rootSubtree(i), skillName, list)) {
				list.insert(tree.rootItem());
				return true;
			}
		}

		return false;
	}

	/**
	 * gets the required number of skill points to use the given skill
	 * @param skillName name of the skill
	 * @return the total cost to unlock the given skill
	 */
	public int skillTotalCost(String skillName) {
		int totalCost = 0;
		LinkedList280<Skill> list = new LinkedList280<>();
		list = skillDependencies(skillName);
		list.goFirst();
		while (list.itemExists()) {
			totalCost += list.item().getSkillCost();
			list.goForth();
		}
		return totalCost;
	}

	public static void main(String[] args) {
		// all the skills
		Skill sword = new Skill("Sword Attack", "Learn various powerful sword attacks!", 2);
		Skill strike = new Skill("Strike", "Strike the enemy down!", 3);
		Skill slash = new Skill("Slash", "Slash your enemies!", 3);
		Skill magicalAttack = new Skill("Magical Attack", "Kill your enemies with magical powers!", 4);
		Skill strike1 = new Skill("Lightning Strike", "Strikes the sword lightning fast and stab the enemy for a heavy blow.", 4);
		Skill lightning2 = new Skill("Lightning Descent", "A heavy ranged attack that involves kicking off the ground with full force and then stabbing the ground with the sword, shooting lightning in all direction.", 7);
		Skill strike2 = new Skill("Whirlwind", "A spinning strike.", 3);
		Skill strike3 = new Skill("Double Cleave", "A double-strike attack skill.", 3);
		Skill slash1 = new Skill("Shock Wave", "Slashes the sword from a distance to do physical damage to enemies by the shockwave.", 4);
		Skill slash2 = new Skill("Horizontal Arc", "A two part skill where you slash the sword first from left-to-right and then from right-to-left.", 5);
		Skill slash3 = new Skill("7 Deadly Sins", "A seven hit skill involving various slashes.", 7);
		Skill petrify = new Skill("Petrify", "Stop enemy mobility for a brief amount of time.", 4);
		Skill petrify1 = new Skill("Wrath of the Frost", "Freeze your enemies for 3 seconds.", 5);
		Skill petrify2 = new Skill("Curse of Medusa", "Turn your enemies into stone for eternity.", 6);

		// create a new tree with sword skills as the head with a max of 3 children
		SkillTree T = new SkillTree(sword, 3);
		T.setRootSubtree(new BasicMAryTree280<>(strike, 3), 1);
		T.setRootSubtree(new BasicMAryTree280<>(slash, 3), 2);
		T.setRootSubtree(new BasicMAryTree280<>(magicalAttack, 3), 3);

		// get the subRootTree of the Strike skill and set its children
		BasicMAryTree280<Skill> strikeSkills = T.rootSubTree(1);
		strikeSkills.setRootSubtree(new BasicMAryTree280<>(strike1, 3), 1);
		strikeSkills.setRootSubtree(new BasicMAryTree280<>(strike2, 3), 2);
		strikeSkills.setRootSubtree(new BasicMAryTree280<>(strike3, 3), 3);

		// get the subRootTree of Lightning Strike and set its children
		BasicMAryTree280<Skill> lightningUpgrade = strikeSkills.rootSubtree(1);
		lightningUpgrade.setRootSubtree(new BasicMAryTree280<>(lightning2, 3), 1);

		// get the subRootTree of the Slash skill and set its children
		BasicMAryTree280<Skill> slashSkills = T.rootSubTree(2);
		slashSkills.setRootSubtree(new BasicMAryTree280<>(slash1, 3), 1);
		slashSkills.setRootSubtree(new BasicMAryTree280<>(slash2, 3), 2);
		slashSkills.setRootSubtree(new BasicMAryTree280<>(slash3, 3), 3);

		// get the subRootTree of Magical Attack and set its children
		BasicMAryTree280<Skill> magicAttackSkills = T.rootSubTree(3);
		magicAttackSkills.setRootSubtree(new BasicMAryTree280<>(petrify, 3), 1);

		// get the subRootTree of Petrify and set its children
		BasicMAryTree280<Skill> petrifySkills = magicAttackSkills.rootSubtree(1);
		petrifySkills.setRootSubtree(new BasicMAryTree280<>(petrify1, 3), 1);
		petrifySkills.setRootSubtree(new BasicMAryTree280<>(petrify2, 3), 2);

		// print tree by level
		System.out.println(T.toStringByLevel());

		// testing skillDependencies for genuine cases
		System.out.println("Dependencies for Lightning Descent: ");
		System.out.println(T.skillDependencies("Lightning Descent"));

		System.out.println("Dependencies for Curse of Medusa: ");
		System.out.println(T.skillDependencies("Curse of Medusa"));

		System.out.println("Dependencies for Magical Attack: ");
		System.out.println(T.skillDependencies("Magical Attack"));

		// testing skillDependencies for incorrect case
		try {
			System.out.println("Dependencies for NoSuch Skill");
			System.out.println(T.skillDependencies("NoSuch Skill"));
		} catch (RuntimeException e) {
			System.out.println("NoSuch Skill not found!");
		}

		// testing skillTotalCost for genuine cases
		int wrathOfTheFrostCost = T.skillTotalCost("Wrath of the Frost");
		System.out.println("To get Wrath of the Frost you must invest " + wrathOfTheFrostCost + " points.");

		int shockWaveCost = T.skillTotalCost("Shock Wave");
		System.out.println("To get Shock Wave you must invest " + shockWaveCost + " points.");

		int strikeCost = T.skillTotalCost("Strike");
		System.out.println("To get Strike you must invest " + strikeCost + " points.");

		// testing skillTotalCost for incorrect case
		try {
			int noSuchSkillCost = T.skillTotalCost("NoSuch Skill");
			System.out.println("To get NoSuch Skill you must invest " + noSuchSkillCost + " points.");
		} catch (RuntimeException e) {
			System.out.println("NoSuch Skill not found!");
		}
	}

}
