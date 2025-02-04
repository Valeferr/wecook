import random
import asyncio
import aiohttp

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

# rANDOM DATA
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

GENERATED_USERS = []

url = "http://localhost:8080/wecook"

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

# Senders
async def sendIngredient(session, ingredient):
    headers = {"Content-Type": "application/json"}
    async with session.post(f"{url}/ingredient", json=ingredient, headers=headers, cookies={"withCredentials": "true"}) as response:
        if response.status == 201:
            print(f"Successfully added ingredient: {ingredient['name']}")
        else:
            print(f"Failed to add ingredient: {ingredient['name']}, Status Code: {response.status}")

async def send_user(session, user):
    headers = {"Content-Type": "application/json"}
    async with session.post(f"{url}/users", json=user, headers=headers) as response:
        if response.status == 201:
            data = await response.json()
            GENERATED_USERS.append(data["id"])
            print(f"Successfully added user: {user['email']}, ID: {data['id']}")
        else:
            print(f"Failed to add user: {user['email']}, Status Code: {response.status}")

# Async jobs
async def addIngredients():
    async with aiohttp.ClientSession() as session:
        tasks = [sendIngredient(session, generateRandomIngredient(ingredient)) for ingredient in INGREDIENTS]
        await asyncio.gather(*tasks)

async def addUsers(n):
    async with aiohttp.ClientSession() as session:
        tasks = [send_user(session, generateRandomUser()) for _ in range(n)]
        await asyncio.gather(*tasks)

def main():
    print("====== Adding Ingredients ======")
    asyncio.run(addIngredients())

    print("\n====== Adding Users ======")
    asyncio.run(addUsers(100))

if __name__ == '__main__':
    main()