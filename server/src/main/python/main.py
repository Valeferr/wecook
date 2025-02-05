import random
import asyncio
import aiohttp
import base64
import os

API_URL = "http://localhost:8080/wecook"
IMAGE_FOLDER = "./images/food"
NUM_RECIPES = 10

# Random data
MEASUREMENTS_UNITS = {
    "MEAT": ["GRAM", "KILOGRAM", "QUANTITY"],
    "FISH": ["GRAM", "KILOGRAM", "QUANTITY"],
    "VEGETABLES": ["GRAM", "KILOGRAM", "QUANTITY", "CUP"],
    "FRUIT": ["GRAM", "KILOGRAM", "QUANTITY", "CUP"],
    "CEREALS": ["GRAM", "KILOGRAM", "CUP"],
    "DAIRY": ["MILLILITER", "LITER", "GRAM", "KILOGRAM"],
    "LEGUMES": ["GRAM", "KILOGRAM", "CUP"],
    "CONDIMENTS": ["TEASPOON", "TABLESPOON", "PINCH", "DASH"],
    "EGGS": ["QUANTITY"],
    "SPICES": ["TEASPOON", "TABLESPOON", "PINCH", "DASH"],
    "BEVERAGES": ["MILLILITER", "LITER", "CENTILITER"],
    "OTHER": ["GRAM", "MILLILITER", "QUANTITY"]
}

FOOD_TYPES = list(MEASUREMENTS_UNITS.keys())

INGREDIENTS = [
    "Chicken", "Beef", "Salmon", "Carrot", "Apple", "Rice", "Milk", "Lentils", "Salt", "Egg", "Cinnamon", "Coffee", "Sugar",
    "Pasta", "Tomato", "Potato", "Orange", "Pork", "Lemon", "Butter", "Pepper", "Cucumber", "Bread", "Cheese", "Onion",
    "Garlic", "Olive Oil", "Honey", "Yogurt", "Cabbage", "Cherry", "Pineapple", "Strawberry", "Blueberry", "Blackberry",
    "Raspberry", "Mango", "Peach", "Plum", "Watermelon", "Melon", "Grape", "Kiwi", "Banana", "Papaya", "Coconut", "Pear",
    "Pomegranate", "Apricot", "Grapefruit", "Avocado", "Cantaloupe", "Fig", "Lime", "Tangerine", "Zucchini", "Asparagus",
    "Broccoli", "Cauliflower", "Celery", "Corn", "Eggplant", "Green Beans", "Lettuce", "Mushroom", "Peas", "Peppers",
    "Pumpkin", "Spinach", "Squash", "Sweet Potato", "Turnip", "Watercress", "Artichoke", "Brussels Sprouts", "Chickpeas",
    "Kidney Beans", "Lima Beans", "Soybeans", "Black Beans", "Pinto Beans", "White Beans", "Chili Powder", "Cumin",
    "Curry Powder", "Ginger", "Nutmeg", "Oregano", "Paprika", "Rosemary", "Thyme", "Turmeric", "Vanilla", "Basil",
    "Bay Leaves", "Cayenne Pepper", "Chives", "Cilantro", "Dill", "Fennel", "Marjoram", "Mint", "Parsley", "Sage",
    "Tarragon", "Anise", "Cardamom", "Coriander", "Mustard", "Saffron", "Allspice", "Cloves", "Garlic Powder",
    "Onion Powder", "Black Pepper", "White Pepper", "Cinnamon Stick", "Cocoa Powder", "Coconut Milk", "Cornstarch",
    "Cream", "Cream Cheese", "Evaporated Milk", "Half and Half", "Heavy Cream", "Sour Cream", "Whipped Cream",
    "Yeast", "Baking Powder", "Baking Soda", "Cornmeal", "Flour", "Brown Sugar", "Powdered Sugar",
    "Granulated Sugar", "Maple Syrup", "Molasses", "Agave Nectar", "Barley", "Buckwheat", "Couscous",
    "Millet", "Oats", "Quinoa", "Wheat", "Almond Milk", "Buttermilk", "Goat Milk", "Soy Milk", "Whole Milk",
]

