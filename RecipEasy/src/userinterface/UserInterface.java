package userinterface;

import data.Recipe;
import java.io.*;

/*For Test*/
public class UserInterface {
	public static void main(String[] args) {
		String tmpDir = System.getProperty("java.io.tmpdir");
		String filename = tmpDir + "recipe1.ser";
		Recipe recipe1 = new Recipe("www.test.com");
		recipe1.setDetails("recipe1", "35", "10:5:00", "8", "Combine the fantastic flavors of lasagna and cheeseburgers in an ooey-gooey good casserole!");
		recipe1.addCategory("Dinner");
		recipe1.addDirection("Spray 13x9-inch (3-quart) baking dish with cooking spray. In 12-inch nonstick skillet, cook beef and onion over medium-high heat 5 to 7 minutes, stirring frequently, until beef is brown; drain. Stir in tomato sauce, water, ketchup and mustard. Simmer 5 minutes, stirring occasionally.");
		recipe1.addDirection("Meanwhile, in medium bowl, beat egg with fork or wire whisk. Stir in ricotta cheese and 2 cups of the cheese blend.");
		recipe1.addDirection("Spread 1 cup beef mixture over bottom of baking dish. Top with 4 uncooked noodles. Spread half of the ricotta mixture over noodles; top with 1 1/2 cups beef mixture. Repeat layers once with 4 noodles, remaining ricotta mixture and 1 1/2 cups beef mixture. Top with remaining 4 noodles, beef mixture and 1 cup Cheddar cheese. Cover with foil; refrigerate at least 8 hours or overnight.");
		recipe1.addDirection("Heat oven to 350°F. Bake lasagna, covered, 45 minutes. Uncover; bake 25 to 35 minutes longer or until bubbly. Remove from oven. Cover with foil; let stand 5 to 10 minutes before cutting.");
		recipe1.addDirection("Just before serving, top with lettuce, tomato and pickles. Serve with additional ketchup if desired.");
		recipe1.addTip("Uncooked noodles will bake to a slightly chewy texture. For softer texture, omit the water and use cooked noodles.");
		recipe1.addTip("Although this recipe is super to make ahead, you can bake it right away, too. Just cover the dish with foil and bake as directed.");
		recipe1.addIngredient("1 1/2 lb lean(at least 80%) ground beef");
		recipe1.addIngredient("3 tablespoons instant minced onion");
		recipe1.addIngredient("1 can (15 oz) tomato sauce");
		recipe1.addIngredient("1 1/2 cups water");
		recipe1.setNutritionInfo("1", "570", "270", "29g", "16g", "1g", "140mg", "1050mg", "38g", "3g", "9g", "38g", "25%", "8%", "40%", "20%");
		recipe1.setID(1000);
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(recipe1);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in " + filename);
		} catch (IOException i) {
			i.printStackTrace();
		}
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			recipe1 = (Recipe) in.readObject();
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
