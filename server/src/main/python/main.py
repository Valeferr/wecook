import random
import asyncio
import aiohttp

# Definizione delle unità di misura coerenti con il tipo di ingrediente
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

ingredients = [
    "Chicken", "Beef", "Salmon", "Carrot", "Apple", "Rice", "Milk", "Lentils", "Salt", "Egg", "Cinnamon", "Coffee", "Sugar",
    "Pasta", "Tomato", "Potato", "Orange", "Pork", "Lemon", "Butter", "Pepper", "Cucumber", "Bread", "Cheese", "Onion",
    "Garlic", "Olive Oil", "Honey", "Yogurt", "Cabbage", "Cherry", "Pineapple", "Strawberry", "Blueberry", "Blackberry",
    "Raspberry", "Mango", "Peach", "Plum", "Watermelon", "Melon", "Grape", "Kiwi", "Banana", "Papaya", "Coconut", "Pear",
    "Pomegranate", "Apricot", "Grapefruit", "Avocado", "Cantaloupe", "Fig", "Lime", "Tangerine", "Zucchini", "Asparagus",
    "Broccoli", "Cauliflower", "Celery", "Corn", "Eggplant", "Green Beans", "Lettuce", "Mushroom", "Peas", "Peppers",
    "Pumpkin", "Spinach", "Squash", "Sweet Potato", "Turnip", "Watercress", "Artichoke", "Brussels Sprouts", "Chickpeas",
    "Kidney Beans", "Lima Beans", "Soybeans", "Black Beans", "Pinto Beans", "White Beans", "Chili Powder", "Cumin",
    "Curry Powder", "Ginger", "Nutmeg", "Oregano", "Paprika", "Rosemary", "Thyme", "Turmeric", "Vanilla", "Basil"
]

url = "http://localhost:8080/wecook"

def generateRandomIngredient(name):
    foodType = random.choice(FOOD_TYPES)
    maxUnits = min(3, len(MEASUREMENTS_UNITS[foodType]))
    return {
        "name": name,
        "type": foodType,
        "measurementUnits": random.sample(MEASUREMENTS_UNITS[foodType], k=random.randint(1, maxUnits))
    }

async def sendIngredient(session, ingredient):
    headers = {"Content-Type": "application/json"}
    async with session.post(f"{url}/ingredient", json=ingredient, headers=headers, cookies={"withCredentials": "true"}) as response:
        if response.status == 201:
            print(f"Successfully added ingredient: {ingredient['name']}")
        else:
            print(f"Failed to add ingredient: {ingredient['name']}, Status Code: {response.status}")

async def addIngredients(n):
    async with aiohttp.ClientSession() as session:
        tasks = [sendIngredient(session, generateRandomIngredient(ingredient)) for ingredient in ingredients]
        await asyncio.gather(*tasks)

def main():
    asyncio.run(addIngredients(1000))

if __name__ == '__main__':
    main()