USERS_DATA = [
    ("John", "Doe"),
    ("Jane", "Doe"),
    ("Alice", "Smith"),
    ("Bob", "Smith"),
    ("Charlie", "Brown"),
    ("Diana", "Brown"),
    ("Eve", "Johnson"),
    ("Frank", "Johnson"),
    ("Grace", "Williams"),
    ("Henry", "Williams"),
    ("Isabel", "Jones"),
    ("Jack", "Jones"),
    ("Katie", "Martinez"),
    ("Liam", "Martinez"),
    ("Mia", "Garcia"),
    ("Noah", "Garcia"),
    ("Olivia", "Rodriguez"),
    ("Paul", "Rodriguez"),
    ("Quinn", "Lopez"),
    ("Ryan", "Lopez"),
    ("Sophia", "Perez"),
    ("Thomas", "Perez"),
    ("Ursula", "Torres"),
    ("Victor", "Torres"),
    ("Wendy", "Hernandez"),
    ("Xavier", "Hernandez"),
    ("Yvonne", "Sanchez"),
    ("Zachary", "Sanchez"),
    ("Ava", "Ramirez"),
    ("Benjamin", "Ramirez"),
    ("Catherine", "Gonzalez"),
    ("David", "Gonzalez"),
    ("Ella", "Wilson"),
    ("Felix", "Wilson"),
    ("Gabriella", "Martinez"),
    ("Hector", "Martinez"),
    ("Iris", "Garcia"),
    ("Jacob", "Garcia"),
    ("Kylie", "Rodriguez"),
    ("Landon", "Rodriguez"),
    ("Megan", "Lopez"),
    ("Nathan", "Lopez"),
    ("Olivia", "Perez"),
    ("Peter", "Perez"),
    ("Quinn", "Torres"),
    ("Richard", "Torres"),
    ("Sophia", "Hernandez"),
    ("Thomas", "Hernandez"),
    ("Ursula", "Sanchez"),
    ("Victor", "Sanchez"),
    ("Wendy", "Ramirez"),
    ("Xavier", "Ramirez"),
    ("Yvonne", "Gonzalez"),
    ("Zachary", "Gonzalez"),
    ("Ava", "Wilson"),
    ("Benjamin", "Wilson"),
    ("Catherine", "Martinez"),
    ("David", "Martinez"),
    ("Ella", "Garcia"),
    ("Felix", "Garcia"),
    ("Gabriella", "Rodriguez"),
    ("Hector", "Rodriguez"),
    ("Iris", "Lopez"),
    ("Jacob", "Lopez"),
    ("Kylie", "Perez"),
    ("Landon", "Perez"),
    ("Megan", "Torres"),
    ("Nathan", "Torres"),
    ("Olivia", "Hernandez"),
    ("Peter", "Hernandez"),
    ("Quinn", "Sanchez"),
    ("Richard", "Sanchez"),
    ("Sophia", "Ramirez"),
    ("Thomas", "Ramirez"),
    ("Ursula", "Gonzalez"),
    ("Victor", "Gonzalez"),
    ("Wendy", "Wilson"),
    ("Xavier", "Wilson"),
    ("Yvonne", "Martinez"),
    ("Zachary", "Martinez"),
    ("Ava", "Garcia"),
    ("Benjamin", "Garcia"),
    ("Catherine", "Rodriguez"),
    ("David", "Rodriguez"),
    ("Ella", "Lopez"),
    ("Felix", "Lopez"),
    ("Gabriella", "Perez"),
    ("Hector", "Perez"),
    ("Iris", "Torres"),
    ("Jacob", "Torres"),
    ("Kylie", "Hernandez"),
    ("Landon", "Hernandez"),
    ("Megan", "Sanchez"),
    ("Nathan", "Sanchez"),
    ("Olivia", "Ramirez"),
    ("Peter", "Ramirez"),
    ("Quinn", "Gonzalez"),
    ("Richard", "Gonzalez"),
    ("Sophia", "Wilson"),
    ("Thomas", "Wilson"),
    ("Ursula", "Martinez"),
    ("Victor", "Martinez"),
    ("Wendy", "Garcia"),
    ("Xavier", "Garcia"),
    ("Yvonne", "Rodriguez"),
    ("Zachary", "Rodriguez"),
    ("Ava", "Lopez"),
    ("Benjamin", "Lopez"),
    ("Catherine", "Perez"),
    ("David", "Perez"),
    ("Ella", "Torres"),
    ("Felix", "Torres"),
    ("Gabriella", "Hernandez"),
    ("Hector", "Hernandez"),
    ("Iris", "Sanchez"),
    ("Jacob", "Sanchez"),
    ("Kylie", "Ramirez"),
    ("Landon", "Ramirez"),
    ("Megan", "Gonzalez"),
    ("Nathan", "Gonzalez"),
    ("Olivia", "Wilson"),
    ("Peter", "Wilson"),
    ("Quinn", "Martinez"),
    ("Richard", "Martinez"),
    ("Sophia", "Garcia"),
    ("Thomas", "Garcia"),
    ("Ursula", "Rodriguez"),
    ("Victor", "Rodriguez"),
    ("Wendy", "Lopez"),
    ("Xavier", "Lopez"),
    ("Yvonne", "Perez"),
    ("Zachary", "Perez"),
    ("Ava", "Torres"),
    ("Benjamin", "Torres"),
    ("Catherine", "Hernandez"),
    ("David", "Hernandez"),
    ("Ella", "Sanchez"),
    ("Felix", "Sanchez"),
    ("Gabriella", "Ramirez"),
    ("Hector", "Ramirez"),
    ("Iris", "Gonzalez"),
    ("Jacob", "Gonzalez"),
    ("Kylie", "Wilson"),
    ("Landon", "Wilson"),
    ("Megan", "Martinez"),
    ("Nathan", "Martinez"),
    ("Olivia", "Garcia"),
    ("Peter", "Garcia"),
    ("Quinn", "Rodriguez"),
    ("Richard", "Rodriguez"),
    ("Sophia", "Lopez"),
    ("Thomas", "Lopez"),
    ("Ursula", "Perez"),
    ("Victor", "Perez"),
    ("Wendy", "Torres"),
    ("Xavier", "Torres"),
    ("Yvonne", "Hernandez"),
    ("Zachary", "Hernandez"),
    ("Ava", "Sanchez"),
    ("Benjamin", "Sanchez"),
    ("Catherine", "Ramirez"),
    ("David", "Ramirez"),
    ("Ella", "Gonzalez"),
    ("Felix", "Gonzalez"),
    ("Gabriella", "Wilson"),
    ("Hector", "Wilson"),
    ("Iris", "Martinez"),
    ("Jacob", "Martinez"),
    ("Kylie", "Garcia"),
    ("Landon", "Garcia"),
    ("Megan", "Rodriguez"),
    ("Nathan", "Rodriguez"),
    ("Olivia", "Lopez"),
    ("Peter", "Lopez"),
    ("Quinn", "Perez"),
    ("Richard", "Perez"),
    ("Sophia", "Torres"),
    ("Thomas", "Torres"),
    ("Ursula", "Hernandez"),
    ("Victor", "Hernandez"),
    ("Wendy", "Sanchez"),
    ("Xavier", "Sanchez"),
    ("Yvonne", "Ramirez"),
    ("Zachary", "Ramirez"),
    ("Ava", "Gonzalez"),
    ("Benjamin", "Gonzalez"),
    ("Catherine", "Wilson"),
    ("David", "Wilson"),
    ("Ella", "Martinez")
]

