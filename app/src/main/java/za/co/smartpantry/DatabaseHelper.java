package za.co.smartpantry;

import android.content.*; import android.database.*; import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
 private static final String DB_NAME="smart_pantry.db"; private static final int DB_VERSION=2;
 public DatabaseHelper(Context c){super(c,DB_NAME,null,DB_VERSION);}
 @Override public void onCreate(SQLiteDatabase db){
  db.execSQL("CREATE TABLE ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,expiry TEXT)");
  db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,steps TEXT NOT NULL)");
  db.execSQL("CREATE TABLE recipe_ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_id INTEGER NOT NULL,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)");
  db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT UNIQUE NOT NULL,password TEXT NOT NULL,full_name TEXT NOT NULL,created_at TEXT)");
  db.execSQL("CREATE TABLE support_tickets(id INTEGER PRIMARY KEY AUTOINCREMENT,user_email TEXT NOT NULL,category TEXT NOT NULL,subject TEXT NOT NULL,message TEXT NOT NULL,status TEXT NOT NULL,created_at TEXT)");
  seed(db);
 }
 @Override public void onConfigure(SQLiteDatabase db){super.onConfigure(db);db.setForeignKeyConstraintsEnabled(true);}
 @Override public void onUpgrade(SQLiteDatabase db,int oldV,int newV){
  db.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT UNIQUE NOT NULL,password TEXT NOT NULL,full_name TEXT NOT NULL,created_at TEXT)");
  db.execSQL("CREATE TABLE IF NOT EXISTS support_tickets(id INTEGER PRIMARY KEY AUTOINCREMENT,user_email TEXT NOT NULL,category TEXT NOT NULL,subject TEXT NOT NULL,message TEXT NOT NULL,status TEXT NOT NULL,created_at TEXT)");
 }
 private void seed(SQLiteDatabase db){
  String[][] data={
   {"Tomato Egg Scramble","Beat eggs, chop tomatoes, cook together in a pan.","egg|2|pieces;tomato|2|pieces;salt|1|pinch"},
   {"Simple Tomato Pasta","Boil pasta. Cook chopped tomato with oil and salt, then combine.","pasta|200|g;tomato|2|pieces;olive oil|1|tbsp;salt|1|pinch"},
   {"Garlic Pasta","Boil pasta. Gently fry garlic in oil and toss with pasta.","pasta|200|g;garlic|2|cloves;olive oil|1|tbsp;salt|1|pinch"},
   {"Cheese Omelette","Whisk eggs, cook in a pan and fold with grated cheese.","egg|2|pieces;cheese|30|g;salt|1|pinch"},
   {"Banana Pancakes","Mash banana, mix with egg and flour, then fry small pancakes.","banana|1|pieces;egg|1|pieces;flour|50|g"},
   {"Rice and Peas","Cook rice. Heat peas and combine with a pinch of salt.","rice|150|g;peas|100|g;salt|1|pinch"},
   {"Potato Hash","Dice potatoes and onion; pan-fry until tender and golden.","potato|2|pieces;onion|1|pieces;olive oil|1|tbsp;salt|1|pinch"},
   {"Carrot Soup","Simmer chopped carrot, onion and stock until soft, then blend.","carrot|3|pieces;onion|1|pieces;stock|500|ml"},
   {"Apple Oat Bowl","Chop apple and serve with oats and milk.","apple|1|pieces;oats|50|g;milk|150|ml"},
   {"Peanut Butter Toast","Toast bread and spread peanut butter evenly.","bread|2|slices;peanut butter|2|tbsp"},
   {"Tuna Rice Bowl","Cook rice, drain tuna and mix with sweetcorn.","rice|150|g;tuna|1|can;sweetcorn|80|g"},
   {"Spinach Egg Pan","Wilt spinach, add beaten eggs and cook through.","spinach|80|g;egg|2|pieces;olive oil|1|tbsp"},
   {"Cheesy Baked Potato","Bake potato until soft and top with cheese.","potato|1|pieces;cheese|40|g"},
   {"Yogurt Fruit Cup","Slice banana and apple; layer with yogurt.","banana|1|pieces;apple|1|pieces;yogurt|150|g"},
   {"Lentil Tomato Stew","Simmer lentils, tomato, onion and stock until tender.","lentils|150|g;tomato|2|pieces;onion|1|pieces;stock|400|ml"},
   {"Corn Fritters","Mix sweetcorn, flour and egg; fry spoonfuls until golden.","sweetcorn|100|g;flour|60|g;egg|1|pieces;olive oil|1|tbsp"},
   {"Cucumber Yogurt Salad","Chop cucumber and mix with yogurt and salt.","cucumber|1|pieces;yogurt|100|g;salt|1|pinch"},
   {"Garlic Spinach Rice","Cook rice; sauté garlic and spinach, then combine.","rice|150|g;garlic|1|cloves;spinach|80|g;olive oil|1|tbsp"},
   {"Simple French Toast","Dip bread in beaten egg and pan-fry until browned.","bread|2|slices;egg|1|pieces;milk|50|ml"},
   {"Carrot Apple Salad","Grate carrot and apple and mix together.","carrot|1|pieces;apple|1|pieces"}
  };
  for(String[] r:data){ContentValues cv=new ContentValues();cv.put("name",r[0]);cv.put("steps",r[1]);long id=db.insert("recipes",null,cv);for(String token:r[2].split(";")){String[] p=token.split("\\|");ContentValues iv=new ContentValues();iv.put("recipe_id",id);iv.put("name",p[0]);iv.put("quantity",Double.parseDouble(p[1]));iv.put("unit",p[2]);db.insert("recipe_ingredients",null,iv);}}
 }

 // User Authentication Methods
 public long registerUser(String fullName, String email, String password) {
  ContentValues v = new ContentValues();
  v.put("full_name", fullName.trim());
  v.put("email", email.trim().toLowerCase(Locale.ROOT));
  v.put("password", password);
  v.put("created_at", String.valueOf(System.currentTimeMillis()));
  try {
   return getWritableDatabase().insert("users", null, v);
  } catch (Exception e) {
   return -1;
  }
 }

 public User authenticateUser(String email, String password) {
  try (Cursor c = getReadableDatabase().rawQuery("SELECT id, full_name, email, created_at FROM users WHERE lower(email)=? AND password=?", new String[]{email.trim().toLowerCase(Locale.ROOT), password})) {
   if (c.moveToFirst()) {
    return new User(c.getLong(0), c.getString(1), c.getString(2), c.getString(3));
   }
  }
  return null;
 }

 public User getUserByEmail(String email) {
  try (Cursor c = getReadableDatabase().rawQuery("SELECT id, full_name, email, created_at FROM users WHERE lower(email)=?", new String[]{email.trim().toLowerCase(Locale.ROOT)})) {
   if (c.moveToFirst()) {
    return new User(c.getLong(0), c.getString(1), c.getString(2), c.getString(3));
   }
  }
  return null;
 }

 // Support Ticket Methods
 public long createSupportTicket(String userEmail, String category, String subject, String message) {
  ContentValues v = new ContentValues();
  v.put("user_email", userEmail);
  v.put("category", category);
  v.put("subject", subject);
  v.put("message", message);
  v.put("status", "OPEN");
  v.put("created_at", String.valueOf(System.currentTimeMillis()));
  return getWritableDatabase().insert("support_tickets", null, v);
 }

 public List<SupportTicket> getSupportTickets(String userEmail) {
  List<SupportTicket> list = new ArrayList<>();
  try (Cursor c = getReadableDatabase().rawQuery("SELECT id, user_email, category, subject, message, status, created_at FROM support_tickets WHERE user_email=? ORDER BY id DESC", new String[]{userEmail})) {
   while (c.moveToNext()) {
    list.add(new SupportTicket(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6)));
   }
  }
  return list;
 }

 // Admin Methods
 public List<User> getAllUsers() {
  List<User> list = new ArrayList<>();
  try (Cursor c = getReadableDatabase().rawQuery("SELECT id, full_name, email, created_at FROM users ORDER BY id DESC", null)) {
   while (c.moveToNext()) {
    list.add(new User(c.getLong(0), c.getString(1), c.getString(2), c.getString(3)));
   }
  }
  return list;
 }

 public List<SupportTicket> getAllSupportTickets() {
  List<SupportTicket> list = new ArrayList<>();
  try (Cursor c = getReadableDatabase().rawQuery("SELECT id, user_email, category, subject, message, status, created_at FROM support_tickets ORDER BY id DESC", null)) {
   while (c.moveToNext()) {
    list.add(new SupportTicket(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6)));
   }
  }
  return list;
 }

 public boolean deleteUser(long id) {
  return getWritableDatabase().delete("users", "id=?", new String[]{String.valueOf(id)}) > 0;
 }

 public boolean updateSupportTicketStatus(long id, String status) {
  ContentValues v = new ContentValues();
  v.put("status", status);
  return getWritableDatabase().update("support_tickets", v, "id=?", new String[]{String.valueOf(id)}) > 0;
 }

 public List<Ingredient> getIngredients(){List<Ingredient> out=new ArrayList<>();try(Cursor c=getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry FROM ingredients ORDER BY name",null)){while(c.moveToNext())out.add(new Ingredient(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4)));}return out;}
 public Ingredient getIngredient(long id){try(Cursor c=getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry FROM ingredients WHERE id=?",new String[]{String.valueOf(id)})){if(c.moveToFirst())return new Ingredient(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4));}return null;}
 public long saveIngredient(Ingredient i){ContentValues v=new ContentValues();v.put("name",i.name.trim());v.put("quantity",i.quantity);v.put("unit",i.unit.trim());v.put("expiry",i.expiry);SQLiteDatabase db=getWritableDatabase();if(i.id>0)return db.update("ingredients",v,"id=?",new String[]{String.valueOf(i.id)})>0?i.id:-1;return db.insert("ingredients",null,v);}
 public boolean deleteIngredient(long id){return getWritableDatabase().delete("ingredients","id=?",new String[]{String.valueOf(id)})>0;}
 public List<Recipe> getAllRecipes(){return queryRecipes(false);}
 public List<Recipe> getSuggestedRecipes(){return queryRecipes(true);}
 private List<Recipe> queryRecipes(boolean strict){List<Recipe> out=new ArrayList<>();Map<String,Ingredient> pantry=new HashMap<>();for(Ingredient i:getIngredients())pantry.put(normalize(i.name),i);try(Cursor c=getReadableDatabase().rawQuery("SELECT id,name,steps FROM recipes ORDER BY name",null)){while(c.moveToNext()){long id=c.getLong(0);List<Recipe.RecipeIngredient> req=new ArrayList<>();try(Cursor ic=getReadableDatabase().rawQuery("SELECT name,quantity,unit FROM recipe_ingredients WHERE recipe_id=?",new String[]{String.valueOf(id)})){while(ic.moveToNext())req.add(new Recipe.RecipeIngredient(ic.getString(0),ic.getDouble(1),ic.getString(2)));}Recipe r=new Recipe(id,c.getString(1),c.getString(2),req);boolean match=true;for(Recipe.RecipeIngredient ri:req){Ingredient have=pantry.get(normalize(ri.name));if(have==null||have.quantity+0.000001<ri.quantity||!unitsCompatible(have.unit,ri.unit)){match=false;break;}}if(!strict||match)out.add(r);}}return out;}
 public Recipe getRecipe(long id){for(Recipe r:getAllRecipes())if(r.id==id)return r;return null;}
 public static String normalize(String s){String n=s==null?"":s.toLowerCase(Locale.ROOT).trim().replaceAll("[^a-z0-9 ]","").replaceAll("\\s+"," ");if(n.endsWith("ies")&&n.length()>4)n=n.substring(0,n.length()-3)+"y";else if(n.endsWith("oes")&&n.length()>4)n=n.substring(0,n.length()-2);else if(n.endsWith("s")&&!n.endsWith("ss")&&n.length()>3)n=n.substring(0,n.length()-1);return n;}
 private boolean unitsCompatible(String a,String b){String x=a.toLowerCase(Locale.ROOT).trim(),y=b.toLowerCase(Locale.ROOT).trim();if(x.equals(y))return true;Map<String,String> groups=new HashMap<>();for(String s:new String[]{"piece","pieces","pc","pcs"})groups.put(s,"piece");for(String s:new String[]{"g","gram","grams"})groups.put(s,"mass");for(String s:new String[]{"ml","milliliter","milliliters"})groups.put(s,"volume");for(String s:new String[]{"tbsp","tablespoon","tablespoons"})groups.put(s,"spoon");for(String s:new String[]{"clove","cloves"})groups.put(s,"clove");return groups.containsKey(x)&&groups.get(x).equals(groups.get(y));}
}