RECIPES_TITLES = [
    "Spaghetti Carbonara", "Chicken Alfredo", "Chicken Parmesan", "Chicken Marsala", "Chicken Piccata", "Chicken Cacciatore",
    "Beef Stroganoff", "Lasagna Bolognese", "Fettuccine Alfredo", "Shrimp Scampi", "Eggplant Parmesan", "Pasta Primavera",
    "Baked Ziti", "Gnocchi with Gorgonzola", "Penne Arrabbiata", "Ravioli with Sage Butter", "Linguine with Clam Sauce",
    "Mac and Cheese", "Pulled Pork Sandwich", "BBQ Ribs", "Buffalo Wings", "Sloppy Joes", "Chicken Pot Pie", "Shepherd’s Pie",
    "Meatloaf", "Chili Con Carne", "Gumbo", "Jambalaya", "Clam Chowder", "Lobster Bisque", "French Onion Soup", "Minestrone",
    "Tomato Basil Soup", "Corn Chowder", "Beef and Barley Soup", "Cream of Mushroom Soup", "New England Clam Chowder",
    "Chicken Tortilla Soup", "Pho", "Ramen", "Udon Noodle Soup", "Soba Noodles with Teriyaki", "Pad Thai", "Drunken Noodles",
    "Chicken Satay", "General Tso’s Chicken", "Kung Pao Chicken", "Sweet and Sour Pork", "Sesame Chicken", "Orange Chicken",
    "Beef and Broccoli", "Teriyaki Salmon", "Peking Duck", "Spring Rolls", "Dim Sum", "Banh Mi Sandwich", "Vietnamese Pho",
    "Tom Yum Soup", "Green Curry Chicken", "Red Curry Beef", "Massaman Curry", "Thai Basil Chicken", "Sushi Rolls",
    "Sashimi Platter", "California Roll", "Dragon Roll", "Tempura Udon", "Katsu Curry", "Tonkotsu Ramen", "Takoyaki",
    "Okonomiyaki", "Yakitori", "Teriyaki Chicken", "Korean Bulgogi", "Kimchi Fried Rice", "Bibimbap", "Japchae",
    "Tandoori Chicken", "Butter Chicken", "Chicken Korma", "Lamb Rogan Josh", "Palak Paneer", "Dal Makhani", "Chana Masala",
    "Aloo Gobi", "Biryani", "Samosas", "Naan Bread", "Tikka Masala", "Goat Curry", "Vindaloo", "Dosa", "Idli", "Pakoras",
    "Hummus and Pita", "Falafel", "Shawarma", "Baba Ganoush", "Fattoush Salad", "Tabbouleh", "Baklava", "Gyro Wrap",
    "Moussaka", "Spanakopita", "Greek Salad", "Pastitsio", "Saganaki", "Dolmades", "Paella", "Gazpacho", "Patatas Bravas",
    "Churros", "Tortilla Española", "Croquetas", "Gambas al Ajillo", "Pisto", "Fabada Asturiana", "Ceviche", "Lomo Saltado",
    "Arepas", "Empanadas", "Feijoada", "Moqueca", "Pão de Queijo", "Tacos al Pastor", "Carnitas Tacos", "Chicken Enchiladas",
    "Chiles Rellenos", "Quesadillas", "Tamales", "Pozole", "Mole Poblano", "Burritos", "Nachos Supreme", "Tres Leches Cake",
    "Flan", "Arroz con Leche", "Coconut Shrimp", "Fish and Chips", "Bangers and Mash", "Cottage Pie", "Full English Breakfast",
    "Scottish Haggis", "Beef Wellington", "Yorkshire Pudding", "Sticky Toffee Pudding", "Scones with Clotted Cream",
    "Apple Pie", "Pumpkin Pie", "Pecan Pie", "Key Lime Pie", "Cheesecake", "Brownies", "Chocolate Chip Cookies",
    "Peanut Butter Cookies", "Tiramisu", "Panna Cotta", "Gelato", "Creme Brulee", "Eclairs", "Profiteroles", "Madeleines",
    "Baba au Rhum", "Black Forest Cake", "Sachertorte", "Dobos Torte", "Apple Strudel", "Linzer Torte", "Stollen",
    "Danish Pastries", "Croissant", "Pain au Chocolat", "Beignets", "Choux Pastry", "Macarons", "Chiffon Cake", "Pavlova",
    "Lamingtons", "Anzac Biscuits", "Banoffee Pie", "Rice Pudding", "Custard Tart", "Rhubarb Crumble", "Bread Pudding",
    "Molten Lava Cake", "Angel Food Cake", "Red Velvet Cake", "Carrot Cake", "German Chocolate Cake", "Boston Cream Pie",
    "Lemon Meringue Pie", "Strawberry Shortcake", "Fruit Tart", "Chocolate Mousse", "Soufflé", "Tart Tatin", "Clafoutis",
    "Ricotta Cheesecake", "Cassata Siciliana", "Zeppole", "Sfogliatelle", "Cannoli", "Biscotti", "Panettone", "Colomba Pasquale",
    "Pastiera Napoletana", "Pistachio Baklava", "Brigadeiro", "Mochi", "Dorayaki", "Taiyaki", "Matcha Cheesecake",
    "Fried Ice Cream", "Halo-Halo", "Ube Cake", "Butter Mochi", "Bingsu", "Egg Tarts", "Churro Ice Cream Sandwich"
]

import random

RECIPES_DESCRIPTIONS = [
    "A classic Italian pasta dish made with eggs, cheese, pancetta, and black pepper. Spaghetti Carbonara is a simple yet flavorful dish that relies on high-quality ingredients. The pancetta is rendered until crispy, then combined with a creamy sauce made from eggs and Pecorino Romano cheese. The residual heat from the pasta helps create a luscious, silky texture without scrambling the eggs. A sprinkle of freshly ground black pepper adds a bold, peppery kick, making this dish a beloved staple in Italian cuisine.",
    "Chicken Alfredo is a creamy, indulgent pasta dish that combines tender pieces of chicken with a rich and velvety Alfredo sauce. Made from butter, heavy cream, and Parmesan cheese, the sauce clings beautifully to fettuccine, creating a comforting and satisfying meal. The chicken is typically grilled or pan-seared to golden perfection, adding depth of flavor. Garnished with fresh parsley and served with garlic bread, this dish is a go-to favorite for pasta lovers around the world.",
    "Beef Stroganoff is a hearty Russian dish consisting of sautéed beef strips, mushrooms, and onions, all coated in a creamy sour cream sauce. Traditionally served over egg noodles or rice, this dish is rich in flavor and has a perfect balance of tanginess from the sour cream and umami depth from the beef and mushrooms. With origins dating back to the 19th century, this meal has become a comfort food classic in many countries.",
    "Pad Thai is one of the most famous Thai street food dishes, known for its balance of sweet, sour, salty, and spicy flavors. Made with stir-fried rice noodles, tofu or shrimp, scrambled eggs, bean sprouts, and peanuts, this dish is tossed in a tangy tamarind-based sauce that gives it its signature flavor. Garnished with lime wedges and crushed peanuts, Pad Thai is a perfect combination of textures and bold flavors that make it a worldwide favorite.",
    "Tiramisu is a beloved Italian dessert made with layers of coffee-soaked ladyfingers, creamy mascarpone cheese, and a dusting of cocoa powder. This no-bake dessert is both light and indulgent, offering the perfect balance between the bitterness of espresso and the sweetness of the mascarpone filling. The origins of Tiramisu date back to the 1960s in the Veneto region of Italy, and it has since become a staple dessert in many restaurants worldwide.",
    "Paella Valenciana is a traditional Spanish dish originating from the Valencia region. This vibrant rice dish is made with a base of saffron-infused rice cooked with a variety of proteins such as chicken, rabbit, and seafood, depending on the variation. The socarrat, a crispy, caramelized layer of rice that forms at the bottom of the pan, is considered the best part of the dish. Enhanced with garlic, tomatoes, paprika, and green beans, Paella is a perfect communal dish that brings people together for a flavorful feast.",
    "French Onion Soup is a comforting, flavorful soup made from slowly caramelized onions, rich beef broth, and topped with a slice of toasted bread covered in melted Gruyère cheese. The deep umami flavors of the onions, cooked low and slow, give this soup its characteristic sweetness and depth. The melted cheese on top, browned to perfection, creates a gooey, delicious contrast to the warm broth underneath. Best served on a cold evening, French Onion Soup is a timeless classic.",
    "Sushi rolls are a staple of Japanese cuisine, offering a perfect combination of vinegared rice, fresh seafood, and crisp vegetables. Sushi rolls, or maki, are wrapped in seaweed (nori) and filled with ingredients such as salmon, tuna, avocado, cucumber, and crab meat. The balance of textures and flavors, along with the umami-rich soy sauce and wasabi, make sushi a beloved dish worldwide. Served with pickled ginger and miso soup, sushi provides a refined and satisfying dining experience.",
    "Butter Chicken, or Murgh Makhani, is a creamy, tomato-based Indian curry that features tender pieces of marinated chicken cooked in a fragrant, spiced sauce. The curry is made with butter, cream, tomatoes, garlic, ginger, and an array of Indian spices, creating a rich and mildly spicy dish that pairs perfectly with basmati rice or naan bread. Originally developed in the 1950s in Delhi, Butter Chicken has since become one of the most popular Indian dishes around the world.",
    "Greek Moussaka is a layered casserole dish featuring eggplant, minced meat (usually lamb or beef), and a rich béchamel sauce. This Mediterranean classic is seasoned with cinnamon, nutmeg, and oregano, giving it a warm and aromatic flavor. The layers of eggplant absorb the savory juices of the meat, while the creamy béchamel sauce on top adds a luxurious texture. Baked until golden brown, Moussaka is a hearty and satisfying dish perfect for family meals.",
    "Clam Chowder is a creamy and comforting soup that originates from New England. Made with fresh clams, diced potatoes, celery, onions, and a rich, creamy broth, this dish is a staple of coastal American cuisine. The combination of briny clams and velvety broth creates a harmonious blend of flavors, making it a popular choice in seafood restaurants. Often served with oyster crackers or crusty bread, Clam Chowder is the ultimate comfort food on a chilly day.",
    "Chocolate Lava Cake is a decadent dessert that features a warm, gooey chocolate center encased in a soft, fluffy cake. When sliced open, the molten chocolate flows out, creating a rich and indulgent experience. Made with high-quality dark chocolate, butter, sugar, eggs, and a hint of espresso, this dessert is often paired with vanilla ice cream or fresh berries. Its combination of crisp outer shell and liquid chocolate interior makes it an irresistible treat for chocolate lovers.",
    "BBQ Ribs are a classic American dish, featuring slow-cooked pork ribs slathered in a smoky, tangy barbecue sauce. The ribs are marinated in a dry rub before being grilled or smoked for hours until tender and flavorful. The caramelization of the sauce creates a sticky, mouthwatering glaze, while the meat remains juicy and falls off the bone. Whether served with coleslaw, baked beans, or cornbread, BBQ ribs are a backyard barbecue essential.",
    "Ceviche is a refreshing Latin American dish made with fresh raw fish cured in citrus juices, typically lime or lemon. The acidity of the citrus 'cooks' the fish, creating a tender texture while preserving its natural flavors. Mixed with diced tomatoes, onions, cilantro, and chili peppers, ceviche is a bright and zesty dish often served with tortilla chips or crispy tostadas. Popular in coastal regions, ceviche is a perfect dish for warm weather and seafood lovers alike.",
    "Classic Apple Pie is a timeless dessert made with a flaky, buttery crust and a sweet, cinnamon-spiced apple filling. The filling, made with tart apples like Granny Smith, sugar, cinnamon, and a hint of lemon juice, is encased in a golden, lattice-style crust. Served warm with a scoop of vanilla ice cream or a dollop of whipped cream, apple pie is a comforting and nostalgic treat enjoyed in homes across the world.",
    "Chicken Pot Pie is a comforting and hearty dish made with tender pieces of chicken, vegetables, and a creamy sauce, all encased in a flaky pie crust. The filling typically includes carrots, peas, onions, and celery, cooked in a rich gravy made from chicken broth and cream. Topped with a second layer of pie crust or a biscuit topping, this dish is baked until golden brown and bubbling. Chicken Pot Pie is a classic comfort food that warms the soul on a cold day.",
    "Pulled Pork Sandwich is a Southern barbecue classic featuring tender, slow-cooked pork shoulder shredded and mixed with a tangy, smoky barbecue sauce. The pork is typically cooked low and slow until it's fall-apart tender, then piled high on a soft hamburger bun. Served with coleslaw, pickles, and a side of baked beans or cornbread, this sandwich is a flavorful and satisfying meal that's perfect for summer cookouts or casual gatherings.",
    "Chocolate Chip Cookies are a beloved treat made with butter, sugar, flour, eggs, and of course, chocolate chips. The dough is typically flavored with vanilla extract and a pinch of salt, creating a perfect balance of sweet and salty flavors. Baked until golden brown and slightly crispy on the edges, these cookies are soft and chewy in the center, with pockets of melted chocolate throughout. Whether enjoyed warm from the oven or dunked in a glass of milk, chocolate chip cookies are a classic dessert that never goes out of style.",
    "Beef Wellington is an elegant and indulgent dish featuring a tender beef fillet coated in mushroom duxelles, wrapped in Parma ham, and encased in flaky puff pastry. The layers of flavors and textures, from the earthy mushrooms to the savory beef and buttery pastry, create a luxurious dining experience. Served with a rich red wine sauce and seasonal vegetables, Beef Wellington is a show-stopping centerpiece for special occasions and holiday feasts.",
    "Tandoori Chicken is a popular Indian dish known for its vibrant red color and bold flavors. The chicken is marinated in a mixture of yogurt, lemon juice, and a blend of aromatic spices, including cumin, coriander, turmeric, and garam masala. Traditionally cooked in a tandoor, a clay oven, the chicken develops a smoky charred exterior while remaining juicy and tender inside. Served with naan bread, rice, and mint chutney, Tandoori Chicken is a flavorful and aromatic dish that's perfect for sharing.",
    "Chocolate Mousse is a luxurious dessert made with rich, dark chocolate, whipped cream, and egg whites. The chocolate is melted and combined with whipped cream to create a smooth and velvety mousse, which is then lightened with fluffy egg whites. Served in individual cups or as a cake filling, chocolate mousse is a decadent treat that's perfect for special occasions or a sweet ending to a meal.",
    "Baklava is a sweet and flaky pastry made with layers of phyllo dough, chopped nuts, and honey or syrup. The nuts, typically walnuts, pistachios, or almonds, are spiced with cinnamon and cloves, adding warmth and depth of flavor. Each layer of phyllo is brushed with butter to create a crisp and golden exterior, while the honey syrup soaks into the pastry, creating a sticky and sweet finish. Baklava is a beloved dessert in Middle Eastern and Mediterranean cuisines, often enjoyed with a cup of strong coffee or tea.",
    "Sushi rolls are a staple of Japanese cuisine, offering a perfect combination of vinegared rice, fresh seafood, and crisp vegetables. Sushi rolls, or maki, are wrapped in seaweed (nori) and filled with ingredients such as salmon, tuna, avocado, cucumber, and crab meat. The balance of textures and flavors, along with the umami-rich soy sauce and wasabi, make sushi a beloved dish worldwide. Served with pickled ginger and miso soup, sushi provides a refined and satisfying dining experience.",
    "Butter Chicken, or Murgh Makhani, is a creamy, tomato-based Indian curry that features tender pieces of marinated chicken cooked in a fragrant, spiced sauce. The curry is made with butter, cream, tomatoes, garlic, ginger, and an array of Indian spices, creating a rich and mildly spicy dish that pairs perfectly with basmati rice or naan bread. Originally developed in the 1950s in Delhi, Butter Chicken has since become one of the most popular Indian dishes around the world.",
    "Greek Moussaka is a layered casserole dish featuring eggplant, minced meat (usually lamb or beef), and a rich béchamel sauce. This Mediterranean classic is seasoned with cinnamon, nutmeg, and oregano, giving it a warm and aromatic flavor. The layers of eggplant absorb the savory juices of the meat, while the creamy béchamel sauce on top adds a luxurious texture. Baked until golden brown, Moussaka is a hearty and satisfying dish perfect for family meals.",
    "Clam Chowder is a creamy and comforting soup that originates from New England. Made with fresh clams, diced potatoes, celery, onions, and a rich, creamy broth, this dish is a staple of coastal American cuisine. The combination of briny clams and velvety broth creates a harmonious blend of flavors, making it a popular choice in seafood restaurants. Often served with oyster crackers or crusty bread, Clam Chowder is the ultimate comfort food on a chilly day.",
    "Chocolate Lava Cake is a decadent dessert that features a warm, gooey chocolate center encased in a soft, fluffy cake. When sliced open, the molten chocolate flows out, creating a rich and indulgent experience. Made with high-quality dark chocolate, butter, sugar, eggs, and a hint of espresso, this dessert is often paired with vanilla ice cream or fresh berries. Its combination of crisp outer shell and liquid chocolate interior makes it an irresistible treat for chocolate lovers."
]


GENERATED_USERS = []
GENERATED_INGREDIENTS = []

url = "http://localhost:8080/wecook"

async def fetchValueSet(session, endpoint):
    async with session.get(f"{API_URL}/valueSets/{endpoint}") as response:
        return await response.json() if response.status == 200 else []
    
def getImageMimeType(filename):
    extension = filename.split(".")[-1].lower()
    return f"image/{'jpeg' if extension in ['jpg', 'jpeg'] else extension}"

async def convertFileToBase64(filePath):
    with open(filePath, "rb") as file:
        base64String = base64.b64encode(file.read()).decode("utf-8")
        mimeType = getImageMimeType(filePath)
        return f"data:{mimeType};base64,{base64String}"

# Random data generators
def generateRandomIngredient(name):
    foodType = random.choice(FOOD_TYPES)
    maxUnits = min(3, len(MEASUREMENTS_UNITS[foodType]))
    return {
        "name": name,
        "type": foodType,
        "measurementUnits": random.sample(MEASUREMENTS_UNITS[foodType], k=random.randint(1, maxUnits))
    }

def generateRandomUser():
    firstName, lastName = random.choice(USERS_DATA)

    email = f"{firstName.lower()}.{lastName.lower()}@gmail.com"
    password = f"{firstName.lower()}.{lastName.lower()}"
    return {
        "email": email,
        "username": f"{firstName.lower()}.{lastName.lower()}_",
        "password": password,
        "role": "STANDARD"
    }

import aiohttp

# Senders
async def postIngredient(session, ingredient):
    headers = {"Content-Type": "application/json"}
    try:
        async with session.post(f"{url}/ingredient", json=ingredient, headers=headers, cookies={"withCredentials": "true"}) as response:
            if response.status in [200, 201]:
                data = await response.json()
                GENERATED_INGREDIENTS.append(data["id"])
                print(f"Successfully added ingredient: {ingredient['name']}")
            else:
                print(f"Failed to add ingredient: {ingredient['name']}, Status Code: {response.status}")
    except Exception as e:
        print(f"Error posting ingredient {ingredient['name']}: {e}")

async def postUser(session, user):
    headers = {"Content-Type": "application/json"}
    try:
        async with session.post(f"{url}/users", json=user, headers=headers) as response:
            if response.status in [200, 201]:
                data = await response.json()
                GENERATED_USERS.append(data["id"])
                print(f"Successfully added user: {user['email']}, ID: {data['id']}")
            else:
                print(f"Failed to add user: {user['email']}, Status Code: {response.status}")
    except Exception as e:
        print(f"Error posting user {user['email']}: {e}")

async def postRecipe(session, recipeData):
    try:
        async with session.post(f"{API_URL}/recipe", json=recipeData) as response:
            if response.status == 201:
                print(f"Successfully added recipe: {recipeData['title']}")
                return await response.json()
            else:
                print(f"Failed to add recipe, Status Code: {response.status}")
                return None
    except Exception as e:
        print(f"Error posting recipe: {e}")
        return None

async def postStep(session, recipeId, stepData):
    try:
        async with session.post(f"{API_URL}/recipe/{recipeId}/step", json=stepData) as response:
            if response.status in [200, 201]:
                print(f"Successfully added step for recipe {recipeId}")
                return await response.json()
            else:
                print(f"Failed to add step for recipe {recipeId}, Status Code: {response.status}")
                return None
    except Exception as e:
        print(f"Error posting step for recipe {recipeId}: {e}")
        return None

async def postRecipeIngredient(session, recipeId, stepId, ingredientData):
    try:
        async with session.post(f"{API_URL}/recipe/{recipeId}/step/{stepId}/recipeIngredient", json=ingredientData) as response:
            if response.status in [200, 201]:
                print(f"Successfully added ingredient to step {stepId} of recipe {recipeId}")
                return True
            else:
                print(f"Failed to add ingredient to step {stepId} of recipe {recipeId}, Status Code: {response.status}")
                return False
    except Exception as e:
        print(f"Error posting recipe ingredient for step {stepId} of recipe {recipeId}: {e}")
        return False

async def postPost(session, postData):
    try:
        async with session.post(f"{API_URL}/post", json=postData) as response:
            if response.status in [200, 201]:
                data = await response.json()
                print(f"Successfully created post with ID: {data['id']}")
                return data
            else:
                print(f"Failed to create post, Status Code: {response.status}")
                return None
    except Exception as e:
        print(f"Error posting post: {e}")
        return None

async def updatePostWithRecipe(session, postId, recipeId):
    headers = {"Content-Type": "application/json"}
    try:
        async with session.put(f"{API_URL}/post/{postId}", json={"recipeId": recipeId}, headers=headers) as response:
            if response.status in [200, 201]:
                data = await response.json()
                GENERATED_USERS.append(data["id"])
                print(f"Successfully added recipe to post: Recipe: {recipeId}, ID: {data['id']}")
            else:
                print(f"Failed to add recipe to post: Recipe: {recipeId}, Status Code: {response.status}")
    except Exception as e:
        print(f"Error updating post {postId} with recipe {recipeId}: {e}")

# Async jobs
async def addIngredients():
    async with aiohttp.ClientSession() as session:
        tasks = [postIngredient(session, generateRandomIngredient(ingredient)) for ingredient in INGREDIENTS]
        await asyncio.gather(*tasks)

async def addUsers(n):
    async with aiohttp.ClientSession() as session:
        tasks = [postUser(session, generateRandomUser()) for _ in range(n)]
        await asyncio.gather(*tasks)

    async with aiohttp.ClientSession() as session:
        tasks = [postUser(session, {
            "email": "s.albino2@studenti.unisa.it",
            "username": f"simone.albino_",
            "password": "simone.albino",
            "role": "STANDARD"
        })]
        await asyncio.gather(*tasks)

async def addRecipes(n):
    async with aiohttp.ClientSession() as session:
        difficulties = await fetchValueSet(session, "difficulties")
        actions = await fetchValueSet(session, "actions")
        categories = await fetchValueSet(session, "foodCategories")

        for i in range(n):
            print(f"--Recipe {i + 1}/{n}")

            imageFile = random.choice(os.listdir(IMAGE_FOLDER))
            imageBase64 = await convertFileToBase64(os.path.join(IMAGE_FOLDER, imageFile))

            recipeData = {
                "title": random.choice(RECIPES_TITLES),
                "description": random.choice(RECIPES_DESCRIPTIONS),
                "category": random.choice(categories),
                "difficulty": random.choice(difficulties)
            }

            recipe = await postRecipe(session, recipeData)
            if not recipe:
                continue

            numSteps = random.randint(2, 5)
            for stepIndex in range(numSteps):
                action = random.choice(actions)
                stepData = {
                    "description": f"Step {stepIndex + 1}: {action} ingredients",
                    "duration": random.randint(5, 30),
                    "action": action,
                    "stepIndex": stepIndex + 1
                }
                createdStep = await postStep(session, recipe["id"], stepData)
                if not createdStep:
                    continue
                
                numIngredients = random.randint(1, 3)
                for _ in range(numIngredients):
                    ingredientType = random.choice(FOOD_TYPES)
                    ingredientUnit = random.choice(MEASUREMENTS_UNITS[ingredientType])
                    ingredientData = {
                        "quantity": random.randint(50, 500),
                        "measurementUnit": ingredientUnit,
                        "ingredientId": random.randint(1, 100)
                    }
                    await postRecipeIngredient(session, recipe["id"], createdStep["id"], ingredientData)
            
            postData = {"standardUserId": random.choice(GENERATED_USERS), "postPicture": imageBase64}
            post = await postPost(session, postData)
            if not post:
                continue
            
            await updatePostWithRecipe(session, post["id"], recipe["id"])

def main():
    print("====== Adding Ingredients ======")
    asyncio.run(addIngredients())

    print("\n====== Adding Users ======")
    asyncio.run(addUsers(100))

    print("\n====== Generating Recipes ======")
    asyncio.run(addRecipes(100))

if __name__ == '__main__':
    main()